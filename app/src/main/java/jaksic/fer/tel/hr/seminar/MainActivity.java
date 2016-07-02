package jaksic.fer.tel.hr.seminar;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.echo.holographlibrary.Line;
import com.echo.holographlibrary.LineGraph;
import com.echo.holographlibrary.LinePoint;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @InjectView(R.id.day)
    protected EditText day;

    @InjectView(R.id.month)
    protected EditText month;

    @InjectView(R.id.year)
    protected EditText year;

    @InjectView(R.id.graph_max)
    protected TextView graphMax;

    @InjectView(R.id.graph_min)
    protected TextView graphMin;

    @InjectView(R.id.line_graph)
    protected LineGraph lineGraph;

    @InjectView(R.id.history_data)
    protected Button historyDataButton;

    protected Resources resources;

    protected final Handler handler = new Handler();

    protected Worker worker;

    private boolean historyDisplayed;

    private NetworkService networkService;

    private ResponseCallback responseCallback = new ResponseCallback();

    private LastMeasurementResponse lastMeasurementResponse = new LastMeasurementResponse();

    private HistoryMeasurementResponse historyMeasurementResponse = new HistoryMeasurementResponse();

    private List<Value> currentData = new ArrayList<>();

    private Line line;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Libraray koji učitava viewove
        ButterKnife.inject(this);
        resources = getResources();

        networkService = NetworkFactory.create(new Storage(this));

        worker = new Worker();
        handler.post(worker);

        lineGraph.setOnPointClickedListener(new LineGraph.OnPointClickedListener() {
            @Override
            public void onClick(int lineIndex, int pointIndex) {
                final int value = currentData.get(pointIndex).value;
                final String readableString = String.format("%.1f", value / 10f);
                Toast.makeText(MainActivity.this, "Selected temp is: " + readableString + " C, " + currentData.get(pointIndex).time, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onStop() {
        worker.autoUpdate = false;
        super.onStop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.history_data)
    public void historyDataClicked() {

        try {
            final int day = Integer.parseInt(this.day.getText().toString());
            final int month = Integer.parseInt(this.month.getText().toString()) - 1;
            final int year = Integer.parseInt(this.year.getText().toString());

            final Calendar nowCalender = Calendar.getInstance();
            final Calendar targetCalender = Calendar.getInstance();
            targetCalender.set(year, month, day);

            Log.d(TAG, "now : " + nowCalender.getTime());
            Log.d(TAG, "target : " + targetCalender.getTime());

            if (nowCalender.compareTo(targetCalender) < 0) {
                Toast.makeText(this, "Unesi ispravan datum (prije današnjeg)", Toast.LENGTH_LONG).show();
                return;
            }

        } catch (Exception e) {
            Toast.makeText(this, "Unesi potpuni datum", Toast.LENGTH_LONG).show();
            return;
        }


        Toast.makeText(this, "Prikazujem za datum: " + day.getText().toString() + "." + month.getText().toString() + "." + year.getText().toString(), Toast.LENGTH_SHORT).show();

        worker.autoUpdate = false;
        handler.removeCallbacks(worker);
        historyDisplayed = true;

        networkService.listHistory(year.getText().toString(), month.getText().toString(), day.getText().toString(), historyMeasurementResponse);
    }

    @OnClick(R.id.real_time_data)
    public void realTimeData() {

        if (!historyDisplayed) {
            Toast.makeText(this, "Live podaci se već prikazuju !", Toast.LENGTH_SHORT).show();
            return;
        }

        worker.firstRun = true;

        currentData.clear();
        lineGraph.removeAllLines();

        Toast.makeText(this, "Pokazujem live podatke!", Toast.LENGTH_SHORT).show();

        worker.autoUpdate = true;
        handler.post(worker);
        historyDisplayed = false;
    }

    private void setData(List<Value> data) {
        currentData.clear();
        currentData.addAll(data);
    }

    private void displayData(List<Value> data) {
        lineGraph.removeAllLines();
        lineGraph.setLineToFill(0);

        line = new Line();
        line.setColor(Color.parseColor("#E53935"));
        line.setStrokeWidth(resources.getDimensionPixelOffset(R.dimen.stroke_width));

        int max = Integer.MIN_VALUE;
        int min = Integer.MAX_VALUE;

        for (int i = 0; i < data.size(); i++) {
            line.addPoint(new LinePoint(i, data.get(i).value));

            if (max < data.get(i).value) {
                max = data.get(i).value;
            }
            if (min > data.get(i).value) {
                min = data.get(i).value;
            }
        }

//        for(int i= data.size(); i<=24; i++) {
//            currentData.add(new Value(min - 2));
//        }

        lineGraph.setRangeX(0, data.size() > 30 ? data.size() : 30);
        lineGraph.setRangeY(min - 2, max + 2);

        graphMax.setText("" + (max / 10f) + "C");
        graphMin.setText("" + (min / 10f) + "C");

        lineGraph.addLine(line);
    }

    private void sendToConfigActivity() {

        startActivity(new Intent(this, StartupActivity.class));
        finish();

    }

    private class ResponseCallback implements Callback<List<Value>> {
        @Override
        public void success(List<Value> measurementResponse, Response response) {
            Collections.reverse(measurementResponse);
            setData(measurementResponse);
            displayData(measurementResponse);
            Log.d(TAG, "ResponseCallback.success");
        }

        @Override
        public void failure(RetrofitError error) {
            Log.d(TAG, "ResponseCallback.failure" + error.getMessage());
            Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
            sendToConfigActivity();
        }
    }

    private class LastMeasurementResponse implements Callback<LastMeasurement> {
        @Override
        public void success(LastMeasurement lastMeasurement, Response response) {
            currentData.add(lastMeasurement.lastValue);
            while(currentData.size() > 30) {
                currentData.remove(0);
            }

            displayData(currentData);
        }

        @Override
        public void failure(RetrofitError error) {
            Log.d(TAG, "LastMeasurementResponse.failure " + error.getMessage());
            Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
            sendToConfigActivity();
        }
    }

    private class HistoryMeasurementResponse implements Callback<List<Value>> {
        @Override
        public void success(List<Value> value, Response response) {

            if (value == null) {
                Toast.makeText(MainActivity.this, "Nema podataka za odabran datum", Toast.LENGTH_SHORT).show();
                currentData = new ArrayList<>();
            } else {
                currentData = value;
            }

            line = new Line();

            int max = Integer.MIN_VALUE;
            int min = Integer.MAX_VALUE;

            for(Value value1 : currentData) {
                line.addPoint(new LinePoint(value1.counter, value1.value));
                if(max < value1.value) {
                    max = value1.value;
                }
                if(min > value1.value) {
                    min = value1.value;
                }
            }
            lineGraph.removeAllLines();

            lineGraph.setRangeX(0, 24);
            lineGraph.setRangeY(min - 5, max + 5);

            graphMax.setText("" + (max / 10f) + "C");
            graphMin.setText("" + (min/10f) + "C");

            line.setColor(Color.parseColor("#D49F43"));
            line.setStrokeWidth(resources.getDimensionPixelOffset(R.dimen.stroke_width));

            lineGraph.addLine(line);

            Log.d(TAG, "HistoryMeasurementResponse.success");
        }

        @Override
        public void failure(RetrofitError error) {
            Log.d(TAG, error.getMessage());
        }
    }


    private final class Worker implements Runnable {

        //Ovo mijenja interval osvježavanja
        private static final int REFRESH_INTERVAL = 10_000;

        private boolean autoUpdate = true;

        private boolean firstRun = true;

        @Override
        public void run() {

            if(firstRun) {
                networkService.listMeasurements(responseCallback);
                firstRun = false;
            } else {
                networkService.getLastData(lastMeasurementResponse);
            }

            if (autoUpdate) {
                handler.postDelayed(this, REFRESH_INTERVAL);
            }

        }

    }

}
