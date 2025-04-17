package com.pplus.luckybol.core.network.model.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by imac on 2018. 1. 2..
 */
@Parcelize
class EventReply(var seqNo: Long? = null,
                 var memberSeqNo: Long? = null,
                 var eventSeqNo: Long? = null,
                 var eventWinSeqNo: Long? = null,
                 var eventWinId: Long? = null,
                 var eventReviewSeqNo: Long? = null,
                 var reply: String? = null,
                 var regDatetime: String? = null,
                 var modDatetime: String? = null,
                 var member: Member? = null) : Parcelable {}