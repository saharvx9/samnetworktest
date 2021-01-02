package com.saharv.samnetworktest.data.source.articles.local

import com.saharv.samnetworktest.application.hilt.scope.Local
import com.saharv.samnetworktest.data.model.ArticleItem
import com.saharv.samnetworktest.data.source.articles.ArticlesDataSource
import com.saharv.samnetworktest.data.source.articles.local.db.ArticleDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@Local
class ArticlesLocalRepository
    @Inject constructor(private val articleDao: ArticleDao) :ArticlesDataSource.Local {

    private var articleItemCache:ArticleItem? = null

    override fun loadArticles(): Flow<List<ArticleItem>> {
        return flow { emit(articleDao.getAllArticles()) }
    }

    override fun saveArticles(articles:List<ArticleItem>): Flow<Unit> {
        return flow { emit(articleDao.insertArticles(articles)) }
    }

    override suspend fun deleteAll() {
        return articleDao.deleteAllArticles()
    }

    override suspend fun saveArticleItemCache(articleItem: ArticleItem){
        this.articleItemCache = articleItem
    }

    override fun loadArticleItemCache(): Flow<ArticleItem> {
       return flow { emit(articleItemCache!!) }
    }
}