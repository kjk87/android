package com.lejel.wowbox.core.network.apis

import com.lejel.wowbox.core.network.model.dto.ServerStatus
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ICdnApi {
    @GET("wowboxStatus.json")
    abstract fun getServerStatus(@Query("timestamp") timestamp: String): Call<ServerStatus>
}