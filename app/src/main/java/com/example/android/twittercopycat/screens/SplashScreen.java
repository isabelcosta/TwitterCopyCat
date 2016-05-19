package com.example.android.twittercopycat.screens;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.example.android.twittercopycat.R;
import com.example.android.twittercopycat.TwitterCopyCatApplication;

public class SplashScreen extends Activity {

    // Splash screen timer
    private static int SPLASH_TIME_OUT = 2000;
    private static String TAG = "SplashScreen";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity

                Intent i;
                boolean isLogged = TwitterCopyCatApplication.getInstance().isLogged();
                Log.d(TAG, "Is the user logged? " + isLogged);
                if(isLogged){
                    i = new Intent(SplashScreen.this, MyTimelineScreen.class);
                } else {
                    i = new Intent(SplashScreen.this, LoginScreen.class);
                }
                startActivity(i);

                // close this activity
                finish();
            }
        }, SPLASH_TIME_OUT);
    }
}
