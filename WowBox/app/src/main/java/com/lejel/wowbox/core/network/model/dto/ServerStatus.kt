package com.lejel.wowbox.core.network.model.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by imac on 2018. 1. 2..
 */
@Parcelize
class ServerStatus(var code: Int? = null,
                   var message: String? = null,
                   var serverUrl: String? = null,
                   var duration: String? = null) : Parcelable {
}