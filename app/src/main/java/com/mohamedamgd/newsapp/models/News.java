package com.mohamedamgd.newsapp.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "news")
public class News implements Serializable {

    @PrimaryKey
    private int id;

    @ColumnInfo(name = "title")
    private String title;

    @ColumnInfo(name = "url")
    private String url;

    @ColumnInfo(name = "urlToImage")
    private String urlToImage;

    @ColumnInfo(name = "isStared")
    private boolean isStared;

    @Ignore
    public News(String title, String url, String urlToImage) {
        this.title = title;
        this.url = url;
        this.urlToImage = urlToImage;

        this.id = hashCode();
        this.isStared = false;
    }

    public News(String title, String url, String urlToImage, Boolean isStared) {
        this.title = title;
        this.url = url;
        this.urlToImage = urlToImage;

        this.isStared = isStared;
        this.id = hashCode();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrlToImage() {
        return urlToImage;
    }

    public void setUrlToImage(String urlToImage) {
        this.urlToImage = urlToImage;
    }

    public boolean isStared() {
        return isStared;
    }

    public void setStared(boolean stared) {
        this.isStared = stared;
    }

    @Override
    public int hashCode() {
        return url.hashCode();
    }
}
