package com.example.android.twittercopycat;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

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

        Log.d(TAG, "Wifi state - " + (isWiFi ? Constants.AVAILABLE : Constants.UNAVAILABLE));
        Log.d(TAG, "Mobile state - " + (isMobile ? Constants.AVAILABLE : Constants.UNAVAILABLE));

        String mode;

        if (isWiFi || isMobile) {
            mode = Constants.ONLINE_MODE;
            // Start sending offline tweets
            Intent sendOfflineTweetsIntent = new Intent(context, MySendOfflineService.class);
            context.startService(sendOfflineTweetsIntent);
        } else {
            mode = Constants.OFFLINE_MODE;
        }

        Toast.makeText(
                context,
                mode,
                Toast.LENGTH_SHORT
        ).show();

//        // TODO: This method is called when the BroadcastReceiver is receiving
//        // an Intent broadcast.
//        throw new UnsupportedOperationException("Not yet implemented");
    }
}
