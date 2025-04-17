package com.pplus.luckybol.core.network.apis

import com.pplus.luckybol.core.network.model.dto.ServerStatus
import com.pplus.luckybol.core.network.model.dto.Zzal
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ICdnApi {
    @GET("luckybol_status.json")
    abstract fun getServerStatus(@Query("timestamp") timestamp: String): Call<ServerStatus>
}