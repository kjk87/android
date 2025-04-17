package com.pplus.luckybol.core.network.model.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by imac on 2018. 1. 2..
 */
@Parcelize
class ProductReview(var seqNo: Long? = null,
                    var memberSeqNo: Long? = null,
                    var pageSeqNo: Long? = null,
                    var productSeqNo: Long? = null,
                    var productPriceSeqNo: Long? = null,
                    var purchaseProductSeqNo: Long? = null,
                    var review: String? = null,
                    var eval: Int? = null,
                    var regDatetime: String? = null,
                    var modDatetime: String? = null,
                    var reviewReply: String? = null,
                    var reviewReplyDate: String? = null,
                    var member: Member? = null,
                    var imageList:List<ProductReviewImage>? = null) : Parcelable {
}