package com.pplus.prnumberuser.core.network.model.dto

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by imac on 2018. 1. 2..
 */
class CommissionPoint(var id: Long? = null,
                      var commission: Float? = null,
                      var point: Float? = null,
                      var card: Float? = null,
                      var updateDate: String? = null,
                      var woodongyi: Boolean? = null) : Parcelable {
    constructor(source: Parcel) : this(
            source.readValue(Long::class.java.classLoader) as Long?,
            source.readValue(Float::class.java.classLoader) as Float?,
            source.readValue(Float::class.java.classLoader) as Float?,
            source.readValue(Float::class.java.classLoader) as Float?,
            source.readString(),
            source.readValue(Boolean::class.java.classLoader) as Boolean?
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeValue(id)
        writeValue(commission)
        writeValue(point)
        writeValue(card)
        writeString(updateDate)
        writeValue(woodongyi)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<CommissionPoint> = object : Parcelable.Creator<CommissionPoint> {
            override fun createFromParcel(source: Parcel): CommissionPoint = CommissionPoint(source)
            override fun newArray(size: Int): Array<CommissionPoint?> = arrayOfNulls(size)
        }
    }
}