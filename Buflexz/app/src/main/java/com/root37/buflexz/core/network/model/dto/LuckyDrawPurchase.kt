package com.root37.buflexz.core.network.model.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by 김종경 on 2016-10-04.
 */
@Parcelize
class LuckyDrawPurchase(var seqNo: Long? = null,
                        var luckyDrawSeqNo: Long? = null,
                        var userKey: String? = null,
                        var title: String? = null,
                        var winNumber: String? = null,
                        var regDatetime: String? = null,
                        var modDatetime: String? = null,
                        var expireDatetime: String? = null,
                        var engagedPrice: Int? = null,
                        var engagedCount: Int? = null,
                        var status: String? = null,
                        var engageType: String? = null,
                        var buyerNation: String? = null,
                        var buyerName: String? = null,
                        var buyerTel: String? = null,
                        var selectNumberList: List<LuckyDrawNumber>? = null) : Parcelable {

}