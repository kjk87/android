package com.root37.buflexz.core.network.model.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by imac on 2018. 1. 15..
 */
@Parcelize
class Member(var userKey: String? = null,
             var nation: String? = null,
             var language: String? = null,
             var memberType: String? = null,
             var nickname: String? = null,
             var status: String? = null,//active, dormancy, stop, leave, waitingLeave
             var joinType: String? = null,
             var joinPlatform: String? = null,
             var joinDatetime: String? = null,
             var recommendeeKey: String? = null,
             var email: String? = null,
             var device: String? = null,
             var authEmail: String? = null,
             var isAuthEmail: Boolean? = null,
             var gender: String? = null,
             var birthday: String? = null,
             var profile: String? = null,
             var platformEmail: String? = null,
             var platformKey: String? = null,
             var lastLoginDatetime: String? = null,
             var leaveFinishDatetime: String? = null,
             var leaveMsg: String? = null,
             var modDatetime: String? = null,
             var point: Double? = null,
             var ball: Int? = null,
             var candy: Int? = null,
             var marketingReceiving: Boolean? = null,
             var token: String? = null,
             var refreshToken: String? = null,
             var termsNo: String? = null) : Parcelable {

}