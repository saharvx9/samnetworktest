package com.saharv.samnetworktest.data.source.articles.local.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.saharv.samnetworktest.data.model.ArticleItem

@Dao
interface ArticleDao {

    @Query("SELECT * FROM articles")
    suspend fun getAllArticles(): List<ArticleItem>

    @Insert
    suspend fun insertArticles(articles:List<ArticleItem>)

    @Query("DELETE FROM articles")
    suspend fun deleteAllArticles()
}