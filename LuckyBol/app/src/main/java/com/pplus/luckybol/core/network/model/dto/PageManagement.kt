package com.pplus.luckybol.core.network.model.dto

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by imac on 2018. 1. 2..
 */
class PageManagement(var pageSeqNo: Long? = null,
                     var deliveryRadius: Float? = null,
                     var deliveryFee: Float? = null,
                     var deliveryMinPrice: Double? = null,
                     var opentimeList: List<PageOpentime>? = null,
                     var closedList: List<PageClosed>? = null,
                     var originDesc: String? = null) : Parcelable {
    constructor(source: Parcel) : this(
            source.readValue(Long::class.java.classLoader) as Long?,
            source.readValue(Float::class.java.classLoader) as Float?,
            source.readValue(Float::class.java.classLoader) as Float?,
            source.readValue(Double::class.java.classLoader) as Double?,
            source.createTypedArrayList(PageOpentime.CREATOR),
            source.createTypedArrayList(PageClosed.CREATOR),
            source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeValue(pageSeqNo)
        writeValue(deliveryRadius)
        writeValue(deliveryFee)
        writeValue(deliveryMinPrice)
        writeTypedList(opentimeList)
        writeTypedList(closedList)
        writeString(originDesc)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<PageManagement> = object : Parcelable.Creator<PageManagement> {
            override fun createFromParcel(source: Parcel): PageManagement = PageManagement(source)
            override fun newArray(size: Int): Array<PageManagement?> = arrayOfNulls(size)
        }
    }
}