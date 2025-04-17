package com.pplus.prnumberbiz.core.network.model.dto

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by imac on 2018. 1. 2..
 */
class DeliveryTotalPrice(var companySeqNo: Long? = null,
                         var payment: String? = null,
                         var price: Float? = null,
                         var count: Int? = null) : Parcelable {
    constructor(source: Parcel) : this(
            source.readValue(Long::class.java.classLoader) as Long?,
            source.readString(),
            source.readValue(Float::class.java.classLoader) as Float?,
            source.readValue(Int::class.java.classLoader) as Int?
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeValue(companySeqNo)
        writeString(payment)
        writeValue(price)
        writeValue(count)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<DeliveryTotalPrice> = object : Parcelable.Creator<DeliveryTotalPrice> {
            override fun createFromParcel(source: Parcel): DeliveryTotalPrice = DeliveryTotalPrice(source)
            override fun newArray(size: Int): Array<DeliveryTotalPrice?> = arrayOfNulls(size)
        }
    }
}