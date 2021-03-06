package com.example.android.twittercopycat.application;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.example.android.twittercopycat.R;
import com.example.android.twittercopycat.receivers.MyAlarmReceiver;
import com.example.android.twittercopycat.helpers.Constants;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.orm.SugarApp;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by IsabelCosta on 20-04-2016.
 */
public class TwitterCopyCatApplication extends SugarApp {

    private static final String LOG_TAG = "TwitterCopyCatApp";
    public static final String SHARED_PREFS_FILENAME = "TwitterCopyCatSharedPrefs";
    public static final String SAVE_CREDENTIALS = "isLogged";
    private int _numberOfTweetsPref;
    private boolean _wifiOnlyPref;
    private int _syncFrequencyPref;
    private String _apiUrl;
    private SharedPreferences _sharedPrefs;
    private String _username;
    private String _password;
    private LinkedList<String> offlineTweets = new LinkedList<>();
    private DisplayImageOptions options;
    // Implementation of singleton pattern
    private static TwitterCopyCatApplication instance;

    private static boolean activityVisible;

    @Override
    public void onCreate()
    {
        super.onCreate();

        // Implementation of singleton pattern
        instance = this;

        // TODO: 11-05-2016 change to default shared preferences
        // FIXME: 11-05-2016 bug preferences settings saved in different files
        // Define application shared preferences
        _sharedPrefs = getSharedPreferences(SHARED_PREFS_FILENAME, Context.MODE_PRIVATE);
//        _sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        //setFirstTime();

        // Get credentials in case the user saved credentials
        if(isLogged()){
            _username = _sharedPrefs.getString(Constants.USERNAME, null);
            _password = _sharedPrefs.getString(Constants.PASSWORD, null);
        }

        _apiUrl = getResources().getString(R.string.api_url);

        // Load onto the application the settings preferences
        loadSettingsPreferences();

        // Alarm to notify about new tweets
        setMyAlarmCheckForNewTweets();

        // Image Loader
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
                .build();

        options  = new DisplayImageOptions.Builder().cacheInMemory(true).build();
        ImageLoader.getInstance().init(config);
    }


    public static boolean isActivityVisible() {
        return activityVisible;
    }

    public static void activityResumed() {
        activityVisible = true;
    }

    public static void activityPaused() {
        activityVisible = false;
    }


    // Implementation of singleton pattern
    public static TwitterCopyCatApplication getInstance() {
        return instance;
    }

    public DisplayImageOptions getOptions() {
        return options;
    }

    public SharedPreferences getAppSharedPreferences(){
        return _sharedPrefs;
    }

    public void saveCredentials(boolean toSave, String username, String password) {
        SharedPreferences.Editor editor = _sharedPrefs.edit();
        editor.putBoolean(SAVE_CREDENTIALS, toSave);
        setUsername(username);
        setPassword(password);
        if(toSave){
            editor.putString(Constants.USERNAME, username);
            editor.putString(Constants.PASSWORD, password);
        }
        editor.apply();
    }

    public void setFirstTime() {
        if(_sharedPrefs.getBoolean(Constants.IS_FIRST_TIME, true)) {
            SharedPreferences.Editor editor = _sharedPrefs.edit();
            editor.putBoolean(Constants.IS_FIRST_TIME, false);
            editor.apply();
        }
    }

    public boolean isFirstTime(){
        return !_sharedPrefs.contains(Constants.IS_FIRST_TIME);
    }

    public void loadSettingsPreferences(){
        _numberOfTweetsPref = _sharedPrefs.getInt(Constants.NUMBER_OF_TWEETS_PREF, 5);
        _wifiOnlyPref = _sharedPrefs.getBoolean(Constants.WIFI_ONLY_PREF, false);
        _syncFrequencyPref = _sharedPrefs.getInt(Constants.SYNC_FREQUENCY_PREF, 15);
    }

    public void setMyAlarmCheckForNewTweets(){
        Intent alarmIntent = new Intent(this, MyAlarmReceiver.class);
//        long scTime = 60* 10000;// 10 minutes
        long scTime = 15000;// 15 seconds
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, alarmIntent, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
//        alarmManager.set(
//                AlarmManager.RTC_WAKEUP,
//                System.currentTimeMillis() + scTime,
//                pendingIntent);

        alarmManager.setInexactRepeating(
                AlarmManager.ELAPSED_REALTIME_WAKEUP,
                scTime,
                scTime,
                pendingIntent
        );
    }

    public boolean isLogged() {
        return _sharedPrefs.getBoolean(SAVE_CREDENTIALS, false);
    }

    public String getUsername(){
        return _username;
    }

    public String getPassword(){
        return _password;
    }

    public void setUsername(String username){
        this._username = username;
    }

    public void setPassword(String password){
        this._password = password;
    }

    public String getApiUrl(){
        return _apiUrl;
    }

    // Check for Network Connection

    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

        boolean isNetAvailable = activeNetworkInfo != null && activeNetworkInfo.isConnected();

        if (isNetAvailable) {
            if (this.getWifiOnlyPref()) {
                if(activeNetworkInfo.getType() != ConnectivityManager.TYPE_WIFI){
                    isNetAvailable = false;
                }
            }
        }

        //should check null because in air plan mode it will be null
        return isNetAvailable;
    }

    // Offline tweets methods

    public void addOfflineTweet(String offTweet){
        offlineTweets.add(offTweet);
    }

    public List<String> getOfflineTweets() {
        return offlineTweets;
    }

    public String removeSentOfflineTweet() {
        return offlineTweets.removeFirst();
    }

    // Preference Settings Getters and Setters

    public void setNumberOfTweetsPref(int numberOfTweets){
        _numberOfTweetsPref = numberOfTweets;
        Log.d(LOG_TAG, Constants.NUMBER_OF_TWEETS_PREF + " - " + _numberOfTweetsPref);

    }
    public void setWifiOnlyPref(boolean wifiOnly){
        _wifiOnlyPref = wifiOnly;
    }
    public void setSyncFrequencyPref(int syncFrequency){
        _syncFrequencyPref = syncFrequency;
    }

    public int getNumberOfTweetsPref(){
        return _numberOfTweetsPref;
    }
    public boolean getWifiOnlyPref(){
        return _wifiOnlyPref;
    }
    public int getSyncFrequencyPref(){
        return _syncFrequencyPref;
    }
}
