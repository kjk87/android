package com.pplus.prnumberuser.core.network.model.dto


import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by 김종경 on 2016-10-04.
 */

@Parcelize
class CategoryMinor(var seqNo: Long? = null,
                    var major: Long? = null,
                    var name: String? = null,
                    var status: String? = null,
                    var image: String? = null,
                    var array: Int? = null,
                    var regDatetime: String? = null,
                    var modDatetime: String? = null,
                    var register: String? = null,
                    var updater: String? = null) : Parcelable {

    override fun equals(other: Any?): Boolean {
        if (other == null) return false

        return if (other is CategoryMinor) {
            other.seqNo == seqNo
        } else {
            false
        }
    }
}
