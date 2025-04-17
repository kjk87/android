package com.pplus.prnumberuser.core.network.model.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by imac on 2017. 8. 23..
 */
@Parcelize
class FaqGroup(var no: Long? = null,
               var name: String? = null,
               var priority: Int? = null,
               var regDate: String? = null) : Parcelable {

}