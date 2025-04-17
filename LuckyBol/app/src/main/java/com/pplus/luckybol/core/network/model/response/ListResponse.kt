package com.pplus.luckybol.core.network.model.response

class ListResponse<T : Any> {

    var list: List<T>? = null
    var total: Int? = null
}
