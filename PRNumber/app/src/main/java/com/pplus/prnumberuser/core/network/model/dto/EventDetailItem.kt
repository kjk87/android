package com.pplus.prnumberuser.core.network.model.dto


import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by 김종경 on 2016-10-04.
 */

@Parcelize
class EventDetailItem(var seqNo: Long? = null,
                      var eventSeqNo: Long? = null,
                      var eventDetailSeqNo: Long? = null,
                      var item: String? = null) : Parcelable {

    override fun equals(other: Any?): Boolean {
        if (other == null) return false

        return if (other is EventDetailItem) {
            other.seqNo == seqNo
        } else {
            false
        }
    }
}
