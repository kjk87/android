package com.pplus.luckybol.core.network.model.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by imac on 2018. 1. 2..
 */
@Parcelize
class BuffMember(var seqNo: Long? = null,
                 var buffSeqNo: Long? = null,
                 var memberSeqNo: Long? = null,
                 var isOwner: Boolean? = null,
                 var receivedBol: Double? = null,
                 var dividedBol: Double? = null,
                 var receivedPoint: Double? = null,
                 var dividedPoint: Double? = null,
                 var regDatetime: String? = null,
                 var isFriend: Boolean? = null,
                 var buff: Buff? = null,
                 var member: Member? = null) : Parcelable {

    override fun equals(other: Any?): Boolean {
        if (other == null) return false

        return if (other is BuffMember) {
            other.seqNo == seqNo
        } else {
            false
        }
    }
}