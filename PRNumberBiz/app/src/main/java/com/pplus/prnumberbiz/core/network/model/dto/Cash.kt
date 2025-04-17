package com.pplus.prnumberbiz.core.network.model.dto

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import java.util.*

/**
 * Created by imac on 2018. 1. 2..
 */
class Cash(var no: Long? = null,
           var user: No? = null,
           var primaryType: String? = null,
           var secondaryType: String? = null,
           var amount: String? = null,
           var regDate: String? = null,
           var subject: String? = null,
           var properties: JsonObject? = null,
           var paymentProperties: CashBillingProperties? = null,
           var target: No? = null) : Parcelable {
    constructor(source: Parcel) : this(
            source.readValue(Long::class.java.classLoader) as Long?,
            source.readParcelable<No>(No::class.java.classLoader),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            JsonParser().parse(source.readString()).asJsonObject,
            source.readParcelable<CashBillingProperties>(CashBillingProperties::class.java.classLoader),
            source.readParcelable<No>(No::class.java.classLoader)
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeValue(no)
        writeParcelable(user, 0)
        writeString(primaryType)
        writeString(secondaryType)
        writeString(amount)
        writeString(regDate)
        writeString(subject)
        if(properties != null){
            writeString(properties.toString())
        }else{
            writeString("{}")
        }
        writeParcelable(paymentProperties, 0)
        writeParcelable(target, 0)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Cash> = object : Parcelable.Creator<Cash> {
            override fun createFromParcel(source: Parcel): Cash = Cash(source)
            override fun newArray(size: Int): Array<Cash?> = arrayOfNulls(size)
        }
    }
}