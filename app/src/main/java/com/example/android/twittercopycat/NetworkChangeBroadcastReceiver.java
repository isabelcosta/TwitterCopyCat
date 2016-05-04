package com.example.android.twittercopycat;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.util.Log;

public class NetworkChangeBroadcastReceiver extends BroadcastReceiver {

    private static String TAG = "NetworkChangeBroadcastReceiver";

    public NetworkChangeBroadcastReceiver() {
    }

    // TODO: 04-05-2016 detect when connection is available to send tweets  
    
    @Override
    public void onReceive(Context context, Intent intent) {

        final ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        final android.net.NetworkInfo wifi = connectivityManager
                .getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        final android.net.NetworkInfo mobile = connectivityManager
                .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        boolean isWifiAvailable = wifi.isAvailable();
        boolean isMobileAvailable = mobile.isAvailable();

        Log.d(TAG, "Wifi state - " + (isWifiAvailable ? "AVAILABLE" : "UNAVAILABLE"));
        Log.d(TAG, "Mobile state - " + (isMobileAvailable ? "AVAILABLE" : "UNAVAILABLE"));

        if (isWifiAvailable || isMobileAvailable) {
            // Do something
        }

//        // TODO: This method is called when the BroadcastReceiver is receiving
//        // an Intent broadcast.
//        throw new UnsupportedOperationException("Not yet implemented");
    }
}
