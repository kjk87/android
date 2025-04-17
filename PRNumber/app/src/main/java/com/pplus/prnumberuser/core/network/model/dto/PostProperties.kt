package com.pplus.prnumberuser.core.network.model.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by kjk on 2017. 6. 20..
 */
@Parcelize
class PostProperties(var luckyBol: String? = null,
                     var starPoint: String? = null,
                     var email: String? = null) : Parcelable {

    override fun toString(): String {
        return "PostProperties{luckyBol='$luckyBol', starPoint='$starPoint', email='$email'}"
    }

}