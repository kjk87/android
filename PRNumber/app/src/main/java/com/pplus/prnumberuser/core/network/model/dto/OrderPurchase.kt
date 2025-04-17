package com.pplus.prnumberuser.core.network.model.dto

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Created by imac on 2018. 1. 2..
 */
@Parcelize
class OrderPurchase(var seqNo: Long? = null,
                    var code: String? = null,
                    var memberSeqNo: Long? = null,
                    var pageSeqNo: Long? = null,
                    var agentSeqNo: Long? = null,
                    var wholesaleCode: String? = null,
                    var distributorCode: String? = null,
                    var dealerCode: String? = null,
                    var pageCommissionRatio: Float? = null,
                    var wholesaleCommissionRatio: Float? = null,
                    var distributorCommissionRatio: Float? = null,
                    var dealerCommissionRatio: Float? = null,
                    var pageCommission: Float? = null,
                    var wholesaleCommission: Float? = null,
                    var distributorCommission: Float? = null,
                    var dealerCommission: Float? = null,
                    var orderId: String? = null,
                    var salesType: Int? = null, // 판매분류 - 1:매장판매, 2:배달, 3:배송, 4:예약, 5:포장, 6:티켓
                    var status: Int? = null,// 0:결제요청, 1:후불주문요청(접수대기), 2:결제승인(접수대기), 3:접수완료, 4:취소요청, 5:취소완료, 99:완료처리
                    var statusRider: Int? = null, // 배달 0:접수대기, 1:접수완료, 2:배달취소, 3:기사배정  4:배달중(기사픽업), 99:배달완료
                    var statusShop: Int? = null,// 매장 0:접수대기, 1:접수완료, 2:취소, 99:사용완료
                    var statusPack: Int? = null, // 포장 0:접수대기, 1:접수완료, 2:포장취소, 99:포장완료
                    var statusReserve: Int? = null, // 예약 0:접수대기, 1:접수완료, 2:예약취소, 99:사용완료
                    var statusTicket: Int? = null, // 티켓 0:접수대기, 1:접수완료, 2:예약취소, 4:사용요청, 99:사용완료
                    var isStatusCompleted: Boolean? = null, // 모든 상태 완료된 값.(구매확정, 배달완료, 사용완료 값이 되면 상태값 true)
                    var title: String? = null,
                    var appType: String? = null,// luckyball, nonMember, pplus
                    var payMethod: String? = null,// card, point, outsideCard, outsideCash
                    var amount: Int? = null,// 주문메뉴갯수-옵션제외
                    var price: Float? = null,//결제금액
                    var optionPrice: Float? = null,// 옵션가격합
                    var menuPrice: Float? = null,// 결제금액 - 배달비
                    var riderFee: Float? = null, // 배달비
                    var savedPoint: Float? = null,
                    var visitTime: String? = null,
                    var riderPaymentType: Int? = null, // 1:무료, 2:유료, 3:조건부무료
                    var riderTime: Int? = null,
                    var riderCompany: String? = null,
                    var riderCompanyCode: String? = null,
                    var riderStartTime: String? = null,
                    var riderCompleteTime: String? = null,
                    var finteckResult: String? = null,
                    var returnPaymentPrice: Float? = null,
                    var paymentFee: Float? = null,
                    var platformFee: Float? = null,
                    var payDatetime: String? = null,
                    var cancelDatetime: String? = null,
                    var cancelMemo: String? = null,
                    var regDatetime: String? = null,
                    var changeStatusDatetime: String? = null,
                    var recommendedMemberSeqNo: Long? = null,
                    var recommendedMemberType: Long? = null,
                    var pg: String? = null,
                    var pgTranId: String? = null,
                    var apprNo: String? = null,
                    var apprTranNo: String? = null,
                    var receiptId: String? = null,
                    var memo: String? = null,
                    var disposableRequired: Boolean? = null,
                    var phone: String? = null,
                    var address: String? = null,
                    var addressDetail: String? = null,
                    var isReviewExist: Boolean? = null,
                    var visitNumber: Int? = null,
                    var isVisitNow: Boolean? = null,
                    var addRiderFee: Float? = null,
                    var receiptDatetime: String? = null,
                    var expireDatetime: String? = null,
                    var usedDatetime: String? = null,
                    var orderPurchaseMenuList: List<OrderPurchaseMenu>? = null,
                    var page:Page2? = null) : Parcelable {
}