package com.saharv.samnetworktest.module.main.fragments.articleinfo

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.saharv.samnetworktest.data.model.ArticleItem
import com.saharv.samnetworktest.data.source.articles.ArticlesRepository
import com.saharv.samnetworktest.module.base.BaseViewModel
import kotlinx.coroutines.flow.catch

class ArticlesInfoViewModel
@ViewModelInject
constructor(
    private val articlesRepository: ArticlesRepository,
    @Assisted private val savedStateHandle: SavedStateHandle) : BaseViewModel() {

    val articleLiveData = MutableLiveData<ArticleItem>()

    override fun start() {
        articlesRepository.loadArticleItemCache()
            .catch { e-> displayError(e) }
            .collectExt { articleLiveData.postValue(it) }
    }
}