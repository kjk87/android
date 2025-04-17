package com.pplus.prnumberuser.core.network.model.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by imac on 2018. 1. 2..
 */
@Parcelize
class VirtualNumber(var virtualNumber: String? = null,
                    var type: String? = null,
                    var reserved: String? = null,
                    var actionSource: String? = null,
                    var actorLoginId: String? = null,
                    var deleted: String? = null) : Parcelable {
}