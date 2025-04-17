package com.root37.buflexz.core.network.model.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by 김종경 on 2016-10-04.
 */
@Parcelize
class ProductImage(var seqNo: Long? = null,
                   var productSeqNo: Long? = null,
                   var image: String? = null,
                   var array: Int? = null) : Parcelable {

}