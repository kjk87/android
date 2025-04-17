package com.pplus.luckybol.core.network.model.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by j2n on 2017. 1. 16..
 */
@Parcelize
class Comment(var no: Long? = null,
              var group_seq_no: Long? = null,
              var comment: String? = null,
              var isBlind: Boolean = false,
              var depth: Int? = null,
              var priority: Int? = null,
              var isDeleted: Boolean = false,
              var parent: Comment? = null,
              var author: User? = null,
              var post: Post? = null,
              var regDate: String? = null) : Parcelable {

    override fun toString(): String {
        return "Comment{" + "no=" + no + ", group_seq_no=" + group_seq_no + ", comment='" + comment + '\'' + ", blind=" + isBlind + ", depth=" + depth + ", priority=" + priority + ", deleted=" + isDeleted + ", parent=" + parent + ", author=" + author + ", post=" + post + ", regDate='" + regDate + '\'' + '}'
    }
}