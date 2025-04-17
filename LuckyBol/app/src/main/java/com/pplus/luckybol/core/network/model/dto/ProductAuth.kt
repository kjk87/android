package com.pplus.luckybol.core.network.model.dto

import android.os.Parcel
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by imac on 2018. 1. 2..
 */
@Parcelize
class ProductAuth(var seqNo: Long? = null,
                  var productSeqNo: Long? = null,
                  var type: String? = null,
                  var agency: String? = null,
                  var authNo: String? = null) : Parcelable {
}