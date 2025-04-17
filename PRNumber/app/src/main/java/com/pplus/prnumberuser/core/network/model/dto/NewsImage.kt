package com.pplus.prnumberuser.core.network.model.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by imac on 2018. 1. 2..
 */
@Parcelize
class NewsImage(
        var id: Long? = null,
        var newsSeqNo: Long? = null,
        var image: String? = null,
        var array: Int? = null,
        var deligate: Boolean? = null) : Parcelable {

}