package com.lejel.wowbox.core.network.model.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by imac on 2018. 1. 2..
 */
@Parcelize
class EventGift(var seqNo: Long? = null,
                var eventSeqNo: Long? = null,
                var giftType: String? = null,
                var title: String? = null,
                var totalCount: Int? = null,
                var alert: String? = null,
                var lotPercent: Int? = null,
                var price: Int? = null,
                var attachSeqNo: Long? = null,
                var remainCount: Int? = null,
                var winOrder: String? = null,
                var expireDate: String? = null,
                var timeType: String? = null,
                var dayType: String? = null,
                var code: String? = null,
                var startTime: String? = null,
                var endTime: String? = null,
                var days: String? = null,
                var reviewPoint: Int? = null,
                var reviewPresent: Boolean? = null,
                var giftLink: String? = null,
                var giftImageUrl: String? = null) : Parcelable {}