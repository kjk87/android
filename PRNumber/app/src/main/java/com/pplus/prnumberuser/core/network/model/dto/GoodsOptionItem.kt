package com.pplus.prnumberuser.core.network.model.dto


import android.os.Parcel
import android.os.Parcelable

/**
 * Created by kjk on 2017. 6. 20..
 */

class GoodsOptionItem(var seqNo: Long? = null,
                      var goodsSeqNo: Long? = null,
                      var optionSeqNo: Long? = null,
                      var item: String? = null) : Parcelable {
    constructor(source: Parcel) : this(
            source.readValue(Long::class.java.classLoader) as Long?,
            source.readValue(Long::class.java.classLoader) as Long?,
            source.readValue(Long::class.java.classLoader) as Long?,
            source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeValue(seqNo)
        writeValue(goodsSeqNo)
        writeValue(optionSeqNo)
        writeString(item)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<GoodsOptionItem> = object : Parcelable.Creator<GoodsOptionItem> {
            override fun createFromParcel(source: Parcel): GoodsOptionItem = GoodsOptionItem(source)
            override fun newArray(size: Int): Array<GoodsOptionItem?> = arrayOfNulls(size)
        }
    }
}
