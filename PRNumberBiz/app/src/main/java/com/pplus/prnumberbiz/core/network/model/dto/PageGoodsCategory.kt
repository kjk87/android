package com.pplus.prnumberbiz.core.network.model.dto

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by imac on 2018. 1. 2..
 */
class PageGoodsCategory(var seqNo: Long? = null,
                        var pageSeqNo: Long? = null,
                        var goodsCategory: GoodsCategory? = null,
                        var goodsCount: Int? = null) : Parcelable {
    constructor(source: Parcel) : this(
            source.readValue(Long::class.java.classLoader) as Long?,
            source.readValue(Long::class.java.classLoader) as Long?,
            source.readParcelable<GoodsCategory>(GoodsCategory::class.java.classLoader),
            source.readValue(Int::class.java.classLoader) as Int?
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeValue(seqNo)
        writeValue(pageSeqNo)
        writeParcelable(goodsCategory, 0)
        writeValue(goodsCount)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<PageGoodsCategory> = object : Parcelable.Creator<PageGoodsCategory> {
            override fun createFromParcel(source: Parcel): PageGoodsCategory = PageGoodsCategory(source)
            override fun newArray(size: Int): Array<PageGoodsCategory?> = arrayOfNulls(size)
        }
    }
}