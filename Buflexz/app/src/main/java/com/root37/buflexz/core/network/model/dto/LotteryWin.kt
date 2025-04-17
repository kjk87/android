package com.root37.buflexz.core.network.model.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by 김종경 on 2016-10-04.
 */
@Parcelize
class LotteryWin(var seqNo: Long? = null,
                 var lotteryRound: Int? = null,
                 var lotteryJoinSeqNo: String? = null,
                 var userKey: String? = null,
                 var grade: Int? = null,
                 var giftType: String? = null, // point, lotto
                 var money: Int? = null,
                 var status: String? = null, // active, request, reRequest, return, complete
                 var statusDatetime: String? = null,
                 var no1: Int? = null,
                 var no2: Int? = null,
                 var no3: Int? = null,
                 var no4: Int? = null,
                 var no5: Int? = null,
                 var no6: Int? = null,
                 var winNo1: Boolean? = null,
                 var winNo2: Boolean? = null,
                 var winNo3: Boolean? = null,
                 var winNo4: Boolean? = null,
                 var winNo5: Boolean? = null,
                 var winNo6: Boolean? = null,
                 var winAdd: Boolean? = null,
                 var joinDatetime: String? = null,
                 var member:Member? = null) : Parcelable {

}