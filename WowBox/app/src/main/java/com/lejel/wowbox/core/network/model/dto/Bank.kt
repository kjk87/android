package com.lejel.wowbox.core.network.model.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by j2n on 2016. 7. 26..
 */
@Parcelize
class Bank(var code: String? = null,
           var name: String? = null,
           var image: String? = null) : Parcelable {

}