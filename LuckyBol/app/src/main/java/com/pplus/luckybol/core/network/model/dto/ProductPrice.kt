package com.pplus.luckybol.core.network.model.dto

import android.os.Parcel
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by imac on 2018. 1. 2..
 */
@Parcelize
class ProductPrice(var seqNo: Long? = null,
                   var code: String? = null,
                   var productSeqNo: Long? = null,
                   var pageSeqNo: Long? = null,
                   var price: Int? = null,
                   var originPrice: Int? = null,
                   var isDiscount: Boolean? = null,
                   var discount: Float? = null,
                   var discountUnit: String? = null, // percent, money
                   var supplyPrice: Float? = null,
                   var consumerPrice: Float? = null,
                   var maximumPrice: Float? = null,
                   var discountRatio: Float? = null,
                   var isLuckyball: Boolean? = null,
                   var marketType: Int? = null, // 1:도매상품, 2:소매상품, 3:가져온 상품
                   var status: Int? = null, //1 : 판매중, 0: 판매중단, 2 : 판매완료(sold out)
                   var product: Product? = null,
                   var productDelivery: ProductDelivery? = null,
                   var page: Page2? = null,
                   var regDatetime: String? = null,
                   var avgEval: Float? = null,
                   var isLike: Boolean? = null,
                   var isPoint: Boolean? = null,
                   var point: Float? = null,
                   var pointUnit: String? = null,
                   var pointPrice: Float? = null) : Parcelable {}