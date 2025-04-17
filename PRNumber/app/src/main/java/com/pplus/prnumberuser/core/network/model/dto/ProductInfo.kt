package com.pplus.prnumberuser.core.network.model.dto

import android.os.Parcel
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by imac on 2018. 1. 2..
 */
@Parcelize
class ProductInfo(var seqNo: Long? = null,
                  var productSeqNo: Long? = null,
                  var model: String? = null,
                  var modelCode: String? = null,
                  var brand: String? = null,
                  var menufacturer: String? = null,// 제조사
                  var originType: String? = null,// 원산지타입 (domestic, imported, etc)
                  var origin: String? = null,
                  var underAge: Boolean? = null,
                  var menufacturedDate: String? = null,
                  var effectiveDate: String? = null) : Parcelable {
}