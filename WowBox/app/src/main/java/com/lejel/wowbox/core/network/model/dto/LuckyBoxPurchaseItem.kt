package com.lejel.wowbox.core.network.model.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by 김종경 on 2016-10-04.
 */
@Parcelize
class LuckyBoxPurchaseItem(var seqNo: Long? = null,
                           var luckyboxSeqNo: Long? = null,
                           var luckyboxPurchaseSeqNo: Long? = null,
                           var userKey: String? = null,
                           var luckyboxTitle: String? = null,
                           var paymentMethod: String? = null,
                           var price: Float? = null,
                           var status: Int? = null, //1:결제요청, 2:결제승인, 3:취소, 4:캐시백교환, 5:럭키볼교환
                           var isOpen: Boolean? = null,
                           var deliveryStatus: Int? = null, // 1:배송대기(배송신청), 2:배송중, 3:배송완료
                           var regDatetime: String? = null,
                           var paymentDatetime: String? = null,
                           var openDatetime: String? = null,
                           var cancelDatetime: String? = null,
                           var completeDatetime: String? = null,
                           var exchangeDatetime: String? = null,
                           var productSeqNo: Long? = null,
                           var productDeliverySeqNo: Long? = null,
                           var supplyPageSeqNo: Long? = null,
                           var productType: String? = null, // delivery
                           var productName: String? = null,
                           var productImage: String? = null,
                           var productPrice: Float? = null,
                           var optionName: String? = null,
                           var optionPrice: Float? = null,
                           var supplyPrice: Float? = null,
                           var supplyPricePaymentFee: Float? = null,
                           var deliveryFee: Float? = null,
                           var deliveryPayStatus: Int? = null, // 1:결제요청, 2:결제승인, 3:취소
                           var luckyboxDeliveryPurchaseSeqNo: Long? = null,
                           var deliveryPaymentPrice: Float? = null, // deliveryFee + optionPrice
                           var luckyboxDeliverySeqNo: Long? = null,
                           var impression: String? = null,
                           var refundBol: Int? = null,
                           var replyCount: Int? = null,
                           var isReviewExist: Boolean? = null,
                           var memberTotal: Member? = null,
                           var luckyboxDeliveryPurchase: LuckyBoxDeliveryPurchase? = null,
                           var luckyboxDelivery: LuckyBoxDelivery? = null) : Parcelable {

}