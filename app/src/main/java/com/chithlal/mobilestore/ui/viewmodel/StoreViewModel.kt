package com.chithlal.mobilestore.ui.viewmodel

import android.app.Activity
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chithlal.mobilestore.model.Features
import com.chithlal.mobilestore.network.StoreRepository
import com.chithlal.mobilestore.utils.NetworkCacheUtil
import com.chithlal.mobilestore.utils.NetworkUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StoreViewModel @Inject constructor(private val storeRepo: StoreRepository) :
    ViewModel() {

    //network util to check internet access
    @Inject
    lateinit var networkUtil: NetworkUtil

    //Network cache util to manage cache
    @Inject
    lateinit var networkCacheUtil: NetworkCacheUtil

    //LiveData to keep track the store api response
    val storeLiveData = MutableLiveData<Features>()

    //LiveData to update error in api call/response
    val storeErrorLiveData = MutableLiveData<String>()

    //function calling store api to get the features
    fun getStoreData(activity: Activity) {
        // if network is available fetch from server
        if (networkUtil.isNetworkAvailable()) {
            viewModelScope.launch {

                try {
                    storeRepo.getFeatures().let {
                        if (it.isSuccessful) {
                            storeLiveData.postValue(it.body())
                            networkCacheUtil.writeCacheFile(
                                activity,
                                it.body()!!
                            ) // write latest data to cache
                        } else {
                            storeErrorLiveData.postValue("Something Went wrong")
                        }
                    }
                } catch (e: Exception) {
                    storeErrorLiveData.postValue("Something Went wrong")
                    Log.d("HTTP:ERROR", "getStoreData: ${e.localizedMessage}")
                }

            }
        } else { // if network not available fetch from cache

            val features = networkCacheUtil.readCacheFile(activity)
            if (features != null) {
                storeLiveData.postValue(features!!)
                storeErrorLiveData.postValue("Network unavailable showing last synced data! ")
            } else {
                storeErrorLiveData.postValue("No Internet access!")
            }
        }

    }

}