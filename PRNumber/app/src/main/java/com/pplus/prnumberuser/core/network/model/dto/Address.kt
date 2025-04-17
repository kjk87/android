package com.pplus.prnumberuser.core.network.model.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by 김종경 on 2016-10-04.
 */
@Parcelize
class Address(var zipCode: String? = null,
              var roadBase: String? = null,
              var roadDetail: String? = null,
              var parcelBase: String? = null,
              var parcelDetail: String? = null) : Parcelable {

}