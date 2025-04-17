package com.pplus.luckybol.core.network.model.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by imac on 2018. 1. 2..
 */
@Parcelize
class EventJoin(var joinNo: Long? = null,
                var user: User? = null,
                var event: Event? = null,
                var joinDate: String? = null) : Parcelable {
}