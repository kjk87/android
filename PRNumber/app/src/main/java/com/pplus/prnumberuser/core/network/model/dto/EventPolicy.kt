package com.pplus.prnumberuser.core.network.model.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by imac on 2018. 1. 2..
 */
@Parcelize
class EventPolicy(var seqNo: Long? = null,
                  var eventSeqNo: Long? = null,
                  var pageSeqNo: Long? = null,
                  var title: String? = null,
                  var url: String? = null) : Parcelable {
}