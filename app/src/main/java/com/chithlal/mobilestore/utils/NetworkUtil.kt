package com.chithlal.mobilestore.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo


class NetworkUtil(val context: Context) {

    /***
     * check for network connection availability
     **/
    fun isNetworkAvailable(): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
        return activeNetwork?.isConnectedOrConnecting == true

    }
}