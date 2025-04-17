package com.root37.buflexz.core.network.model.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by 김종경 on 2016-10-04.
 */
@Parcelize
class Inquire(var seqNo: Long? = null,
              var userKey: String? = null,
              var nation: String? = null,
              var type: String? = null,//general, partnership, error, etc
              var status: String? = null,
              var title: String? = null,
              var contents: String? = null,
              var reply: String? = null,
              var replyer: Long? = null,
              var regDatetime: String? = null,
              var replyDatetime: String? = null,
              var imageList:List<InquireImage>? = null) : Parcelable {

}