package com.pplus.luckybol.core.network.model.dto

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by imac on 2018. 1. 2..
 */
class MobonMall(var p_key: String? = null,
                var p_img: String? = null,
                var p_name: String? = null,
                var p_price: String? = null,
                var p_link: String? = null,
                var drc_link: String? = null) : Parcelable {
    constructor(source: Parcel) : this(
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(p_key)
        writeString(p_img)
        writeString(p_name)
        writeString(p_price)
        writeString(p_link)
        writeString(drc_link)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<MobonMall> = object : Parcelable.Creator<MobonMall> {
            override fun createFromParcel(source: Parcel): MobonMall = MobonMall(source)
            override fun newArray(size: Int): Array<MobonMall?> = arrayOfNulls(size)
        }
    }
}