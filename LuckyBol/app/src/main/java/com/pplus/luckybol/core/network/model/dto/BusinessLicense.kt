package com.pplus.luckybol.core.network.model.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by imac on 2018. 1. 2..
 */

@Parcelize
class BusinessLicense(var id: Long? = null,
                      var page: Long? = null,
                      var companyName: String? = null,
                      var ceo: String? = null,
                      var corporateNumber: String? = null,
                      var companyAddress: String? = null,
                      var businessType: String? = null,
                      var items: String? = null,
                      var businessOperatorType: String? = null,
                      var regDatetime: String? = null) : Parcelable {
}