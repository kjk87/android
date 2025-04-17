package com.pplus.prnumberuser.core.network.model.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by imac on 2018. 1. 2..
 */
@Parcelize
class MemberAttendance(var seqNo: Long? = null,
                       var memberSeqNo: Long? = null,
                       var attendanceCount: Int? = null,
                       var attendanceDatetime: String? = null,
                       var isAttendance: Boolean? = null) : Parcelable {}