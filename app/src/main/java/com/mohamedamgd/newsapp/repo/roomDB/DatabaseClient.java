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
package com.mohamedamgd.newsapp.repo.roomDB;

import android.content.Context;
import android.os.AsyncTask;

import androidx.lifecycle.MutableLiveData;

import com.mohamedamgd.newsapp.models.News;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class DatabaseClient {
    private static DatabaseClient mInstance;
    private NewsDao mNewsDao;
    private MutableLiveData<List<News>> mNewsList = new MutableLiveData<>();


    private DatabaseClient(Context context) {
        AppDatabase mAppDatabase = AppDatabase.getInstance(context);
        mNewsDao = mAppDatabase.newsDao();
    }

    public static synchronized DatabaseClient getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new DatabaseClient(context);
        }
        return mInstance;
    }

    public void insert(News news) {
        new InsertAsyncTask(mNewsDao).execute(news);
    }

    public void delete(News news) {
        new DeleteAsyncTask(mNewsDao).execute(news);
    }

    public News getNews(int id) throws ExecutionException, InterruptedException {
        return new GetNewsAsyncTask(mNewsDao).execute(id).get();
    }

    public MutableLiveData<List<News>> getNewsList() {
        try {
            mNewsList.setValue(new GetNewsListAsyncTask(mNewsDao).execute().get());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mNewsList;
    }


    private static class GetNewsListAsyncTask extends AsyncTask<Void, Void, List<News>> {
        private NewsDao mNewsDao;

        public GetNewsListAsyncTask(NewsDao newsDao) {
            mNewsDao = newsDao;
        }

        @Override
        protected List<News> doInBackground(Void... voids) {
            return mNewsDao.getAll();
        }
    }


    private static class InsertAsyncTask extends AsyncTask<News, Void, Void> {
        private NewsDao mNewsDao;

        public InsertAsyncTask(NewsDao newsDao) {
            mNewsDao = newsDao;
        }

        @Override
        protected Void doInBackground(News... news) {
            mNewsDao.insert(news[0]);
            return null;
        }
    }

    private static class DeleteAsyncTask extends AsyncTask<News, Void, Void> {
        private NewsDao mNewsDao;

        public DeleteAsyncTask(NewsDao newsDao) {
            mNewsDao = newsDao;
        }

        @Override
        protected Void doInBackground(News... news) {
            mNewsDao.delete(news[0]);
            return null;
        }
    }

    private static class GetNewsAsyncTask extends AsyncTask<Integer, Void, News> {
        private NewsDao mNewsDao;

        public GetNewsAsyncTask(NewsDao newsDao) {
            mNewsDao = newsDao;
        }

        @Override
        protected News doInBackground(Integer... integers) {
            return mNewsDao.getNewsById(integers[0]);
        }
    }
}
