package com.pplus.prnumberbiz.core.network.model.dto

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by imac on 2018. 1. 2..
 */
class BuyGoodsTypePrice(var generalPrice: Float? = null,
                        var hotdealPrice: Float? = null,
                        var plusPrice: Float? = null,
                        var totalPrice: Float? = null) : Parcelable {
    constructor(source: Parcel) : this(
            source.readValue(Float::class.java.classLoader) as Float?,
            source.readValue(Float::class.java.classLoader) as Float?,
            source.readValue(Float::class.java.classLoader) as Float?,
            source.readValue(Float::class.java.classLoader) as Float?
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeValue(generalPrice)
        writeValue(hotdealPrice)
        writeValue(plusPrice)
        writeValue(totalPrice)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<BuyGoodsTypePrice> = object : Parcelable.Creator<BuyGoodsTypePrice> {
            override fun createFromParcel(source: Parcel): BuyGoodsTypePrice = BuyGoodsTypePrice(source)
            override fun newArray(size: Int): Array<BuyGoodsTypePrice?> = arrayOfNulls(size)
        }
    }
}