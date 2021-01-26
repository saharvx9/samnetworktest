package com.saharv.samnetworktest.data.source.articles

import com.saharv.samnetworktest.data.model.ArticleItem
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class ArticlesRepository
@Inject constructor(
    private val local: ArticlesDataSource.Local,
    private val remote: ArticlesDataSource.Remote
) {


    fun loadArticles(online: Boolean): Flow<List<ArticleItem>> {
        return if (online) {
            flow { emit(local.deleteAll()) }
                .flatMapConcat {
                    remote.loadBitCoinArticles()
                        .zip(remote.loadTopHeadlinesArticles()) { a, b -> ArrayList(a + b) } }
                .flatMapConcat { list -> local.saveArticles(list).map { list } }
        } else {
            local.loadArticles()
        }
    }

    suspend fun saveArticleItemCache(article:ArticleItem){
        local.saveArticleItemCache(article)
    }

    fun loadArticleItemCache():Flow<ArticleItem>{
        return local.loadArticleItemCache()
    }
}