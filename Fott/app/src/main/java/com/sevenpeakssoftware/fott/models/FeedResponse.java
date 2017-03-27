package com.sevenpeakssoftware.fott.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class FeedResponse {

    @SerializedName("content")
    private List<Article> articles = new ArrayList<>();

    @SerializedName("status")
    private String status;
    @SerializedName("serverTime")
    private long serverTime;

    public List<Article> getArticles() {
        return articles;
    }

    public void setArticles(List<Article> articles) {
        this.articles = articles;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public long getServerTime() {
        return serverTime;
    }

    public void setServerTime(long serverTime) {
        this.serverTime = serverTime;
    }
}
