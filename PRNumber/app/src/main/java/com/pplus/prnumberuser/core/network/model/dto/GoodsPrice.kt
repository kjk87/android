package com.pplus.prnumberuser.core.network.model.dto

import android.os.Parcel
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by imac on 2018. 1. 2..
 */
@Parcelize
class GoodsPrice(var seqNo: Long? = null,
                 var goodsSeqNo: Long? = null,
                 var pageSeqNo: Long? = null,
                 var goods: Goods? = null,
                 var page: Page? = null,
                 var supplyPrice: Int? = null,
                 var consumerPrice: Int? = null,
                 var maximumPrice: Int? = null,
                 var originPrice: Int? = null,
                 var price: Int? = null,
                 var status: Int? = null,
                 var discountRatio: Float? = null,
                 var regDatetime: String? = null,
                 var isLuckyball: Boolean? = null,
                 var isWholesale: Boolean? = null,
                 var avgEval: Float? = null) : Parcelable {
}