package com.pplus.prnumberbiz.core.network.model.dto

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import java.util.*

/**
 * Created by imac on 2018. 1. 2..
 */
class NoticeInfo(var key: String? = null,
                 var value: String? = null,
                 var required: Boolean? = null) : Parcelable {


    constructor(source: Parcel) : this(
            source.readString(),
            source.readString(),
            source.readValue(Boolean::class.java.classLoader) as Boolean?
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(key)
        writeString(value)
        writeValue(required)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<NoticeInfo> = object : Parcelable.Creator<NoticeInfo> {
            override fun createFromParcel(source: Parcel): NoticeInfo = NoticeInfo(source)
            override fun newArray(size: Int): Array<NoticeInfo?> = arrayOfNulls(size)
        }
    }
}