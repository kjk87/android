package com.lejel.wowbox.core.network.model.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by j2n on 2016. 7. 26..
 */
@Parcelize
class Withdraw(var seqNo: Long? = null,
               var userKey: String? = null,
               var nickname: String? = null,
               var status: String? = null,//pending, return, complete
               var bank: String? = null,
               var name: String? = null,
               var account: String? = null,
               var request: Int? = null,
               var withdraw: Double? = null,
               var fee: Float? = null,
               var regDatetime: String? = null,
               var changeDatetime: String? = null,
               var comment: String? = null) : Parcelable {

}