package com.lejel.wowbox.core.network.model.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by imac on 2018. 1. 2..
 */
@Parcelize
class LuckyBoxReviewImage(var id: Long? = null,
                          var luckyboxReviewSeqNo: Long? = null,
                          var image: String? = null,
                          var array: Int? = null,
                          var type: String? = null) : Parcelable {

}