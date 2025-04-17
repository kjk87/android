package com.root37.buflexz.core.network.model.response

import com.google.gson.annotations.SerializedName

class NewResultResponse<T> {
    /**
     * 상태 코드
     */
    var code = 0
    var result: T? = null
    var message: String? = null

    override fun toString(): String {
        return "NewResultResponse(code=$code, result=$result, message=$message)"
    }

}