package com.pplus.prnumberbiz.core.network.model.dto

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by imac on 2018. 1. 2..
 */
class BuyGoods(var seqNo: Long? = null,
               var buySeqNo: Long? = null,
               var memberSeqNo: Long? = null,
               var goodsSeqNo: Long? = null,
               var goods: Goods? = null,
               var count: Int? = null,
               var memo: String? = null,
               var price: Long? = null,
               var goodsPrice: Long? = null,
               var vat: Long? = null,
               var process: Int? = null,//0 :  대기, 1:승인 2:승인후 취소, 3:사용, 4:사용후환불, -1: 우리서버 에러, -2: PG 에러
               var regDatetime: String? = null,
               var modDatetime: String? = null,
               var payDatetime: String? = null,
               var cancelDatetime: String? = null,
               var useDatetime: String? = null,
               var refundDatetime: String? = null,
               var expireDatetime: String? = null,
               var buy: Buy? = null,
               var buycol: String? = null,
               var pointRatio: Float? = null,
               var commissionRatio: Float? = null,
               var savedPoint: Int? = null,
               var agentSeqNo: Long? = null,
               var isPaymentPoint: Boolean? = null,
               var title: String? = null,
               var serviceCondition: String? = null,
               var timeOption: String? = null) : Parcelable {

    constructor(source: Parcel) : this(
            source.readValue(Long::class.java.classLoader) as Long?,
            source.readValue(Long::class.java.classLoader) as Long?,
            source.readValue(Long::class.java.classLoader) as Long?,
            source.readValue(Long::class.java.classLoader) as Long?,
            source.readParcelable<Goods>(Goods::class.java.classLoader),
            source.readValue(Int::class.java.classLoader) as Int?,
            source.readString(),
            source.readValue(Long::class.java.classLoader) as Long?,
            source.readValue(Long::class.java.classLoader) as Long?,
            source.readValue(Long::class.java.classLoader) as Long?,
            source.readValue(Int::class.java.classLoader) as Int?,
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readParcelable<Buy>(Buy::class.java.classLoader),
            source.readString(),
            source.readValue(Float::class.java.classLoader) as Float?,
            source.readValue(Float::class.java.classLoader) as Float?,
            source.readValue(Int::class.java.classLoader) as Int?,
            source.readValue(Long::class.java.classLoader) as Long?,
            source.readValue(Boolean::class.java.classLoader) as Boolean?,
            source.readString(),
            source.readString(),
            source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeValue(seqNo)
        writeValue(buySeqNo)
        writeValue(memberSeqNo)
        writeValue(goodsSeqNo)
        writeParcelable(goods, 0)
        writeValue(count)
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
        writeParcelable(buy, 0)
        writeString(buycol)
        writeValue(pointRatio)
        writeValue(commissionRatio)
        writeValue(savedPoint)
        writeValue(agentSeqNo)
        writeValue(isPaymentPoint)
        writeString(title)
        writeString(serviceCondition)
        writeString(timeOption)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<BuyGoods> = object : Parcelable.Creator<BuyGoods> {
            override fun createFromParcel(source: Parcel): BuyGoods = BuyGoods(source)
            override fun newArray(size: Int): Array<BuyGoods?> = arrayOfNulls(size)
        }
    }
}