package com.pplus.prnumberbiz.core.network.model.dto

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by imac on 2018. 1. 2..
 */
class DeliveryGoods(var seqNo: Long? = null,
                    var deliverySeqNo: Long? = null,
                    var name: String? = null,
                    var price: Long? = null,
                    var count: Int? = null) : Parcelable {
    constructor(source: Parcel) : this(
            source.readValue(Long::class.java.classLoader) as Long?,
            source.readValue(Long::class.java.classLoader) as Long?,
            source.readString(),
            source.readValue(Long::class.java.classLoader) as Long?,
            source.readValue(Int::class.java.classLoader) as Int?
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeValue(seqNo)
        writeValue(deliverySeqNo)
        writeString(name)
        writeValue(price)
        writeValue(count)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<DeliveryGoods> = object : Parcelable.Creator<DeliveryGoods> {
            override fun createFromParcel(source: Parcel): DeliveryGoods = DeliveryGoods(source)
            override fun newArray(size: Int): Array<DeliveryGoods?> = arrayOfNulls(size)
        }
    }
}