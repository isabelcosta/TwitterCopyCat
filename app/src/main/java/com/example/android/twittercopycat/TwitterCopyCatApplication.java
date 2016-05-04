package com.example.android.twittercopycat;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.orm.SugarApp;

import java.util.LinkedList;

/**
 * Created by IsabelCosta on 20-04-2016.
 */
public class TwitterCopyCatApplication extends SugarApp {

    public static final String SHARED_PREFS_FILENAME = "TwitterCopyCatSharedPrefs";
    public static final String SAVE_CREDENTIALS = "isLogged";
    private static final String PASSWORD = "password";
    private static final String USERNAME = "student";

    private SharedPreferences _sharedPrefs;
    private String _username;
    private String _password;
    private LinkedList<String> offlineTweets = new LinkedList<>();
    private DisplayImageOptions options;

    // Implementation of singleton pattern
    private static TwitterCopyCatApplication instance ;

    @Override
    public void onCreate()
    {
        super.onCreate();

        // Implementation of singleton pattern
        instance = this;

        // Get credentials in case the user saved credentials
        _sharedPrefs = getSharedPreferences(SHARED_PREFS_FILENAME, Context.MODE_PRIVATE);
        if(isLogged()){
            _username = _sharedPrefs.getString(USERNAME, null);
            _password = _sharedPrefs.getString(PASSWORD, null);
        }

        //Image Loader
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
                .build();

        options  = new DisplayImageOptions.Builder().cacheInMemory(true).build();
        ImageLoader.getInstance().init(config);
    }

    // Implementation of singleton pattern
    public static TwitterCopyCatApplication getInstance() {
        return instance;
    }

    public DisplayImageOptions getOptions() {
        return options;
    }

    public void saveCredentials(boolean toSave, String username, String password) {
        SharedPreferences.Editor editor = _sharedPrefs.edit();
        editor.putBoolean(SAVE_CREDENTIALS, toSave);
        setUsername(username);
        setPassword(password);
        if(toSave){
            editor.putString(USERNAME, username);
            editor.putString(PASSWORD, password);
        }
        editor.apply();
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

    public void setCredentialsFromSharedPrefs(){}

    // Check for Network Connection

    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void addOfflineTweet(String offTweet){
        offlineTweets.add(offTweet);
    }

}
