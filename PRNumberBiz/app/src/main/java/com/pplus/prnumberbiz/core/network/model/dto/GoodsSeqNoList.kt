package com.pplus.prnumberbiz.core.network.model.dto

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by imac on 2018. 1. 2..
 */
class GoodsSeqNoList(var seqNoList: List<Long>? = null) : Parcelable {
    constructor(source: Parcel) : this(
            ArrayList<Long>().apply { source.readList(this, Long::class.java.classLoader) }
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeList(seqNoList)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<GoodsSeqNoList> = object : Parcelable.Creator<GoodsSeqNoList> {
            override fun createFromParcel(source: Parcel): GoodsSeqNoList = GoodsSeqNoList(source)
            override fun newArray(size: Int): Array<GoodsSeqNoList?> = arrayOfNulls(size)
        }
    }
}