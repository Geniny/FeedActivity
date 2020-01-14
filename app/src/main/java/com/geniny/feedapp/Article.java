package com.geniny.feedapp;

import java.io.Serializable;

class Article implements Serializable
{
    private String title = "";
    private String description = "";
    private String imageURL = "";
    private String date = "";
    private String guid = "";
    private String preview = "";
    private String link = "";

    String getTitle() {
        return title;
    }

    void setTitle(String title) {
        this.title = title;
    }

    String getDescription() {
        return description;
    }

    void setDescription(String description) {
        this.description = description;
    }

    String getImageURL() {
        return imageURL;
    }

    void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    String getDate() {
        return date;
    }

    void setDate(String date) {
        this.date = date;
    }

    void setGuid(String guid) {
        this.guid = guid;
    }

    String getPreview() {
        return preview;
    }

    void setPreview(String preview) {
        this.preview = preview;
    }

    String getLink() {
        return link;
    }

    void setLink(String link) {
        this.link = link;
    }
}
