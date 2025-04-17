package com.pplus.luckybol.core.network.model.dto

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by imac on 2018. 1. 2..
 */
class VirtualNumber(var virtualNumber: String? = null,
                    var type: String? = null,
                    var reserved: String? = null,
                    var actionSource: String? = null,
                    var actorLoginId: String? = null,
                    var deleted: String? = null) : Parcelable {
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
        writeString(virtualNumber)
        writeString(type)
        writeString(reserved)
        writeString(actionSource)
        writeString(actorLoginId)
        writeString(deleted)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<VirtualNumber> = object : Parcelable.Creator<VirtualNumber> {
            override fun createFromParcel(source: Parcel): VirtualNumber = VirtualNumber(source)
            override fun newArray(size: Int): Array<VirtualNumber?> = arrayOfNulls(size)
        }
    }
}