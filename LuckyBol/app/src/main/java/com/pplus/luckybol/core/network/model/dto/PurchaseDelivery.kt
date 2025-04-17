package com.pplus.luckybol.core.network.model.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by imac on 2018. 1. 2..
 */
@Parcelize
class PurchaseDelivery(var seqNo: Long? = null,
                       var purchaseSeqNo: Long? = null,
                       var purchaseProductSeqNo: Long? = null,
                       var type: Int? = null, // 1:무료, 2:유료, 3:조건부 무료
                       var method: Int? = null, // 판매분류 - 1:매장판매, 2:배달, 3:배송, 4:예약, 5:픽업
                       var status: Int? = null,// 배송방법 ( 1:택배/우편, 2:직접전달(화물)
                       var paymentMethod: String? = null,// 배송비 결제방식 before, after
                       var receiverName: String? = null,
                       var receiverTel: String? = null,
                       var receiverPostCode: String? = null,
                       var receiverAddress: String? = null,
                       var receiverAddressDetail: String? = null,
                       var deliveryMemo: String? = null,
                       var deliveryFee: Float? = null,
                       var deliveryStartDatetime: String? = null,
                       var deliveryCompleteDatetime: String? = null,
                       var deliveryAddFee1: Float? = null,// 제주 추가배송비
                       var deliveryAddFee2: Float? = null,// 도서산간 추가배송비
                       var shippingCompany: String? = null,
                       var transportNumber: String? = null,
                       var shippingCompanyCode: String? = null) : Parcelable {
}