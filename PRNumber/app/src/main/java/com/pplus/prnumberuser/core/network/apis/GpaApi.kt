package com.pplus.prnumberuser.core.network.apis

import retrofit2.Call
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface GpaApi {

    @FormUrlEncoded
    @POST("advertisement/comp")
    abstract fun gpaComp(@FieldMap params: Map<String, String>): Call<Any>

}