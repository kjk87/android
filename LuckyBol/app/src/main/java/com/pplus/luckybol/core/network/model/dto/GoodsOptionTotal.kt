package com.pplus.luckybol.core.network.model.dto


import android.os.Parcel
import android.os.Parcelable

/**
 * Created by kjk on 2017. 6. 20..
 */

class GoodsOptionTotal(var goodsOptionList: List<GoodsOption>? = null,
                       var goodsOptionItemList: List<GoodsOptionItem>? = null,
                       var goodsOptionDetailList: List<GoodsOptionDetail>? = null) : Parcelable {
    constructor(source: Parcel) : this(
            source.createTypedArrayList(GoodsOption.CREATOR),
            source.createTypedArrayList(GoodsOptionItem.CREATOR),
            source.createTypedArrayList(GoodsOptionDetail.CREATOR)
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeTypedList(goodsOptionList)
        writeTypedList(goodsOptionItemList)
        writeTypedList(goodsOptionDetailList)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<GoodsOptionTotal> = object : Parcelable.Creator<GoodsOptionTotal> {
            override fun createFromParcel(source: Parcel): GoodsOptionTotal = GoodsOptionTotal(source)
            override fun newArray(size: Int): Array<GoodsOptionTotal?> = arrayOfNulls(size)
        }
    }
}
