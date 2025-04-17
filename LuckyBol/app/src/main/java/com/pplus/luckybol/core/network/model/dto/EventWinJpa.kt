package com.pplus.luckybol.core.network.model.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by imac on 2018. 1. 2..
 */
@Parcelize
class EventWinJpa(var id: Long? = null,
                  var eventSeqNo: Long? = null,
                  var seqNo: Long? = null,
                  var memberSeqNo: Long? = null,
                  var giftSeqNo: Long? = null,
                  var winDatetime: String? = null,
                  var impression: String? = null,
                  var blind: Boolean? = null,
                  var amount: Float? = null,
                  var giftStatus: Int? = null,
                  var useDatetime: String? = null,
                  var isLotto: Boolean? = null,
                  var giftType: String? = null,
                  var giftTitle: String? = null,
                  var eventJoinSeqNo: Long? = null,
                  var eventGift: EventGiftJpa? = null,
                  var eventJoin: EventJoinJpa? = null,
                  var member: Member? = null) : Parcelable {}