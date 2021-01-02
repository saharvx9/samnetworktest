package com.saharv.samnetworktest.application.service

import androidx.annotation.DrawableRes
import com.saharv.samnetworktest.R

enum class ConnectivityType(@DrawableRes val image: Int) {

    CONNECTED(R.drawable.ic_online),
    CONNECTING(-1),
    DISCONNECTING(-1),
    DISCONNECTED(R.drawable.ic_offline);

}