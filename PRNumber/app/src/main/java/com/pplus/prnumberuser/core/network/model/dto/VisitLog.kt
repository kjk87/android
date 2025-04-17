package com.pplus.prnumberuser.core.network.model.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by imac on 2018. 1. 2..
 */
@Parcelize
class VisitLog(var seqNo: Long? = null,
               var pageSeqNo: Long? = null,
               var memberSeqNo: Long? = null,
               var status: String? = null, // request, completed, reject
               var note: String? = null,
               var regDatetime: String? = null,
               var type: String? = null,
               var statusDatetime: String? = null) : Parcelable {}