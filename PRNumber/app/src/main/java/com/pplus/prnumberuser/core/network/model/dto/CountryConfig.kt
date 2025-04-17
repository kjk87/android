package com.pplus.prnumberuser.core.network.model.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by imac on 2017. 8. 29..
 */
@Parcelize
class CountryConfig(var no: Long? = null,
                    var number: String? = null,
                    var code: String? = null,
                    var engName: String? = null,
                    var currency: String? = null,
                    var status: String? = null,
                    var properties: CountryConfigProperties? = null) : Parcelable {

}