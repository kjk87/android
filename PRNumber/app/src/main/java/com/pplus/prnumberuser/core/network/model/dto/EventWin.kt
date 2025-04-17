package com.pplus.prnumberuser.core.network.model.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by imac on 2018. 1. 2..
 */
@Parcelize
class EventWin(var id: Long? = null,
               var winNo: Long? = null,
               var user: User? = null,
               var gift: EventGift? = null,
               var event: Event? = null,
               var impression: String? = null,
               var winDate: String? = null,
               var amount: String? = null,
               var giftStatus: Int? = null,
               var useDatetime: String? = null,
               var replyCount: Int? = null,
               var openStatus: Boolean? = null,
               var deliveryAddress: String? = null,
               var recipient: String? = null,
               var deliveryPhone: String? = null,
               var deliveryPostCode: String? = null) : Parcelable {}