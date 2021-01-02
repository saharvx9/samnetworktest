package com.saharv.samnetworktest.data.source.articles

import com.saharv.samnetworktest.data.model.ArticleItem
import kotlinx.coroutines.flow.Flow

interface ArticlesDataSource {

    interface Local {

        fun loadArticles(): Flow<List<ArticleItem>>

        fun saveArticles(articles:List<ArticleItem>):Flow<Unit>

        suspend fun deleteAll()

        suspend fun saveArticleItemCache(articleItem:ArticleItem)

        fun loadArticleItemCache():Flow<ArticleItem>

    }

    interface Remote{

        fun loadBitCoinArticles():Flow<List<ArticleItem>>

        fun loadTopHeadlinesArticles():Flow<List<ArticleItem>>
    }
}