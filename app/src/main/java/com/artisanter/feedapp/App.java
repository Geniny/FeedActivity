package com.artisanter.feedapp;

import android.app.Application;

import com.squareup.picasso.LruCache;
import com.squareup.picasso.Picasso;

public class App extends Application
{
    private static boolean applicationState = false;
    private static AllRecords applicationAllRecorders = null;

    public static AllRecords getApplicationAllRecorders()
    {
        return applicationAllRecorders;
    }

    public static void setApplicationAllRecorders(AllRecords state)
    {
        applicationAllRecorders = state;
    }

    public static boolean getApplicationState()
    {
        return applicationState;
    }

    public static void setApplicationState(boolean state)
    {
        applicationState = state;
    }


    @Override
    public void onCreate()
    {
        super.onCreate();
        setPicasso();
    }

    // Использование библиотеки пикасо (позволяет грузить фотки из сети) (кэширует картинки)
    void setPicasso()
    {
        Picasso picasso = new Picasso.Builder(this)
                .memoryCache(new LruCache(1024 * 1024 * 100) {
                })
                .build();
        if(android.os.Debug.isDebuggerConnected())
            picasso.setIndicatorsEnabled(true);
        Picasso.setSingletonInstance(picasso);
    }
}
