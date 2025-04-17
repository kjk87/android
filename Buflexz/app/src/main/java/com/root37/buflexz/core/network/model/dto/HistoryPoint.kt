package com.root37.buflexz.core.network.model.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by 김종경 on 2016-10-04.
 */
@Parcelize
class HistoryPoint(var seqNo: Long? = null,
                   var userKey: String? = null,
                   var category: String? = null,
                   var type: String? = null,
                   var point: Float? = null,
                   var subject: String? = null,
                   var comment: String? = null,
                   var regDatetime: String? = null) : Parcelable {

}