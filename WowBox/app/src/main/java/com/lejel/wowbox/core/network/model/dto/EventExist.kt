package com.lejel.wowbox.core.network.model.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by imac on 2018. 1. 2..
 */
@Parcelize
class EventExist(var join: Boolean? = null,
                 var win: Boolean? = null) : Parcelable {}