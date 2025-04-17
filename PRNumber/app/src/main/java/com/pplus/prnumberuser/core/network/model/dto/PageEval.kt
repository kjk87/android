package com.pplus.prnumberuser.core.network.model.dto

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by imac on 2018. 1. 15..
 */
class PageEval(var reviewCount: Int? = null,
               var avgEval: Double? = null) : Parcelable {
    constructor(source: Parcel) : this(
            source.readValue(Int::class.java.classLoader) as Int?,
            source.readValue(Double::class.java.classLoader) as Double?
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeValue(reviewCount)
        writeValue(avgEval)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<PageEval> = object : Parcelable.Creator<PageEval> {
            override fun createFromParcel(source: Parcel): PageEval = PageEval(source)
            override fun newArray(size: Int): Array<PageEval?> = arrayOfNulls(size)
        }
    }
}