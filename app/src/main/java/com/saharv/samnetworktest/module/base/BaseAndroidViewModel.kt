package com.saharv.samnetworktest.module.base

import android.app.Application
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import androidx.lifecycle.*
import com.saharv.samnetworktest.utils.extenstion.default
import com.saharv.samnetworktest.application.App
import com.saharv.samnetworktest.application.service.ConnectivityType
import com.saharv.samnetworktest.application.service.NetWorkObserverService
import com.saharv.samnetworktest.utils.extenstion.logDebug
import com.saharv.samnetworktest.utils.extenstion.logError
import com.saharv.samnetworktest.utils.extenstion.logInfo
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

abstract class BaseAndroidViewModel(application: Application)
    : AndroidViewModel(application), LifecycleObserver {

    protected var netWorkObserverService: NetWorkObserverService? = null
    private var serviceConnection: ServiceConnection? = null
    val appBarTitleLiveData = MutableLiveData<Int>()
    val connectivityTypeLiveData = MutableLiveData<ConnectivityType>().default(ConnectivityType.DISCONNECTED)


    fun startBind() {
        serviceConnection = object : ServiceConnection {

            override fun onServiceConnected(className: ComponentName, service: IBinder) {
                // We've bound to BleService, cast the IBinder and get BleService instance
                val binder = service as NetWorkObserverService.LocalBleBinder
                netWorkObserverService = binder.getService()
                logDebug("Success binding NetWorkObserverService")
                observeNetwork()
            }

            override fun onServiceDisconnected(arg0: ComponentName) {
                logError("Failed binding NetWorkObserverService")
            }
        }
        serviceConnection?.apply {
            Intent(getApplication(), NetWorkObserverService::class.java).also { intent ->
                getApplication<App>().bindService(intent, this, Context.BIND_AUTO_CREATE)
            }
        }
    }

    private fun observeNetwork(){
        netWorkObserverService?.let {
           this.logInfo("start connectivityTypeChannel!!")
            viewModelScope.launch {
                it.connectivityTypeChannel
                    .asFlow()
                    .collect {
                        this@BaseAndroidViewModel.logInfo("COLLECT $it")
                        connectivityTypeLiveData.postValue(it)
                    }
            }
        }
    }


    override fun onCleared() {
        super.onCleared()
        serviceConnection?.apply { getApplication<App>().unbindService(this) }
    }

}