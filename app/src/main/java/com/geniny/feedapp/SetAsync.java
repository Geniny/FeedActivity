package com.geniny.feedapp;

import android.os.AsyncTask;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;


public class SetAsync extends AsyncTask<Void, Void, Object> {

    private MainActivity context;
    private static final String filename = "file";

    SetAsync(MainActivity context)
    {
        this.context = context;
    }

    @Override
    protected Object doInBackground(Void... voids) {
        try
        {
            return set();
        }
        catch (Exception e){
            return e;
        }
    }

    @Override
    protected void onPostExecute(Object data) {
        super.onPostExecute(data);
        if (data instanceof Exception)
            context.onError((Exception) data);
        else {
            context.setArticlesContext((Articles) data);
        }
    }

    private Articles set() throws IOException, ClassNotFoundException{
        File file = new File(context.getFilesDir(), filename);
        FileInputStream fis = new FileInputStream(file);
        ObjectInputStream is = new ObjectInputStream(fis);
        Articles articles;
        articles = (Articles)is.readObject();
        is.close();
        fis.close();
        return articles;
    }

}
