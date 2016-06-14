package com.example.android.twittercopycat.services;

import android.content.Intent;
import android.widget.RemoteViewsService;

import com.example.android.twittercopycat.helpers.TweetsViewsFactory;

public class WidgetService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return (new TweetsViewsFactory(this.getApplicationContext(), intent));
    }
}
