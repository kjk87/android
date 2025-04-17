package com.pplus.prnumberuser.core.network.model.dto

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by imac on 2018. 1. 2..
 */
class GoodsCategory(var seqNo: Long? = null,
                    var parentSeqNo: Long? = null,
                    var depth: Int? = null,
                    var sortNum: Int? = null,
                    var name: String? = null,
                    var regDatetime: String? = null,
                    var modDatetime: String? = null) : Parcelable {
    constructor(source: Parcel) : this(
            source.readValue(Long::class.java.classLoader) as Long?,
            source.readValue(Long::class.java.classLoader) as Long?,
            source.readValue(Int::class.java.classLoader) as Int?,
            source.readValue(Int::class.java.classLoader) as Int?,
            source.readString(),
            source.readString(),
            source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeValue(seqNo)
        writeValue(parentSeqNo)
        writeValue(depth)
        writeValue(sortNum)
        writeString(name)
        writeString(regDatetime)
        writeString(modDatetime)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<GoodsCategory> = object : Parcelable.Creator<GoodsCategory> {
            override fun createFromParcel(source: Parcel): GoodsCategory = GoodsCategory(source)
            override fun newArray(size: Int): Array<GoodsCategory?> = arrayOfNulls(size)
        }
    }
}