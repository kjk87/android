package com.pplus.prnumberbiz.core.network.model.dto

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by imac on 2018. 1. 15..
 */
class PageSellerProperties(var bizRegImgId: String? = null,
                           var bankBookImgId: String? = null) : Parcelable {
    constructor(source: Parcel) : this(
            source.readString(),
            source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(bizRegImgId)
        writeString(bankBookImgId)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<PageSellerProperties> = object : Parcelable.Creator<PageSellerProperties> {
            override fun createFromParcel(source: Parcel): PageSellerProperties = PageSellerProperties(source)
            override fun newArray(size: Int): Array<PageSellerProperties?> = arrayOfNulls(size)
        }
    }
}