package com.lejel.wowbox.core.network.model.dto


import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by kjk on 2017. 6. 20..
 */

@Parcelize
class ProductOptionTotal(var option: List<ProductOption>? = null,
                         var detail: List<ProductOptionDetail>? = null) : Parcelable {
}
