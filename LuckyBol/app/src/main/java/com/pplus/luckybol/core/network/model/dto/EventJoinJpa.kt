package com.pplus.luckybol.core.network.model.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by imac on 2018. 1. 2..
 */
@Parcelize
class EventJoinJpa(var id: Long? = null,
                   var eventSeqNo: Long? = null,
                   var seqNo: Int? = null,
                   var memberSeqNo: Long? = null,
                   var joinDatetime: String? = null,
                   var joinProp: String? = null,
                   var winCode: String? = null,
                   var isBuy: Boolean? = null,
                   var eventBuySeqNo: Long? = null,
                   var lottoSelectedNumberList: List<LottoSelectedNumber>? = null) : Parcelable {}