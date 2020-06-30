package com.mohamedamgd.newsapp.repo.retrofit;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {
    @GET("top-headlines")
    Call<JsonObject> getTopHeadlines(@Query("qInTitle") String q,
                                     @Query("country") String country,
                                     @Query("category") String category,
                                     @Query("pageSize") int pageSize,
                                     @Query(("page")) int page,
                                     @Query("apiKey") String key);

    @GET("everything")
    Call<JsonObject> getEverything(@Query("qInTitle") String q,
                                   @Query("from") String from,
                                   @Query("to") String to,
                                   @Query("language") String language,
                                   @Query("sortBy") String sortBy,
                                   @Query("pageSize") int pageSize,
                                   @Query("apiKey") String key);

}
