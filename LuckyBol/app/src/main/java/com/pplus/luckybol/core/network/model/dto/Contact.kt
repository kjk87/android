package com.pplus.luckybol.core.network.model.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by imac on 2018. 1. 2..
 */
@Parcelize
class Contact(var memberSeqNo: Long? = null,
              var mobileNumber: String? = null,
              var regDatetime: String? = null,
              var modDatetime: Int? = null,
              var isMember: Boolean? = null,
              var member: Member? = null) : Parcelable {}