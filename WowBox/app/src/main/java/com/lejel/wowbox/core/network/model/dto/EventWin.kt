package com.lejel.wowbox.core.network.model.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by imac on 2018. 1. 2..
 */
@Parcelize
class EventWin(var seqNo: Long? = null,
               var eventSeqNo: Long? = null,
               var userKey: String? = null,
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
               var giftPrice: Int? = null,
               var eventJoinSeqNo: Long? = null,
               var replyCount: Int? = null,
               var event: Event? = null,
               var eventGift: EventGift? = null,
               var memberTotal: Member? = null) : Parcelable {}