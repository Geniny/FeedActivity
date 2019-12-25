package com.artisanter.feedapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

// Используется для получения сообщения о изменении состояния сети
public class NetworkStateReceiver extends BroadcastReceiver
{
    private BaseActivity activity;
    NetworkStateReceiver(BaseActivity activity){
        this.activity = activity;
    }

    @Override
    public void onReceive(Context context, Intent intent)
    {
        activity.onNetworkChange(getState()); // Вызывается при изменении состояния сети
    }

    public boolean getState() // Проверяет есть ли подключение или нет 
    {
        ConnectivityManager manager = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        assert manager != null; // завершает программу сразу же после обнаружения некорректных данных
        NetworkInfo ni = manager.getActiveNetworkInfo();
        return ni != null && ni.getState() == NetworkInfo.State.CONNECTED;
    }
}