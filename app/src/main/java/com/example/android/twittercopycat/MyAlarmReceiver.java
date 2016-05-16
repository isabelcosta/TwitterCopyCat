package com.example.android.twittercopycat;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class MyAlarmReceiver extends BroadcastReceiver {
    private String LOG_TAG = "MyAlarmReceiver";

    public MyAlarmReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(LOG_TAG, "Alarm Received! YAAAY");
        Intent i = new Intent(context, NewTweetService.class);
        context.startService(i);
    }
}
