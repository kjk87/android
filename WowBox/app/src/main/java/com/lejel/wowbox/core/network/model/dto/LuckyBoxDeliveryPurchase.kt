package com.lejel.wowbox.core.network.model.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by 김종경 on 2016-10-04.
 */
@Parcelize
class LuckyBoxDeliveryPurchase(var seqNo: Long? = null,
                               var luckyboxPurchaseItemSeqNo: Long? = null,
                               var userKey: String? = null,
                               var orderNo: String? = null,
                               var paymentMethod: String? = null,
                               var price: Float? = null,
                               var pgPrice: Int? = null,
                               var usePoint: Int? = null,
                               var status: Int? = null, //1:결제요청, 2:결제승인, 3:취소
                               var regDatetime: String? = null,
                               var paymentDatetime: String? = null,
                               var cancelDatetime: String? = null,
                               var invoiceUrl: String? = null,
                               var selectOption: LuckyBoxPurchaseItemOption? = null,
                               var selectDelivery: LuckyBoxDelivery? = null) : Parcelable {

}