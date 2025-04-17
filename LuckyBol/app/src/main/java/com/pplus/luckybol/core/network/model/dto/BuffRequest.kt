package com.pplus.luckybol.core.network.model.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by imac on 2018. 1. 2..
 */
@Parcelize
class BuffRequest(var seqNo: Long? = null,
                  var buffSeqNo: Long? = null,
                  var memberSeqNo: Long? = null,
                  var requester: Long? = null,
                  var status: String? = null, //request,reject,consent,withdraw
                  var statusDatetime: String? = null,
                  var withdrawType: String? = null, //compulsory(강퇴), oneself(스스로)
                  var note: String? = null,
                  var regDatetime: String? = null,
                  var buff: Buff? = null,
                  var member: Member? = null) : Parcelable {}