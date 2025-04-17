package com.pplus.prnumberuser.core.network.model.dto


import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Created by 김종경 on 2016-10-04.
 */

@Parcelize
class DeliveryAddress(var address: String? = null,
                      var jibunAddress: String? = null,
                      var addressDetail: String? = null,
                      var sido: String? = null,
                      var sigungu: String? = null,
                      var dongli: String? = null,
                      var buildNo: String? = null,
                      var roadName: String? = null,
                      var zipNo: String? = null,
                      var latitude: Double? = null,
                      var longitude: Double? = null,
                      var pick: Boolean? = null) : Parcelable {
}
