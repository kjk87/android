package com.pplus.prnumberuser.core.network.model.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class ChatData(var msg: String? = null,
               var name: String? = null,
               var roomName: String? = null,
               var pageSeqNo: Long? = null,
               var memberSeqNo: Long? = null,
               var targetMemberSeqNo: Long? = null,
               var timeMillis: Long? = null) : Parcelable {

}
