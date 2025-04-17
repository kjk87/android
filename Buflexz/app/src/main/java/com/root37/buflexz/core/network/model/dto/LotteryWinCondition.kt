package com.root37.buflexz.core.network.model.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by 김종경 on 2016-10-04.
 */
@Parcelize
class LotteryWinCondition(var seqNo: Long? = null,
                          var lotteryRound: Int? = null,
                          var firstGrade: Int? = null,
                          var secondGrade: Int? = null,
                          var thirdGrade: Int? = null,
                          var forthGrade: Int? = null,
                          var fifthGrade: Int? = null,
                          var firstMoney: Int? = null,
                          var secondMoney: Int? = null,
                          var thirdMoney: Int? = null,
                          var forthMoney: Int? = null,
                          var fifthMoney: Int? = null,
                          var winnerCount: Int? = null,
                          var winnerUsdtAmount: Int? = null) : Parcelable {

}