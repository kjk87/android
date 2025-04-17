package com.pplus.prnumberbiz.core.network.model.dto

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by imac on 2018. 1. 2..
 */
class GoodsAttachments(var images: List<String>? = null) : Parcelable {
    constructor(source: Parcel) : this(
            source.createStringArrayList()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeStringList(images)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<GoodsAttachments> = object : Parcelable.Creator<GoodsAttachments> {
            override fun createFromParcel(source: Parcel): GoodsAttachments = GoodsAttachments(source)
            override fun newArray(size: Int): Array<GoodsAttachments?> = arrayOfNulls(size)
        }
    }
}