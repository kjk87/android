package com.lejel.wowbox.core.network.model.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by 김종경 on 2016-10-04.
 */
@Parcelize
class NotificationBox(var seqNo: Long? = null,
                      var userKey: String? = null,
                      var title: String? = null,
                      var contents: String? = null,
                      var innerType: String? = null,
                      var moveType: String? = null,
                      var outerUrl: String? = null,
                      var target: Long? = null,
                      var regDatetime: String? = null,
                      var readDatetime: String? = null,
                      var isRead: Boolean? = null) : Parcelable {
}