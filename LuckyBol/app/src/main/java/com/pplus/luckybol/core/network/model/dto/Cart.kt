package com.pplus.luckybol.core.network.model.dto

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by imac on 2018. 1. 2..
 */
class Cart(var seqNo: Long? = null,
           var memberSeqNo: Long? = null,
           var goodsSeqNo: Long? = null,
           var goods: Goods? = null,
           var count: Int? = null,
           var goodsProp: CartProperties? = null,
           var price: Long? = null,
           var vat: Long? = null,
           var regDatetime: String? = null,
           var modDatetime: String? = null,
           var check: Boolean = false) : Parcelable {
    constructor(source: Parcel) : this(
            source.readValue(Long::class.java.classLoader) as Long?,
            source.readValue(Long::class.java.classLoader) as Long?,
            source.readValue(Long::class.java.classLoader) as Long?,
            source.readParcelable<Goods>(Goods::class.java.classLoader),
            source.readValue(Int::class.java.classLoader) as Int?,
            source.readParcelable<CartProperties>(CartProperties::class.java.classLoader),
            source.readValue(Long::class.java.classLoader) as Long?,
            source.readValue(Long::class.java.classLoader) as Long?,
            source.readString(),
            source.readString(),
            1 == source.readInt()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeValue(seqNo)
        writeValue(memberSeqNo)
        writeValue(goodsSeqNo)
        writeParcelable(goods, 0)
        writeValue(count)
        writeParcelable(goodsProp, 0)
        writeValue(price)
        writeValue(vat)
        writeString(regDatetime)
        writeString(modDatetime)
        writeInt((if (check) 1 else 0))
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Cart> = object : Parcelable.Creator<Cart> {
            override fun createFromParcel(source: Parcel): Cart = Cart(source)
            override fun newArray(size: Int): Array<Cart?> = arrayOfNulls(size)
        }
    }
}