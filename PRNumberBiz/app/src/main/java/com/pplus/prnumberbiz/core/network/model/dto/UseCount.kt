package com.pplus.prnumberbiz.core.network.model.dto

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by imac on 2018. 1. 2..
 */
class UseCount(var label: String? = null, var count: Int? = null) : Parcelable {

    constructor(source: Parcel) : this(
            source.readString(),
            source.readValue(Int::class.java.classLoader) as Int?
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(label)
        writeValue(count)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<UseCount> = object : Parcelable.Creator<UseCount> {
            override fun createFromParcel(source: Parcel): UseCount = UseCount(source)
            override fun newArray(size: Int): Array<UseCount?> = arrayOfNulls(size)
        }
    }
}