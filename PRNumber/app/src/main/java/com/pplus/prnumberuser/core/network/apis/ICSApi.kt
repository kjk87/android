package com.pplus.prnumberuser.core.network.apis

import com.pplus.prnumberuser.core.network.model.dto.ResultRiderFee
import retrofit2.Call
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ICSApi {

    @FormUrlEncoded
    @POST("cs/getRiderFee2")
    abstract fun getRiderFee(@FieldMap params: Map<String, String>): Call<ResultRiderFee>
}