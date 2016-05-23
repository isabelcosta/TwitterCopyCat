package com.example.android.twittercopycat.services;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.android.twittercopycat.screens.PublicTimelineScreen;
import com.example.android.twittercopycat.R;
import com.example.android.twittercopycat.entities.TweetItem;
import com.example.android.twittercopycat.application.TwitterCopyCatApplication;

import java.util.List;

import winterwell.jtwitter.Twitter;

/**
 * Service that searches for new tweet to notify the user
 */
public class NewTweetService extends IntentService {

        private static String LOG_TAG = "NewTweetService";

    /**
     * A constructor is required, and must call the super IntentService(String)
     * constructor with a name for the worker thread.
     */
    public NewTweetService() {
        super("NewTweetService");
    }

    /**
     * The IntentService calls this method from the default worker thread with
     * the intent that started the service. When this method returns, IntentService
     * stops the service, as appropriate.
     */

    @Override
    protected void onHandleIntent(Intent intent) {

        TwitterCopyCatApplication app = TwitterCopyCatApplication.getInstance();

        if(app.isNetworkAvailable() && !TwitterCopyCatApplication.isActivityVisible()){

            // Get last tweet saved into database
            String highestIdQuery = String.format(
                            "SELECT * FROM %s WHERE %s = ? AND %s IN (SELECT MAX(%s) FROM %s)",
                            TweetItem.TABLE_NAME,
                            TweetItem.IS_PUBLIC_COLUMN_NAME,
                            TweetItem.TWEET_ID_COLUMN_NAME,
                            TweetItem.TWEET_ID_COLUMN_NAME,
                            TweetItem.TABLE_NAME
            );

            // Query the TweetItem table for the last public ("1") tweet saved
            List<TweetItem> offTweets = TweetItem.findWithQuery(TweetItem.class, highestIdQuery, "1");

            if(!offTweets.isEmpty()){
                Log.d(LOG_TAG, "Offline Tweets results da query are not empty");
                long lastSavedTweetID = offTweets.get(0).getTweetId();

                Twitter t = new Twitter();
                t.setAPIRootUrl(app.getApiUrl());
                t.setCount(1);

                // Get the last online tweet
                List<Twitter.Status> onTweets = t.getPublicTimeline();
                long lastOnlineTweetID = onTweets.get(0).getId();

                // FIXME: 16-05-2016 Query the last tweet

                Log.d(LOG_TAG,"EIS OS IDs: " + String.valueOf(lastOnlineTweetID) + "    " + String.valueOf(lastSavedTweetID));

                if(lastOnlineTweetID != lastSavedTweetID){
                    // Create Notification
                    createNewTweetNotification();
                }
            }
        }
    }

    private void createNewTweetNotification() {
        Context ctx = getApplicationContext();
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(ctx)
                        .setSmallIcon(R.drawable.notification_icon)
                        .setContentTitle(getString(R.string.notification_title_tweet))
                        .setContentText(getString(R.string.notification_text_tweet))
                        .setAutoCancel(true);

        Intent resultIntent = new Intent(ctx, PublicTimelineScreen.class);

        // Because clicking the notification opens a new ("special") activity, there's
        // no need to create an artificial back stack.
        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        ctx,
                        0,
                        resultIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );

        mBuilder.setContentIntent(resultPendingIntent);

        // Sets an ID for the notification
        int mNotificationId = 001;

        // Gets an instance of the NotificationManager service
        NotificationManager mNotifyMgr =
                (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);

        // Builds the notification and issues it.
        mNotifyMgr.notify(mNotificationId, mBuilder.build());
    }

}
