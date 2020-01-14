package com.geniny.feedapp;

import android.os.AsyncTask;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Pattern;

public class Parser extends AsyncTask<Void, Void, Object> {

    private BaseActivity context;
    private InputStream stream;

    Parser(BaseActivity context, InputStream stream) {
        this.context = context;
        this.stream = stream;
    }

    @Override
    protected Object doInBackground(Void... ignored)
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

    @Override
    protected void onPostExecute(Object data)
    {
        super.onPostExecute(data);
        if (data instanceof Exception)
            context.onError((Exception) data);
        else
            {
            context.setArticlesContext((Articles) data);
            new Save(context, (Articles)data).execute();
        }
    }

    private Articles parse() throws IOException, XmlPullParserException {
        XmlPullParser parser = XmlPullParserFactory.newInstance().newPullParser();

        parser.setInput(stream, null);

        String text = null;
        boolean inItem = false;
        Articles articles = new Articles();
        Article article = new Article();

        int event = parser.getEventType();

        while (event != XmlPullParser.END_DOCUMENT) {
            String tagName = parser.getName();

            switch (event) {
                case XmlPullParser.START_TAG:
                    if (tagName.equalsIgnoreCase("item")) {
                        article = new Article();
                        inItem = true;
                    } else if (tagName.equalsIgnoreCase("enclosure")) {
                        String type = parser.getAttributeValue(null, "type");
                        if(type.equals("image/jpeg")) {
                            article.setImageURL(parser.getAttributeValue(null, "url"));
                        }

                    }
                    break;

                case XmlPullParser.TEXT:
                    text = parser.getText();
                    break;

                case XmlPullParser.END_TAG:

                    if (inItem) {
                        if (tagName.equalsIgnoreCase("title")) {
                            article.setTitle(text);

                        } else if (tagName.equalsIgnoreCase("description")) {
                            article.setDescription(text);

                        } else if (tagName.equalsIgnoreCase("pubDate")) {
                            article.setDate(text);

                        } else if (tagName.equalsIgnoreCase("guid")) {
                            article.setGuid(text);

                        } else if (tagName.equalsIgnoreCase("link")) {
                            article.setLink(text);

                        }
                    }
                    else{
                        if (tagName.equalsIgnoreCase("title")) {
                            articles.setTitle(text);
                        }
                    }

                    if (tagName.equalsIgnoreCase("item")) {
                        String preview = article.getDescription()
                                .replaceAll("&#x3C;", "<")
                                .replaceAll("&#x3E;", ">")
                                .replaceAll("<br>", "\n")
                                .replaceAll("<p>", "\n");
                        preview = Pattern.compile("<[^<]*>")
                                .matcher(preview)
                                .replaceAll("");

                        article.setPreview(preview);
                        articles.getArticles().add(article);
                        inItem = false;
                    }
                    break;
            }
            event = parser.next();
        }
        return articles;
    }
}