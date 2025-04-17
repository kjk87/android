package com.pplus.luckybol.core.network.model.dto

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by imac on 2018. 1. 2..
 */
class PaymentVerify(var message: String? = null) : Parcelable {

    constructor(source: Parcel) : this(
            source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(message)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<PaymentVerify> = object : Parcelable.Creator<PaymentVerify> {
            override fun createFromParcel(source: Parcel): PaymentVerify = PaymentVerify(source)
            override fun newArray(size: Int): Array<PaymentVerify?> = arrayOfNulls(size)
        }
    }
}