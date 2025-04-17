package com.pplus.prnumberuser.core.network.model.dto

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import java.util.*

/**
 * Created by imac on 2018. 1. 2..
 */
class Province(var id: Long? = null,
               var doname: String? = null,
               var dosubname: String? = null,
               var docode: String? = null) : Parcelable {
    constructor(source: Parcel) : this(
            source.readValue(Long::class.java.classLoader) as Long?,
            source.readString(),
            source.readString(),
            source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeValue(id)
        writeString(doname)
        writeString(dosubname)
        writeString(docode)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Province> = object : Parcelable.Creator<Province> {
            override fun createFromParcel(source: Parcel): Province = Province(source)
            override fun newArray(size: Int): Array<Province?> = arrayOfNulls(size)
        }
    }
}