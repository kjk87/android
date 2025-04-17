package com.pplus.luckybol.core.network.model.dto

import android.os.Parcel
import android.os.Parcelable
import java.util.*

/**
 * Created by imac on 2018. 1. 2..
 */
class EventUv(var banner: EventBanner? = null,
              var user: User? = null,
              var pageViewNo: Long? = null,
              var regDate: Date? = null) : Parcelable {
    constructor(source: Parcel) : this(
            source.readParcelable<EventBanner>(EventBanner::class.java.classLoader),
            source.readParcelable<User>(User::class.java.classLoader),
            source.readValue(Long::class.java.classLoader) as Long?,
            source.readSerializable() as Date?
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeParcelable(banner, 0)
        writeParcelable(user, 0)
        writeValue(pageViewNo)
        writeSerializable(regDate)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<EventUv> = object : Parcelable.Creator<EventUv> {
            override fun createFromParcel(source: Parcel): EventUv = EventUv(source)
            override fun newArray(size: Int): Array<EventUv?> = arrayOfNulls(size)
        }
    }
}