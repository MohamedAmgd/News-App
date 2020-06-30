package com.mohamedamgd.newsapp.viewModels;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.preference.PreferenceManager;

import com.mohamedamgd.newsapp.R;
import com.mohamedamgd.newsapp.models.News;
import com.mohamedamgd.newsapp.repo.Repository;

import java.util.List;
import java.util.Objects;

public class NewsViewModel extends AndroidViewModel {
    public MutableLiveData<List<News>> mNewsMutableLiveData = new MutableLiveData<>();
    public MutableLiveData<String> mNewsDataState = new MutableLiveData<>();
    private Repository mRepository;
    private Bundle mBundle;

    public NewsViewModel(@NonNull Application application) {
        super(application);
    }

    public void getNews(Bundle bundle) {
        mRepository = Repository.getInstance(getApplication().getApplicationContext());
        mBundle = bundle;
        checkState();
        switch (Objects.requireNonNull(bundle.getString("type"))) {
            case "search":
                getSearchNews(mBundle.getString("query"));
                break;
            case "topHeadlines":
                getTopHeadlines(mBundle.getInt("categoryIndex"));
                break;
            case "favorites":
                getFavoriteNews();
                break;

        }
    }

    public void onStarClicked(News news) {
        if (news.isStared()) {
            mRepository.removeFromFavorites(news);
            news.setStared(false);
            Toast.makeText(getApplication().getApplicationContext(),
                    getApplication().getString(R.string.removed_from_fav),
                    Toast.LENGTH_SHORT).show();
        } else {
            mRepository.addToFavorites(news);
            Toast.makeText(getApplication().getApplicationContext(),
                    getApplication().getString(R.string.added_to_fav),
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void getSearchNews(String query) {
        SharedPreferences preferences =
                PreferenceManager.getDefaultSharedPreferences(getApplication().getApplicationContext());
        String lang = preferences.getString("search_lang", "en");
        String sortBy = preferences.getString("sort_by", "relevancy");
        mNewsMutableLiveData = mRepository.getSearchNews(query, lang, sortBy);
    }

    private void getTopHeadlines(int categoryIndex) {
        SharedPreferences preferences =
                PreferenceManager.getDefaultSharedPreferences(getApplication().getApplicationContext());
        String country = preferences.getString("country", "us");
        mNewsMutableLiveData = mRepository.getTopHeadlinesNews(country, categoryIndex);
    }
    private void getFavoriteNews() {
        mNewsMutableLiveData = mRepository.getFavoriteNews();
        checkState();
    }


    public void checkState(){
        if (!mBundle.getString("type").equals("favorites") && !isNetworkAvailable()) {
            mNewsDataState.setValue("no internet");
            return;
        }
        mNewsDataState.setValue("loading");
        if (mNewsMutableLiveData.getValue() == null) {
            return;
        }
        if (mNewsMutableLiveData.getValue().isEmpty()) {
            mNewsDataState.setValue("nothing found");
        } else {
            mNewsDataState.setValue("done");
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getApplication().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}