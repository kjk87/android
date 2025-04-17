package com.root37.buflexz.core.network.model.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by 김종경 on 2016-10-04.
 */
@Parcelize
class HistoryCommission(var seqNo: Long? = null,
                        var type: String? = null,//ad, ball, bonus
                        var category: String? = null,
                        var userKey: String? = null,
                        var nickname: String? = null,
                        var partner: String? = null,
                        var partnerType: String? = null,//partner1, partner2
                        var commission: Float? = null,
                        var regDatetime: String? = null) : Parcelable {

}