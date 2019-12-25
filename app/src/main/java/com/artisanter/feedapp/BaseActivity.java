package com.artisanter.feedapp;

import android.annotation.SuppressLint;
import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

// Базовый класс для всех активити приложения, кидает сообщения о состоянии сети
public abstract class BaseActivity extends AppCompatActivity {
    private NetworkStateReceiver stateReceiver;
    private boolean isOnline;
    protected Toast onlineToast;
    protected Toast offlineToast;
    protected RSSAddres RSSAddres;

    @SuppressLint("ShowToast")
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        RSSAddres = new RSSAddres(this);
        // Сообщение о том есть ли соединение
        onlineToast =  Toast.makeText(this, R.string.online, Toast.LENGTH_LONG);
        offlineToast = Toast.makeText(this, R.string.offline, Toast.LENGTH_LONG);

        stateReceiver = new NetworkStateReceiver(this); // Проверяем онлайн или нет
        isOnline = stateReceiver.getState();
    }
    @Override
    protected void onResume()
    {
        super.onResume();
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(stateReceiver, filter);  // Включаем BroadcastReceiver
        onNetworkChange(stateReceiver.getState());
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        unregisterReceiver(stateReceiver); // Выключаем BroadcastReceiver
    }

    void onNetworkChange(boolean isOnline)
    {
        if(this.isOnline == isOnline)
            return;
        this.isOnline = isOnline;
        if(isOnline)
            onlineToast.show();
        else
            offlineToast.show();
    }

    public boolean isOnline() // возвращает true если есть подключение к сети
    {
        return isOnline;
    }

    void onGetRecords(AllRecords allRecords){}

    void onError(Exception e) // базовая обработка ошибок - кидает toast с информацией о ошибке
    {
        Toast.makeText(this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
    }
}
