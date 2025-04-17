package com.pplus.luckybol.core.network.model.dto

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by imac on 2018. 1. 2..
 */
class MobonResult(var result_code: Int? = null,
                  var user_ids: String? = null,
                  var data: List<MobonMall>? = null,
                  var client: List<MobonBannerResult>? = null) : Parcelable {
    constructor(source: Parcel) : this(
            source.readValue(Int::class.java.classLoader) as Int?,
            source.readString(),
            source.createTypedArrayList(MobonMall.CREATOR),
            source.createTypedArrayList(MobonBannerResult.CREATOR)
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeValue(result_code)
        writeString(user_ids)
        writeTypedList(data)
        writeTypedList(client)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<MobonResult> = object : Parcelable.Creator<MobonResult> {
            override fun createFromParcel(source: Parcel): MobonResult = MobonResult(source)
            override fun newArray(size: Int): Array<MobonResult?> = arrayOfNulls(size)
        }
    }
}