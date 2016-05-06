package com.example.android.twittercopycat;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class NetworkChangeBroadcastReceiver extends BroadcastReceiver {

    private static String TAG = "NetworkChangeBroadcastReceiver";

    public NetworkChangeBroadcastReceiver() {
    }

    // TODO: 04-05-2016 detect when connection is available to send tweets  
    
    @Override
    public void onReceive(Context context, Intent intent) {

        final ConnectivityManager connectivityManager =
                (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        final NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        boolean isWiFi = false;
        boolean isMobile = false;

        if(activeNetwork != null) {
            isWiFi = activeNetwork.getType() == ConnectivityManager.TYPE_WIFI;
            isMobile = activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE;
        }

        Log.d(TAG, "Wifi state - " + (isWiFi ? "AVAILABLE" : "UNAVAILABLE"));
        Log.d(TAG, "Mobile state - " + (isMobile ? "AVAILABLE" : "UNAVAILABLE"));

        if (isWiFi || isMobile) {
            // Do something
        }

//        // TODO: This method is called when the BroadcastReceiver is receiving
//        // an Intent broadcast.
//        throw new UnsupportedOperationException("Not yet implemented");
    }
}
