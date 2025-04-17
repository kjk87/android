package com.pplus.prnumberuser.core.network.model.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by imac on 2018. 1. 2..
 */
@Parcelize
class News(
        var seqNo: Long? = null,
        var pageSeqNo: Long? = null,
        var type: String? = null,//link, product, none
        var title: String? = null,
        var content: String? = null,
        var link: String? = null,
        var productSeqNo: Long? = null,
        var hits: Int? = null,
        var regDatetime: String? = null,
        var modDatetime: String? = null,
        var deleted: Boolean? = null,
        var newsImageList: List<NewsImage>? = null,
        var page: Page2? = null) : Parcelable {

}