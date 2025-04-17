package com.pplus.prnumberuser.core.network.model.dto

import android.os.Parcel
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by j2n on 2016. 7. 26..
 */
@Parcelize
class Terms(var no: Long? = null,
            var code: String? = null,
            var status: String? = null,
            var subject: String? = null,
            var isCompulsory: Boolean = false,
            var name: String? = null,
            var contents: String? = null,
            var regDate: String? = null,
            var modDate: String? = null,
            var url: String? = null) : Parcelable {

}