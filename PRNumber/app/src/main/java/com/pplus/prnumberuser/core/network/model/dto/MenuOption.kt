package com.pplus.prnumberuser.core.network.model.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by imac on 2018. 1. 2..
 */
@Parcelize
class MenuOption(var seqNo: Long? = null,
                 var pageSeqNo: Long? = null,
                 var type: Int? = null,
                 var title: String? = null,
                 var array: Int? = null,
                 var isSoldOut: Boolean? = null,
                 var regDatetime: String? = null,
                 var detailList: List<MenuOptionDetail>? = null) : Parcelable {
}