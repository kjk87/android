package com.pplus.prnumberbiz.core.network.model.dto

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by imac on 2018. 1. 15..
 */
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
           var totalBol: Int = 0,
           var profileImage: ImgUrl? = null,
           var recommendKey: String? = null,
           var normalNumberCount: String? = null,
           var number: PRNumber? = null,
           var gender: String? = null,
           var birthday: String? = null,
           var recommendationCode: String? = null,//가입시 추천인 코드
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
           var plusTerms: Boolean? = null) : Parcelable {
    override fun equals(o: Any?): Boolean {

        if (o == null) return false

        return if (o is User) {
            o.no == no
        } else {
            false
        }
    }

    constructor(source: Parcel) : this(
            source.readValue(Long::class.java.classLoader) as Long?,
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readParcelable<Verification>(Verification::class.java.classLoader),
            source.readString(),
            source.readString(),
            source.readParcelable<Page>(Page::class.java.classLoader),
            source.readString(),
            source.readValue(Int::class.java.classLoader) as Int?,
            source.readValue(Long::class.java.classLoader) as Long?,
            source.readString(),
            1 == source.readInt(),
            1 == source.readInt(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readParcelable<Device>(Device::class.java.classLoader),
            source.readString(),
            source.createTypedArrayList(No.CREATOR),
            source.readParcelable<No>(No::class.java.classLoader),
            source.readInt(),
            source.readInt(),
            source.readParcelable<ImgUrl>(ImgUrl::class.java.classLoader),
            source.readString(),
            source.readString(),
            source.readParcelable<PRNumber>(PRNumber::class.java.classLoader),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readValue(Int::class.java.classLoader) as Int?,
            source.readString(),
            source.readValue(Boolean::class.java.classLoader) as Boolean?,
            source.readValue(Boolean::class.java.classLoader) as Boolean?,
            source.readString(),
            source.readString(),
            source.readString(),
            source.readValue(Long::class.java.classLoader) as Long?,
            source.readValue(Long::class.java.classLoader) as Long?,
            source.readParcelable<UserProperties>(UserProperties::class.java.classLoader),
            1 == source.readInt(),
            1 == source.readInt(),
            source.readValue(Long::class.java.classLoader) as Long?,
            source.readValue(Int::class.java.classLoader) as Int?,
            source.readValue(Int::class.java.classLoader) as Int?,
            source.readValue(Double::class.java.classLoader) as Double?,
            source.readValue(Double::class.java.classLoader) as Double?,
            source.readString(),
            source.readValue(Int::class.java.classLoader) as Int?,
            source.readValue(Boolean::class.java.classLoader) as Boolean?
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeValue(no)
        writeString(loginId)
        writeString(name)
        writeString(password)
        writeString(accountType)
        writeString(platform)
        writeString(memberType)
        writeString(useStatus)
        writeString(restrictionStatus)
        writeString(restrictionClsDate)
        writeString(nickname)
        writeString(mobile)
        writeParcelable(verification, 0)
        writeString(joinDate)
        writeString(regType)
        writeParcelable(page, 0)
        writeString(lastLoginDate)
        writeValue(loginFailCount)
        writeValue(contactVersion)
        writeString(reqLeaveDate)
        writeInt((if (calculated) 1 else 0))
        writeInt((if (sendbirdUser) 1 else 0))
        writeString(talkRecvBound)
        writeString(talkDenyDay)
        writeString(talkDenyStartTime)
        writeString(talkDenyEndTime)
        writeString(modDate)
        writeParcelable(device, 0)
        writeString(sessionKey)
        writeTypedList(termsList)
        writeParcelable(country, 0)
        writeInt(totalCash)
        writeInt(totalBol)
        writeParcelable(profileImage, 0)
        writeString(recommendKey)
        writeString(normalNumberCount)
        writeParcelable(number, 0)
        writeString(gender)
        writeString(birthday)
        writeString(recommendationCode)
        writeValue(certificationLevel)
        writeString(displayName)
        writeValue(married)
        writeValue(hasChild)
        writeString(jobType)
        writeString(zipCode)
        writeString(baseAddr)
        writeValue(ranking)
        writeValue(rankingCount)
        writeParcelable(properties, 0)
        writeInt((if (friend) 1 else 0))
        writeInt((if (haveSameFriends) 1 else 0))
        writeValue(boardSeqNo)
        writeValue(lottoDefaultTicketCount)
        writeValue(lottoTicketCount)
        writeValue(latitude)
        writeValue(longitude)
        writeString(agentCode)
        writeValue(eventTicketCount)
        writeValue(plusTerms)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<User> = object : Parcelable.Creator<User> {
            override fun createFromParcel(source: Parcel): User = User(source)
            override fun newArray(size: Int): Array<User?> = arrayOfNulls(size)
        }
    }
}