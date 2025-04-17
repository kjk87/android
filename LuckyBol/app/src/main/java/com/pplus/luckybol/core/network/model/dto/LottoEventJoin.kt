package com.pplus.luckybol.core.network.model.dto

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by imac on 2018. 2. 9..
 */
class LottoEventJoin(var joinNo: Long? = null,
                     var joinDate: String? = null,
                     var joinProp: String? = null,
                     var winCode: String? = null) : Parcelable {
    constructor(source: Parcel) : this(
            source.readValue(Long::class.java.classLoader) as Long?,
            source.readString(),
            source.readString(),
            source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeValue(joinNo)
        writeString(joinDate)
        writeString(joinProp)
        writeString(winCode)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<LottoEventJoin> = object : Parcelable.Creator<LottoEventJoin> {
            override fun createFromParcel(source: Parcel): LottoEventJoin = LottoEventJoin(source)
            override fun newArray(size: Int): Array<LottoEventJoin?> = arrayOfNulls(size)
        }
    }
}