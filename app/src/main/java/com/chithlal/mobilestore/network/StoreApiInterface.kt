package com.chithlal.mobilestore.network


import com.chithlal.mobilestore.model.Features
import retrofit2.Response
import retrofit2.http.GET

interface StoreApiInterface {

    @GET("esper-assignment/db")
    suspend fun getFeatures(): Response<Features>

}