package com.artisanter.feedapp;
import java.io.Serializable;
import java.util.ArrayList;

// Класс представляющий все записи в ленте
class AllRecords implements Serializable
{
    private String title = "";
    private ArrayList<Record> records = new ArrayList<>();

    ArrayList<Record> getRecords() {
        return records;
    }

    String getTitle() {
        return title;
    }

    void setTitle(String title) {
        this.title = title;
    }
}
