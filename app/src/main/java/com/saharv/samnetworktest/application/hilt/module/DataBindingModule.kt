package com.saharv.samnetworktest.application.hilt.module

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.Serializer
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.createDataStore
import androidx.room.Room
import com.saharv.samnetworktest.application.db.AppDatabase
import com.saharv.samnetworktest.data.source.articles.local.db.ArticleDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
class DataBindingModule {

    @Provides
    @Singleton
    fun provideDataStore(@ApplicationContext context: Context): DataStore<Preferences> {
        return context.createDataStore(name = "settings")
    }

    @Provides
    @Singleton
    fun providesAppDataBase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "ArticlesDb")
            .build()
    }

    @Provides
    fun providesArticleDao(appDatabase: AppDatabase): ArticleDao{
        return appDatabase.articleDao()
    }

}



