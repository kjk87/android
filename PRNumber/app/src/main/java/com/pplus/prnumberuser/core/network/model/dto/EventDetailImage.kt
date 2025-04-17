package com.pplus.prnumberuser.core.network.model.dto


import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by 김종경 on 2016-10-04.
 */

@Parcelize
class EventDetailImage(var seqNo: Long? = null,
                       var eventSeqNo: Long? = null,
                       var image: String? = null) : Parcelable {

    override fun equals(other: Any?): Boolean {
        if (other == null) return false

        return if (other is EventDetailImage) {
            other.seqNo == seqNo
        } else {
            false
        }
    }
}
