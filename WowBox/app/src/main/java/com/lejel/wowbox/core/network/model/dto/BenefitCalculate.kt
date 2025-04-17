package com.lejel.wowbox.core.network.model.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by j2n on 2016. 7. 26..
 */
@Parcelize
class BenefitCalculate(var seqNo: Long? = null,
                       var userKey: String? = null,
                       var calculateMonth: String? = null,
                       var status: String? = null,//pending: 정산대기 transfer: 이월 complete: 정산완료
                       var transferedBenefit: Double? = null,
                       var benefit: Double? = null,
                       var totalProceeds: Double? = null,
                       var regDatetime: String? = null,
                       var changeDatetime: String? = null,
                       var comment: String? = null) : Parcelable {

}