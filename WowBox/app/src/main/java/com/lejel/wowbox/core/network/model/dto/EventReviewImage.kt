package com.lejel.wowbox.core.network.model.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by imac on 2018. 1. 2..
 */
@Parcelize
class EventReviewImage(var seqNo: Long? = null,
                       var eventReviewSeqNo: Long? = null,
                       var image: String? = null,
                       var array: Int? = null,
                       var type: String? = null) : Parcelable {

}