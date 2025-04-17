package com.pplus.luckybol.core.network.model.dto

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by imac on 2018. 1. 2..
 */
class PgUrl(var url: String? = null,
            var orderId: String? = null) : Parcelable {
    constructor(source: Parcel) : this(
            source.readString(),
            source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(url)
        writeString(orderId)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<PgUrl> = object : Parcelable.Creator<PgUrl> {
            override fun createFromParcel(source: Parcel): PgUrl = PgUrl(source)
            override fun newArray(size: Int): Array<PgUrl?> = arrayOfNulls(size)
        }
    }
}