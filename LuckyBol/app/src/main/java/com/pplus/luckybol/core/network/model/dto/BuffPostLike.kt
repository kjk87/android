package com.pplus.luckybol.core.network.model.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by imac on 2018. 1. 2..
 */
@Parcelize
class BuffPostLike(var seqNo: Long? = null,
                   var memberSeqNo: Long? = null,
                   var buffPostSeqNo: Long? = null,
                   var regDatetime: String? = null,
                   var isFriend: Boolean? = null,
                   var member: Member? = null) : Parcelable {

}