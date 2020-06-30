/**
 * Copyright 2020 Mohamed Amgd
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
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
