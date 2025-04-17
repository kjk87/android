package com.pplus.luckybol.core.network.model.dto

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by imac on 2018. 1. 2..
 */
class ServerStatus(var resultCode: Int? = null,
                   var message: String? = null,
                   var duration: String? = null) : Parcelable {
    constructor(source: Parcel) : this(
            source.readValue(Int::class.java.classLoader) as Int?,
            source.readString(),
            source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeValue(resultCode)
        writeString(message)
        writeString(duration)
    }

    override fun toString(): String {
        return "ServerStatus(resultCode=$resultCode, message=$message, duration=$duration)"
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<ServerStatus> = object : Parcelable.Creator<ServerStatus> {
            override fun createFromParcel(source: Parcel): ServerStatus = ServerStatus(source)
            override fun newArray(size: Int): Array<ServerStatus?> = arrayOfNulls(size)
        }
    }


}