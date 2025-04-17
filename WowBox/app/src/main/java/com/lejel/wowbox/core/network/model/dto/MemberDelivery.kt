package com.lejel.wowbox.core.network.model.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by 김종경 on 2016-10-04.
 */
@Parcelize
class MemberDelivery(var seqNo: Long? = null,
                     var userKey: String? = null,
                     var nation: String? = null,
                     var receiverName: String? = null,
                     var receiverFamilyName: String? = null,
                     var receiverPostCode: String? = null,
                     var receiverTel: String? = null,
                     var receiverAddress: String? = null,
                     var receiverAddress2: String? = null,
                     var receiverProvinsi: String? = null,
                     var receiverKabkota: String? = null,
                     var receiverKecamatan: String? = null,
                     var deliveryMemo: String? = null) : Parcelable {

}