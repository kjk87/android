package com.pplus.luckybol.core.network.model.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by imac on 2018. 1. 2..
 */
@Parcelize
class BuffPost(var seqNo: Long? = null,
               var buffSeqNo: Long? = null,
               var memberSeqNo: Long? = null,
               var type: String? = null, // normal, shoppingBuff, lottoBuff , eventBuff, eventGift
               var title: String? = null,
               var content: String? = null,
               var divideAmount: Float? = null,
               var divideType: String? = null, //bol, point
               var productPriceSeqNo: Long? = null,
               var regDatetime: String? = null,
               var modDatetime: String? = null,
               var hidden: Boolean? = null,
               var deleted: Boolean? = null,
               var winPrice: Float? = null,
               var isFriend: Boolean? = null,
               var likeCount: Int? = null,
               var replyCount: Int? = null,
               var isLike: Boolean? = null,
               var imageList: List<BuffPostImage>? = null,
               var member: Member? = null) : Parcelable {

}