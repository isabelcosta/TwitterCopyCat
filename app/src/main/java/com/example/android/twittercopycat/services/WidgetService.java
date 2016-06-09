package com.example.android.twittercopycat.services;

import android.content.Intent;
import android.widget.RemoteViewsService;

import com.example.android.twittercopycat.application.TwitterCopyCatApplication;
import com.example.android.twittercopycat.helpers.TweetsViewsFactory;

public class WidgetService extends RemoteViewsService {
    /*
    * So pretty simple just defining the Adapter of the listview
    * here Adapter is ListProvider
    * */

    private final static int WIDGET_NUMBER_OF_TWEETS = 5;
    private TwitterCopyCatApplication app = TwitterCopyCatApplication.getInstance();
    private String[] tweets = new String[WIDGET_NUMBER_OF_TWEETS];

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return (new TweetsViewsFactory(this.getApplicationContext(), intent));
    }
}
