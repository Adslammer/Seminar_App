package jaksic.fer.tel.hr.seminar;

import java.util.List;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;

public interface NetworkService {

    //Ovo su tri meteode koje koristiš.

    //Ovo ti je za izlist za povijesne dane, imaš tri parametra.
    @GET("/history_data.php")
    void listHistory(@Query("year") String year, @Query("month") String month, @Query("day") String day, Callback<List<Value>> responseCallback);

    //Ovo ti je zadnjih 10 real time podataka
    @GET("/last_10.php")
    void listMeasurements(Callback<List<Value>> responseCallback);

    //Najzadnje mjerenje
    @GET("/last_measurement.php")
    void getLastData(Callback<LastMeasurement> responseCallback);

}
