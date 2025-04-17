package com.lejel.wowbox.core.network.model.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by 김종경 on 2016-10-04.
 */
@Parcelize
class LuckyDrawGroup(var seqNo: Long? = null,
                     var status: String? = null,
                     var title: String? = null,
                     var contents: String? = null,
                     var image: String? = null,
                     var comment: String? = null,
                     var regDatetime: String? = null) : Parcelable {

}