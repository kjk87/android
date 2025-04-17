package com.lejel.wowbox.core.network.model.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by 김종경 on 2016-10-04.
 */
@Parcelize
class LuckyBoxProductGroupItem(var seqNo: Long? = null,
                               var luckyboxProductGroupSeqNo: Long? = null,
                               var productSeqNo: Long? = null,
                               var regDatetime: String? = null,
                               var productName: String? = null,
                               var price: Float? = null,
                               var image: String? = null) : Parcelable {

}