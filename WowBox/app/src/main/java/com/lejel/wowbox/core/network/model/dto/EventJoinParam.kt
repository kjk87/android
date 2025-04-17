package com.lejel.wowbox.core.network.model.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by imac on 2018. 1. 2..
 */
@Parcelize
class EventJoinParam(var event: Event? = null,
                     var join: EventJoin? = null) : Parcelable {}