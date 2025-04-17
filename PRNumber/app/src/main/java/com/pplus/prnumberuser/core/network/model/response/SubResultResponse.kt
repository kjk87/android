package com.pplus.prnumberuser.core.network.model.response

/**
 * Created by J2N on 16. 6. 21..<br></br> <h><t>제일 기본이되는 res 객체</t></h> <br></br> 'T' 부분을 정의하지않으면 Map<String , Object> 형태로 반환한다. <br></br>
</String> */
class SubResultResponse<T : Any> {

    var content: List<T>? = null
    var totalPages: Int? = null
    var totalElements: Int? = null
    var last: Boolean? = null
    var first: Boolean? = null
}
