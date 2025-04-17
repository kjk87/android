package com.pplus.prnumberuser.core.network.model.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.*

/**
 * Created by imac on 2018. 1. 2..
 */
@Parcelize
class EventJoin(var joinNo: Long? = null,
                var user: User? = null,
                var event: Event? = null,
                var joinDate: Date? = null) : Parcelable {
}