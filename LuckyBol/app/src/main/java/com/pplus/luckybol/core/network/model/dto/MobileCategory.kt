package com.pplus.luckybol.core.network.model.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by imac on 2018. 1. 2..
 */
@Parcelize
class MobileCategory(var seqNo: Long? = null,
                     var name: String? = null,
                     var image: String? = null,
                     var status: String? = null,
                     var array: Int? = null,
                     var regDatetime: String? = null) : Parcelable {}