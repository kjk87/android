package com.pplus.prnumberuser.core.network.model.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by imac on 2018. 1. 2..
 */
@Parcelize
class Purchase(var seqNo: Long? = null,
               var memberSeqNo: Long? = null,
               var pageSeqNo: Long? = null,
               var orderId: String? = null,
               var salesType: Long? = null, // 판매분류 - 1:매장판매, 2:배달, 3:배송, 4:예약, 5:픽업
               var status: Int? = null,
               var payMethod: String? = null,//card, point
               var appType: String? = null,// luckyball, nonMember, pplus
               var pg: String? = null,
               var pgTranId: String? = null,
               var title: String? = null,
               var buyerEmail: String? = null,
               var buyerName: String? = null,
               var buyerTel: String? = null,
               var price: Float? = null,//결제금액
               var productPrice: Float? = null,// 상품가격합
               var optionPrice: Float? = null,// 옵션가격합
               var deliveryFee: Float? = null,// 배송비합(추가배송비 제외)
               var deliveryAddFee: Float? = null,// 제주/도서산간 추가배송비 합
               var nonMember: Boolean? = null,
               var finteckResult: String? = null,
               var returnPaymentPrice: Float? = null,
               var savedPoint: Int? = null,
               var agentSeqNo: Long? = null,
               var loginId: String? = null,
               var regDatetime: String? = null,
               var modDatetime: String? = null,
               var receiptId: String? = null,
               var installment: String? = null,
               var purchaseProductSelectList: List<PurchaseProduct>? = null) : Parcelable {
}
/*
status
      1:결제요청, 2:결제승인
      11:취소요청, 12:부분취소요청, 13:취소완료, 14:부분취소완료
      21:환불요청, 22:부분환불요청, 23:환불완료, 24:부분환불완료
      31:교환요청, 32:부분교환요청, 33:교환완료, 34:부분교환완료
      99:완료처리
     */