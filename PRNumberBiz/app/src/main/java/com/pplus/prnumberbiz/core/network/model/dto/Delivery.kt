package com.pplus.prnumberbiz.core.network.model.dto

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by imac on 2018. 1. 2..
 */
class Delivery(var seqNo: Long? = null,
               var agentSeqNo: Long? = null,
               var companySeqNo: Long? = null,
               var pageSeqNo: Long? = null,
               var deliveryGoodsList: List<DeliveryGoods>? = null,
               var id: String? = null,
               var regDatetime: String? = null,
               var modDatetime: String? = null,
               var clientAddress: String? = null,
               var clientTel: String? = null,
               var clientMemo: String? = null,
               var totalPrice: Float? = null,
               var payment: String? = null) : Parcelable {
    constructor(source: Parcel) : this(
            source.readValue(Long::class.java.classLoader) as Long?,
            source.readValue(Long::class.java.classLoader) as Long?,
            source.readValue(Long::class.java.classLoader) as Long?,
            source.readValue(Long::class.java.classLoader) as Long?,
            source.createTypedArrayList(DeliveryGoods.CREATOR),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readValue(Float::class.java.classLoader) as Float?,
            source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeValue(seqNo)
        writeValue(agentSeqNo)
        writeValue(companySeqNo)
        writeValue(pageSeqNo)
        writeTypedList(deliveryGoodsList)
        writeString(id)
        writeString(regDatetime)
        writeString(modDatetime)
        writeString(clientAddress)
        writeString(clientTel)
        writeString(clientMemo)
        writeValue(totalPrice)
        writeString(payment)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Delivery> = object : Parcelable.Creator<Delivery> {
            override fun createFromParcel(source: Parcel): Delivery = Delivery(source)
            override fun newArray(size: Int): Array<Delivery?> = arrayOfNulls(size)
        }
    }
}