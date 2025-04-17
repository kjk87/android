package com.root37.buflexz.core.network.model.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by 김종경 on 2016-10-04.
 */
@Parcelize
class MemberDelivery(var seqNo: Long? = null,
                     var userKey: String? = null,
                     var nation: String? = null,
                     var nationKr: String? = null,
                     var receiverName: String? = null,
                     var method: String? = null,
                     var zipcode: String? = null,
                     var addr1: String? = null,
                     var addr2: String? = null,
                     var state: String? = null,
                     var city: String? = null,
                     var detail: String? = null,
                     var telNation: String? = null,
                     var telRegion: String? = null,
                     var tel1: String? = null,
                     var tel2: String? = null,
                     var email: String? = null) : Parcelable {

}