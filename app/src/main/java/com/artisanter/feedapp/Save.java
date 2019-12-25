package com.artisanter.feedapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class Save extends AsyncTask<Void, Void, Void> {
    // Context
    // Интерфейс для глобальной информации о среде приложения.
    // Это абстрактный класс, реализация которого обеспечивается системой Android.
    // Он позволяет получить доступ к ресурсам и классам, относящимся к конкретному приложению.
    @SuppressLint("StaticFieldLeak")
    private Context context;
    private AllRecords allRecords;
    private static final String filename = "file";

    Save(Context context, AllRecords allRecords){
        this.context = context;
        this.allRecords = allRecords;
    }

    @Override
    protected Void doInBackground(Void... voids)
    {
        try
        {
            save();
        }catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }

    private void save() throws IOException
    {
        File file = new File(context.getFilesDir(), filename);  // Файл для записи
        FileOutputStream fos = new FileOutputStream(file);  // Поток записи - принимает файл для записи
        ObjectOutputStream os = new ObjectOutputStream(fos);  // Сереализация
        os.writeObject(allRecords);
        os.close();
        fos.close();
    }
}
