package com.saharv.samnetworktest.module.main.fragments.articles

import com.saharv.samnetworktest.data.model.ArticleItem

sealed class ArticlesState {
    object LoadingState : ArticlesState()
    class ArticlesListState(val list:List<ArticleItem>):ArticlesState()
    class ErrorState(val message:String):ArticlesState()

}