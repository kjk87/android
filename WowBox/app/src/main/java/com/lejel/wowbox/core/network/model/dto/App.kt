package com.lejel.wowbox.core.network.model.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by j2n on 2016. 7. 26..
 */
@Parcelize
class App(var seqNo: Long? = null,
          var platform: String? = null,
          var title: String? = null,
          var status: String? = null,
          var version: String? = null,
          var isVital: Boolean? = null,
          var isOpen: Boolean? = null,
          var storeUrl: String? = null,
          var comment: String? = null,
          var regDatetime: String? = null) : Parcelable {

}