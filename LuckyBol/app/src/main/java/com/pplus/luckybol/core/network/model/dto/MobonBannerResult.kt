package com.pplus.luckybol.core.network.model.dto

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by imac on 2018. 1. 2..
 */
class MobonBannerResult(var target: String? = null,
                        var length: Int? = null,
                        var bntype: String? = null,
                        var data: List<MobonBanner>? = null) : Parcelable {
    constructor(source: Parcel) : this(
            source.readString(),
            source.readValue(Int::class.java.classLoader) as Int?,
            source.readString(),
            source.createTypedArrayList(MobonBanner.CREATOR)
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(target)
        writeValue(length)
        writeString(bntype)
        writeTypedList(data)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<MobonBannerResult> = object : Parcelable.Creator<MobonBannerResult> {
            override fun createFromParcel(source: Parcel): MobonBannerResult = MobonBannerResult(source)
            override fun newArray(size: Int): Array<MobonBannerResult?> = arrayOfNulls(size)
        }
    }
}