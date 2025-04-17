package com.pplus.prnumberuser.core.network.model.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by imac on 2018. 1. 2..
 */
@Parcelize
class Prepayment(var seqNo: Long? = null,
                 var pageSeqNo: Long? = null,
                 var price: Float? = null,
                 var addPrice: Float? = null,
                 var notice: String? = null,
                 var regDatetime: String? = null,
                 var modiDatetime: String? = null,
                 var status: String? = null,
                 var discount: Float? = null,
                 var statusDatetime: String? = null,
                 var wholesaleSeqNo: Long? = null,
                 var distributorSeqNo: Long? = null,
                 var page:Page2? = null) : Parcelable {}