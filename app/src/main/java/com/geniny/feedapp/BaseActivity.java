package com.geniny.feedapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public abstract class BaseActivity extends AppCompatActivity {
    private Receiever stateReceiver;
    private boolean isConnected;
    protected Toast onlineToast, offlineToast, cashToast;
    protected Adress currentAdress;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        onlineToast =  Toast.makeText(this, "Cоединение установлено", Toast.LENGTH_LONG);
        offlineToast = Toast.makeText(this, "Нет сети", Toast.LENGTH_LONG);
        cashToast = Toast.makeText(this, "Загружено", Toast.LENGTH_LONG);
        currentAdress = new Adress(this);
        stateReceiver = new Receiever(this);
        isConnected = stateReceiver.getState();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(stateReceiver, filter);
        onNetworkChange(stateReceiver.getState());
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        unregisterReceiver(stateReceiver);
    }

    void onNetworkChange(boolean isOnline)
    {
        if(this.isConnected == isOnline)
            return;
        this.isConnected = isOnline;
        if(isOnline)
            onlineToast.show();
        else
            offlineToast.show();
    }

    public boolean isConnected()
    {
        return isConnected;
    }

    void setArticlesContext(Articles articles){}

    void onError(Exception e)
    {
        Toast.makeText(this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
    }

    public class Receiever extends BroadcastReceiver
    {
        private BaseActivity activity;

        Receiever(BaseActivity activity)
        {
            this.activity = activity;

        }

        @Override
        public void onReceive(Context context, Intent intent)
        {
            activity.onNetworkChange(getState());
        }

        public boolean getState()
        {
            ConnectivityManager manager = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = manager.getActiveNetworkInfo();
            return networkInfo != null && networkInfo.getState() == NetworkInfo.State.CONNECTED;
        }
    }
}
