package com.pplus.prnumberbiz.core.network.model.dto

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by imac on 2018. 1. 2..
 */
class PRNumber2(var virtualNumber: VirtualNumber? = null) : Parcelable {
    constructor(source: Parcel) : this(
            source.readParcelable<VirtualNumber>(VirtualNumber::class.java.classLoader)
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeParcelable(virtualNumber, 0)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<PRNumber2> = object : Parcelable.Creator<PRNumber2> {
            override fun createFromParcel(source: Parcel): PRNumber2 = PRNumber2(source)
            override fun newArray(size: Int): Array<PRNumber2?> = arrayOfNulls(size)
        }
    }
}