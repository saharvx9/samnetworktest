package com.saharv.samnetworktest.application.hilt.module

import com.saharv.hilt_tut.application.hilt.scope.Remote
import com.saharv.samnetworktest.application.hilt.scope.Local
import com.saharv.samnetworktest.data.source.articles.ArticlesDataSource
import com.saharv.samnetworktest.data.source.articles.ArticlesRepository
import com.saharv.samnetworktest.data.source.articles.remote.api.ArticlesApi
import com.saharv.samnetworktest.data.source.articles.local.ArticlesLocalRepository
import com.saharv.samnetworktest.data.source.articles.local.db.ArticleDao
import com.saharv.samnetworktest.data.source.articles.remote.ArticlesRemoteRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
class RepositoryBindingModule {

    @Provides
    @Singleton
    fun provideUserRepository(@Local articlesLocalDataSource: ArticlesDataSource.Local,
                              @Remote articlesRemoteDataSource: ArticlesDataSource.Remote): ArticlesRepository {
        return ArticlesRepository(articlesLocalDataSource, articlesRemoteDataSource)
    }

    @Provides
    @Singleton
    @Local
    fun provideLocalArticlesDataSource(articleDao: ArticleDao): ArticlesDataSource.Local {
        return ArticlesLocalRepository(articleDao)
    }

    @Provides
    @Remote
    fun provideRemoteArticlesDataSource(articlesApi: ArticlesApi): ArticlesDataSource.Remote {
        return ArticlesRemoteRepository(articlesApi)
    }

}