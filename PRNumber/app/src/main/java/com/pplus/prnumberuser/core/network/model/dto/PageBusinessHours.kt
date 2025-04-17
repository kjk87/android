package com.pplus.prnumberuser.core.network.model.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by imac on 2018. 1. 2..
 */
@Parcelize
class PageBusinessHours(var seqNo: Long? = null,
                        var pageSeqNo: Long? = null,
                        var day: Int? = null,//1:일요일
                        var postcode: String? = null,
                        var openTime: String? = null,
                        var closeTime: String? = null) : Parcelable {}