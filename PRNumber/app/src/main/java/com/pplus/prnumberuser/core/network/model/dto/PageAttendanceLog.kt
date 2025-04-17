package com.pplus.prnumberuser.core.network.model.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by imac on 2018. 1. 2..
 */
@Parcelize
class PageAttendanceLog(var seqNo: Long? = null,
                        var pageAttendanceSeqNo: Long? = null,
                        var memberSeqNo: Long? = null,
                        var regDatetime: String? = null) : Parcelable {}