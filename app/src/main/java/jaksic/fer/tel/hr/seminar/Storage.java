package jaksic.fer.tel.hr.seminar;

import android.content.Context;
import android.content.SharedPreferences;

public class Storage {

    private static final String KEY_IP_ADDRESS = "key_ip_address";

    private final SharedPreferences preferences;


    public Storage(Context context) {
        this.preferences = context.getSharedPreferences("preference", Context.MODE_PRIVATE);
    }

    public String getPlainAddres() {
        return preferences.getString(KEY_IP_ADDRESS, "");
    }

    public String getHttpAddress() {
        return "http://" + getPlainAddres();
    }

    public void setIpAddress(String ipAddress) {
        preferences.edit().putString(KEY_IP_ADDRESS, ipAddress).apply();
    }

}
