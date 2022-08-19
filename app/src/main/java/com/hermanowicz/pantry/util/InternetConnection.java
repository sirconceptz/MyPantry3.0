package com.hermanowicz.pantry.util;

import android.content.Context;
import android.net.ConnectivityManager;

public class InternetConnection {

    private final Context context;

    public InternetConnection(Context context){
        this.context = context;
    }

    public boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }
}