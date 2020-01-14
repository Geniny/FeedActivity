package com.geniny.feedapp;

import android.content.Context;
import android.os.AsyncTask;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class Save extends AsyncTask<Void, Void, Void>
{
    private Context context;
    private Articles articles;
    private static final String filename = "file";

    Save(Context context, Articles articles){
        this.context = context;
        this.articles = articles;
    }

    @Override
    protected Void doInBackground(Void... voids)
    {
        try
        {
            save();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    private void save() throws IOException
    {
        File file = new File(context.getFilesDir(), filename);
        FileOutputStream fos = new FileOutputStream(file);
        ObjectOutputStream os = new ObjectOutputStream(fos);
        os.writeObject(articles);
        os.close();
        fos.close();
    }
}
