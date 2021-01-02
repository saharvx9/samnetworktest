package com.saharv.hilt_tut.utils.extenstion

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings


fun Context.intentToSettings(){
    val intent = Intent().apply {
        action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
        val uri = Uri.fromParts("package", packageName, null)
        data = uri
    }
    startActivity(intent)
}