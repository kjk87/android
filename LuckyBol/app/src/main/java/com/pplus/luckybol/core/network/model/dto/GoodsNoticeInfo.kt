package com.pplus.luckybol.core.network.model.dto

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by imac on 2018. 1. 2..
 */
class GoodsNoticeInfo(var seqNo: Long? = null,
                      var pageSeqNo: Long? = null,
                      var goodsSeqNo: Long? = null,
                      var infoPropList: List<NoticeInfo>? = null,
                      var category: String? = null,
                      var regDatetime: String? = null) : Parcelable {

    constructor(source: Parcel) : this(
            source.readValue(Long::class.java.classLoader) as Long?,
            source.readValue(Long::class.java.classLoader) as Long?,
            source.readValue(Long::class.java.classLoader) as Long?,
            source.createTypedArrayList(NoticeInfo.CREATOR),
            source.readString(),
            source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeValue(seqNo)
        writeValue(pageSeqNo)
        writeValue(goodsSeqNo)
        writeTypedList(infoPropList)
        writeString(category)
        writeString(regDatetime)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<GoodsNoticeInfo> = object : Parcelable.Creator<GoodsNoticeInfo> {
            override fun createFromParcel(source: Parcel): GoodsNoticeInfo = GoodsNoticeInfo(source)
            override fun newArray(size: Int): Array<GoodsNoticeInfo?> = arrayOfNulls(size)
        }
    }
}