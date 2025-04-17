package com.pplus.prnumberbiz.core.network.model.dto


import android.os.Parcel
import android.os.Parcelable

/**
 * Created by kjk on 2017. 6. 20..
 */

class Price(var price: Int? = null) : Parcelable {
    constructor(source: Parcel) : this(
            source.readValue(Int::class.java.classLoader) as Int?
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeValue(price)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Price> = object : Parcelable.Creator<Price> {
            override fun createFromParcel(source: Parcel): Price = Price(source)
            override fun newArray(size: Int): Array<Price?> = arrayOfNulls(size)
        }
    }
}
