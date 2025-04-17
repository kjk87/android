package com.pplus.luckybol.core.network.model.dto

import android.os.Parcel
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by imac on 2018. 1. 2..
 */
@Parcelize
class EventResultJpa(var win: EventWinJpa? = null,
                     var join: EventJoinJpa? = null) : Parcelable {}