package com.lejel.wowbox.core.network.model.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by imac on 2018. 1. 2..
 */
@Parcelize
class EventReview(var seqNo: Long? = null,
                  var userKey: String? = null,
                  var eventSeqNo: Long? = null,
                  var eventWinSeqNo: Long? = null,
                  var eventGiftSeqNo: Long? = null,
                  var review: String? = null,
                  var regDatetime: String? = null,
                  var modDatetime: String? = null,
                  var status: String? = null,
                  var eventReviewImage: List<EventReviewImage>? = null,
                  var replyCount: Int? = null,
                  var memberTotal: Member? = null) : Parcelable {}