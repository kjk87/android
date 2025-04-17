package com.pplus.luckybol.core.network.model.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by imac on 2018. 1. 2..
 */

@Parcelize
class Post(var no: Long? = null,
           var subject: String? = null,
           var priority: Int? = null,
           var viewCount: Int? = null,
           var blind: Boolean = false,
           var type: String? = null,
           var commentCount: Int? = null,
           var imageCount: Int? = null,
           var contents: String? = null,
           var attachList: List<Attachment>? = null,
           var regDate: String? = null,
           var articleUrl: String? = null,
           var properties: PostProperties? = null,
           var author: User? = null,
           var board: No? = null,
           var page: Page? = null,
           var appType: String? = null) : Parcelable {
    override fun equals(other: Any?): Boolean {
        if (other == null) return false

        return if (other is Post) {
            other.no == no
        } else {
            false
        }
    }

}