package com.root37.buflexz.core.network.model.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by 김종경 on 2016-10-04.
 */
@Parcelize
class LuckyDrawWin(var seqNo: Long? = null,
                   var userKey: String? = null,
                   var luckyDrawSeqNo: Long? = null,
                   var luckyDrawPurchaseSeqNo: Long? = null,
                   var luckyDrawGiftSeqNo: Long? = null,
                   var status: String? = null,
                   var impression: String? = null,
                   var giftGrade: Int? = null,
                   var giftName: String? = null,
                   var giftPrice: Int? = null,
                   var giftImage: String? = null,
                   var winType: Int? = null,
                   var winNumber: String? = null,
                   var uniqueNumber: String? = null,
                   var regDatetime: String? = null,
                   var replyCount: Int? = null,
                   var memberTotal:Member? = null,
                   var luckyDrawGift: LuckyDrawGift? = null,
                   var luckyDraw:LuckyDraw? = null) : Parcelable {

}