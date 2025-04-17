package com.lejel.wowbox.core.network.model.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by j2n on 2016. 7. 26..
 */
@Parcelize
class Nation(var code: String? = null,
             var name: String? = null,
             var nameEn: String? = null,
             var nationNo: String? = null,
             var bonusCommission: Float? = null,
             var adCommission: Float? = null,
             var ballCommission: Float? = null,
             var regDatetime: String? = null) : Parcelable {

}