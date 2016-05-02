package com.example.android.twittercopycat.SugarClasses;

import com.orm.SugarRecord;

/**
 * Created by IsabelCosta on 22-04-2016.
 */
public class SugarMyTweet extends SugarRecord {


    String text;
    long tweetId;
    String authorName;
    String authorDescription;
    String authorPicture;
    String date;

    public SugarMyTweet(){

    }

    public SugarMyTweet(String text, long tweetId, String authorName, String authorDescription, String authorPicture, String date) {
        this.text = text;
        this.tweetId = tweetId;
        this.authorName = authorName;
        this.authorDescription = authorDescription;
        this.authorPicture = authorPicture;
        this.date = date;
    }
}
