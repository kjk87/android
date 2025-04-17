package com.root37.buflexz.core.network.model.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by 김종경 on 2016-10-04.
 */
@Parcelize
class Product(var seqNo: Long? = null,
              var categorySeqNo: Long? = null,
              var title: String? = null,
              var status: String? = null,
              var price: Float? = null,
              var regDatetime: String? = null,
              var imageList:List<ProductImage>? = null) : Parcelable {

}