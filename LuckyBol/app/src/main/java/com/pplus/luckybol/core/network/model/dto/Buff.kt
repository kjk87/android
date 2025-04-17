package com.pplus.luckybol.core.network.model.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by imac on 2018. 1. 2..
 */
@Parcelize
class Buff(var seqNo: Long? = null,
           var title: String? = null,
           var info: String? = null,
           var capacity: Int? = null,
           var image: String? = null,
           var owner: Long? = null,
           var launchers: Long? = null,
           var totalDividedBol: Double? = null,
           var totalDividedPoint: Double? = null,
           var deleted: Boolean? = null,
           var regDatetime: String? = null) : Parcelable {

}