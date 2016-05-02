package com.example.android.twittercopycat;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.orm.SugarApp;

import java.net.InetAddress;

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

    @Override
    public void onCreate()
    {
        super.onCreate();
        _sharedPrefs = getSharedPreferences(SHARED_PREFS_FILENAME, Context.MODE_PRIVATE);
        if(isLogged()){
            _username = _sharedPrefs.getString(USERNAME, null);
            _password = _sharedPrefs.getString(PASSWORD, null);
        }
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

    // Check for Internet Connection

    public boolean isInternetAvailable() {
        try {
            InetAddress ipAddr = InetAddress.getByName("google.com"); //You can replace it with your name

            if (ipAddr.equals("")) {
                return false;
            } else {
                return true;
            }

        } catch (Exception e) {
            return false;
        }

    }

}
