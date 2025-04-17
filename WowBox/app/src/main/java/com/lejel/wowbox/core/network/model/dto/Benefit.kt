package com.lejel.wowbox.core.network.model.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by j2n on 2016. 7. 26..
 */
@Parcelize
class Benefit(var seqNo: Long? = null,
              var userKey: String? = null,
              var status: String? = null,//active: 정산대상 유저 inactive: 정산정지 유저
              var buff: Double? = null,
              var wallet: String? = null,
              var name: String? = null,
              var totalBenefit: Double? = null,
              var regDatetime: String? = null,
              var calcDatetime: String? = null,
              var comment: String? = null) : Parcelable {

}