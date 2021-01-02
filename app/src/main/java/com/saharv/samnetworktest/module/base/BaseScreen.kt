package com.saharv.samnetworktest.module.base

import androidx.annotation.LayoutRes

interface BaseScreen<T : BaseViewModel> {

    @LayoutRes
    fun getLayoutResource(): Int

    fun getToolBarTitle():Int

    fun setupViews()

    fun bindViewModel()

}