package com.pplus.prnumberbiz.core.network.model.dto

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import java.util.*

/**
 * Created by imac on 2018. 1. 2..
 */
class Agent(var no: Long? = null,
            var name: String? = null,
            var contractStart: String? = null,
            var contractEnd: String? = null,
            var status: String? = null,
            var phone: String? = null,
            var zipCode: String? = null,
            var baseAddr: String? = null,
            var detailAddr: String? = null,
            var homepage: String? = null,
            var chargeName: String? = null,
            var chargeOrg: String? = null,
            var chargeId: String? = null,
            var chargeEmail: String? = null,
            var chargeMobile: String? = null,
            var code: String? = null) : Parcelable {
    constructor(source: Parcel) : this(
            source.readValue(Long::class.java.classLoader) as Long?,
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
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
        writeValue(no)
        writeString(name)
        writeString(contractStart)
        writeString(contractEnd)
        writeString(status)
        writeString(phone)
        writeString(zipCode)
        writeString(baseAddr)
        writeString(detailAddr)
        writeString(homepage)
        writeString(chargeName)
        writeString(chargeOrg)
        writeString(chargeId)
        writeString(chargeEmail)
        writeString(chargeMobile)
        writeString(code)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Agent> = object : Parcelable.Creator<Agent> {
            override fun createFromParcel(source: Parcel): Agent = Agent(source)
            override fun newArray(size: Int): Array<Agent?> = arrayOfNulls(size)
        }
    }
}