package com.pplus.prnumberbiz.core.network.model.dto

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import java.util.*

/**
 * Created by imac on 2018. 1. 2..
 */
class Bol(var no: Long? = null,
          var user: User? = null,
          var primaryType: String? = null,
          var secondaryType: String? = null,
          var amount: String? = null,
          var regDate: String? = null,
          var subject: String? = null,
          var properties: JsonObject? = null,
          var likeCount: Int? = null,
          var target: No? = null,
          var targetList: List<User>? = null) : Parcelable {
    constructor(source: Parcel) : this(
            source.readValue(Long::class.java.classLoader) as Long?,
            source.readParcelable<User>(User::class.java.classLoader),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            JsonParser().parse(source.readString()).asJsonObject,
            source.readValue(Int::class.java.classLoader) as Int?,
            source.readParcelable<No>(No::class.java.classLoader),
            source.createTypedArrayList(User.CREATOR)
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
        writeValue(likeCount)
        writeParcelable(target, 0)
        writeTypedList(targetList)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Bol> = object : Parcelable.Creator<Bol> {
            override fun createFromParcel(source: Parcel): Bol = Bol(source)
            override fun newArray(size: Int): Array<Bol?> = arrayOfNulls(size)
        }
    }
}