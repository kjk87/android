package com.pplus.luckybol.core.network.model.dto


import android.os.Parcel
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by 김종경 on 2016-10-04.
 */
@Parcelize
class GiftishowBuy(var seqNo: Long? = null,
                   var status: String? = null,
                   var memberSeqNo: Long? = null,
                   var giftishowSeqNo: Long? = null,
                   var totalCount: Int? = null,
                   var unitPrice: Int? = null,
                   var price: Int? = null,
                   var msg: String? = null,
                   var targetList: List<GiftishowTarget>? = null,
                   var regDatetime: String? = null,
                   var giftishow: Giftishow? = null) : Parcelable {}
