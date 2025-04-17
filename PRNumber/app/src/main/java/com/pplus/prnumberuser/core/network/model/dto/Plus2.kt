package com.pplus.prnumberuser.core.network.model.dto

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
            var agreement: Boolean? = null,
            var regDatetime: String? = null,
            var newsList: List<News>? = null,
            var totalNewsElements: Int? = null,
            var plusGiftReceived: Boolean? = null,
            var plusGiftReceivedDateTime:String? = null) : Parcelable {
    override fun equals(other: Any?): Boolean {
        if (other == null) return false

        return if (other is Plus2) {
            other.seqNo == seqNo
        } else {
            false
        }
    }
}