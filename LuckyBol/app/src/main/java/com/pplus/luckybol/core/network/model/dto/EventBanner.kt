package com.pplus.luckybol.core.network.model.dto

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by imac on 2018. 1. 2..
 */
class EventBanner(var bannerNo: Long? = null,
                  var title: String? = null,
                  var moveType: String? = null,
                  var moveTargetString: String? = null,
                  var image: ImgUrl? = null,
                  var pageViewCount: Int? = null,
                  var userViewCount: Int? = null,
                  var priority: Int? = null) : Parcelable {
    constructor(source: Parcel) : this(
            source.readValue(Long::class.java.classLoader) as Long?,
            source.readString(),
            source.readString(),
            source.readString(),
            source.readParcelable<ImgUrl>(ImgUrl::class.java.classLoader),
            source.readValue(Int::class.java.classLoader) as Int?,
            source.readValue(Int::class.java.classLoader) as Int?,
            source.readValue(Int::class.java.classLoader) as Int?
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeValue(bannerNo)
        writeString(title)
        writeString(moveType)
        writeString(moveTargetString)
        writeParcelable(image, 0)
        writeValue(pageViewCount)
        writeValue(userViewCount)
        writeValue(priority)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<EventBanner> = object : Parcelable.Creator<EventBanner> {
            override fun createFromParcel(source: Parcel): EventBanner = EventBanner(source)
            override fun newArray(size: Int): Array<EventBanner?> = arrayOfNulls(size)
        }
    }
}