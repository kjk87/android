package com.lejel.wowbox.core.network.model.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by imac on 2018. 1. 2..
 */
@Parcelize
class ProductImage(var seqNo: Long? = null,
                   var productSeqNo: Long? = null,
                   var image: String? = null,
                   var array: Int? = null,
                   var deligate: Boolean? = null) : Parcelable {
}