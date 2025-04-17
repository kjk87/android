package com.pplus.luckybol.core.network.model.dto

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by imac on 2018. 1. 2..
 */
class LottoGift(var winnerCount: Int? = null,
                var gift: EventGift? = null) : Parcelable {
    constructor(source: Parcel) : this(
            source.readValue(Int::class.java.classLoader) as Int?,
            source.readParcelable<EventGift>(EventGift::class.java.classLoader)
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeValue(winnerCount)
        writeParcelable(gift, 0)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<LottoGift> = object : Parcelable.Creator<LottoGift> {
            override fun createFromParcel(source: Parcel): LottoGift = LottoGift(source)
            override fun newArray(size: Int): Array<LottoGift?> = arrayOfNulls(size)
        }
    }
}