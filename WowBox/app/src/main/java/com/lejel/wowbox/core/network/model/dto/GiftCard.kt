package com.lejel.wowbox.core.network.model.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by j2n on 2016. 7. 26..
 */
@Parcelize
class GiftCard(var seqNo: Long? = null,
               var brandSeqNo: Long? = null,
               var title: String? = null,
               var status: String? = null,
               var comment: String? = null,
               var image: String? = null,
               var price: Float? = null,
               var regDatetime: String? = null) : Parcelable {

}