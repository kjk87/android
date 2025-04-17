package com.lejel.wowbox.core.network.model.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by 김종경 on 2016-10-04.
 */
@Parcelize
class HistoryCash(var seqNo: Long? = null,
                  var userKey: String? = null,
                  var category: String? = null,
                  var type: String? = null,
                  var cash: Float? = null,
                  var subject: String? = null,
                  var comment: String? = null,
                  var regDatetime: String? = null) : Parcelable {

}