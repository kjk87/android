package com.pplus.luckybol.core.network.model.dto

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by imac on 2018. 1. 15..
 */
class Plus(var plusNo: Long? = null,
           var no: Long? = null,
           var status: String? = null,
           var nickname: String? = null,
           var type: String? = null,
           var name: String? = null,
           var phone: String? = null,
           var openBound: String? = null,
           var homepageLink: String? = null,
           var catchphrase: String? = null,
           var categoryText: String? = null,
           var todayViewCount: Int? = null,
           var totalViewCount: Int? = null,
           var blind: Boolean = false,
           var talkRecvBound: String? = null,
           var talkDenyDay: String? = null,
           var talkDenyStartTime: String? = null,
           var talkDenyEndTime: String? = null,
           var latitude: Double? = null,
           var longitude: Double? = null,
           var distance: Double? = null,
           var customerCount: Int? = null,
           var plusCount: Int? = null,
           var valuationCount: Int? = null,
           var valuationPoint: Int? = null,
           var prBoard: No? = null,
           var reviewBoard: No? = null,
           var profileImage: ImgUrl? = null,
           var backgroundImage: ImgUrl? = null,
           var address: Address? = null,
           var properties: PageProperties? = null,
           var numberList: List<PRNumber>? = null,
           var searchKeyword: String? = null,
           var introduction: String? = null,
           var code: String? = null,
           var mainMovieUrl: String? = null,
           var cooperation: No? = null,
           var coopStatus: String? = null,
           var user: User? = null,
           var authCode: String? = null,
           var plus: Boolean? = null,
           var mainGoodsSeqNo: Long? = null,
           var reviewCount: Int? = null,
           var goodsCount: Int? = null,
           var avgEval: Double? = null,
           var isLink: Boolean? = null,
           var hashtag: String? = null,
           var virtualPage: Boolean? = null,
           var isShopOrderable: Boolean? = null,
           var isPackingOrderable: Boolean? = null,
           var isDeliveryOrderable: Boolean? = null,
           var management: PageManagement? = null,
           var isParkingAvailable: Boolean? = null,
           var isValetParkingAvailable: Boolean? = null,
           var usePrnumber: Boolean? = null,
           var distributorAgentCode: String? = null,
           var isSeller: Boolean? = null,
           var isBrand: Boolean? = null,
           var bank: String? = null,
           var bankAccount: String? = null,
           var depositor: String? = null,
           var reason: String? = null,
           var email: String? = null,
           var licenseImage: String? = null,
           var ableNfc: Boolean? = null,
           var thumbnail: String? = null,
           var goodsNotiType: String? = null,
           var goodsNotification: String? = null,
           var point: Float? = null,
           var woodongyi: Boolean? = null,
           var qrImage: String? = null,
           var categoryMinorSeqNo: Long? = null,
           var categoryMajorSeqNo: Long? = null,
           var buyCount: Int? = null,
           var lastBuyDatetime: String? = null,
           var pushActivate: Boolean? = null) : Parcelable {
    override fun equals(other: Any?): Boolean {
        if (other == null) return false

        return if (other is Plus) {
            other.no == no
        } else {
            false
        }
    }

    constructor(source: Parcel) : this(
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
            source.readValue(Int::class.java.classLoader) as Int?,
            source.readValue(Int::class.java.classLoader) as Int?,
            1 == source.readInt(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readValue(Double::class.java.classLoader) as Double?,
            source.readValue(Double::class.java.classLoader) as Double?,
            source.readValue(Double::class.java.classLoader) as Double?,
            source.readValue(Int::class.java.classLoader) as Int?,
            source.readValue(Int::class.java.classLoader) as Int?,
            source.readValue(Int::class.java.classLoader) as Int?,
            source.readValue(Int::class.java.classLoader) as Int?,
            source.readParcelable<No>(No::class.java.classLoader),
            source.readParcelable<No>(No::class.java.classLoader),
            source.readParcelable<ImgUrl>(ImgUrl::class.java.classLoader),
            source.readParcelable<ImgUrl>(ImgUrl::class.java.classLoader),
            source.readParcelable<Address>(Address::class.java.classLoader),
            source.readParcelable<PageProperties>(PageProperties::class.java.classLoader),
            source.createTypedArrayList(PRNumber.CREATOR),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readParcelable<No>(No::class.java.classLoader),
            source.readString(),
            source.readParcelable<User>(User::class.java.classLoader),
            source.readString(),
            source.readValue(Boolean::class.java.classLoader) as Boolean?,
            source.readValue(Long::class.java.classLoader) as Long?,
            source.readValue(Int::class.java.classLoader) as Int?,
            source.readValue(Int::class.java.classLoader) as Int?,
            source.readValue(Double::class.java.classLoader) as Double?,
            source.readValue(Boolean::class.java.classLoader) as Boolean?,
            source.readString(),
            source.readValue(Boolean::class.java.classLoader) as Boolean?,
            source.readValue(Boolean::class.java.classLoader) as Boolean?,
            source.readValue(Boolean::class.java.classLoader) as Boolean?,
            source.readValue(Boolean::class.java.classLoader) as Boolean?,
            source.readParcelable<PageManagement>(PageManagement::class.java.classLoader),
            source.readValue(Boolean::class.java.classLoader) as Boolean?,
            source.readValue(Boolean::class.java.classLoader) as Boolean?,
            source.readValue(Boolean::class.java.classLoader) as Boolean?,
            source.readString(),
            source.readValue(Boolean::class.java.classLoader) as Boolean?,
            source.readValue(Boolean::class.java.classLoader) as Boolean?,
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readValue(Boolean::class.java.classLoader) as Boolean?,
            source.readString(),
            source.readString(),
            source.readString(),
            source.readValue(Float::class.java.classLoader) as Float?,
            source.readValue(Boolean::class.java.classLoader) as Boolean?,
            source.readString(),
            source.readValue(Long::class.java.classLoader) as Long?,
            source.readValue(Long::class.java.classLoader) as Long?,
            source.readValue(Int::class.java.classLoader) as Int?,
            source.readString(),
            source.readValue(Boolean::class.java.classLoader) as Boolean?
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeValue(plusNo)
        writeValue(no)
        writeString(status)
        writeString(nickname)
        writeString(type)
        writeString(name)
        writeString(phone)
        writeString(openBound)
        writeString(homepageLink)
        writeString(catchphrase)
        writeString(categoryText)
        writeValue(todayViewCount)
        writeValue(totalViewCount)
        writeInt((if (blind) 1 else 0))
        writeString(talkRecvBound)
        writeString(talkDenyDay)
        writeString(talkDenyStartTime)
        writeString(talkDenyEndTime)
        writeValue(latitude)
        writeValue(longitude)
        writeValue(distance)
        writeValue(customerCount)
        writeValue(plusCount)
        writeValue(valuationCount)
        writeValue(valuationPoint)
        writeParcelable(prBoard, 0)
        writeParcelable(reviewBoard, 0)
        writeParcelable(profileImage, 0)
        writeParcelable(backgroundImage, 0)
        writeParcelable(address, 0)
        writeParcelable(properties, 0)
        writeTypedList(numberList)
        writeString(searchKeyword)
        writeString(introduction)
        writeString(code)
        writeString(mainMovieUrl)
        writeParcelable(cooperation, 0)
        writeString(coopStatus)
        writeParcelable(user, 0)
        writeString(authCode)
        writeValue(plus)
        writeValue(mainGoodsSeqNo)
        writeValue(reviewCount)
        writeValue(goodsCount)
        writeValue(avgEval)
        writeValue(isLink)
        writeString(hashtag)
        writeValue(virtualPage)
        writeValue(isShopOrderable)
        writeValue(isPackingOrderable)
        writeValue(isDeliveryOrderable)
        writeParcelable(management, 0)
        writeValue(isParkingAvailable)
        writeValue(isValetParkingAvailable)
        writeValue(usePrnumber)
        writeString(distributorAgentCode)
        writeValue(isSeller)
        writeValue(isBrand)
        writeString(bank)
        writeString(bankAccount)
        writeString(depositor)
        writeString(reason)
        writeString(email)
        writeString(licenseImage)
        writeValue(ableNfc)
        writeString(thumbnail)
        writeString(goodsNotiType)
        writeString(goodsNotification)
        writeValue(point)
        writeValue(woodongyi)
        writeString(qrImage)
        writeValue(categoryMinorSeqNo)
        writeValue(categoryMajorSeqNo)
        writeValue(buyCount)
        writeString(lastBuyDatetime)
        writeValue(pushActivate)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Plus> = object : Parcelable.Creator<Plus> {
            override fun createFromParcel(source: Parcel): Plus = Plus(source)
            override fun newArray(size: Int): Array<Plus?> = arrayOfNulls(size)
        }
    }
}