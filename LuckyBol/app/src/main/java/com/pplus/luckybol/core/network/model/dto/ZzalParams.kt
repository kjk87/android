package com.pplus.luckybol.core.network.model.dto

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by imac on 2018. 1. 2..
 */
class ZzalParams(var advertiseID: String? = null,
                 var participateID: String? = null,
                 var actionResult: Int? = null) : Parcelable {
    constructor(source: Parcel) : this(
            source.readString(),
            source.readString(),
            source.readValue(Int::class.java.classLoader) as Int?
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(advertiseID)
        writeString(participateID)
        writeValue(actionResult)
    }

    override fun toString(): String {
        return "ZzalParams(advertiseID=$advertiseID, participateID=$participateID, actionResult=$actionResult)"
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<ZzalParams> = object : Parcelable.Creator<ZzalParams> {
            override fun createFromParcel(source: Parcel): ZzalParams = ZzalParams(source)
            override fun newArray(size: Int): Array<ZzalParams?> = arrayOfNulls(size)
        }
    }


}