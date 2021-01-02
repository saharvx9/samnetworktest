package com.saharv.samnetworktest.data.source.articles.remote

import com.saharv.hilt_tut.application.hilt.scope.Remote
import com.saharv.samnetworktest.utils.extenstion.flowOnIOThread
import com.saharv.samnetworktest.data.model.ArticleItem
import com.saharv.samnetworktest.data.source.articles.ArticlesDataSource
import com.saharv.samnetworktest.data.source.articles.remote.api.ArticlesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter
import javax.inject.Inject

@Remote
class ArticlesRemoteRepository
    @Inject constructor(private val articlesApi: ArticlesApi)
    :ArticlesDataSource.Remote {

    override fun loadBitCoinArticles(): Flow<List<ArticleItem>> {
       return flow { emit(articlesApi.loadBitCoinArticles(from=LocalDate.now().toSimpleFormat())) }
           .map { it.articles?: listOf() }
           .flowOnIOThread()
    }

    override fun loadTopHeadlinesArticles(): Flow<List<ArticleItem>> {
        return flow { emit(articlesApi.loadTopHeadLinesArticles()) }
            .map { it.articles?: listOf() }
            .flowOnIOThread()
    }

    private fun LocalDate.toSimpleFormat() = format(DateTimeFormatter.ofPattern("yy-MM-dd"))
}