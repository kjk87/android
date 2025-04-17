package com.pplus.luckybol.core.network.model.dto

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by imac on 2018. 1. 2..
 */

class GoodsLike(var memberSeqNo: Long? = null,
                var pageSeqNo: Long? = null,
                var goodsSeqNo: Long? = null,
                var goodsPriceSeqNo: Long? = null,
                var goods: Goods? = null,
                var goodsPrice: GoodsPrice? = null,
                var page: Page2? = null,
                var status: Int? = null,
                var regDatetime: String? = null) : Parcelable {
    constructor(source: Parcel) : this(source.readValue(Long::class.java.classLoader) as Long?, source.readValue(Long::class.java.classLoader) as Long?, source.readValue(Long::class.java.classLoader) as Long?, source.readValue(Long::class.java.classLoader) as Long?, source.readParcelable<Goods>(Goods::class.java.classLoader), source.readParcelable<GoodsPrice>(GoodsPrice::class.java.classLoader), source.readParcelable<Page2>(Page2::class.java.classLoader), source.readValue(Int::class.java.classLoader) as Int?, source.readString())

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeValue(memberSeqNo)
        writeValue(pageSeqNo)
        writeValue(goodsSeqNo)
        writeValue(goodsPriceSeqNo)
        writeParcelable(goods, 0)
        writeParcelable(goodsPrice, 0)
        writeParcelable(page, 0)
        writeValue(status)
        writeString(regDatetime)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<GoodsLike> = object : Parcelable.Creator<GoodsLike> {
            override fun createFromParcel(source: Parcel): GoodsLike = GoodsLike(source)
            override fun newArray(size: Int): Array<GoodsLike?> = arrayOfNulls(size)
        }
    }
}