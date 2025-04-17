package com.pplus.luckybol.core.network.model.dto

import android.os.Parcel
import android.os.Parcelable
import com.pplus.luckybol.core.network.model.dto.Cart
import kotlinx.parcelize.Parcelize

/**
 * Created by imac on 2018. 1. 2..
 */
@Parcelize
class Buy(var seqNo: Long? = null,
          var memberSeqNo: Long? = null,
          var pageSeqNo: Long? = null,
          var orderId: String? = null,
          var process: Int? = null,//0 :  대기, 1:승인 2:승인후 취소, 3:사용, 4:사용후환불, -1: 우리서버 에러, -2: PG 에러
          var payMethod: String? = null,
          var pg: String? = null,
          var pgTranId: String? = null,
          var pgAcceptId: String? = null,
          var carts: GoodsSeqNoList? = null,
          var title: String? = null,
          var buyerEmail: String? = null,
          var buyerName: String? = null,
          var buyerTel: String? = null,
          var buyerAddress: String? = null,
          var buyerPostcode: String? = null,
          var price: Long? = null,
          var vat: Long? = null,
          var regDatetime: String? = null,
          var modDatetime: String? = null,
          var buyGoods: Cart? = null,
          var buyGoodsList: List<BuyGoods>? = null,
          var buyGoodsSelectList: List<BuyGoods>? = null,
          var orderType: Int? = null,
          var orderProcess: Int? = null,
          var bookDatetime: String? = null,
          var clientAddress: String? = null,
          var memo: String? = null,
          var deliveryFee: Int? = null,
          var type: String? = null,
          var confirmDatetime: String? = null,
          var cancelDatetime: String? = null,
          var completeDatetime: String? = null,
          var page: Page2? = null,
          var isReviewExist: Boolean? = null,
          var installment: String? = null,
          var buycol: String? = null,
          var pointRatio: Float? = null,
          var commissionRatio: Float? = null,
          var savedPoint: Int? = null,
          var agentSeqNo: Long? = null,
          var isPaymentPoint: Boolean? = null,
          var payType: String? = null,
          var payDatetime: String? = null) : Parcelable {
}