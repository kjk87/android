package com.pplus.prnumberuser.core.network.model.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by imac on 2018. 1. 2..
 */
@Parcelize
class PurchaseProduct(var seqNo: Long? = null,
                      var purchaseSeqNo: Long? = null,
                      var memberSeqNo: Long? = null,
                      var pageSeqNo: Long? = null,
                      var friendMemberSeqNo: Long? = null,
                      var productSeqNo: Long? = null,
                      var productPriceCode: String? = null,
                      var productDeliverySeqNo: Long? = null,
                      var salesType: Long? = null, // 판매분류 - 1:매장판매, 2:배달, 3:배송, 4:예약, 5:픽업
                      var status: Int? = null, // 결제 상태값. 1:결제요청, 2:결제승인, 11:취소요청, 12:취소완료, 21:환불요청, 22:환불완료, 31:교환요청, 32:교환완료
                      var deliveryStatus: Int? = null, // 배송 상태값. 1:상품준비중, 2:주문취소, 3:배송중, 4:배송완료, 5:환불수거중, 6:환불수거완료, 7:교환수거중, 8:교환상품준비중, 9:교환배송중, 10:교환배송완료, 99:구매확정
                      var riderStatus: Int? = null, // 배달 상태값. 1:배달준비, 2:배달취소, 3:배달중, 99:배달완료
                      var reserveStatus: Int? = null, // 예약 상태값. 1:예약중, 2:예약취소, 99:사용완료
                      var isStatusCompleted: String? = null, // 모든 상태 완료된 값.(구매확정, 배달완료, 사용완료 값이 되면 상태값 true)
                      var title: String? = null,
                      var count: Int? = null,
                      var price: Float? = null, // 구매 상품 가격
                      var productPrice: Float? = null, // 상품가격+옵션가격 합계(배송비 제외)
                      var unitPrice: Float? = null, //상품 개당가격
                      var optionPrice: Float? = null, // 옵션가격의 합계 optionPrice * count
                      var supplyPageSeqNo: Long? = null,
                      var supplyPrice: Float? = null,
                      var supplyPricePaymentFee: Float? = null,
                      var benefitPaymentFee: Float? = null,
                      var deliveryFeePaymentFee: Float? = null,
                      var supplyPriceFee: Float? = null,
                      var benefitFee: Float? = null,
                      var deliveryFeeFee: Float? = null,
                      var returnPaymentPrice: Float? = null,
                      var paymentFee: Float? = null,
                      var platformFee: Float? = null,
                      var payDatetime: String? = null,
                      var cancelDatetime: String? = null,
                      var cancelMemo: String? = null,
                      var exchangeDatetime: String? = null,
                      var exchangeMemo: String? = null,
                      var refundDatetime: String? = null,
                      var refundMemo: String? = null,
                      var agentSeqNo: Long? = null,
                      var isPaymentPoint: Boolean? = null,
                      var savedPoint: Int? = null,
                      var regDatetime: String? = null,
                      var changeStatusDatetime: String? = null,
                      var endDate: String? = null,
                      var product: Product? = null,
                      var productPriceData: ProductPrice? = null,
                      var purchase: Purchase? = null,
                      var page: Page2? = null,
                      var purchaseProductOptionSelectList: List<PurchaseProductOption>? = null,
                      var purchaseProductOptionList: List<PurchaseProductOption>? = null,
                      var purchaseDeliverySelect: PurchaseDelivery? = null,
                      var purchaseDelivery: PurchaseDelivery? = null,
                      var isReviewExist: Boolean? = null,
                      var startTime:String? = null,
                      var endTime:String? = null) : Parcelable {}