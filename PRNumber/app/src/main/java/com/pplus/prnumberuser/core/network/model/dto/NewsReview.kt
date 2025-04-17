package com.pplus.prnumberuser.core.network.model.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by imac on 2018. 1. 2..
 */
@Parcelize
class NewsReview(
        var seqNo: Long? = null,
        var newsSeqNo: Long? = null,
        var pageSeqNo: Long? = null,
        var memberSeqNo: Long? = null,
        var review: String? = null,
        var regDatetime: String? = null,
        var modDatetime: String? = null,
        var deleted: Boolean? = null,
        var member: Member? = null) : Parcelable {

}