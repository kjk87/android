package com.pplus.luckybol.core.network.model.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by 김종경 on 2016-10-04.
 */
@Parcelize
class IslandsRegion(var id: Long? = null,
                    var postcode: String? = null,
                    var address: String? = null,
                    var isJeju: Boolean? = null) : Parcelable {

}