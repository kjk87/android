package com.pplus.luckybol.core.network.model.dto

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by imac on 2018. 1. 2..
 */
class CardReq(var cardNo: String? = null,
              var expireDt: String? = null,
              var cardAuth: String? = null,
              var cardPassword: String? = null) : Parcelable {
    constructor(source: Parcel) : this(
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(cardNo)
        writeString(expireDt)
        writeString(cardAuth)
        writeString(cardPassword)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<CardReq> = object : Parcelable.Creator<CardReq> {
            override fun createFromParcel(source: Parcel): CardReq = CardReq(source)
            override fun newArray(size: Int): Array<CardReq?> = arrayOfNulls(size)
        }
    }
}