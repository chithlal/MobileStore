package com.chithlal.mobilestore.network

import com.chithlal.mobilestore.model.Features
import retrofit2.Response
import javax.inject.Inject

class StoreRepository @Inject constructor(private val apiInterface: StoreApiInterface) {

    //function retuning mobile features from api call
    suspend fun getFeatures(): Response<Features> = apiInterface.getFeatures()



}