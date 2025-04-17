package com.pplus.prnumberuser.core.network.model.dto

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import java.util.*

/**
 * Created by imac on 2018. 1. 2..
 */
class BillingData(var amount: String? = null,
                  var point: String? = null,
                  var id: String? = null) : Parcelable {
    constructor(source: Parcel) : this(
            source.readString(),
            source.readString(),
            source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(amount)
        writeString(point)
        writeString(id)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<BillingData> = object : Parcelable.Creator<BillingData> {
            override fun createFromParcel(source: Parcel): BillingData = BillingData(source)
            override fun newArray(size: Int): Array<BillingData?> = arrayOfNulls(size)
        }
    }
}