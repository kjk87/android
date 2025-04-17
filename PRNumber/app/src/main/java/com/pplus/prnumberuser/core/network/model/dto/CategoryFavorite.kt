package com.pplus.prnumberuser.core.network.model.dto


import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by 김종경 on 2016-10-04.
 */

@Parcelize
class CategoryFavorite(var id: Long? = null,
                       var memberSeqNo: Long? = null,
                       var categoryMinorSeqNo: Long? = null,
                       var categoryMajorSeqNo: Long? = null,
                       var categoryMinor: CategoryMinor? = null) : Parcelable {
    override fun equals(other: Any?): Boolean {
        if (other == null) return false

        return if (other is CategoryFavorite) {
            other.id == id
        } else {
            false
        }
    }

}
