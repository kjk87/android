package com.lejel.wowbox.core.network.model.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by imac on 2018. 1. 15..
 */
@Parcelize
class Partner(var userKey: String? = null,
              var nation: String? = null,
              var language: String? = null,
              var type: String? = null,//partner1, partner2
              var status: String? = null,//active, pending, reapply, return
              var parents: String? = null,
              var name: String? = null,
              var identityCard: String? = null,
              var bankName: String? = null,
              var accountType: String? = null,
              var routingNo: String? = null,
              var accountNo: String? = null,
              var accountFirstName: String? = null,
              var accountLastName: String? = null,
              var phoneNationNo: String? = null,
              var phoneNumber: String? = null,
              var instargram: String? = null,
              var youtube: String? = null,
              var twitter: String? = null,
              var requestDatetime: String? = null,
              var changeStatusDatetime: String? = null,
              var bonusCommission: Float? = null,
              var adCommission: Float? = null,
              var ballCommission: Float? = null,
              var reason: String? = null,
              var adCount: Int? = null,
              var lastCheckCount: Int? = null,
              var lastCheckDatetime: String? = null,
              var email: String? = null,
              var memberTotal:Member? = null) : Parcelable {

}