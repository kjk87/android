package com.pplus.luckybol.core.network.model.dto


import android.os.Parcel
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by 김종경 on 2016-10-04.
 */
@Parcelize
class GiftishowTarget(var seqNo: Long? = null,
                      var giftishowBuySeqNo: Long? = null,
                      var mobileNumber: String? = null,
                      var name: String? = null,
                      var trId: String? = null,
                      var orderNo: String? = null,
                      var regDatetime: String? = null) : Parcelable {
}
