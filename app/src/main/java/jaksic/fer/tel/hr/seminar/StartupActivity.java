package jaksic.fer.tel.hr.seminar;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class StartupActivity extends AppCompatActivity {

    private static final String IPADDRESS_PATTERN =
            "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
            "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
            "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
            "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";

    private static Pattern pattern = Pattern.compile(IPADDRESS_PATTERN);

    @InjectView(R.id.startup_edit_text)
    protected EditText ipAddress;

    private Storage storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startup);
        ButterKnife.inject(this);

        Toast.makeText(this, "Unesite IP adresu servera gdje se vrte skripte za dohvat podataka", Toast.LENGTH_SHORT).show();

        storage = new Storage(this);
        ipAddress.setText(storage.getPlainAddres());
    }

    @OnClick(R.id.startup_next_button)
    protected void buttonNextClicked() {
        final String value = ipAddress.getText().toString();
        final Matcher matcher = pattern.matcher(value);

        if(matcher.matches()) {
            storage.setIpAddress(value);
            startActivity(new Intent(this, MainActivity.class));
        } else {
            Toast.makeText(this, "Krivi format IP adrese", Toast.LENGTH_LONG).show();
        }
    }

}
