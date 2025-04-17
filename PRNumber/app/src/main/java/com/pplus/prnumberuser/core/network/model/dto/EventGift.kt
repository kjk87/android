package com.pplus.prnumberuser.core.network.model.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by imac on 2018. 1. 2..
 */
@Parcelize
class EventGift(var giftNo: Long? = null,
                var type: String? = null,
                var title: String? = null,
                var totalCount: Int? = null,
                var remainCount: Int? = null,
                var alert: String? = null,
                var lotPercent: Int? = null,
                var price: Int? = null,
                var event: Event? = null,
                var expireDate: String? = null,
                var timeType: String? = null,
                var dayType: String? = null,
                var startTime: String? = null,
                var endTime: String? = null,
                var days: String? = null,
                var reviewPoint: Int? = null,
                var giftLink: String? = null,
                var giftImageUrl: String? = null,
                var delivery: Boolean? = null) : Parcelable {}