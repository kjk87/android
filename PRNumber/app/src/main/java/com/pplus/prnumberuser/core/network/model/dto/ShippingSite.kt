package com.pplus.prnumberuser.core.network.model.dto

import android.os.Parcel
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by imac on 2018. 1. 2..
 */
@Parcelize
class ShippingSite(var seqNo: Long? = null,
                   var memberSeqNo: Long? = null,
                   var siteName: String? = null,
                   var postCode: String? = null,
                   var address: String? = null,
                   var addressDetail: String? = null,
                   var isDefault: Boolean? = null,
                   var receiverName: String? = null,
                   var receiverTel: String? = null) : Parcelable {
}