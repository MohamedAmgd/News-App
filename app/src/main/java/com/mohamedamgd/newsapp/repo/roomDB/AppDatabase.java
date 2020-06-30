package com.mohamedamgd.newsapp.repo.roomDB;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.mohamedamgd.newsapp.models.News;

@Database(entities = News.class, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase mInstance;

    public static synchronized AppDatabase getInstance(Context context) {
        if (mInstance == null) {
            mInstance = Room.databaseBuilder(context.getApplicationContext()
                    , AppDatabase.class, "news_db").fallbackToDestructiveMigration().build();
        }
        return mInstance;
    }

    public abstract NewsDao newsDao();


}
