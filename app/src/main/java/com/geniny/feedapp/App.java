package com.geniny.feedapp;

import android.app.Application;

import com.squareup.picasso.LruCache;
import com.squareup.picasso.Picasso;

public class App extends Application
{
    private static Articles commonArticles = null;

    public static Articles getArticles()
    {
        return commonArticles;
    }

    public static void setArticles(Articles articles)
    {
        commonArticles = articles;
    }


    @Override
    public void onCreate()
    {
        super.onCreate();
        setPicasso();
    }

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
