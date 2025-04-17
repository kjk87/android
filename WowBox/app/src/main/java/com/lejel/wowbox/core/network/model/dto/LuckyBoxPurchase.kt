package com.lejel.wowbox.core.network.model.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by 김종경 on 2016-10-04.
 */
@Parcelize
class LuckyBoxPurchase(var seqNo: Long? = null,
                       var luckyboxSeqNo: Long? = null,
                       var userKey: String? = null,
                       var orderNo: String? = null,
                       var salesType: String? = null,
                       var title: String? = null,
                       var paymentMethod: String? = null,
                       var quantity: Int? = null,
                       var price: Float? = null,
                       var pgPrice: Int? = null,
                       var useCash: Int? = null,
                       var unitPrice: Float? = null,
                       var cancelQuantity: Int? = null,
                       var cancelPrice: Float? = null,
                       var remainPrice: Float? = null,
                       var status: Int? = null,
                       var regDatetime: String? = null,
                       var paymentDatetime: String? = null,
                       var changeStatusDatetime: String? = null,
                       var isCancelable: Boolean? = null,
                       var invoiceUrl: String? = null,
                       var luckyboxPurchaseItem: List<LuckyBoxPurchaseItem>? = null) : Parcelable {

}