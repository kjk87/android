package com.pplus.luckybol.core.network.model.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by imac on 2018. 1. 2..
 */
@Parcelize
class PopupMange(var seqNo: Long? = null,
                 var android: Boolean? = null,
                 var ios: Boolean? = null,
                 var title: String? = null,
                 var image: String? = null,
                 var display: Boolean? = null,
                 var startDatetime: String? = null,
                 var endDatetime: String? = null,
                 var moveType: String? = null,
                 var innerType: String? = null,
                 var moveTarget: String? = null,
                 var regDatetime: String? = null) : Parcelable {}