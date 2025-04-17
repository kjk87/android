package com.pplus.luckybol.core.network.model.dto

import android.os.Parcel
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by imac on 2018. 1. 15..
 */

@Parcelize
class Plus2(var seqNo: Long? = null,
            var block: Boolean? = null,
            var memberSeqNo: Long? = null,
            var pageSeqNo: Long? = null,
            var page: Page2? = null,
            var pushActivate: Boolean? = null,
            var regDatetime: String? = null,
            var goodsList: List<Goods>? = null,
            var totalGoodsElements: Int? = null) : Parcelable {
    override fun equals(other: Any?): Boolean {
        if (other == null) return false

        return if (other is Plus2) {
            other.seqNo == seqNo
        } else {
            false
        }
    }
}