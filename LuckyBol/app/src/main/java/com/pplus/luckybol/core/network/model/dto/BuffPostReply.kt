package com.pplus.luckybol.core.network.model.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by imac on 2018. 1. 2..
 */
@Parcelize
class BuffPostReply(var seqNo: Long? = null,
                    var memberSeqNo: Long? = null,
                    var buffPostSeqNo: Long? = null,
                    var reply: String? = null,
                    var regDatetime: String? = null,
                    var modDatetime: String? = null,
                    var deleted: Boolean? = null,
                    var isFriend: Boolean? = null,
                    var member: Member? = null) : Parcelable {

}