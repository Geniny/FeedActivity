package com.artisanter.feedapp;

import android.annotation.SuppressLint;
import android.os.AsyncTask;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;


public class Load extends AsyncTask<Void, Void, Object> {
    @SuppressLint("StaticFieldLeak")
    private MainActivity context;
    private static final String filename = "file";

    Load(MainActivity context){
        this.context = context;
    }

    @Override
    protected Object doInBackground(Void... voids) {
        try{
            return load();
        }catch (Exception e){
            return e;
        }
    }

    @Override
    protected void onPostExecute(Object data) {
        super.onPostExecute(data);
        if (data instanceof Exception)
            context.onError((Exception) data); // Вызов функции исключения из MainActivity
        else {
            context.onGetRecords((AllRecords) data);
        }
    }

    private AllRecords load() throws IOException, ClassNotFoundException{
        File file = new File(context.getFilesDir(), filename);
        FileInputStream fis = new FileInputStream(file);
        ObjectInputStream is = new ObjectInputStream(fis); // Востановление сериализованного файла
        AllRecords allRecords;
        allRecords = (AllRecords)is.readObject();
        is.close();
        fis.close();
        return allRecords; // Возвращение востановленных записей
    }

}
