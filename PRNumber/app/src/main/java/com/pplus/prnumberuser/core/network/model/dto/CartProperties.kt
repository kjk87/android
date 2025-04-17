package com.pplus.prnumberuser.core.network.model.dto

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.JsonObject
import com.google.gson.JsonParser

/**
 * Created by imac on 2018. 1. 2..
 */
class CartProperties() : Parcelable {
    constructor(source: Parcel) : this(
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {}

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<CartProperties> = object : Parcelable.Creator<CartProperties> {
            override fun createFromParcel(source: Parcel): CartProperties = CartProperties(source)
            override fun newArray(size: Int): Array<CartProperties?> = arrayOfNulls(size)
        }
    }
}