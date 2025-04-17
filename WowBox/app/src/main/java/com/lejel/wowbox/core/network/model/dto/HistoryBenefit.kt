package com.lejel.wowbox.core.network.model.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by j2n on 2016. 7. 26..
 */
@Parcelize
class HistoryBenefit(var seqNo: Long? = null,
                     var userKey: String? = null,
                     var nickname: String? = null,
                     var proceeds: Double? = null,
                     var totalBenefit: Double? = null,
                     var benefit: Double? = null,
                     var mBuff: Double? = null,
                     var cBuff: Double? = null,
                     var percent: Float? = null,
                     var regDatetime: String? = null) : Parcelable {

}