package com.lejel.wowbox.core.network.model.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by 김종경 on 2016-10-04.
 */
@Parcelize
class LuckyBox(var seqNo: Long? = null,
               var title: String? = null,
               var refundBol: Int? = null,
               var provideBol: Int? = null,
               var engagePrice: Int? = null,
               var regDatetime: String? = null,
               var modDatetime: String? = null) : Parcelable {

}