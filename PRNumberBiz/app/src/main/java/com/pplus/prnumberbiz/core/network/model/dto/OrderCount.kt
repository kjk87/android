package com.pplus.prnumberbiz.core.network.model.dto

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by imac on 2018. 1. 2..
 */
class OrderCount(var readyCount: Int? = null,
                 var ingCount: Int? = null,
                 var completeCount: Int? = null) : Parcelable {

    constructor(source: Parcel) : this(
            source.readValue(Int::class.java.classLoader) as Int?,
            source.readValue(Int::class.java.classLoader) as Int?,
            source.readValue(Int::class.java.classLoader) as Int?
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeValue(readyCount)
        writeValue(ingCount)
        writeValue(completeCount)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<OrderCount> = object : Parcelable.Creator<OrderCount> {
            override fun createFromParcel(source: Parcel): OrderCount = OrderCount(source)
            override fun newArray(size: Int): Array<OrderCount?> = arrayOfNulls(size)
        }
    }
}