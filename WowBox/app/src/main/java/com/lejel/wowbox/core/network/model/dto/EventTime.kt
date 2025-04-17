package com.lejel.wowbox.core.network.model.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by imac on 2018. 1. 2..
 */
@Parcelize
class EventTime(var seqNo: Long? = null,
                var eventSeqNo: Long? = null,
                var startTime: String? = null,
                var endTime: String? = null) : Parcelable {}