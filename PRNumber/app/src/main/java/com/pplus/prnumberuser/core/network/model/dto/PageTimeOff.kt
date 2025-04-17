package com.pplus.prnumberuser.core.network.model.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by imac on 2018. 1. 2..
 */
@Parcelize
class PageTimeOff(var seqNo: Long? = null,
                  var pageSeqNo: Long? = null,
                  var start: String? = null,//1:일요일
                  var end: String? = null) : Parcelable {}