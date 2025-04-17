package com.root37.buflexz.core.network.model.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by imac on 2018. 1. 2..
 */
@Parcelize
class LuckyDrawWinReply(var seqNo: Long? = null,
                        var userKey: String? = null,
                        var luckyDrawWinSeqNo: Long? = null,
                        var reply: String? = null,
                        var regDatetime: String? = null,
                        var modDatetime: String? = null,
                        var memberTotal: Member? = null) : Parcelable {}