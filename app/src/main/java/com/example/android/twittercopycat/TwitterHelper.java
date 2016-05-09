package com.example.android.twittercopycat;

import android.util.Log;

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



}
