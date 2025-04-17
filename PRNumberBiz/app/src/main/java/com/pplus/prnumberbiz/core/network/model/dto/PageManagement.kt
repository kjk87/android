package com.pplus.prnumberbiz.core.network.model.dto

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by imac on 2018. 1. 2..
 */
class PageManagement(var pageSeqNo: Long? = null,
                     var deliveryFee: Float? = null,
                     var deliveryMinPrice: Float? = null,
                     var deliveryRadius: Double? = null,
                     var opentimeList: List<OpenTime>? = null,
                     var closedList: List<CloseTime>? = null,
                     var originDesc: String? = null) : Parcelable {
    constructor(source: Parcel) : this(
            source.readValue(Long::class.java.classLoader) as Long?,
            source.readValue(Float::class.java.classLoader) as Float?,
            source.readValue(Float::class.java.classLoader) as Float?,
            source.readValue(Double::class.java.classLoader) as Double?,
            source.createTypedArrayList(OpenTime.CREATOR),
            source.createTypedArrayList(CloseTime.CREATOR),
            source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeValue(pageSeqNo)
        writeValue(deliveryFee)
        writeValue(deliveryMinPrice)
        writeValue(deliveryRadius)
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