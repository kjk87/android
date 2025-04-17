package com.pplus.prnumberbiz.core.network.model.dto

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by imac on 2018. 1. 2..
 */
class DateData(var year: String? = null,
               var month: String? = null,
               var day: String? = null,
               var hour: String? = null,
               var min: String? = null) : Parcelable {
    constructor(source: Parcel) : this(
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(year)
        writeString(month)
        writeString(day)
        writeString(hour)
        writeString(min)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<DateData> = object : Parcelable.Creator<DateData> {
            override fun createFromParcel(source: Parcel): DateData = DateData(source)
            override fun newArray(size: Int): Array<DateData?> = arrayOfNulls(size)
        }
    }
}