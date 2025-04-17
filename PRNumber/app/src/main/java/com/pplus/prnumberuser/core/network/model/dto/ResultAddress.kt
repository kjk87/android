package com.pplus.prnumberuser.core.network.model.dto

import android.os.Parcel
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by imac on 2017. 6. 29..
 */
@Parcelize
class ResultAddress(var results: SearchAddress? = null,
                    var oldAddress: String? = null) : Parcelable {
}