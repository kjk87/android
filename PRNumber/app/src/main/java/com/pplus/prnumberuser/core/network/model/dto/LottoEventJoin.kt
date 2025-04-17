package com.pplus.prnumberuser.core.network.model.dto

import android.os.Parcel
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by imac on 2018. 2. 9..
 */
@Parcelize
class LottoEventJoin(var joinNo: Long? = null,
                     var joinDate: String? = null,
                     var joinProp: String? = null,
                     var winCode: String? = null) : Parcelable {
}