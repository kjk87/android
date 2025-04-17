package com.pplus.prnumberuser.core.network.model.dto

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by imac on 2018. 1. 2..
 */
class VerificationMe(var order_id: String? = null,
                     var username: String? = null,
                     var phone: String? = null,
                     var birth: String? = null,
                     var gender: String? = null,
                     var unique: String? = null,
                     var di: String? = null,
                     var token: String? = null) : Parcelable {

    constructor(source: Parcel) : this(
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(order_id)
        writeString(username)
        writeString(phone)
        writeString(birth)
        writeString(gender)
        writeString(unique)
        writeString(di)
        writeString(token)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<VerificationMe> = object : Parcelable.Creator<VerificationMe> {
            override fun createFromParcel(source: Parcel): VerificationMe = VerificationMe(source)
            override fun newArray(size: Int): Array<VerificationMe?> = arrayOfNulls(size)
        }
    }
}