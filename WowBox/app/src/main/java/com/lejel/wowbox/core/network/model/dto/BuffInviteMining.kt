package com.lejel.wowbox.core.network.model.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by imac on 2018. 1. 2..
 */
@Parcelize
class BuffInviteMining(var seqNo: Long? = null,
                       var userKey: String? = null,
                       var coin: Double? = null,
                       var regDatetime: String? = null) : Parcelable {}