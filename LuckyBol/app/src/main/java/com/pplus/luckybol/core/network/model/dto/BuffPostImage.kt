package com.pplus.luckybol.core.network.model.dto

import android.os.Parcel
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by imac on 2018. 1. 2..
 */
@Parcelize
class BuffPostImage(var seqNo: Long? = null,
                    var productSeqNo: Long? = null,
                    var image: String? = null,
                    var array: Int? = null) : Parcelable {
}