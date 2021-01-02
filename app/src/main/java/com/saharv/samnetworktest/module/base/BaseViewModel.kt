package com.saharv.samnetworktest.module.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.saharv.samnetworktest.utils.extenstion.logError
import com.saharv.samnetworktest.utils.extenstion.logInfo
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.sendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

abstract class BaseViewModel : ViewModel() {

    val serverExceptionLiveData = MutableLiveData<Any>()
    val onlineChannel = BroadcastChannel<Boolean>(Channel.CONFLATED)

    open fun start() = Unit

    fun updateOnline(online:Boolean){
        logInfo("update online $online")
        onlineChannel.sendBlocking(online)
    }

    protected fun displayError(error:Throwable){
        logError("Show error: ${error.message}")
        serverExceptionLiveData.postValue(Any())
    }

    protected inline fun <T> Flow<T>.collectExt(crossinline block: (T) -> Unit) {
        viewModelScope.launch {
            collect { block(it) }
        }
    }
}