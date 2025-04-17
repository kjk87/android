package com.pplus.prnumberuser.core.network.model.dto

import android.os.Parcel
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by imac on 2018. 1. 2..
 */
@Parcelize
class Card(var id: Long? = null,
           var memberSeqNo: Long? = null,
           var cardNumber: String? = null,
           var autoKey: String? = null,
           var cardCode: String? = null,
           var represent: Boolean? = null,
           var genDate: String? = null,
           var errorMsg: String? = null) : Parcelable {
}