package com.pplus.prnumberuser.core.network.model.dto

import android.os.Parcel
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by imac on 2018. 1. 2..
 */

@Parcelize
class GoodsLike(var memberSeqNo: Long? = null,
                var pageSeqNo: Long? = null,
                var goodsSeqNo: Long? = null,
                var goodsPriceSeqNo: Long? = null,
                var goods: Goods? = null,
                var goodsPrice: GoodsPrice? = null,
                var page: Page2? = null,
                var status: Int? = null,
                var regDatetime: String? = null) : Parcelable {
}