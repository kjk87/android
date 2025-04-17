package com.pplus.luckybol.core.network.model.dto


import android.os.Parcel
import android.os.Parcelable

/**
 * Created by kjk on 2017. 6. 20..
 */

class PageProperties(var openingHours: String? = null,
                     var parkingInfo: String? = null,
                     var convenienceInfo: String? = null,
                     var kakaoId: String? = null,
                     var email: String? = null,
                     var homepageUrl: String? = null,
                     var blogUrl: String? = null,
                     var multiPost: Boolean? = null,
                     var emptyNumberCause: String? = null) : Parcelable {

    constructor(source: Parcel) : this(
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readValue(Boolean::class.java.classLoader) as Boolean?,
            source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(openingHours)
        writeString(parkingInfo)
        writeString(convenienceInfo)
        writeString(kakaoId)
        writeString(email)
        writeString(homepageUrl)
        writeString(blogUrl)
        writeValue(multiPost)
        writeString(emptyNumberCause)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<PageProperties> = object : Parcelable.Creator<PageProperties> {
            override fun createFromParcel(source: Parcel): PageProperties = PageProperties(source)
            override fun newArray(size: Int): Array<PageProperties?> = arrayOfNulls(size)
        }
    }
}
