package com.lejel.wowbox.core.network.model.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by imac on 2018. 1. 2..
 */
@Parcelize
class LuckyBoxReview(var seqNo: Long? = null,
                     var userKey: String? = null,
                     var luckyboxPurchaseItemSeqNo: Long? = null,
                     var review: String? = null,
                     var regDatetime: String? = null,
                     var modDatetime: String? = null,
                     var imageList: List<LuckyBoxReviewImage>? = null,
                     var replyCount: Int? = null,
                     var friend: Boolean? = null,
                     var memberTotal: Member? = null,
                     var luckyboxPurchaseItem: LuckyBoxPurchaseItem? = null) : Parcelable {}