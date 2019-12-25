package com.artisanter.feedapp;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashSet;
import java.util.Set;

// История каналов которые вводил пользователь
class History {
    private SharedPreferences preferences; // Интерфейс для доступа и изменения данных предпочтений
    private Set<String> defaultSet;

    History(Activity activity){
        preferences = activity.getPreferences(Context.MODE_PRIVATE);
        defaultSet = new HashSet<>();
        defaultSet.add(activity.getResources().getString(R.string.default_url));
    }

    Set<String> get(){
        return preferences.getStringSet("history", defaultSet);
    }

    void update(String url){
        SharedPreferences.Editor editor = preferences.edit(); // Вызов редактора
        Set<String> set = get(); // Устанавливаем историю каналов поиска
        set.add(url);
        editor.putStringSet("history", set);
        editor.apply(); // Принятие изменений
    }
}