package com.chithlal.mobilestore.ui.activity

import android.net.ConnectivityManager
import android.net.Network
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import com.chithlal.mobilestore.R
import com.chithlal.mobilestore.databinding.ActivityMainBinding
import com.chithlal.mobilestore.ui.viewmodel.StoreViewModel
import com.chithlal.mobilestore.utils.NetworkUtil
import javax.inject.Inject

class MainActivity : AppCompatActivity() {
    private lateinit var mainBinding: ActivityMainBinding
    @Inject() lateinit var networkUtil: NetworkUtil
    val storeViewModel: StoreViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)

    }

    override fun onResume() {
        super.onResume()

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N ){
            registerNetworkCallback()
        }
        else{
            if(networkUtil.isNetworkAvailable()){
                storeViewModel.getStoreData()
            }
            else{

            }
        }

    }
    @RequiresApi(Build.VERSION_CODES.N)
    private fun registerNetworkCallback(){
        val connectivityManager = getSystemService(ConnectivityManager::class.java)
        connectivityManager.registerDefaultNetworkCallback( object : ConnectivityManager.NetworkCallback(){
            override fun onAvailable(network: Network) {
                super.onAvailable(network)
            }

            override fun onLost(network: Network) {
                super.onLost(network)
            }

            override fun onUnavailable() {
                super.onUnavailable()
            }
        })

    }

}