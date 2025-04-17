package com.pplus.prnumberuser.core.network.model.dto


import android.os.Parcel
import android.os.Parcelable

/**
 * Created by kjk on 2017. 6. 20..
 */

class Count(var count: Int? = null) : Parcelable {
    constructor(source: Parcel) : this(
            source.readValue(Int::class.java.classLoader) as Int?
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeValue(count)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Count> = object : Parcelable.Creator<Count> {
            override fun createFromParcel(source: Parcel): Count = Count(source)
            override fun newArray(size: Int): Array<Count?> = arrayOfNulls(size)
        }
    }
}
