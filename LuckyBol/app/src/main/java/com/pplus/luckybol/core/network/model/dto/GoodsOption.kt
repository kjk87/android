package com.pplus.luckybol.core.network.model.dto


import android.os.Parcel
import android.os.Parcelable

/**
 * Created by kjk on 2017. 6. 20..
 */

class GoodsOption(var seqNo: Long? = null,
                  var goodsSeqNo: Long? = null,
                  var name: String? = null) : Parcelable {
    constructor(source: Parcel) : this(
            source.readValue(Long::class.java.classLoader) as Long?,
            source.readValue(Long::class.java.classLoader) as Long?,
            source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeValue(seqNo)
        writeValue(goodsSeqNo)
        writeString(name)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<GoodsOption> = object : Parcelable.Creator<GoodsOption> {
            override fun createFromParcel(source: Parcel): GoodsOption = GoodsOption(source)
            override fun newArray(size: Int): Array<GoodsOption?> = arrayOfNulls(size)
        }
    }
}
