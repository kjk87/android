package com.pplus.prnumberuser.core.network.apis

import com.pplus.prnumberuser.core.network.model.dto.ServerStatus
import com.pplus.prnumberuser.core.network.model.dto.Zzal
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ICdnApi {
    @GET("STATUS.json")
    abstract fun getServerSatus(@Query("timestamp") timestamp: String): Call<ServerStatus>

    @GET("zzal.json")
    abstract fun getZzal(@Query("timestamp") timestamp: String): Call<Zzal>

}