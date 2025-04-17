package com.lejel.wowbox.core.network.model.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by imac on 2018. 1. 2..
 */
@Parcelize
class Device(var seqNo: Long? = null,
             var deviceId: String? = null,
             var pushId: String? = null,
             var pushActivate: Boolean? = null,
             var regDatetime: String? = null) : Parcelable {}