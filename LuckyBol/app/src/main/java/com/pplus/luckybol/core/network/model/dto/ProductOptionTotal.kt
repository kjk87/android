package com.pplus.luckybol.core.network.model.dto


import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by kjk on 2017. 6. 20..
 */

@Parcelize
class ProductOptionTotal(var productOptionList: List<ProductOption>? = null,
                         var productOptionItemList: List<ProductOptionItem>? = null,
                         var productOptionDetailList: List<ProductOptionDetail>? = null) : Parcelable {
}
