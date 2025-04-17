package com.lejel.wowbox.core.network.model.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by j2n on 2016. 7. 26..
 */
@Parcelize
class GiftCardBrand(var seqNo: Long? = null,
                    var title: String? = null,
                    var status: String? = null,
                    var comment: String? = null,
                    var backgroundImage: String? = null,
                    var delegateImage: String? = null,
                    var array: Int? = null,
                    var regDatetime: String? = null) : Parcelable {

}