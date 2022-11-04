package com.example.videostream;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkConnectivity extends BroadcastReceiver {
    private NetworkListner networkListner;

    public NetworkConnectivity(NetworkListner networkListner) {
        this.networkListner = networkListner;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (isNetwokOn(context.getApplicationContext())) {
            networkListner.getState(0);
        } else {
            networkListner.getState(1);
        }
    }

    private static boolean isNetwokOn(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return (netInfo != null && netInfo.isConnected());
    }

}
