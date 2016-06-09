package com.example.android.twittercopycat.helpers;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.android.twittercopycat.R;
import com.example.android.twittercopycat.TCCWidgetProvider;
import com.example.android.twittercopycat.application.TwitterCopyCatApplication;

import java.util.List;

import winterwell.jtwitter.Twitter;

public class TweetsViewsFactory implements RemoteViewsService.RemoteViewsFactory {

//    private static final String[] tweets={"lorem", "ipsum", "dolor",
//            "sit", "amet", "consectetuer",
//            "adipiscing", "elit", "morbi",
//            "vel", "ligula", "vitae",
//            "arcu", "aliquet", "mollis",
//            "etiam", "vel", "erat",
//            "placerat", "ante",
//            "porttitor", "sodales",
//            "pellentesque", "augue",
//            "purus"};

    private final static int WIDGET_NUMBER_OF_TWEETS = 5;
    private TwitterCopyCatApplication app = TwitterCopyCatApplication.getInstance();
    private String[] tweets = new String[WIDGET_NUMBER_OF_TWEETS];

    private Context ctxt = null;
    private int appWidgetId;

    public TweetsViewsFactory(Context ctxt, Intent intent) {
        this.ctxt = ctxt;
        appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);
    }

    @Override
    public void onCreate() {
        // no-op
    }

    @Override
    public void onDestroy() {
        // no-op
    }

    @Override
    public int getCount() {
        return(tweets.length);
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews row = new RemoteViews(ctxt.getPackageName(),
                R.layout.tweet_widget_view);

        row.setTextViewText(R.id.widget_list_text_view, tweets[position]);

        Intent intent = new Intent();
        Bundle extras = new Bundle();

        extras.putString(TCCWidgetProvider.EXTRA_WORD, tweets[position]);
        intent.putExtras(extras);
        row.setOnClickFillInIntent(R.id.widget_list_text_view, intent);

        return (row);
    }

    @Override
    public RemoteViews getLoadingView() {
        return(null);
    }

    @Override
    public int getViewTypeCount() {
        return(1);
    }

    @Override
    public long getItemId(int position) {
        return(position);
    }

    @Override
    public boolean hasStableIds() {
        return(true);
    }

    @Override
    public void onDataSetChanged() {

        if(app.isNetworkAvailable()){
            // TODO: 08-06-2016 put get public timeline in helper
            //Check for public Timeline
            Twitter t = new Twitter();
            t.setAPIRootUrl(app.getApiUrl());
            t.setCount(WIDGET_NUMBER_OF_TWEETS);

            // Get the last 5 tweets
            List<Twitter.Status> fetchedTweets = t.getPublicTimeline();

            if(!fetchedTweets.isEmpty()){
                for(int i=0; i < WIDGET_NUMBER_OF_TWEETS; i++){
                    tweets[i] = fetchedTweets.get(i).getText();
                }
            }

        } else {
            //get tweets from database
        }
    }
}