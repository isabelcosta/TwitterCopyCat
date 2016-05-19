package com.example.android.twittercopycat.screens;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.android.twittercopycat.fragments.PublicTimelineFragment;
import com.example.android.twittercopycat.R;

public class PublicTimelineScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_public_timeline_screen);
            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.container, new PublicTimelineFragment())
                        .commit();
            }

    }
}
