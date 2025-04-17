package com.lejel.wowbox.core.network.model.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by imac on 2018. 1. 2..
 */
@Parcelize
class EventJoin(var seqNo: Long? = null,
                var eventSeqNo: Long? = null,
                var userKey: String? = null,
                var joinDatetime: String? = null,
                var joinProp: HashMap<String, String>? = null,
                var winCode: String? = null,
                var isBuy: Boolean? = null,
                var eventBuySeqNo: Long? = null) : Parcelable {}