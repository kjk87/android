package com.root37.buflexz.core.network.model.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by 김종경 on 2016-10-04.
 */
@Parcelize
class ProductPurchase(var seqNo: Long? = null,
                      var code: String? = null,
                      var userKey: String? = null,
                      var buyerNickname: String? = null,
                      var memberDeliverySeqNo: Long? = null,
                      var productSeqNo: Long? = null,
                      var productName: String? = null,
                      var productImage: String? = null,
                      var status: Int? = null,
                      var deliveryStatus: Int? = null,//0:주문확인전, 1:상품준비중, 2:배송중, 3:배송완료, 4:취소
                      var shippingCompany: String? = null,
                      var transportNumber: String? = null,
                      var usePoint: Float? = null,
                      var exchangeRate: Float? = null,
                      var price: Float? = null,
                      var amount: Int? = null,
                      var unitPrice: Int? = null,
                      var regDatetime: String? = null,
                      var sendDatetime: String? = null,
                      var statusDatetime: String? = null,
                      var delivery: MemberDelivery? = null) : Parcelable {

}