package com.chithlal.mobilestore.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chithlal.mobilestore.model.Features
import com.chithlal.mobilestore.network.StoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StoreViewModel @Inject constructor(private val storeRepo: StoreRepository) :
    ViewModel() {

    //LiveData to keep track the store api response
    val storeLiveData = MutableLiveData<Features>()

    //LiveData to update error in api call/response
    val storeErrorLiveData = MutableLiveData<String>()

    //function calling store api to get the features
    fun getStoreData(){
        viewModelScope.launch{

            try{
                storeRepo.getFeatures().let {
                    if (it.isSuccessful){
                        storeLiveData.postValue(it.body())
                    }
                    else{
                        storeErrorLiveData.postValue("Something Went wrong")
                    }
                }
            }catch (e : Exception){
                storeErrorLiveData.postValue("Something Went wrong")
            }

        }
    }

}