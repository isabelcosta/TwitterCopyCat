package com.example.android.twittercopycat.SugarClasses;

import com.orm.SugarRecord;

import java.net.URI;

/**
 * Created by IsabelCosta on 22-04-2016.
 */
public class SugarPublicTweet extends SugarRecord{

    Long id;
    String authorName;
    URI authorPicture;
    String authorDescription;
    String date;
    String text;

    public SugarPublicTweet() {

    }
    public SugarPublicTweet(Long id, String authorName, URI authorPicture, String authorDescription, String date, String text) {
        this.id = id;
        this.authorName = authorName;
        this.authorPicture = authorPicture;
        this.authorDescription = authorDescription;
        this.date = date;
        this.text = text;
    }
}
