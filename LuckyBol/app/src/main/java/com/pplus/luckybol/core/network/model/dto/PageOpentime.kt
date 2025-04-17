package com.pplus.luckybol.core.network.model.dto

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by imac on 2018. 1. 2..
 */
class PageOpentime(var seqNo: Long? = null,
                   var pageSeqNo: Long? = null,
                   var type: Int? = null,
                   var weekDay: String? = null,
                   var startTime: String? = null,
                   var endTime: String? = null,
                   var nextDay: Boolean? = null) : Parcelable {
    constructor(source: Parcel) : this(
            source.readValue(Long::class.java.classLoader) as Long?,
            source.readValue(Long::class.java.classLoader) as Long?,
            source.readValue(Int::class.java.classLoader) as Int?,
            source.readString(),
            source.readString(),
            source.readString(),
            source.readValue(Boolean::class.java.classLoader) as Boolean?
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeValue(seqNo)
        writeValue(pageSeqNo)
        writeValue(type)
        writeString(weekDay)
        writeString(startTime)
        writeString(endTime)
        writeValue(nextDay)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<PageOpentime> = object : Parcelable.Creator<PageOpentime> {
            override fun createFromParcel(source: Parcel): PageOpentime = PageOpentime(source)
            override fun newArray(size: Int): Array<PageOpentime?> = arrayOfNulls(size)
        }
    }
}