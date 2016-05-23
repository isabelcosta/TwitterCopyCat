package com.example.android.twittercopycat.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

import com.example.android.twittercopycat.helpers.Constants;
import com.example.android.twittercopycat.services.MySendOfflineService;
import com.example.android.twittercopycat.TwitterCopyCatApplication;

/**
 * Detects Network connection either mobile or wifi
 */
public class NetworkChangeBroadcastReceiver extends BroadcastReceiver {

    private static String TAG = "NetworkChangeBroadcastReceiver";

    public NetworkChangeBroadcastReceiver() {
    }

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

        Log.d(TAG, "Wifi state - " + (isWiFi ? Constants.AVAILABLE : Constants.UNAVAILABLE));
        Log.d(TAG, "Mobile state - " + (isMobile ? Constants.AVAILABLE : Constants.UNAVAILABLE));

        String mode;

        // TODO: 16-05-2016 Possibly improve this if condition
        if (TwitterCopyCatApplication.getInstance().getWifiOnlyPref()){
            if (isWiFi) {
                // Send offline tweet only through wifi
                // Start sending offline tweets
                Intent sendOfflineTweetsIntent = new Intent(context, MySendOfflineService.class);
                context.startService(sendOfflineTweetsIntent);
            }
        } else {
            // Send offline tweet through any "type of connection"
            if (isWiFi || isMobile) {
                // Start sending offline tweets
                Intent sendOfflineTweetsIntent = new Intent(context, MySendOfflineService.class);
                context.startService(sendOfflineTweetsIntent);
            }
        }

        // Alert the user for network connection

        if (isWiFi || isMobile) {
            mode = Constants.ONLINE_MODE;
        } else {
            mode = Constants.OFFLINE_MODE;
        }

        Toast.makeText(
                context,
                mode,
                Toast.LENGTH_SHORT
        ).show();
    }
}
