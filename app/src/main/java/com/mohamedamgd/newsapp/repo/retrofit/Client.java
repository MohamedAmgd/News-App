package com.mohamedamgd.newsapp.repo.retrofit;

import com.google.gson.JsonArray;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.mohamedamgd.newsapp.models.News;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Client {
    private static Client mInstance;
    private final String BASE_URL = "http://newsapi.org/v2/";
    private final String API_KEY = "a4e1d99fd47d4e0095a791c42c738f80";
    private final int DEFAULT_PAGE_SIZE = 100;
    private final String[] CATEGORY_VALUES =
            {"general", "sports", "science", "technology", "health", "business", "entertainment"};
    private Retrofit mRetrofit;
    private ApiInterface mApiInterface;

    private Client() {
        mRetrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        mApiInterface = mRetrofit.create(ApiInterface.class);
    }

    public static Client getInstance() {
        if (mInstance == null) mInstance = new Client();
        return mInstance;
    }

    public Call<JsonObject> getEverythingCall(String query,
                                              String from,
                                              String to,
                                              String language,
                                              String sortBy) {
        if (mRetrofit != null && mApiInterface != null) {
            return mApiInterface.getEverything(query,
                    from,
                    to,
                    language,
                    sortBy,
                    DEFAULT_PAGE_SIZE,
                    API_KEY);
        }
        return null;
    }

    public Call<JsonObject> getTopHeadlinesCall(String query
            , String countryCode
            , int categoryIndex
            , int page) {

        if (mRetrofit != null && mApiInterface != null) {
            String category = "";
            if (categoryIndex != -1 && categoryIndex < CATEGORY_VALUES.length) {
                category = CATEGORY_VALUES[categoryIndex];
            }
            return mApiInterface.getTopHeadlines(query,
                    countryCode,
                    category,
                    DEFAULT_PAGE_SIZE,
                    page,
                    API_KEY);
        }
        return null;
    }

    public ArrayList<News> ResponseConverter(Response<JsonObject> response) {
        ArrayList<News> list = new ArrayList<>();
        JsonArray elements = null;
        if (response.body() != null) {
            elements = response.body().getAsJsonArray("articles");
            for (int i = 0; i < elements.size(); i++) {
                JsonObject current = elements.get(i).getAsJsonObject();

                String title = "", url = "", urlToImage = "";
                if (!(current.get("title") instanceof JsonNull))
                    title = current.getAsJsonPrimitive("title").getAsString();
                if (!(current.get("url") instanceof JsonNull))
                    url = current.getAsJsonPrimitive("url").getAsString();
                if (!(current.get("urlToImage") instanceof JsonNull))
                    urlToImage = current.getAsJsonPrimitive("urlToImage").getAsString();
                list.add(new News(title, url, urlToImage));
            }
        }
        return list;
    }
}
