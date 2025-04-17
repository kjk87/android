package com.pplus.prnumberuser.core.network.model.dto

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by imac on 2018. 2. 9..
 */
class Lotto(var seqNo: Long? = null,
            var lottoTimes: String? = null,
            var lottoPrevTimes: String? = null,
            var winCode: String? = null,
            var joinLuckybol: Int? = null,
            var lottoLuckybol: Int? = null,
            var url1: String? = null,
            var selector1: String? = null,
            var success1: Boolean? = null,
            var url2: String? = null,
            var selector2: String? = null,
            var success2: Boolean? = null,
            var url3: String? = null,
            var selector3: String? = null,
            var success3: Boolean? = null,
            var modDatetime: String? = null) : Parcelable {
    constructor(source: Parcel) : this(
            source.readValue(Long::class.java.classLoader) as Long?,
            source.readString(),
            source.readString(),
            source.readString(),
            source.readValue(Int::class.java.classLoader) as Int?,
            source.readValue(Int::class.java.classLoader) as Int?,
            source.readString(),
            source.readString(),
            source.readValue(Boolean::class.java.classLoader) as Boolean?,
            source.readString(),
            source.readString(),
            source.readValue(Boolean::class.java.classLoader) as Boolean?,
            source.readString(),
            source.readString(),
            source.readValue(Boolean::class.java.classLoader) as Boolean?,
            source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeValue(seqNo)
        writeString(lottoTimes)
        writeString(lottoPrevTimes)
        writeString(winCode)
        writeValue(joinLuckybol)
        writeValue(lottoLuckybol)
        writeString(url1)
        writeString(selector1)
        writeValue(success1)
        writeString(url2)
        writeString(selector2)
        writeValue(success2)
        writeString(url3)
        writeString(selector3)
        writeValue(success3)
        writeString(modDatetime)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Lotto> = object : Parcelable.Creator<Lotto> {
            override fun createFromParcel(source: Parcel): Lotto = Lotto(source)
            override fun newArray(size: Int): Array<Lotto?> = arrayOfNulls(size)
        }
    }
}