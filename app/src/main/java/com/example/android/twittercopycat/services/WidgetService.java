package com.example.android.twittercopycat.services;

import android.content.Intent;
import android.widget.RemoteViewsService;

import com.example.android.twittercopycat.helpers.TweetsViewsFactory;

public class WidgetService extends RemoteViewsService {
    /*
    * So pretty simple just defining the Adapter of the listview
    * here Adapter is ListProvider
    * */

    private final static int WIDGET_NUMBER_OF_TWEETS = 5;

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {

/*
        int appWidgetId = intent.getIntExtra(
                AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID
        );

        TwitterCopyCatApplication app = TwitterCopyCatApplication.getInstance();

        String[] items={"lorem", "ipsum", "dolor",
                "sit", "amet", "consectetuer",
                "adipiscing", "elit", "morbi",
                "vel", "ligula", "vitae",
                "arcu", "aliquet", "mollis",
                "etiam", "vel", "erat",
                "placerat", "ante",
                "porttitor", "sodales",
                "pellentesque", "augue",
                "purus"};

        if(app.isNetworkAvailable()){
            // TODO: 08-06-2016 put get public timeline in helper
            //Check for public Timeline
            Twitter t = new Twitter();
            t.setAPIRootUrl(app.getApiUrl());
            t.setCount(WIDGET_NUMBER_OF_TWEETS);

            // Get the last 5 tweets
            List<Twitter.Status> tweets = t.getPublicTimeline();
        }
*/
        return (new TweetsViewsFactory(this.getApplicationContext(), intent));
    }

}
