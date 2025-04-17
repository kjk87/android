package com.pplus.luckybol.core.network.model.dto

import android.os.Parcel
import android.os.Parcelable
import java.util.*

/**
 * Created by imac on 2018. 1. 2..
 */
class EventPv(var banner: EventBanner? = null,
              var user: User? = null,
              var giveReward: Boolean? = null,
              var regDate: Date? = null) : Parcelable {
    constructor(source: Parcel) : this(
            source.readParcelable<EventBanner>(EventBanner::class.java.classLoader),
            source.readParcelable<User>(User::class.java.classLoader),
            source.readValue(Boolean::class.java.classLoader) as Boolean?,
            source.readSerializable() as Date?
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeParcelable(banner, 0)
        writeParcelable(user, 0)
        writeValue(giveReward)
        writeSerializable(regDate)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<EventPv> = object : Parcelable.Creator<EventPv> {
            override fun createFromParcel(source: Parcel): EventPv = EventPv(source)
            override fun newArray(size: Int): Array<EventPv?> = arrayOfNulls(size)
        }
    }
}