package com.pplus.prnumberbiz.core.network.model.dto

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by imac on 2018. 1. 15..
 */
class PageSeller(var memberSeqNo: Long? = null,
                 var pageSeqNo: Long? = null,
                 var bizEmail: String? = null,
                 var bizBankCode: String? = null,
                 var bizBankBookNo: String? = null,
                 var bizBankBookOwner: String? = null,
                 var bizName: String? = null,
                 var bizOwner: String? = null,
                 var bizRegNo: String? = null,
                 var bizAddress: String? = null,
                 var bizType: String? = null,
                 var bizCategory: String? = null,
                 var bizCancelMsg: String? = null,
                 var bizPayRatio: String? = null,
                 var isSeller: Boolean? = null,
                 var isTermsAccept: Boolean? = null,
                 var bizProp: PageSellerProperties? = null,
                 var regDatetime: String? = null,
                 var modDatetime: String? = null) : Parcelable {
    constructor(source: Parcel) : this(
            source.readValue(Long::class.java.classLoader) as Long?,
            source.readValue(Long::class.java.classLoader) as Long?,
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readValue(Boolean::class.java.classLoader) as Boolean?,
            source.readValue(Boolean::class.java.classLoader) as Boolean?,
            source.readParcelable<PageSellerProperties>(PageSellerProperties::class.java.classLoader),
            source.readString(),
            source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeValue(memberSeqNo)
        writeValue(pageSeqNo)
        writeString(bizEmail)
        writeString(bizBankCode)
        writeString(bizBankBookNo)
        writeString(bizBankBookOwner)
        writeString(bizName)
        writeString(bizOwner)
        writeString(bizRegNo)
        writeString(bizAddress)
        writeString(bizType)
        writeString(bizCategory)
        writeString(bizCancelMsg)
        writeString(bizPayRatio)
        writeValue(isSeller)
        writeValue(isTermsAccept)
        writeParcelable(bizProp, 0)
        writeString(regDatetime)
        writeString(modDatetime)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<PageSeller> = object : Parcelable.Creator<PageSeller> {
            override fun createFromParcel(source: Parcel): PageSeller = PageSeller(source)
            override fun newArray(size: Int): Array<PageSeller?> = arrayOfNulls(size)
        }
    }
}