package com.pplus.prnumberuser.core.network.model.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by imac on 2018. 1. 2..
 */
@Parcelize
class MenuOptionDetail(var seqNo: Long? = null,
                       var menuOptionSeqNo: Long? = null,
                       var title: String? = null,
                       var price: Float? = null,
                       var isSelect:Boolean? = null) : Parcelable {
}