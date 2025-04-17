package com.lejel.wowbox.core.network.model.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by 김종경 on 2016-10-04.
 */
@Parcelize
class PointMallCategory(var seqNo: Long? = null,
                        var status: String? = null,
                        var title: String? = null,
                        var image: String? = null,
                        var url: String? = null,
                        var array: Int? = null,
                        var regDatetime: String? = null) : Parcelable {

}