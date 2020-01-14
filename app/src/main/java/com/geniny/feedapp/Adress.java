package com.geniny.feedapp;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

class Adress {
    private SharedPreferences preferences;
    private String defaultURL;
    private String rss;

    Adress(Activity activity){
        preferences = activity.getPreferences(Context.MODE_PRIVATE);
        defaultURL = "";
        rss = "RSS:";
    }

    String get(){
        return preferences.getString(rss, defaultURL);
    }

    void set(String url){
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.putString(rss, url);
        editor.apply();
    }
}
