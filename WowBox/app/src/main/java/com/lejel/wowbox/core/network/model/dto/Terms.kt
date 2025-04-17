package com.lejel.wowbox.core.network.model.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by 김종경 on 2016-10-04.
 */
@Parcelize
class Terms(var seqNo: Long? = null,
            var code: String? = null,
            var nation: String? = null,
            var compulsory: Boolean? = null,
            var status: String? = null,
            var title: String? = null,
//            var contents: String? = null,
            var array: Int? = null,
            var url: String? = null,
            var regDatetime: String? = null) : Parcelable {

}