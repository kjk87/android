package com.lejel.wowbox.core.network.model.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by 김종경 on 2016-10-04.
 */
@Parcelize
class Banner(var seqNo: Long? = null,
             var aos: Boolean? = null,
             var ios: Boolean? = null,
             var type: String? = null,// home, shopping
             var nation: String? = null,
             var title: String? = null,
             var image: String? = null,
             var display: Boolean? = null,
             var startDatetime: String? = null,
             var endDatetime: String? = null,
             var moveType: String? = null,// none, inner, outer
             var innerType: String? = null, //notice, faq, main
             var outerUrl: String? = null,
             var target1: Long? = null,
             var target2: Long? = null,
             var regDatetime: String? = null,
             var androidArray: Int? = null,
             var iosArray: Int? = null) : Parcelable {

}