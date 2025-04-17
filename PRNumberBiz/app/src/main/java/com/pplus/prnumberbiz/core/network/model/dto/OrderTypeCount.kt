package com.pplus.prnumberbiz.core.network.model.dto

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by imac on 2018. 1. 2..
 */
class OrderTypeCount(var payCount: Float? = null,
                     var wrapCount: Float? = null,
                     var deliveryCount: Float? = null) : Parcelable {
    constructor(source: Parcel) : this(
            source.readValue(Float::class.java.classLoader) as Float?,
            source.readValue(Float::class.java.classLoader) as Float?,
            source.readValue(Float::class.java.classLoader) as Float?
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeValue(payCount)
        writeValue(wrapCount)
        writeValue(deliveryCount)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<OrderTypeCount> = object : Parcelable.Creator<OrderTypeCount> {
            override fun createFromParcel(source: Parcel): OrderTypeCount = OrderTypeCount(source)
            override fun newArray(size: Int): Array<OrderTypeCount?> = arrayOfNulls(size)
        }
    }
}