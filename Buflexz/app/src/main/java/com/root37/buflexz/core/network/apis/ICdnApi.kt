package com.root37.buflexz.core.network.apis

import com.root37.buflexz.core.network.model.dto.ServerStatus
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ICdnApi {
    @GET("buflexzStatus.json")
    abstract fun getServerStatus(@Query("timestamp") timestamp: String): Call<ServerStatus>
}