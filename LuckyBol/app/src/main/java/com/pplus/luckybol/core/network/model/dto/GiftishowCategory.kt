package com.pplus.luckybol.core.network.model.dto


import android.os.Parcel
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by 김종경 on 2016-10-04.
 */
@Parcelize
class GiftishowCategory(var seqNo: Long? = null,
                        var name: String? = null,
                        var status: String? = null,
                        var regDatetime: String? = null) : Parcelable {
}
