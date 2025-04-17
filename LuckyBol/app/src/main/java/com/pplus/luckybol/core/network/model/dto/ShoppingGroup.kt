package com.pplus.luckybol.core.network.model.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by imac on 2018. 1. 2..
 */
@Parcelize
class ShoppingGroup(var seqNo: Long? = null,
                    var title: String? = null,
                    var status: String? = null,
                    var note: String? = null,
                    var regDatetime: String? = null) : Parcelable {

}