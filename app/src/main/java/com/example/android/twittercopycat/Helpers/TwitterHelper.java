package com.example.android.twittercopycat.helpers;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.android.twittercopycat.R;
import com.example.android.twittercopycat.fragments.TimelineFragment;
import com.example.android.twittercopycat.TwitterCopyCatApplication;

import java.util.List;

import winterwell.jtwitter.Twitter;
import winterwell.jtwitter.TwitterException;

/**
 * Created by IsabelCosta on 09-05-2016.
 */
public class TwitterHelper {

    public static boolean sendOfflineTweets(String apiUrl, String LOG_TAG){
        try{
            TwitterCopyCatApplication app = TwitterCopyCatApplication.getInstance();

            List<String> tweets = app.getOfflineTweets();

            Twitter t = new Twitter(app.getUsername(), app.getPassword());
            t.setAPIRootUrl(apiUrl);
            for(String tweet : tweets) {
                t.updateStatus(tweet);
                Log.d(LOG_TAG, "Sending this tweet: " + tweet);
                Log.d(LOG_TAG, "Removing this tweet: " + app.removeSentOfflineTweet());
            }
        } catch (TwitterException e){
            return false;
        }
        return true;
    }

    public static void showNotification(String tweet, Context ctx) {

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(ctx)
                        .setSmallIcon(R.drawable.notification_icon)
                        .setContentTitle("New Tweet!")
                        .setContentText(tweet);

        Intent resultIntent = new Intent(ctx, TimelineFragment.class);
//        ...
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

//        ...
        // Sets an ID for the notification
        int mNotificationId = 001;
        // Gets an instance of the NotificationManager service
        NotificationManager mNotifyMgr =
                (NotificationManager) ctx.getSystemService(ctx.NOTIFICATION_SERVICE);
        // Builds the notification and issues it.
        mNotifyMgr.notify(mNotificationId, mBuilder.build());
//
//        // Set the icon, scrolling text and timestamp
//        Notification notification = new Notification(R.drawable.noti_icon,
//                "Brand new tweet!", System.currentTimeMillis());
//
//        // The PendingIntent to launch our activity if the user selects this
//        // notification
//        PendingIntent contentIntent = PendingIntent.getActivity(ctx, 0,
//                new Intent(ctx, MyTimelineScreen.class), 0);
//
//        // Set the info for the views that show in the notification panel.
//        notification.setLatestEventInfo(ctx, "New Tweet? like i think", tweet,
//                contentIntent);
//
//        // Send the notification.
//        mNotifyMgr.notify("Timeline updated!", 0, notification);
    }

}
