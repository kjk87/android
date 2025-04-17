package com.pplus.luckybol.core.network.model.dto

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by imac on 2018. 1. 2..
 */
class Juso(var name: String? = null,
           var value: String? = null,
           var engname: String? = null) : Parcelable {
    constructor(source: Parcel) : this(
            source.readString(),
            source.readString(),
            source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(name)
        writeString(value)
        writeString(engname)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Juso> = object : Parcelable.Creator<Juso> {
            override fun createFromParcel(source: Parcel): Juso = Juso(source)
            override fun newArray(size: Int): Array<Juso?> = arrayOfNulls(size)
        }
    }
}