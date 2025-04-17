package com.root37.buflexz.core.network.model.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by j2n on 2016. 7. 26..
 */
@Parcelize
class GiftCardPurchase(var seqNo: Long? = null,
                       var userKey: String? = null,
                       var buyerNickname: String? = null,
                       var giftCardSeqNo: Long? = null,
                       var giftCardImage: String? = null,
                       var giftCardName: String? = null,
                       var status: Int? = null,//0: 결제요청, 1:결제승인, 2:취소요청, 3:취소완료
                       var deliveryStatus: Int? = null,//0:주문확인전, 1:발송대기, 2:발송반려, 3:발송완료
                       var usePoint: Float? = null,
                       var exchangeRate: Float? = null,
                       var price: Float? = null,
                       var amount: Int? = null,
                       var unitPrice: Float? = null,
                       var regDatetime: String? = null,
                       var statusDatetime: String? = null,
                       var comment: String? = null,
                       var buyerEmail:String? = null) : Parcelable {

}