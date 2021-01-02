package com.saharv.samnetworktest.application.service

import android.app.Service
import android.content.Context
import android.content.Intent
import android.net.*
import android.os.Binder
import android.os.IBinder
import com.saharv.samnetworktest.utils.extenstion.logError
import com.saharv.samnetworktest.utils.extenstion.logInfo
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.channels.sendBlocking
import kotlinx.coroutines.flow.*

class NetWorkObserverService : Service() {

    private var cm: ConnectivityManager? = null
    private val builder: NetworkRequest.Builder = NetworkRequest.Builder()
    private var callbackNetWork:ConnectivityManager.NetworkCallback? = null
    val connectivityTypeChannel = BroadcastChannel<ConnectivityType>(Channel.CONFLATED)


    /**
     * Class used for the client Binder.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with IPC.
     */
    inner class LocalBleBinder : Binder() {
        // Return this instance of LocalService so clients can call public methods
        fun getService(): NetWorkObserverService = this@NetWorkObserverService
    }

    // Binder given to clients
    private val binder = LocalBleBinder()

    override fun onBind(intent: Intent?): IBinder = binder

    override fun onCreate() {
        super.onCreate()
        cm = application.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        observeNetWorkConnectionState()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        logInfo("Service start!!")
        return START_NOT_STICKY
    }


    private fun observeNetWorkConnectionState() {
        callbackNetWork = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                this@NetWorkObserverService.logInfo("NETWORK onAvailable")
                connectivityTypeChannel.sendBlocking(ConnectivityType.CONNECTED)
            }

            override fun onLost(network: Network) {
                this@NetWorkObserverService.logInfo("NETWORK onLost")
                connectivityTypeChannel.sendBlocking(ConnectivityType.DISCONNECTED)
            }

            override fun onLosing(network: Network, maxMsToLive: Int) {
                super.onLosing(network, maxMsToLive)
                this@NetWorkObserverService.logInfo("NETWORK onLosing")
                connectivityTypeChannel.sendBlocking(ConnectivityType.DISCONNECTING)
            }

        }
        cm?.registerNetworkCallback(builder.build(), callbackNetWork!!)
    }

    override fun onDestroy() {
        super.onDestroy()
        callbackNetWork?.apply { cm!!.unregisterNetworkCallback(this) }
    }
}