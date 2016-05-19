package com.example.android.twittercopycat.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.android.twittercopycat.services.NewTweetService;

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
