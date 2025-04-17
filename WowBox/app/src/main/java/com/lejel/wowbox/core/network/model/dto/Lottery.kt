package com.lejel.wowbox.core.network.model.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by 김종경 on 2016-10-04.
 */
@Parcelize
class Lottery(var lotteryRound: Int? = null,
              var eventStartDatetime: String? = null,
              var eventEndDatetime: String? = null,
              var status: String? = null,// active, before, expire, complete, inactive
              var announceDatetime: String? = null,
              var no1: Int? = null,
              var no2: Int? = null,
              var no3: Int? = null,
              var no4: Int? = null,
              var no5: Int? = null,
              var no6: Int? = null,
              var bonusNo: Int? = null,
              var firstType: String? = null,// point, lotto
              var firstMoney: Int? = null,
              var secondType: String? = null,// point, lotto
              var secondMoney: Int? = null,
              var thirdType: String? = null,// point, lotto
              var thirdMoney: Int? = null,
              var forthType: String? = null,// point, lotto
              var forthMoney: Int? = null,
              var fifthType: String? = null,// point, lotto
              var fifthMoney: Int? = null,
              var regDate: String? = null,
              var modDatetime: String? = null,
              var totalJoinCount: Int? = null,
              var announceUrl: String? = null,
              var flagFirst: Boolean? = null,
              var flagSecond: Boolean? = null,
              var flagThird: Boolean? = null,
              var flagForth: Boolean? = null,
              var flagFifth: Boolean? = null,
              var flagDeleteUser: Boolean? = null,
              var flagDeleteJoin: Boolean? = null,
              var bannerImage: String? = null) : Parcelable {

}