package com.pplus.luckybol.core.network.model.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by imac on 2018. 1. 2..
 */
@Parcelize
class BuffParam(var buffSeqNo: Long? = null,
                var inviteList: List<Long>? = null,
                var exitList: List<Long>? = null,
                var reason: String? = null) : Parcelable {}