package com.pplus.prnumberuser.core.network.model.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by imac on 2018. 1. 2..
 */
@Parcelize
class OrderMenuGroup(var seqNo: Long? = null,
                     var pageSeqNo: Long? = null,
                     var name: String? = null,
                     var array: Int? = null,
                     var orderMenuList: List<OrderMenu>? = null) : Parcelable {
}