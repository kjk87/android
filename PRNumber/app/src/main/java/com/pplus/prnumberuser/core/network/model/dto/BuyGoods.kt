package com.pplus.prnumberuser.core.network.model.dto

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by imac on 2018. 1. 2..
 */
class BuyGoods(var seqNo: Long? = null,
               var buySeqNo: Long? = null,
               var memberSeqNo: Long? = null,
               var pageSeqNo: Long? = null,
               var goodsSeqNo: Long? = null,
               var goodsPriceSeqNo: Long? = null,
               var goods: Goods? = null,
               var page: Page2? = null,
               var count: Int? = null,
               var goodsProp: CartProperties? = null,
               var memo: String? = null,
               var price: Long? = null,
               var goodsPrice: Long? = null,
               var vat: Long? = null,
               var process: Int? = null, //0 :  대기, 1:승인 2:승인후 취소, 3:사용, 4:사용후환불, -1: 우리서버 에러, -2: PG 에러
               var regDatetime: String? = null,
               var modDatetime: String? = null,
               var payDatetime: String? = null,
               var cancelDatetime: String? = null,
               var useDatetime: String? = null,
               var refundDatetime: String? = null,
               var expireDatetime: String? = null,
               var reviewCount: Int? = null,
               var buy: Buy? = null,
               var isReviewExist: Boolean? = null,
               var check: Boolean? = null,
               var buycol: String? = null,
               var pointRatio: Float? = null,
               var commissionRatio: Float? = null,
               var savedPoint: Int? = null,
               var agentSeqNo: Long? = null,
               var isPaymentPoint: Boolean? = null,
               var title: String? = null,
               var serviceCondition: String? = null,
               var timeOption: String? = null,
               var allDays: Boolean? = null,
               var allWeeks: Boolean? = null,
               var dayOfWeeks: String? = null,
               var startTime: String? = null,
               var endTime: String? = null,
               var optionPrice: Int? = null,
               var deliveryFee: Int? = null,
               var transportNumber: String? = null,
               var receiverName: String? = null,
               var receiverTel: String? = null,
               var receiverPostCode: String? = null,
               var receiverAddress: String? = null,
               var deliveryMemo: String? = null,
               var orderProcess: Int? = null,
               var reviewPoint: Int? = null,
               var deliveryStartDatetime: String? = null,
               var deliveryCompleteDatetime: String? = null,
               var shippingCompany: String? = null,
               var shippingCompanyCode: String? = null,
               var completeDatetime: String? = null,
               var goodsPriceData: GoodsPrice? = null,
               var buyGoodsOptionList: List<BuyGoodsOption>? = null,
               var buyGoodsOptionSelectList: List<BuyGoodsOption>? = null) : Parcelable {
    constructor(source: Parcel) : this(source.readValue(Long::class.java.classLoader) as Long?, source.readValue(Long::class.java.classLoader) as Long?, source.readValue(Long::class.java.classLoader) as Long?, source.readValue(Long::class.java.classLoader) as Long?, source.readValue(Long::class.java.classLoader) as Long?, source.readValue(Long::class.java.classLoader) as Long?, source.readParcelable<Goods>(Goods::class.java.classLoader), source.readParcelable<Page2>(Page2::class.java.classLoader), source.readValue(Int::class.java.classLoader) as Int?, source.readParcelable<CartProperties>(CartProperties::class.java.classLoader), source.readString(), source.readValue(Long::class.java.classLoader) as Long?, source.readValue(Long::class.java.classLoader) as Long?, source.readValue(Long::class.java.classLoader) as Long?, source.readValue(Int::class.java.classLoader) as Int?, source.readString(), source.readString(), source.readString(), source.readString(), source.readString(), source.readString(), source.readString(), source.readValue(Int::class.java.classLoader) as Int?, source.readParcelable<Buy>(Buy::class.java.classLoader), source.readValue(Boolean::class.java.classLoader) as Boolean?, source.readValue(Boolean::class.java.classLoader) as Boolean?, source.readString(), source.readValue(Float::class.java.classLoader) as Float?, source.readValue(Float::class.java.classLoader) as Float?, source.readValue(Int::class.java.classLoader) as Int?, source.readValue(Long::class.java.classLoader) as Long?, source.readValue(Boolean::class.java.classLoader) as Boolean?, source.readString(), source.readString(), source.readString(), source.readValue(Boolean::class.java.classLoader) as Boolean?, source.readValue(Boolean::class.java.classLoader) as Boolean?, source.readString(), source.readString(), source.readString(), source.readValue(Int::class.java.classLoader) as Int?, source.readValue(Int::class.java.classLoader) as Int?, source.readString(), source.readString(), source.readString(), source.readString(), source.readString(), source.readString(), source.readValue(Int::class.java.classLoader) as Int?, source.readValue(Int::class.java.classLoader) as Int?, source.readString(), source.readString(), source.readString(), source.readString(), source.readString(), source.readParcelable<GoodsPrice>(GoodsPrice::class.java.classLoader), source.createTypedArrayList(BuyGoodsOption.CREATOR), source.createTypedArrayList(BuyGoodsOption.CREATOR))

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeValue(seqNo)
        writeValue(buySeqNo)
        writeValue(memberSeqNo)
        writeValue(pageSeqNo)
        writeValue(goodsSeqNo)
        writeValue(goodsPriceSeqNo)
        writeParcelable(goods, 0)
        writeParcelable(page, 0)
        writeValue(count)
        writeParcelable(goodsProp, 0)
        writeString(memo)
        writeValue(price)
        writeValue(goodsPrice)
        writeValue(vat)
        writeValue(process)
        writeString(regDatetime)
        writeString(modDatetime)
        writeString(payDatetime)
        writeString(cancelDatetime)
        writeString(useDatetime)
        writeString(refundDatetime)
        writeString(expireDatetime)
        writeValue(reviewCount)
        writeParcelable(buy, 0)
        writeValue(isReviewExist)
        writeValue(check)
        writeString(buycol)
        writeValue(pointRatio)
        writeValue(commissionRatio)
        writeValue(savedPoint)
        writeValue(agentSeqNo)
        writeValue(isPaymentPoint)
        writeString(title)
        writeString(serviceCondition)
        writeString(timeOption)
        writeValue(allDays)
        writeValue(allWeeks)
        writeString(dayOfWeeks)
        writeString(startTime)
        writeString(endTime)
        writeValue(optionPrice)
        writeValue(deliveryFee)
        writeString(transportNumber)
        writeString(receiverName)
        writeString(receiverTel)
        writeString(receiverPostCode)
        writeString(receiverAddress)
        writeString(deliveryMemo)
        writeValue(orderProcess)
        writeValue(reviewPoint)
        writeString(deliveryStartDatetime)
        writeString(deliveryCompleteDatetime)
        writeString(shippingCompany)
        writeString(shippingCompanyCode)
        writeString(completeDatetime)
        writeParcelable(goodsPriceData, 0)
        writeTypedList(buyGoodsOptionList)
        writeTypedList(buyGoodsOptionSelectList)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<BuyGoods> = object : Parcelable.Creator<BuyGoods> {
            override fun createFromParcel(source: Parcel): BuyGoods = BuyGoods(source)
            override fun newArray(size: Int): Array<BuyGoods?> = arrayOfNulls(size)
        }
    }
}