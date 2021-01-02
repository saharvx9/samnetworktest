package com.saharv.samnetworktest.module.main.fragments.articles

import androidx.annotation.VisibleForTesting
import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.saharv.samnetworktest.data.model.ArticleItem
import com.saharv.samnetworktest.data.source.articles.ArticlesRepository
import com.saharv.samnetworktest.module.base.BaseViewModel
import com.saharv.samnetworktest.module.main.fragments.articles.ArticlesState.*
import com.saharv.samnetworktest.utils.extenstion.logInfo
import kotlinx.coroutines.flow.*

class ArticlesViewModel
@ViewModelInject
constructor(private val articlesRepository: ArticlesRepository,
    @Assisted private val savedStateHandle: SavedStateHandle) : BaseViewModel() {

    val articlesStateLiveData = MutableLiveData<ArticlesState>()
    var currentPosition:Int? = null


    override fun start() {
        onlineChannel.asFlow()
            .distinctUntilChanged()
            .flatMapConcat {
                logInfo("start loading online: $it")
                articlesRepository.loadArticles(it)
            }.onStart { articlesStateLiveData.postValue(LoadingState) }
            .catch { e ->
                articlesStateLiveData.postValue(ErrorState(e.message ?: "Failed load articles list"))
            }.map { ArticlesListState(it) }
            .collectExt {
                logInfo("show articles list collect size ${it.list.size}")
                articlesStateLiveData.postValue(it)
            }
    }

    @VisibleForTesting
    fun loadArticles(){
        articlesRepository.loadArticles(true)
            .onStart { articlesStateLiveData.postValue(LoadingState) }
            .catch {e-> articlesStateLiveData.postValue(ErrorState(e.message ?: "Failed load articles list")) }
            .map { ArticlesListState(it) }
            .collectExt {
                logInfo("show articles list collect size ${it.list.size}")
                articlesStateLiveData.postValue(it)
            }
    }

    suspend fun saveArticleItemCache(item:ArticleItem){
        articlesRepository.saveArticleItemCache(item)
    }
}