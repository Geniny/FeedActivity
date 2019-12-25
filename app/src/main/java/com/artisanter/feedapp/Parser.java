package com.artisanter.feedapp;

import android.annotation.SuppressLint;
import android.os.AsyncTask;

import com.squareup.picasso.Picasso;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Pattern;

// Используется для получения контента из XML файла отправленного с канала
public class Parser extends AsyncTask<Void, Void, Object> {

    @SuppressLint("StaticFieldLeak")
    private Picasso picasso;
    private BaseActivity context;
    private InputStream stream;

    Parser(BaseActivity context, InputStream stream) {
        this.context = context;
        this.stream = stream;
    }

    // ``` Не переопределённые методы
    // Метод onPreExecute(): вызывается из главного потока перед запуском метода doInBackground()
    // Метод onProgressUpdate(): позволяет сигнализировать пользователю о выполнении фонового потока
    // ```

    @Override
    protected Object doInBackground(Void... ignored) // Выполнение в фоновом режиме
    {
        try
        {
            return parse();
        }
        catch (Exception e)
        {
            return e;
        }
    }

    // Выполняется из главного потока после завершения работы метода doInBackground()
    @Override
    protected void onPostExecute(Object data)
    {
        super.onPostExecute(data);
        if (data instanceof Exception)
            context.onError((Exception) data);
        else {
            context.onGetRecords((AllRecords) data);
            new Save(context, (AllRecords)data).execute();
        }
    }

    // Вспомогательный метод который выполняет парсинг XML разметки
    private AllRecords parse() throws IOException, XmlPullParserException {
        XmlPullParser parser = XmlPullParserFactory.newInstance().newPullParser();

        parser.setInput(stream, null);

        String text = null;
        boolean inItem = false;
        AllRecords allRecords = new AllRecords();
        Record record = new Record();

        int event = parser.getEventType();

        while (event != XmlPullParser.END_DOCUMENT) {
            String tagName = parser.getName();

            switch (event) {
                case XmlPullParser.START_TAG:
                    if (tagName.equalsIgnoreCase("item")) {
                        record = new Record();
                        inItem = true;
                    } else if (tagName.equalsIgnoreCase("enclosure")) {
                        String type = parser.getAttributeValue(null, "type");
                        if(type.equals("image/jpeg")) {
                            record.setImageURL(parser.getAttributeValue(null, "url"));
                        }

                    }
                    break;

                case XmlPullParser.TEXT:
                    text = parser.getText();
                    break;

                case XmlPullParser.END_TAG:

                    if (inItem) {
                        if (tagName.equalsIgnoreCase("title")) {
                            record.setTitle(text);

                        } else if (tagName.equalsIgnoreCase("description")) {
                            record.setDescription(text);

                        } else if (tagName.equalsIgnoreCase("pubDate")) {
                            record.setDate(text);

                        } else if (tagName.equalsIgnoreCase("guid")) {
                            record.setGuid(text);

                        } else if (tagName.equalsIgnoreCase("link")) {
                            record.setLink(text);

                        }
                    }
                    else{
                        if (tagName.equalsIgnoreCase("title")) {
                            allRecords.setTitle(text);
                        }
                    }

                    if (tagName.equalsIgnoreCase("item")) {
                        String preview = record.getDescription()
                                .replaceAll("&#x3C;", "<")
                                .replaceAll("&#x3E;", ">")
                                .replaceAll("<br>", "\n")
                                .replaceAll("<p>", "\n");
                        preview = Pattern.compile("<[^<]*>")
                                .matcher(preview)
                                .replaceAll("");

                        record.setPreview(preview);
                        allRecords.getRecords().add(record);
                        inItem = false;
                    }
                    break;
            }
            event = parser.next();
        }
        return allRecords;
    }
}