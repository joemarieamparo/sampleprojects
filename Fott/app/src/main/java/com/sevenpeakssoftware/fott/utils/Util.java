package com.sevenpeakssoftware.fott.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.format.DateUtils;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Util {

    public static String formatDate(String dateTime) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        try {
            Date date = dateFormat.parse(dateTime);
            if (DateUtils.isToday(date.getTime())) {
                SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
                return "Today, " + timeFormat.format(date);
            } else {
                SimpleDateFormat dayFormat = new SimpleDateFormat("dd MMMM, HH:mm");
                return dayFormat.format(date);
            }
        } catch (ParseException e) {
            Log.e("JOME", " ERROR : " + e.getMessage());
            return dateTime;
        }
    }

    public static boolean isNetworkConnectionAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        if (info == null) return false;
        NetworkInfo.State network = info.getState();
        return (network == NetworkInfo.State.CONNECTED || network == NetworkInfo.State.CONNECTING);
    }
}
