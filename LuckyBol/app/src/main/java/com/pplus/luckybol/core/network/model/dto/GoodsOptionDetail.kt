package com.pplus.luckybol.core.network.model.dto


import android.os.Parcel
import android.os.Parcelable

/**
 * Created by kjk on 2017. 6. 20..
 */

class GoodsOptionDetail(var seqNo: Long? = null,
                        var goodsSeqNo: Long? = null,
                        var depth1ItemSeqNo: Long? = null,
                        var depth2ItemSeqNo: Long? = null,
                        var depth3ItemSeqNo: Long? = null,
                        var amount: Int? = null,
                        var soldCount: Int? = null,
                        var price: Int? = null,
                        var item1: GoodsOptionItem? = null,
                        var item2: GoodsOptionItem? = null,
                        var item3: GoodsOptionItem? = null) : Parcelable {
    constructor(source: Parcel) : this(
            source.readValue(Long::class.java.classLoader) as Long?,
            source.readValue(Long::class.java.classLoader) as Long?,
            source.readValue(Long::class.java.classLoader) as Long?,
            source.readValue(Long::class.java.classLoader) as Long?,
            source.readValue(Long::class.java.classLoader) as Long?,
            source.readValue(Int::class.java.classLoader) as Int?,
            source.readValue(Int::class.java.classLoader) as Int?,
            source.readValue(Int::class.java.classLoader) as Int?,
            source.readParcelable<GoodsOptionItem>(GoodsOptionItem::class.java.classLoader),
            source.readParcelable<GoodsOptionItem>(GoodsOptionItem::class.java.classLoader),
            source.readParcelable<GoodsOptionItem>(GoodsOptionItem::class.java.classLoader)
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeValue(seqNo)
        writeValue(goodsSeqNo)
        writeValue(depth1ItemSeqNo)
        writeValue(depth2ItemSeqNo)
        writeValue(depth3ItemSeqNo)
        writeValue(amount)
        writeValue(soldCount)
        writeValue(price)
        writeParcelable(item1, 0)
        writeParcelable(item2, 0)
        writeParcelable(item3, 0)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<GoodsOptionDetail> = object : Parcelable.Creator<GoodsOptionDetail> {
            override fun createFromParcel(source: Parcel): GoodsOptionDetail = GoodsOptionDetail(source)
            override fun newArray(size: Int): Array<GoodsOptionDetail?> = arrayOfNulls(size)
        }
    }
}
