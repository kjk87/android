package com.pplus.prnumberbiz.core.network.apis

import com.pplus.prnumberbiz.core.network.model.dto.ServerStatus
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ICdnApi {
    //좌표→주소 변환
    @GET("STATUS.json")
    abstract fun getServerSatus(@Query("timestamp") timestamp: String): Call<ServerStatus>

}