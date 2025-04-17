package com.pplus.prnumberuser.core.network.model.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by imac on 2018. 1. 2..
 */
@Parcelize
class ProductNotice(var seqNo: Long? = null,
                    var noticeGroup: String? = null,
                    var productSeqNo: Long? = null,
                    var title: String? = null,
                    var note: String? = null) : Parcelable {
}