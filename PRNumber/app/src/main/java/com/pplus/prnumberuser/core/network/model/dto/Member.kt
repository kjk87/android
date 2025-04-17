package com.pplus.prnumberuser.core.network.model.dto

import android.os.Parcel
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by imac on 2018. 1. 15..
 */
@Parcelize
class Member(var seqNo: Long? = null,
             var countrySeqNo: Long? = null,
             var memberName: String? = null,
             var accountType: String? = null,
             var loginId: String? = null,
             var password: String? = null,
             var memberType: String? = null,
             var useStatus: String? = null,
             var restrictionStatus: String? = null,
             var restrictionClearDatetime: String? = null,
             var nickname: String? = null,
             var mobileNumber: String? = null,
             var email: String? = null,
             var zipCode: String? = null,
             var baseAddress: String? = null,
             var joinDatetime: String? = null,
             var joinPlatform: String? = null,
             var verificationMedia: String? = null,
             var recommendationCode: String? = null,
             var gender: String? = null,
             var married: Boolean? = null,
             var child: Boolean? = null,
             var birthday: String? = null,
             var job: String? = null,
             var talkReceiveBounds: String? = null,
             var talkDenyDay: String? = null,
             var talkDenyStartTime: String? = null,
             var talkDenyEndTime: String? = null,
             var talkReceive: Boolean? = null,
             var talkPushReceive: Boolean? = null,
             var lastLoginDatetime: String? = null,
             var loginFailCount: Int? = null,
             var lastLoginFailDatetime: String? = null,
             var contactListVersion: Long? = null,
             var leaveRequestDatetime: String? = null,
             var leaveFinishDatetime: String? = null,
             var calculated: Boolean? = null,
             var calculatedMonth: String? = null,
             var regType: String? = null,
             var cash: Long? = null,
             var bol: Long? = null,
             var point: Long? = null,
             var modDatetime: String? = null,
             var modifierSeqNo: Long? = null,
             var recommendUniqueKey: String? = null,
             var lottoTicketCount: Int? = null,
             var lottoDefaultTicketCount: Int? = null,
             var eventTicketCount: Int? = null,
             var agentCode: String? = null,
             var plusTerms: Boolean? = null,
             var woodongyi: Boolean? = null,
             var setPayPassword: Boolean? = null,
             var buyPlusTerms: Boolean? = null,
             var pushCount: Int? = null,
             var activeArea1Value: String? = null,
             var activeArea1Name: String? = null,
             var activeArea2Value: String? = null,
             var activeArea2Name: String? = null,
             var appType: String? = null,
             var isVirtual: Boolean? = null,
             var adRewardCount: Int? = null,
             var adRewardDatetime: String? = null,
             var profileAttachment: Attachment? = null,
             var qrImage: String? = null,
             var joinType:String? = null,
             var page:Page2) : Parcelable {
    override fun equals(o: Any?): Boolean {

        if (o == null) return false

        return if (o is Member) {
            o.seqNo == seqNo
        } else {
            false
        }
    }

}