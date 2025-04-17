package com.pplus.prnumberuser.core.network.model.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by ksh on 2016-09-28.
 */
@Parcelize
class Faq(var no: Long? = null,
          var group: FaqGroup? = null,
          var status: String? = null,
          var subject: String? = null,
          var contents: String? = null,
          var priority: Int? = null) : Parcelable {

}