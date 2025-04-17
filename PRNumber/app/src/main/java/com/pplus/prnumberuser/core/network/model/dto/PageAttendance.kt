package com.pplus.prnumberuser.core.network.model.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by imac on 2018. 1. 2..
 */
@Parcelize
class PageAttendance(var seqNo: Long? = null,
                     var pageSeqNo: Long? = null,
                     var memberSeqNo: Long? = null,
                     var attendanceCount: Int? = null,
                     var totalCount: Int? = null,
                     var status: Int? = null,
                     var regDatetime: String? = null,
                     var page:Page2? = null) : Parcelable {}