package com.pplus.prnumberuser.core.network.model.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by imac on 2018. 1. 2..
 */
@Parcelize
class ProductLike(var seqNo: Long? = null,
                  var memberSeqNo: Long? = null,
                  var productPriceSeqNo: Long? = null,
                  var productSeqNo: Long? = null,
                  var productPrice: ProductPrice? = null,
                  var status: Int? = null,
                  var regDatetime: String? = null,
                  var expireDatetime: String? = null) : Parcelable {
}