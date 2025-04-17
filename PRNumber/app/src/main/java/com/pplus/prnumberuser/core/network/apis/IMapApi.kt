package com.pplus.prnumberuser.core.network.apis

import com.pplus.prnumberuser.core.network.model.response.result.ResultDaumAddress
import com.pplus.prnumberuser.core.network.model.response.result.ResultDaumKeyword
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface IMapApi {
    //좌표→주소 변환
    @Headers("Authorization: KakaoAK 899b424c1e6b0591a057d4e4c8da1ee6")
    @GET("v2/local/geo/coord2address.json")
    abstract fun requestAddressByGeo(@Query("x") x: String, @Query("y") y: String): Call<ResultDaumAddress>

    @Headers("Authorization: KakaoAK 899b424c1e6b0591a057d4e4c8da1ee6")
    @GET("v2/local/search/keyword.json")
    abstract fun requestSearchLocationKeyword(@Query("query") query: String): Call<ResultDaumKeyword>
}