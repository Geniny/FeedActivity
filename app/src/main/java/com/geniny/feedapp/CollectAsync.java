package com.geniny.feedapp;

import android.os.AsyncTask;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;

public class CollectAsync extends AsyncTask<Void, Void, Object>
{

    private BaseActivity context;
    private String URL;

    CollectAsync(BaseActivity context, String urlString)
    {
        this.context = context;
        this.URL = urlString;
    }

    @Override
    protected Object doInBackground(Void... ignored)
    {
        try
        {
            return collect(URL);
        }
        catch (IOException e)
        {
            return e;
        }
    }

    @Override
    protected void onPostExecute(Object data)
    {
        super.onPostExecute(data);
        if(data instanceof IOException)
            context.onError((IOException) data);
        else
            new Parser(context, (InputStream) data).execute();
    }

    private static InputStream collect(String URL) throws IOException{
        HttpURLConnection connection = connect(URL);
        int responseCode = connection.getResponseCode();
        if(responseCode == HttpURLConnection.HTTP_OK)
            return new BufferedInputStream(connection.getInputStream());
        throw new ConnectException(connection.getResponseMessage());
    }

    private static HttpURLConnection connect(String URL) throws IOException
    {
        URL url = new URL(URL);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setConnectTimeout(10000);
        connection.setReadTimeout(10000);
        connection.setDoInput(true);

        return connection;
    }
}
