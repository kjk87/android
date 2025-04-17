package com.pplus.prnumberuser.core.network.model.dto

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.JsonObject
import com.google.gson.JsonParser

/**
 * Created by imac on 2018. 1. 2..
 */
class GoodsProperties(var origin_price: Long? = null) : Parcelable {
    constructor(source: Parcel) : this(
            source.readValue(Long::class.java.classLoader) as Long?
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeValue(origin_price)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<GoodsProperties> = object : Parcelable.Creator<GoodsProperties> {
            override fun createFromParcel(source: Parcel): GoodsProperties = GoodsProperties(source)
            override fun newArray(size: Int): Array<GoodsProperties?> = arrayOfNulls(size)
        }
    }
}