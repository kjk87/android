package com.lejel.wowbox.core.network.model.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by imac on 2018. 1. 2..
 */
@Parcelize
class LuckyBoxReply(var seqNo: Long? = null,
                    var userKey: String? = null,
                    var luckyboxPurchaseItemSeqNo: Long? = null,
                    var luckyboxReviewSeqNo: Long? = null,
                    var reply: String? = null,
                    var regDatetime: String? = null,
                    var modDatetime: String? = null,
                    var memberTotal: Member? = null) : Parcelable {}