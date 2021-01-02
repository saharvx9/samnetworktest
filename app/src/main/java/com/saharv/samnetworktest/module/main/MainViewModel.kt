package com.saharv.samnetworktest.module.main

import android.content.Context
import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.saharv.samnetworktest.application.App
import com.saharv.samnetworktest.data.source.articles.ArticlesRepository
import com.saharv.samnetworktest.module.base.BaseAndroidViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


class MainViewModel
@ViewModelInject constructor(
    @ApplicationContext applicationContext: Context,
    @Assisted private val savedStateHandle: SavedStateHandle) : BaseAndroidViewModel(applicationContext as App) {






}