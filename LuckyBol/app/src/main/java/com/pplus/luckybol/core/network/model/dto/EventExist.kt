package com.pplus.luckybol.core.network.model.dto

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by imac on 2018. 1. 2..
 */
class EventExist(var join: Boolean? = null,
                 var win: Boolean? = null) : Parcelable {
    constructor(source: Parcel) : this(
            source.readValue(Boolean::class.java.classLoader) as Boolean?,
            source.readValue(Boolean::class.java.classLoader) as Boolean?
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeValue(join)
        writeValue(win)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<EventExist> = object : Parcelable.Creator<EventExist> {
            override fun createFromParcel(source: Parcel): EventExist = EventExist(source)
            override fun newArray(size: Int): Array<EventExist?> = arrayOfNulls(size)
        }
    }
}