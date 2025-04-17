package com.pplus.prnumberbiz.core.network.model.dto

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by imac on 2018. 1. 2..
 */
class HashTag(var key: String? = null,
              var value: String? = null) : Parcelable {

    constructor(source: Parcel) : this(
            source.readString(),
            source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(key)
        writeString(value)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<HashTag> = object : Parcelable.Creator<HashTag> {
            override fun createFromParcel(source: Parcel): HashTag = HashTag(source)
            override fun newArray(size: Int): Array<HashTag?> = arrayOfNulls(size)
        }
    }
}