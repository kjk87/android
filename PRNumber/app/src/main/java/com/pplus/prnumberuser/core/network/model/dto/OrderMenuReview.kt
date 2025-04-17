package com.pplus.prnumberuser.core.network.model.dto

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Created by imac on 2018. 1. 2..
 */
@Parcelize
class OrderMenuReview(var seqNo: Long? = null,
                      var memberSeqNo: Long? = null,
                      var pageSeqNo: Long? = null,
                      var orderPurchaseSeqNo: Long? = null,
                      var review: String? = null,
                      var eval: Int? = null,
                      var regDatetime: String? = null,
                      var modDatetime: String? = null,
                      var reviewReply: String? = null,
                      var reviewReplyDate: String? = null,
                      var member: Member? = null,
                      var imageList:List<ReviewImage>? = null) : Parcelable {
}