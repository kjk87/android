package com.pplus.prnumberbiz.core.network.model.dto

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by imac on 2018. 1. 2..
 */
class CouponTemplate(var no: Long? = null,
                     var name: String? = null,
                     var note: String? = null,
                     var duration: Duration? = null,
                     var downloadLimit: Int? = null,
                     var discountType: String? = null,
                     var discount: String? = null,
                     var condition: String? = null,
                     var status: String? = null,
                     var downloadCount: Long? = null,
                     var giftCount: Long? = null,
                     var useCount: Long? = null,
                     var regDate: String? = null,
                     var publisherType: String? = null,
                     var publisher: Page? = null,
                     var icon: ImgUrl? = null,
                     var display: Boolean = false,
                     var givePlus: Boolean = false,
                     var type: String? = null,
                     var lastAdvertise: Advertise? = null) : Parcelable {

    constructor(source: Parcel) : this(
            source.readValue(Long::class.java.classLoader) as Long?,
            source.readString(),
            source.readString(),
            source.readParcelable<Duration>(Duration::class.java.classLoader),
            source.readValue(Int::class.java.classLoader) as Int?,
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readValue(Long::class.java.classLoader) as Long?,
            source.readValue(Long::class.java.classLoader) as Long?,
            source.readValue(Long::class.java.classLoader) as Long?,
            source.readString(),
            source.readString(),
            source.readParcelable<Page>(Page::class.java.classLoader),
            source.readParcelable<ImgUrl>(ImgUrl::class.java.classLoader),
            1 == source.readInt(),
            1 == source.readInt(),
            source.readString(),
            source.readParcelable<Advertise>(Advertise::class.java.classLoader)
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeValue(no)
        writeString(name)
        writeString(note)
        writeParcelable(duration, 0)
        writeValue(downloadLimit)
        writeString(discountType)
        writeString(discount)
        writeString(condition)
        writeString(status)
        writeValue(downloadCount)
        writeValue(giftCount)
        writeValue(useCount)
        writeString(regDate)
        writeString(publisherType)
        writeParcelable(publisher, 0)
        writeParcelable(icon, 0)
        writeInt((if (display) 1 else 0))
        writeInt((if (givePlus) 1 else 0))
        writeString(type)
        writeParcelable(lastAdvertise, 0)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<CouponTemplate> = object : Parcelable.Creator<CouponTemplate> {
            override fun createFromParcel(source: Parcel): CouponTemplate = CouponTemplate(source)
            override fun newArray(size: Int): Array<CouponTemplate?> = arrayOfNulls(size)
        }
    }
}