package com.pplus.prnumberuser.core.network.model.dto


import android.os.Parcelable
import kotlinx.android.parcel.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

/**
 * Created by 김종경 on 2016-10-04.
 */

@Parcelize
class EventDetail(var seqNo: Long? = null,
                  var eventSeqNo: Long? = null,
                  var type: Int? = null,//1:단답형, 2:장문형, 3:객관식 단일선택(radio), 4:객관식 단일선택(dropbox), 5:객관식 복수선택(checkbox)
                  var question: String? = null,
                  var guide: String? = null,
                  var compulsory: Boolean? = null) : Parcelable {

    @IgnoredOnParcel var data:Any? = null

    override fun equals(other: Any?): Boolean {
        if (other == null) return false

        return if (other is EventDetail) {
            other.seqNo == seqNo
        } else {
            false
        }
    }
}
