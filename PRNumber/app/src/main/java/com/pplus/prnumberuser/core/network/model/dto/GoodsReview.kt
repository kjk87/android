package com.pplus.prnumberuser.core.network.model.dto

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import kotlinx.parcelize.Parcelize

/**
 * Created by imac on 2018. 1. 2..
 */
@Parcelize
class GoodsReview(var seqNo: Long? = null,
                  var memberSeqNo: Long? = null,
                  var goodsSeqNo: Long? = null,
                  var pageSeqNo: Long? = null,
                  var goodsPriceSeqNo: Long? = null,
                  var buyGoodsSeqNo: Long? = null,
                  var buySeqNo: Long? = null,
                  var eventSeqNo: Long? = null,
                  var eventWinSeqNo: Long? = null,
                  var eventGiftSeqNo: Long? = null,
                  var goods: Goods? = null,
                  var review: String? = null,
                  var reviewReply: String? = null,
                  var reviewReplyDate: String? = null,
                  var eval: Int? = null,
                  var regDatetime: String? = null,
                  var modDatetime: String? = null,
                  var attachments: GoodsAttachments? = null,
                  var member: Member? = null,
                  var page: Page2? = null,
                  var buy: Buy? = null) : Parcelable, Cloneable {
}