package com.example.android.twittercopycat;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class PublicTimelineScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_public_timeline_screen);
//        if(((TwitterCopyCatApplication)getApplication()).isInternetAvailable()){
            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.container, new PublicTimelineFragment())
                        .commit();
            }
//        } else {
//            Toast.makeText(getBaseContext(), "no internet connection", Toast.LENGTH_LONG).show();
//
//        }

    }
}
