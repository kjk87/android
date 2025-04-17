package com.pplus.prnumberuser.core.network.model.dto

import android.os.Parcel
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by imac on 2018. 1. 2..
 */
@Parcelize
class CardReq(var cardNo: String? = null,
              var expireDt: String? = null,
              var cardAuth: String? = null,
              var cardPassword: String? = null) : Parcelable {
}