package com.sevenpeakssoftware.fott.network;

import com.sevenpeakssoftware.fott.models.FeedResponse;

import retrofit2.Call;
import retrofit2.http.GET;

public interface FeedsInterface {

    @GET("article/get_articles_list")
    Call<FeedResponse> getFeeds();
}
