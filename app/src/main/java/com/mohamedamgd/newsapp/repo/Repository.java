package com.mohamedamgd.newsapp.repo;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.JsonObject;
import com.mohamedamgd.newsapp.models.News;
import com.mohamedamgd.newsapp.repo.retrofit.Client;
import com.mohamedamgd.newsapp.repo.roomDB.DatabaseClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Repository {
    public static Repository mInstance;
    private Client mClient;
    private DatabaseClient mDatabaseClient;

    private ArrayList<News> mNewsList;
    private MutableLiveData<List<News>> mNewsLiveData = new MutableLiveData<>();

    private Repository(Context context) {
        mClient = Client.getInstance();
        mDatabaseClient = DatabaseClient.getInstance(context);
    }

    public static Repository getInstance(Context context) {
        if (mInstance == null) mInstance = new Repository(context);
        return mInstance;
    }

    public MutableLiveData<List<News>> getTopHeadlinesNews(String country, int categoryIndex) {
        mClient.getTopHeadlinesCall("",
                country,
                categoryIndex,
                0)
                .enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        mNewsList = mClient.ResponseConverter(response);
                        staredCheck(mNewsList);
                        mNewsLiveData.setValue(mNewsList);
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        mNewsLiveData.setValue(new ArrayList<>());
                    }
                });

        return mNewsLiveData;
    }

    public MutableLiveData<List<News>> getSearchNews(String query, String lang, String sortBy) {
        mClient.getEverythingCall(query,
                "",
                "",
                lang,
                sortBy).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                mNewsList = mClient.ResponseConverter(response);
                staredCheck(mNewsList);
                mNewsLiveData.setValue(mNewsList);

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                mNewsLiveData.setValue(new ArrayList<>());
            }
        });
        return mNewsLiveData;
    }


    public MutableLiveData<List<News>> getFavoriteNews() {
        mNewsLiveData = mDatabaseClient.getNewsList();
        return mNewsLiveData;
    }

    public void addToFavorites(@NonNull News news) {
        news.setStared(true);
        mDatabaseClient.insert(news);
    }

    public void removeFromFavorites(@NonNull News news) {
        news.setStared(false);
        mDatabaseClient.delete(news);
    }


    private void staredCheck(@NonNull ArrayList<News> newsList) {
        for (News news : newsList) {
            int id = news.getId();
            News temp = null;
            try {
                temp = mDatabaseClient.getNews(id);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (temp != null) {
                news.setStared(temp.isStared());
            }
        }
    }

}
