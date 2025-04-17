package com.pplus.prnumberuser.core.network.model.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by imac on 2018. 1. 2..
 */
@Parcelize
class PrepaymentLog(var seqNo: Long? = null,
                    var prepaymentSeqNo: Long? = null,
                    var prepaymentPublishSeqNo: Long? = null,
                    var pageSeqNo: Long? = null,
                    var memberSeqNo: Long? = null,
                    var regDatetime: String? = null,
                    var usePrice: Float? = null,
                    var status: String? = null,
                    var statusDatetime: String? = null) : Parcelable {}