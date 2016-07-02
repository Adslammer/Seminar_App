package jaksic.fer.tel.hr.seminar;

import com.google.gson.annotations.SerializedName;

/**
 * Ova klasa predstavlja jedno mjerenje koje se sastoji od countera(to smo mi dodali), id mjerenja, vremena kad se dogodilo i vriejdnost mjerenjea
 */
public class Value {

    @SerializedName("counter")
    public int counter;

    @SerializedName("id")
    public int id;

    @SerializedName("time")
    public String time;

    @SerializedName("value")
    public int value;

    public Value() {

    }

    public Value(int value) {
        this.value = value;
    }

}
