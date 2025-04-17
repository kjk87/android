package com.lejel.wowbox.core.network.model.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by 김종경 on 2016-10-04.
 */
@Parcelize
class LuckyDraw(var seqNo: Long? = null,
                var aos: Boolean? = null,
                var ios: Boolean? = null,
                var announceType: String? = null, //live,auto
                var engageType: String? = null, //ball,free
                var title: String? = null,
                var startDatetime: String? = null,
                var endDatetime: String? = null,
                var engageBall: Int? = null,
                var totalEngage: Int? = null,
                var engageNumber: Int? = null,
                var totalPrice: Int? = null,
                var bannerImage: String? = null,
                var detailImage: String? = null,
                var announceImage: String? = null,
                var status: String? = null,
                var winAnnounceDatetime: String? = null,
                var liveUrl: String? = null,
                var regDatetime: String? = null,
                var modDatetime: String? = null,
                var winType: Int? = null,//1:ABC, 2:AC, 3:C
                var first: String? = null,
                var second: String? = null,
                var third: String? = null,
                var winRate1: String? = null,
                var winRate2: String? = null,
                var winRate3: String? = null,
                var isPrivate: Boolean? = null,
                var contents: String? = null,
                var joinCount: Int? = null,
                var giftList: List<LuckyDrawGift>? = null) : Parcelable {

}