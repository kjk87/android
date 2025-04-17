package com.pplus.prnumberbiz.core.network.model.dto

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by imac on 2018. 1. 2..
 */
class Advertise(var no: Long? = null,
                var type: String? = null,
                var status: String? = null,
                var duration: Duration? = null,
                var totalCount: Int? = null,
                var currentCount: Int? = null,
                var cost: Long? = null,
                var serviceReward: Int? = null,
                var reward: Long? = null,
                var like: Boolean? = null,
                var contactCount: Int? = null,
                var likeCount: Int? = null,
                var regDate: String? = null,
                var article: Post? = null,
                var template: CouponTemplate? = null,
                var baseCost: Int? = null,
                var free: Boolean? = null,
                var properties: AdvertiseProperties? = null) : Parcelable {
    override fun equals(other: Any?): Boolean {
        if (other == null) return false

        return if (other is Advertise) {
            other.no == no
        } else {
            false
        }
    }

    constructor(source: Parcel) : this(
            source.readValue(Long::class.java.classLoader) as Long?,
            source.readString(),
            source.readString(),
            source.readParcelable<Duration>(Duration::class.java.classLoader),
            source.readValue(Int::class.java.classLoader) as Int?,
            source.readValue(Int::class.java.classLoader) as Int?,
            source.readValue(Long::class.java.classLoader) as Long?,
            source.readValue(Int::class.java.classLoader) as Int?,
            source.readValue(Long::class.java.classLoader) as Long?,
            source.readValue(Boolean::class.java.classLoader) as Boolean?,
            source.readValue(Int::class.java.classLoader) as Int?,
            source.readValue(Int::class.java.classLoader) as Int?,
            source.readString(),
            source.readParcelable<Post>(Post::class.java.classLoader),
            source.readParcelable<CouponTemplate>(CouponTemplate::class.java.classLoader),
            source.readValue(Int::class.java.classLoader) as Int?,
            source.readValue(Boolean::class.java.classLoader) as Boolean?,
            source.readParcelable<AdvertiseProperties>(AdvertiseProperties::class.java.classLoader)
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeValue(no)
        writeString(type)
        writeString(status)
        writeParcelable(duration, 0)
        writeValue(totalCount)
        writeValue(currentCount)
        writeValue(cost)
        writeValue(serviceReward)
        writeValue(reward)
        writeValue(like)
        writeValue(contactCount)
        writeValue(likeCount)
        writeString(regDate)
        writeParcelable(article, 0)
        writeParcelable(template, 0)
        writeValue(baseCost)
        writeValue(free)
        writeParcelable(properties, 0)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Advertise> = object : Parcelable.Creator<Advertise> {
            override fun createFromParcel(source: Parcel): Advertise = Advertise(source)
            override fun newArray(size: Int): Array<Advertise?> = arrayOfNulls(size)
        }
    }
}