package com.pplus.luckybol.core.network.model.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by imac on 2017. 8. 29..
 */

@Parcelize
class CountryConfigProperties(var baseUrl: String? = null,
                              var profileReward: Int? = null,
                              var maxAdRewardCount: Int? = null,
                              var maxAttendanceDay: Int? = null,
                              var maxAttendanceBol: Int? = null,
                              var lmsPrice: Int? = null,
                              var smsPrice: Int? = null,
                              var pushPrice: Int? = null,
                              var adLmsPrice: Int? = null,
                              var adSmsPrice: Int? = null,
                              var recommendBol: Int? = null,
                              var recommendeeBol: Int? = null,
                              var activateRecommendBol: Int? = null,
                              var bolRatio: Int? = null,
                              var taxRatio: Int? = null,
                              var inquiryBoard: Long? = null,
                              var suggestBoard: Long? = null,
                              var coopBoard: Long? = null,
                              var offerBoard: Long? = null,
                              var sysTypeBoard: Long? = null,
                              var sysTypeLastUpdate: String? = null,
                              var recommendKeyword: String? = null,
                              var inviteImageUrl: String? = null,
                              var weeklyRanking1: Int? = null,
                              var weeklyRanking2: Int? = null,
                              var weeklyRanking3: Int? = null,
                              var monthlyRanking1: Int? = null,
                              var monthlyRanking2: Int? = null,
                              var monthlyRanking3: Int? = null,
                              var joinBol: Int? = null,
                              var lottoOpen: Boolean? = null) : Parcelable {
}
