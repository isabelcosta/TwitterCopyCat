package com.example.android.twittercopycat.helpers;

import com.example.android.twittercopycat.entities.TweetItem;

import java.util.List;

import winterwell.jtwitter.Twitter;

/**
 * Created by IsabelCosta on 09-06-2016.
 */
public class TwitterCopyCatHelper {

    public static List<Twitter.Status> getOnlinePublicTimeline(String apiUrl, int numberOfTweets){
        Twitter t = new Twitter();
        t.setAPIRootUrl(apiUrl);
        t.setCount(numberOfTweets);

        // Get the last 5 tweets
        return t.getPublicTimeline();
    }

    public static List<Twitter.Status> getOnlinePrivateTimeline(
            String apiUrl,
            int numberOfTweets,
            String username,
            String password
    ){
        Twitter t = new Twitter(username, password);
        t.setAPIRootUrl(apiUrl);
        t.setCount(numberOfTweets);

        // Get the last 5 tweets
        return t.getPublicTimeline();
    }

    public static void deleteOldTweets(boolean isPublic){
        List<TweetItem> oldTweets = getOfflineTimeline(isPublic);
        for(TweetItem t : oldTweets){
            t.delete();
        }
    }

    public static List<TweetItem> getOfflineTimeline(boolean isPublic) {
        return TweetItem.find(
                TweetItem.class,
                String.format("%s = ?", TweetItem.IS_PUBLIC_COLUMN_NAME),
                isPublic ? "1" : "0");
    }

    public static long getTimeInMiliSeconds(Long hours, Long minutes, Long seconds) {
        long miliSeconds = 1000;
        long hoursInMiliSec = 0;
        long minutesInMiliSec = 0;
        long secondsInMiliSec = 0;

        if (hours != null) {
            hoursInMiliSec = hours * 60 * 60 * miliSeconds;
        }

        if (minutes != null) {
            minutesInMiliSec = minutes * 60 * miliSeconds;
        }

        if (seconds != null) {
            secondsInMiliSec = seconds * miliSeconds;
        }

        return hoursInMiliSec + minutesInMiliSec + secondsInMiliSec;
    }
}
