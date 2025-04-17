package com.lejel.wowbox.core.network.model.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by imac on 2018. 1. 2..
 */
@Parcelize
class ProductDelivery(var seqNo: Long? = null,
                      var productSeqNo: Long? = null,
                      var method: Int? = null, // 배송방법 ( 1:택배/우편, 2:직접전달(화물)
                      var type: Int? = null, // 1:무료, 2:유료, 3:조건부 무료
                      var shippingCompany: String? = null, //택배사
                      var forwardingAddr: Long? = null, // 출고지 (pageAddress seqNo)
                      var returnAddr: Long? = null, // 반품/교환 주소 (pageAddress seqNo)
                      var paymentMethod: String? = null, // 배송비 결제방식 before, after
                      var deliveryFee: Float? = null, //배송비
                      var isAddFee: Boolean? = null, // 도서산간 추가배송비 설정
                      var deliveryAddFee1: Float? = null, // 제주지역 추가배송비
                      var deliveryAddFee2: Float? = null, // 도서산간 추가배송비
                      var deliveryMinPrice: Float? = null, // 무료배송비
                      var deliveryReturnFee: Float? = null, //반품 배송비
                      var deliveryExchangeFee: Float? = null, //교환배송비
                      var asTel: String? = null, //as전화
                      var asMent: String? = null, //as안내
                      var specialNote: String? = null, // 반품/교환 안내(판매자 특이사항)
                      ) : Parcelable {}