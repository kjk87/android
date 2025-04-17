package com.pplus.luckybol.core.network.model.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by imac on 2018. 1. 2..
 */
@Parcelize
class EventReview(var seqNo: Long? = null,
                  var memberSeqNo: Long? = null,
                  var eventSeqNo: Long? = null,
                  var eventWinSeqNo: Long? = null,
                  var eventWinId: Long? = null,
                  var eventGiftSeqNo: Long? = null,
                  var review: String? = null,
                  var regDatetime: String? = null,
                  var modDatetime: String? = null,
                  var imageList: List<EventReviewImage>? = null,
                  var eventWin: EventWinJpa? = null,
                  var replyCount: Int? = null,
                  var member: Member? = null,
                  var friend: Boolean? = null) : Parcelable {}