package com.pplus.prnumberuser.core.network.model.dto

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import java.util.*

/**
 * Created by imac on 2018. 1. 2..
 */
class GoodsImage(var id: Long? = null,
                 var goodsSeqNo: Long? = null,
                 var image: String? = null,
                 var array: Int? = null,
                 var type: String? = null) : Parcelable {
    constructor(source: Parcel) : this(
            source.readValue(Long::class.java.classLoader) as Long?,
            source.readValue(Long::class.java.classLoader) as Long?,
            source.readString(),
            source.readValue(Int::class.java.classLoader) as Int?,
            source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeValue(id)
        writeValue(goodsSeqNo)
        writeString(image)
        writeValue(array)
        writeString(type)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<GoodsImage> = object : Parcelable.Creator<GoodsImage> {
            override fun createFromParcel(source: Parcel): GoodsImage = GoodsImage(source)
            override fun newArray(size: Int): Array<GoodsImage?> = arrayOfNulls(size)
        }
    }
}