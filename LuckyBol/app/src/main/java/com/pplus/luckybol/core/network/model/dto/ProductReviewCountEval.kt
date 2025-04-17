package com.pplus.luckybol.core.network.model.dto


import android.os.Parcel
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by 김종경 on 2016-10-04.
 */
@Parcelize
class ProductReviewCountEval(var count: Int? = null,
                             var eval: Int = 0) : Parcelable {
}
