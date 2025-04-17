package com.pplus.luckybol.core.network.model.dto

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by imac on 2018. 1. 2..
 */
class EventGroup(var no: Long? = null,
                 var title: String? = null) : Parcelable {
    constructor(source: Parcel) : this(
            source.readValue(Long::class.java.classLoader) as Long?,
            source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeValue(no)
        writeString(title)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<EventGroup> = object : Parcelable.Creator<EventGroup> {
            override fun createFromParcel(source: Parcel): EventGroup = EventGroup(source)
            override fun newArray(size: Int): Array<EventGroup?> = arrayOfNulls(size)
        }
    }
}