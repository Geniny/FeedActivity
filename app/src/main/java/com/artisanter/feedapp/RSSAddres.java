package com.artisanter.feedapp;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

// Для работы с RSS каналами
class RSSAddres {
    private SharedPreferences preferences;  // Предпочтения пользователя
    private String defaultURL;
    private String rss;

    RSSAddres(Activity activity){
        preferences = activity.getPreferences(Context.MODE_PRIVATE);
        defaultURL = activity.getResources().getString(R.string.default_url);
        rss = activity.getResources().getString(R.string.rss);
    }

    String get(){
        return preferences.getString(rss, defaultURL);
    }

    void set(String url){
        SharedPreferences.Editor editor = preferences.edit(); // Открытие редактора
        editor.putString(rss, url); // Установка строки
        editor.apply(); // Принятие изменеий
    }
}
