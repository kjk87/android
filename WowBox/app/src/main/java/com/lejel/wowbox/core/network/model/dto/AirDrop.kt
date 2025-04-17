package com.lejel.wowbox.core.network.model.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by j2n on 2016. 7. 26..
 */
@Parcelize
class AirDrop(var seqNo: Long? = null,
              var userKey: String? = null,
              var status: String? = null,//pending: 신청 return: 반려 normal: 승인 redemand: 재신청
              var wallet: String? = null,
              var amount: Float? = null,
              var comment: String? = null,
              var requestDatetime: String? = null,
              var changeDatetime: String? = null,
              var regDatetime: String? = null) : Parcelable {

}