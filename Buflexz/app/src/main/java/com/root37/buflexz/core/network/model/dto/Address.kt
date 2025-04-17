package com.root37.buflexz.core.network.model.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by j2n on 2016. 7. 26..
 */
@Parcelize
class Address(var addr: String? = null,
              var roadAddress: String? = null,
              var jibunAddress: String? = null,
              var postno: String? = null) : Parcelable {

}