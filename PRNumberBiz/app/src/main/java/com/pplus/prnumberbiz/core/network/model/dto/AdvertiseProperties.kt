package com.pplus.prnumberbiz.core.network.model.dto

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by imac on 2018. 1. 2..
 */
class AdvertiseProperties(var discountType: String? = null,
                          var discount: String? = null) : Parcelable {
    constructor(source: Parcel) : this(
            source.readString(),
            source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(discountType)
        writeString(discount)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<AdvertiseProperties> = object : Parcelable.Creator<AdvertiseProperties> {
            override fun createFromParcel(source: Parcel): AdvertiseProperties = AdvertiseProperties(source)
            override fun newArray(size: Int): Array<AdvertiseProperties?> = arrayOfNulls(size)
        }
    }
}