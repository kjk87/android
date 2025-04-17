package com.pplus.luckybol.core.network.model.dto

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by imac on 2018. 1. 2..
 */
class GoodsSeqNoList(
    var seqNoList: List<Long>? = null,
    var buyGoodsList: List<BuyGoods>? = null
) : Parcelable {
    constructor(source: Parcel) : this(
        ArrayList<Long>().apply { source.readList(this as List<*>, Long::class.java.classLoader) },
        source.createTypedArrayList(BuyGoods.CREATOR)
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeList(seqNoList)
        writeTypedList(buyGoodsList)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<GoodsSeqNoList> =
            object : Parcelable.Creator<GoodsSeqNoList> {
                override fun createFromParcel(source: Parcel): GoodsSeqNoList =
                    GoodsSeqNoList(source)

                override fun newArray(size: Int): Array<GoodsSeqNoList?> = arrayOfNulls(size)
            }
    }
}