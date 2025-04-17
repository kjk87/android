package com.lejel.wowbox.core.network.model.response

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