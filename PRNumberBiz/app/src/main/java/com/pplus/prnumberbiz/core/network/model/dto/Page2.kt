package com.pplus.prnumberbiz.core.network.model.dto

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by imac on 2018. 1. 15..
 */
class Page2(var seqNo: Long? = null,
            var memberSeqNo: Long? = null,
            var coopSeqNo: Long? = null,
            var status: String? = null,
            var name: String? = null,
            var phone: String? = null,
            var openBound: String? = null,
            var zipCode: String? = null,
            var roadAddress: String? = null,
            var roadDetailAddress: String? = null,
            var parcelAddress: String? = null,
            var parcelDetailAddress: String? = null,
            var latitude: Double? = null,
            var longitude: Double? = null,
            var catchphrase: String? = null,
            var categoryText: String? = null,
            var todayViewCount: Int? = null,
            var totalViewCount: Int? = null,
            var blind: Boolean = false,
            var talkRecvBound: String? = null,
            var talkDenyDay: String? = null,
            var talkDenyStartTime: String? = null,
            var talkDenyEndTime: String? = null,
            var customerCount: Int? = null,
            var plusCount: Int? = null,
            var pageProp: String? = null,
            var regDatetime: String? = null,
            var modDatetime: String? = null,
            var type: String? = null,
            var modifierSeqNo: Long? = null,
            var profileSeqNo: Long? = null,
            var profileAttachment: Attachment? = null,
            var bgSeqNo: Long? = null,
            var valuationCount: Int? = null,
            var valuationPoint: Int? = null,
            var offerLimitDate: String? = null,
            var virtualPage: String? = null,
            var searchKeyword: String? = null,
            var code: String? = null,
            var introduction: String? = null,
            var mainMovieUrl: String? = null,
            var coopStatus: String? = null,
            var pageLevel: Int? = null,
            var authCode: String? = null,
            var incorrectAuthCodeCount: Int? = null,
            var agentSeqNo: Long? = null,
            var recommendationCode: String? = null,
            var settlementUrl: String? = null,
            var distance: Double? = null,
            var goods: Goods? = null,
            var mainGoodsSeqNo: Long? = null,
            var reviewCount: Int? = null,
            var goodsCount: Int? = null,
            var avgEval: Double? = null,
            var homepageLink: String? = null,
            var isLink: Boolean? = null,
            var plus: Boolean? = null,
            var hashtag: String? = null,
            var numberList: List<PRNumber2>? = null,
            var isShopOrderable: Boolean? = null,
            var isPackingOrderable: Boolean? = null,
            var isDeliveryOrderable: Boolean? = null,
            var management: PageManagement? = null,
            var usePrnumber: Boolean? = null,
            var distributorAgentCode: String? = null,
            var bank: String? = null,
            var bankAccount: String? = null,
            var depositor: String? = null,
            var reason: String? = null,
            var email: String? = null,
            var licenseImage: String? = null,
            var ableNfc: Boolean? = null,
            var commissionPoint: CommissionPoint? = null,
            var shopCode: String? = null) : Parcelable {
    override fun equals(other: Any?): Boolean {
        if (other == null) return false

        return if (other is Page2) {
            other.seqNo == seqNo
        } else {
            false
        }
    }

    constructor(source: Parcel) : this(
            source.readValue(Long::class.java.classLoader) as Long?,
            source.readValue(Long::class.java.classLoader) as Long?,
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
            source.readValue(Double::class.java.classLoader) as Double?,
            source.readValue(Double::class.java.classLoader) as Double?,
            source.readString(),
            source.readString(),
            source.readValue(Int::class.java.classLoader) as Int?,
            source.readValue(Int::class.java.classLoader) as Int?,
            1 == source.readInt(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readValue(Int::class.java.classLoader) as Int?,
            source.readValue(Int::class.java.classLoader) as Int?,
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readValue(Long::class.java.classLoader) as Long?,
            source.readValue(Long::class.java.classLoader) as Long?,
            source.readParcelable<Attachment>(Attachment::class.java.classLoader),
            source.readValue(Long::class.java.classLoader) as Long?,
            source.readValue(Int::class.java.classLoader) as Int?,
            source.readValue(Int::class.java.classLoader) as Int?,
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readValue(Int::class.java.classLoader) as Int?,
            source.readString(),
            source.readValue(Int::class.java.classLoader) as Int?,
            source.readValue(Long::class.java.classLoader) as Long?,
            source.readString(),
            source.readString(),
            source.readValue(Double::class.java.classLoader) as Double?,
            source.readParcelable<Goods>(Goods::class.java.classLoader),
            source.readValue(Long::class.java.classLoader) as Long?,
            source.readValue(Int::class.java.classLoader) as Int?,
            source.readValue(Int::class.java.classLoader) as Int?,
            source.readValue(Double::class.java.classLoader) as Double?,
            source.readString(),
            source.readValue(Boolean::class.java.classLoader) as Boolean?,
            source.readValue(Boolean::class.java.classLoader) as Boolean?,
            source.readString(),
            source.createTypedArrayList(PRNumber2.CREATOR),
            source.readValue(Boolean::class.java.classLoader) as Boolean?,
            source.readValue(Boolean::class.java.classLoader) as Boolean?,
            source.readValue(Boolean::class.java.classLoader) as Boolean?,
            source.readParcelable<PageManagement>(PageManagement::class.java.classLoader),
            source.readValue(Boolean::class.java.classLoader) as Boolean?,
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readValue(Boolean::class.java.classLoader) as Boolean?,
            source.readParcelable<CommissionPoint>(CommissionPoint::class.java.classLoader),
            source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeValue(seqNo)
        writeValue(memberSeqNo)
        writeValue(coopSeqNo)
        writeString(status)
        writeString(name)
        writeString(phone)
        writeString(openBound)
        writeString(zipCode)
        writeString(roadAddress)
        writeString(roadDetailAddress)
        writeString(parcelAddress)
        writeString(parcelDetailAddress)
        writeValue(latitude)
        writeValue(longitude)
        writeString(catchphrase)
        writeString(categoryText)
        writeValue(todayViewCount)
        writeValue(totalViewCount)
        writeInt((if (blind) 1 else 0))
        writeString(talkRecvBound)
        writeString(talkDenyDay)
        writeString(talkDenyStartTime)
        writeString(talkDenyEndTime)
        writeValue(customerCount)
        writeValue(plusCount)
        writeString(pageProp)
        writeString(regDatetime)
        writeString(modDatetime)
        writeString(type)
        writeValue(modifierSeqNo)
        writeValue(profileSeqNo)
        writeParcelable(profileAttachment, 0)
        writeValue(bgSeqNo)
        writeValue(valuationCount)
        writeValue(valuationPoint)
        writeString(offerLimitDate)
        writeString(virtualPage)
        writeString(searchKeyword)
        writeString(code)
        writeString(introduction)
        writeString(mainMovieUrl)
        writeString(coopStatus)
        writeValue(pageLevel)
        writeString(authCode)
        writeValue(incorrectAuthCodeCount)
        writeValue(agentSeqNo)
        writeString(recommendationCode)
        writeString(settlementUrl)
        writeValue(distance)
        writeParcelable(goods, 0)
        writeValue(mainGoodsSeqNo)
        writeValue(reviewCount)
        writeValue(goodsCount)
        writeValue(avgEval)
        writeString(homepageLink)
        writeValue(isLink)
        writeValue(plus)
        writeString(hashtag)
        writeTypedList(numberList)
        writeValue(isShopOrderable)
        writeValue(isPackingOrderable)
        writeValue(isDeliveryOrderable)
        writeParcelable(management, 0)
        writeValue(usePrnumber)
        writeString(distributorAgentCode)
        writeString(bank)
        writeString(bankAccount)
        writeString(depositor)
        writeString(reason)
        writeString(email)
        writeString(licenseImage)
        writeValue(ableNfc)
        writeParcelable(commissionPoint, 0)
        writeString(shopCode)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Page2> = object : Parcelable.Creator<Page2> {
            override fun createFromParcel(source: Parcel): Page2 = Page2(source)
            override fun newArray(size: Int): Array<Page2?> = arrayOfNulls(size)
        }
    }
}