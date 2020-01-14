package com.geniny.feedapp;
import java.io.Serializable;
import java.util.ArrayList;

class Articles implements Serializable
{
    private String title = "";
    private ArrayList<Article> articles = new ArrayList<>();

    ArrayList<Article> getArticles() {
        return articles;
    }

    String getTitle() {
        return title;
    }

    void setTitle(String title) {
        this.title = title;
    }
}
