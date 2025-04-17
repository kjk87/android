package com.pplus.luckybol.core.network.model.dto

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by imac on 2018. 1. 2..
 */
class BuyGoodsOption(var seqNo: Long? = null,
                     var buySeqNo: Long? = null,
                     var buyGoodsSeqNo: Long? = null,
                     var goodsSeqNo: Long? = null,
                     var goodsOptionDetailSeqNo: Long? = null,
                     var amount: Int? = null,
                     var price: Int? = null,
                     var depth1: String? = null,
                     var depth2: String? = null,
                     var depth3: String? = null) : Parcelable {
    constructor(source: Parcel) : this(
            source.readValue(Long::class.java.classLoader) as Long?,
            source.readValue(Long::class.java.classLoader) as Long?,
            source.readValue(Long::class.java.classLoader) as Long?,
            source.readValue(Long::class.java.classLoader) as Long?,
            source.readValue(Long::class.java.classLoader) as Long?,
            source.readValue(Int::class.java.classLoader) as Int?,
            source.readValue(Int::class.java.classLoader) as Int?,
            source.readString(),
            source.readString(),
            source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeValue(seqNo)
        writeValue(buySeqNo)
        writeValue(buyGoodsSeqNo)
        writeValue(goodsSeqNo)
        writeValue(goodsOptionDetailSeqNo)
        writeValue(amount)
        writeValue(price)
        writeString(depth1)
        writeString(depth2)
        writeString(depth3)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<BuyGoodsOption> = object : Parcelable.Creator<BuyGoodsOption> {
            override fun createFromParcel(source: Parcel): BuyGoodsOption = BuyGoodsOption(source)
            override fun newArray(size: Int): Array<BuyGoodsOption?> = arrayOfNulls(size)
        }
    }
}