package com.pplus.prnumberuser.core.network.model.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by imac on 2018. 1. 2..
 */
@Parcelize
class VirtualNumberManageGroupItem(var seqNo: Long? = null,
                                   var manageSeqNo: Long? = null,
                                   var type: String? = null,
                                   var pageSeqNo: Long? = null,
                                   var productPriceSeqNo: Long? = null,
                                   var page:Page2? = null) : Parcelable {
}