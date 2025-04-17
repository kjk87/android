package com.lejel.wowbox.core.network.model.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by 김종경 on 2016-10-04.
 */
@Parcelize
class LuckyDrawNumber(var seqNo: Long? = null,
                      var first: String? = null,
                      var second: String? = null,
                      var third: String? = null,
                      var winNumber: String? = null,
                      var array: Int? = null,
                      var used: Boolean? = null) : Parcelable {

}