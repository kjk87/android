package com.pplus.prnumberuser.core.network.model.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by imac on 2018. 1. 2..
 */
@Parcelize
class OrderMenuImage(var seqNo: Long? = null,
                     var menuSeqNo: Long? = null,
                     var image: String? = null) : Parcelable {
}