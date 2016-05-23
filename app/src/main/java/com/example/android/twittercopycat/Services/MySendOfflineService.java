package com.example.android.twittercopycat.services;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.example.android.twittercopycat.R;
import com.example.android.twittercopycat.application.TwitterCopyCatApplication;

import java.util.LinkedList;
import java.util.List;

import winterwell.jtwitter.Twitter;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class MySendOfflineService extends IntentService {
    private static final String LOG_TAG = "MySendOfflineService";

    public MySendOfflineService() {
        super("MySendOfflineService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {

            TwitterCopyCatApplication app = TwitterCopyCatApplication.getInstance();
            List<String> tweetsCopy = new LinkedList<>(app.getOfflineTweets());

            Twitter t = new Twitter(app.getUsername(), app.getPassword());
            t.setAPIRootUrl(app.getApiUrl());

            for(String tweet : tweetsCopy) {
                t.updateStatus(tweet);
                Log.d(LOG_TAG, "Sending this tweet: " + tweet);
//                Log.d(LOG_TAG, "Removing this tweet: " + app.removeSentOfflineTweet());
            }

            Toast.makeText(
                    getApplicationContext(),
                    getString(R.string.offline_tweets_sent),
                    Toast.LENGTH_LONG
            ).show();
        }
    }
}
