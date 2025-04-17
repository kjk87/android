package com.pplus.luckybol.core.network.model.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by imac on 2018. 1. 15..
 */
@Parcelize
class User(var no: Long? = null,
           var loginId: String? = null,
           var name: String? = null,
           var password: String? = null,
           var accountType: String? = "pplus",
           var platform: String? = "aos",
           var memberType: String? = null, //MemberTypeCode
           var useStatus: String? = null,
           var restrictionStatus: String? = null, //RestrictionStatusCode
           var restrictionClsDate: String? = null,
           var nickname: String? = null,
           var mobile: String? = null,
           var email: String? = null,
           var verification: Verification? = null,
           var joinDate: String? = null,
           var regType: String? = null,
           var page: Page? = null,
           var lastLoginDate: String? = null,
           var loginFailCount: Int? = null,
           var contactVersion: Long? = null,
           var reqLeaveDate: String? = null,
           var calculated: Boolean = false,
           var sendbirdUser: Boolean = false,
           var talkRecvBound: String? = null, // TalkReceiveBoundsCode
           var talkDenyDay: String? = null,
           var talkDenyStartTime: String? = null,
           var talkDenyEndTime: String? = null,
           var modDate: String? = null,
           var device: Device? = null,
           var sessionKey: String? = null,
           var termsList: List<No>? = null,
           var country: No? = No(1L),
           var totalCash: Int = 0,
           var totalBol: Double = 0.0,
           var profileImage: ImgUrl? = null,
           var recommendKey: String? = null,
           var normalNumberCount: String? = null,
           var number: PRNumber? = null,
           var gender: String? = null,
           var birthday: String? = null,
           var recommendationCode: String? = null, //가입시 추천인 코드
           var certificationLevel: Int? = null,
           var displayName: String? = null,
           var married: Boolean? = null,
           var hasChild: Boolean? = null,
           var jobType: String? = null,
           var zipCode: String? = null,
           var baseAddr: String? = null,
           var ranking: Long? = null,
           var rankingCount: Long? = null,
           var properties: UserProperties? = null,
           var friend: Boolean = false,
           var haveSameFriends: Boolean = false,
           var boardSeqNo: Long? = null,
           var lottoDefaultTicketCount: Int? = null,
           var lottoTicketCount: Int? = null,
           var latitude: Double? = null,
           var longitude: Double? = null,
           var agentCode: String? = null,
           var eventTicketCount: Int? = null,
           var plusTerms: Boolean? = null,
           var woodongyi: Boolean? = null,
           var setPayPassword: Boolean? = null,
           var buyPlusTerms: Boolean? = null,
           var activeArea1Value: String? = null,
           var activeArea1Name: String? = null,
           var activeArea2Value: String? = null,
           var activeArea2Name: String? = null,
           var appType: String? = null,
           var point: Double? = null,
           var adRewardCount: Int? = null,
           var adRewardDatetime: String? = null,
           var encrypted: Boolean? = null,
           var buffPostPublic: Boolean? = null) : Parcelable {

    override fun equals(o: Any?): Boolean {

        if (o == null) return false

        return if (o is User) {
            o.no == no
        } else {
            false
        }
    }

}