package com.saharv.samnetworktest.utils.extenstion

import androidx.annotation.MainThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn


@MainThread
fun <X, Y> LiveData<X>.switchMap(func: (X) -> LiveData<Y>): LiveData<Y> {
    return Transformations.switchMap(this, func)
}

fun <T> MutableLiveData<T>.onNext(eventValue: T) {
    value = eventValue
}

fun <A, B> LiveData<A>.combineLatest(b: LiveData<B>): LiveData<Pair<A, B>> {
    return MediatorLiveData<Pair<A, B>>().apply {
        var lastA: A? = null
        var lastB: B? = null

        addSource(this@combineLatest) {
            if (it == null && value != null) value = null
            lastA = it
            if (lastA != null && lastB != null) value = lastA!! to lastB!!
        }

        addSource(b) {
            if (it == null && value != null) value = null
            lastB = it
            if (lastA != null && lastB != null) value = lastA!! to lastB!!
        }
    }
}


/**
 * Inspired from https://medium.com/@gauravgyal/combine-results-from-multiple-async-requests-90b6b45978f7
 * */
fun <A, B> zipLiveData(a: LiveData<A>, b: LiveData<B>): LiveData<Pair<A, B>> {
    return MediatorLiveData<Pair<A, B>>().apply {
        var lastA: A? = null
        var lastB: B? = null

        fun update() {
            val localLastA = lastA
            val localLastB = lastB
            if (localLastA != null && localLastB != null)
                this.value = Pair(localLastA, localLastB)
        }

        addSource(a) {
            lastA = it
            update()
        }
        addSource(b) {
            lastB = it
            update()
        }
    }
}

@MainThread
fun <X> LiveData<X>.filter(test: (X) -> Boolean): LiveData<X> {
    val result = MediatorLiveData<X>()

    result.addSource(this) {
        if (it != null && test(it)) {
            result.value = it
        }
    }

    return result
}

fun <T> Flow<T>.throttleFirst(periodMillis: Long): Flow<T> {
    require(periodMillis > 0) { "period should be positive" }
    return flow {
        var lastTime = 0L
        collect {
            val currentTime = System.currentTimeMillis()
            if (currentTime - lastTime >= periodMillis) {
                lastTime = currentTime
                emit(it)
            }
        }
    }
}

fun <T : Any?> MutableLiveData<T>.default(initialValue: T) = apply { setValue(initialValue) }


// ~~~~~~~~~~~~~~ Coroutines Ext ~~~~~~~~~~~~~~
fun <T> CoroutineScope.asyncIOThread(block: suspend CoroutineScope.() -> T): Deferred<T> = async(Dispatchers.IO) { block() }
fun <T> CoroutineScope.asyncDefaultThread(block: suspend CoroutineScope.() -> T): Deferred<T>  = async(Dispatchers.Default) { block() }

// ~~~~~~~~~~~~~~ Flow Ext ~~~~~~~~~~~~~~
fun <T> Flow<T>.flowOnMainThread(): Flow<T> = flowOn(Dispatchers.Main)
fun <T> Flow<T>.flowOnIOThread(): Flow<T> = flowOn(Dispatchers.IO)
fun <T> Flow<T>.flowOnDefaultThread(): Flow<T> = flowOn(Dispatchers.Default)



