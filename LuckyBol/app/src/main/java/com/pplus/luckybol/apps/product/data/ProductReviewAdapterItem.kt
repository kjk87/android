package com.pplus.luckybol.apps.product.data

import android.os.Parcelable
import com.pplus.luckybol.core.network.model.dto.ProductReview
import kotlinx.parcelize.Parcelize

/**
 * Created By Lonnie on 2020/05/08
 *
 */

@Parcelize
data class ProductReviewAdapterItem(var type: Int, var data:ProductReview? = null):Parcelable