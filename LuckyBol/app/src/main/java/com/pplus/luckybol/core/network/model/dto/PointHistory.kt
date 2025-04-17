package com.pplus.luckybol.core.network.model.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by imac on 2018. 1. 2..
 */
@Parcelize
class PointHistory(var seqNo: Long? = null,
                   var memberSeqNo: Long? = null,
                   var type: String? = null,//charge, used
                   var point: Float? = null,
                   var subject: String? = null,
                   var historyProp: HashMap<String, String>? = null,
                   var regDatetime: String? = null) : Parcelable {
}