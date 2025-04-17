package com.pplus.prnumberuser.core.network.model.dto

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Created by imac on 2018. 1. 2..
 */
@Parcelize
class CartOption(var seqNo: Long? = null,
                 var cartSeqNo: Long? = null,
                 var menuOptionDetailSeqNo: Long? = null,
                 var type: Int? = null,// 필수여부
                 var menuOptionDetail: MenuOptionDetail? = null) : Parcelable {
}