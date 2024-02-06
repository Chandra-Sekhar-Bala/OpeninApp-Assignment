package com.openinapp.task.repo.network

import com.openinapp.task.model.LinkResponse
import retrofit2.http.GET
import retrofit2.http.Header


interface LinkApi {

    @GET("api/v1/dashboardNew")
    suspend fun getLinkData(@Header("Authorization") authorization: String)
            : LinkResponse

}
