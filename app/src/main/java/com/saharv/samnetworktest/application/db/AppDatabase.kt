package com.saharv.samnetworktest.application.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.saharv.samnetworktest.data.model.ArticleItem
import com.saharv.samnetworktest.data.source.articles.local.db.ArticleDao

@Database(entities = [ArticleItem::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun articleDao(): ArticleDao
}