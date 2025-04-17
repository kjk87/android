package com.pplus.prnumberuser.core.network.model.dto

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import java.util.*

/**
 * Created by imac on 2018. 1. 2..
 */
class PageImage(var id: Long? = null,
                var pageSeqNo: Long? = null,
                var image: String? = null,
                var array: Int? = null) : Parcelable {
    constructor(source: Parcel) : this(
            source.readValue(Long::class.java.classLoader) as Long?,
            source.readValue(Long::class.java.classLoader) as Long?,
            source.readString(),
            source.readValue(Int::class.java.classLoader) as Int?
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeValue(id)
        writeValue(pageSeqNo)
        writeString(image)
        writeValue(array)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<PageImage> = object : Parcelable.Creator<PageImage> {
            override fun createFromParcel(source: Parcel): PageImage = PageImage(source)
            override fun newArray(size: Int): Array<PageImage?> = arrayOfNulls(size)
        }
    }
}