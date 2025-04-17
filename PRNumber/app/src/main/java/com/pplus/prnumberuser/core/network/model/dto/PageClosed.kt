package com.pplus.prnumberuser.core.network.model.dto

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by imac on 2018. 1. 2..
 */
class PageClosed(var seqNo: Long? = null,
                 var pageSeqNo: Long? = null,
                 var everyWeek: Int? = null,
                 var weekDay: String? = null) : Parcelable {
    constructor(source: Parcel) : this(
            source.readValue(Long::class.java.classLoader) as Long?,
            source.readValue(Long::class.java.classLoader) as Long?,
            source.readValue(Int::class.java.classLoader) as Int?,
            source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeValue(seqNo)
        writeValue(pageSeqNo)
        writeValue(everyWeek)
        writeString(weekDay)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<PageClosed> = object : Parcelable.Creator<PageClosed> {
            override fun createFromParcel(source: Parcel): PageClosed = PageClosed(source)
            override fun newArray(size: Int): Array<PageClosed?> = arrayOfNulls(size)
        }
    }
}