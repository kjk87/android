package com.lejel.wowbox.core.network.model.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by imac on 2018. 1. 2..
 */
@Parcelize
class CommunityApply(var seqNo: Long? = null,
                     var userKey: String? = null,
                     var image: String? = null,
                     var status: String? = null,
                     var reason: String? = null,
                     var regDatetime: String? = null,
                     var statusDatetime: String? = null) : Parcelable {}