package com.pplus.luckybol.core.network.model.response

import com.google.gson.annotations.SerializedName

class NewResultResponse<T> {
    /**
     * 상태 코드
     */
    var resultCode = 0

    /**
     * 서버로 부터 받은 결과물 Object
     */
    @SerializedName("row")
    var data: T? = null

    /**
     * 서버로 부터 받은 결과물 Object List
     */
    @SerializedName("rows")
    var datas: List<T>? = null

    /**
     * 서버로 부터 받은 결과물 error값
     */
    @SerializedName("errorExtra")
    var errorExtra: Map<String, String>? = null

    override fun toString(): String {
        return "NewResultResponse{resultCode=$resultCode, data=$data, datas=$datas, errorExtra=$errorExtra}"
    }
}