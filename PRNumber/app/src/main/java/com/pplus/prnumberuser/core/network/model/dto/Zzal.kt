package com.pplus.prnumberuser.core.network.model.dto

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by imac on 2018. 1. 2..
 */
class Zzal(var url: String? = null,
           var advertiseID: String? = null) : Parcelable {

    constructor(source: Parcel) : this(
            source.readString(),
            source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(url)
        writeString(advertiseID)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Zzal> = object : Parcelable.Creator<Zzal> {
            override fun createFromParcel(source: Parcel): Zzal = Zzal(source)
            override fun newArray(size: Int): Array<Zzal?> = arrayOfNulls(size)
        }
    }
}