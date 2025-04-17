package com.root37.buflexz.core.network.model.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by 김종경 on 2016-10-04.
 */
@Parcelize
class InquireImage(var seqNo: Long? = null,
                   var inquireSeqNo: Long? = null,
                   var image: String? = null,
                   var regDatetime: String? = null) : Parcelable {

}