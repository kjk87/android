package com.pplus.prnumberuser.core.network.model.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by imac on 2018. 1. 2..
 */
@Parcelize
class ThemeCategory(var seqNo: Long? = null,
                    var name: String? = null,
                    var status: String? = null,
                    var array: Int? = null,
                    var icon: String? = null,
                    var banner: String? = null,
                    var regDatetime: String? = null) : Parcelable {}