package com.pplus.prnumberuser.core.network.model.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by 김종경 on 2016-10-04.
 */
@Parcelize
class NotificationBox(var seqNo: Long? = null,
                      var memberSeqNo: Long? = null,
                      var subject: String? = null,
                      var contents: String? = null,
                      var moveType1: String? = null,
                      var moveType2: String? = null,
                      var moveSeqNo: Long? = null,
                      var moveString: String? = null,
                      var regDatetime: String? = null,
                      var readDatetime: String? = null,
                      var isRead: Boolean? = null) : Parcelable {

}