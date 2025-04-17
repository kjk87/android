package com.pplus.luckybol.core.network.model.dto

import android.os.Parcel
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by imac on 2018. 1. 2..
 */
@Parcelize
class ProductReviewImage(var id: Long? = null,
                         var productReviewSeqNo: Long? = null,
                         var image: String? = null,
                         var array: Int? = null,
                         var type: String? = null) : Parcelable {
}