package com.mohamedamgd.newsapp.repo.roomDB;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.mohamedamgd.newsapp.models.News;

import java.util.List;

@Dao
public interface NewsDao {

    @Query("SELECT * FROM news")
    List<News> getAll();

    @Query("SELECT * FROM news WHERE id LIKE :input")
    News getNewsById(int input);

    @Insert
    void insert(News news);

    @Delete
    void delete(News news);

}
