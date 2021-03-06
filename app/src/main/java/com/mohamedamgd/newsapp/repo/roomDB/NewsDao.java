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
