package com.pplus.prnumberuser.core.network.model.dto

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by imac on 2018. 1. 2..
 */
class Goods(var seqNo: Long? = null,
            var pageSeqNo: Long? = null,
            var categorySeqNo: Long? = null,
            var pageCategorySeqNo: Long? = null,
            var name: String? = null,
            var hashtag: String? = null,
            var description: String? = null,
            var count: Int? = null,
            var soldCount: Int? = null,
            var status: Int? = null,//1 : 판매중, 0: 판매중단, 2 : 판매완료(sold out)
            var main: Int? = null,
            var price: Long? = null,
            var originPrice: Long? = null,
            var regDatetime: String? = null,
            var modDatetime: String? = null,
            var expireDatetime: String? = null,
            var attachments: GoodsAttachments? = null,
            var page: Page2? = null,
            var reviewCount: Int? = null,
            var distance: Double? = null,
            var avgEval: Double? = null,
            var rewardLuckybol: Long? = null,
            var isHotdeal: Boolean? = null,
            var isPlus: Boolean? = null,
            var category: GoodsCategory? = null,
            var type: String? = null,
            var startTime: String? = null,
            var endTime: String? = null,
            var rewardPrLink: Long? = null,
            var registerType: String? = null,
            var register: String? = null,
            var blind: Boolean? = null,
            var represent: Boolean? = null,
            var note: String? = null,
            var reason: String? = null,
            var serviceCondition: String? = null,
            var timeOption: String? = null,
            var isDeleted: Boolean? = null,
            var discountRatio: Float? = null,
            var isCoupon: Boolean? = null,
            var allDays: Boolean? = null,
            var allWeeks: Boolean? = null,
            var dayOfWeeks: String? = null,
            var goodsImageList: List<GoodsImage>? = null,
            var salesType: Int? = null,//1:매장상품, 3:배송상품
            var isPacking: Boolean? = null,
            var isStore: Boolean? = null,
            var deliveryFee: Int? = null,
            var refundDeliveryFee: Int? = null,
            var deliveryAddFee: Int? = null,
            var deliveryMinPrice: Int? = null,
            var reservationMinNumber: Int? = null,
            var reservationMaxNumber: Int? = null,
            var isDeliveryPlus: Boolean? = null,
            var isDeliveryHotdeal: Boolean? = null,
            var externalUrl: String? = null,
            var optionType: Int? = null,//0:없음, 1:필수, 2:선택
            var buyableCount: Int? = null,
            var detailType: String? = null,
            var reviewPoint: Int? = null,
            var recommend: Boolean? = null) : Parcelable {
    constructor(source: Parcel) : this(
            source.readValue(Long::class.java.classLoader) as Long?,
            source.readValue(Long::class.java.classLoader) as Long?,
            source.readValue(Long::class.java.classLoader) as Long?,
            source.readValue(Long::class.java.classLoader) as Long?,
            source.readString(),
            source.readString(),
            source.readString(),
            source.readValue(Int::class.java.classLoader) as Int?,
            source.readValue(Int::class.java.classLoader) as Int?,
            source.readValue(Int::class.java.classLoader) as Int?,
            source.readValue(Int::class.java.classLoader) as Int?,
            source.readValue(Long::class.java.classLoader) as Long?,
            source.readValue(Long::class.java.classLoader) as Long?,
            source.readString(),
            source.readString(),
            source.readString(),
            source.readParcelable<GoodsAttachments>(GoodsAttachments::class.java.classLoader),
            source.readParcelable<Page2>(Page2::class.java.classLoader),
            source.readValue(Int::class.java.classLoader) as Int?,
            source.readValue(Double::class.java.classLoader) as Double?,
            source.readValue(Double::class.java.classLoader) as Double?,
            source.readValue(Long::class.java.classLoader) as Long?,
            source.readValue(Boolean::class.java.classLoader) as Boolean?,
            source.readValue(Boolean::class.java.classLoader) as Boolean?,
            source.readParcelable<GoodsCategory>(GoodsCategory::class.java.classLoader),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readValue(Long::class.java.classLoader) as Long?,
            source.readString(),
            source.readString(),
            source.readValue(Boolean::class.java.classLoader) as Boolean?,
            source.readValue(Boolean::class.java.classLoader) as Boolean?,
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readValue(Boolean::class.java.classLoader) as Boolean?,
            source.readValue(Float::class.java.classLoader) as Float?,
            source.readValue(Boolean::class.java.classLoader) as Boolean?,
            source.readValue(Boolean::class.java.classLoader) as Boolean?,
            source.readValue(Boolean::class.java.classLoader) as Boolean?,
            source.readString(),
            source.createTypedArrayList(GoodsImage.CREATOR),
            source.readValue(Int::class.java.classLoader) as Int?,
            source.readValue(Boolean::class.java.classLoader) as Boolean?,
            source.readValue(Boolean::class.java.classLoader) as Boolean?,
            source.readValue(Int::class.java.classLoader) as Int?,
            source.readValue(Int::class.java.classLoader) as Int?,
            source.readValue(Int::class.java.classLoader) as Int?,
            source.readValue(Int::class.java.classLoader) as Int?,
            source.readValue(Int::class.java.classLoader) as Int?,
            source.readValue(Int::class.java.classLoader) as Int?,
            source.readValue(Boolean::class.java.classLoader) as Boolean?,
            source.readValue(Boolean::class.java.classLoader) as Boolean?,
            source.readString(),
            source.readValue(Int::class.java.classLoader) as Int?,
            source.readValue(Int::class.java.classLoader) as Int?,
            source.readString(),
            source.readValue(Int::class.java.classLoader) as Int?,
            source.readValue(Boolean::class.java.classLoader) as Boolean?
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeValue(seqNo)
        writeValue(pageSeqNo)
        writeValue(categorySeqNo)
        writeValue(pageCategorySeqNo)
        writeString(name)
        writeString(hashtag)
        writeString(description)
        writeValue(count)
        writeValue(soldCount)
        writeValue(status)
        writeValue(main)
        writeValue(price)
        writeValue(originPrice)
        writeString(regDatetime)
        writeString(modDatetime)
        writeString(expireDatetime)
        writeParcelable(attachments, 0)
        writeParcelable(page, 0)
        writeValue(reviewCount)
        writeValue(distance)
        writeValue(avgEval)
        writeValue(rewardLuckybol)
        writeValue(isHotdeal)
        writeValue(isPlus)
        writeParcelable(category, 0)
        writeString(type)
        writeString(startTime)
        writeString(endTime)
        writeValue(rewardPrLink)
        writeString(registerType)
        writeString(register)
        writeValue(blind)
        writeValue(represent)
        writeString(note)
        writeString(reason)
        writeString(serviceCondition)
        writeString(timeOption)
        writeValue(isDeleted)
        writeValue(discountRatio)
        writeValue(isCoupon)
        writeValue(allDays)
        writeValue(allWeeks)
        writeString(dayOfWeeks)
        writeTypedList(goodsImageList)
        writeValue(salesType)
        writeValue(isPacking)
        writeValue(isStore)
        writeValue(deliveryFee)
        writeValue(refundDeliveryFee)
        writeValue(deliveryAddFee)
        writeValue(deliveryMinPrice)
        writeValue(reservationMinNumber)
        writeValue(reservationMaxNumber)
        writeValue(isDeliveryPlus)
        writeValue(isDeliveryHotdeal)
        writeString(externalUrl)
        writeValue(optionType)
        writeValue(buyableCount)
        writeString(detailType)
        writeValue(reviewPoint)
        writeValue(recommend)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Goods> = object : Parcelable.Creator<Goods> {
            override fun createFromParcel(source: Parcel): Goods = Goods(source)
            override fun newArray(size: Int): Array<Goods?> = arrayOfNulls(size)
        }
    }
}