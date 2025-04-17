package com.pplus.prnumberbiz.core.network.model.dto

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.JsonObject
import com.google.gson.JsonParser

/**
 * Created by imac on 2018. 1. 2..
 */
class GoodsReview(var seqNo: Long? = null,
                  var memberSeqNo: Long? = null,
                  var goodsSeqNo: Long? = null,
                  var pageSeqNo: Long? = null,
                  var buyGoodsSeqNo: Long? = null,
                  var buySeqNo: Long? = null,
                  var goods: Goods? = null,
                  var review: String? = null,
                  var reviewReply: String? = null,
                  var reviewReplyDate: String? = null,
                  var eval: Int? = null,
                  var regDatetime: String? = null,
                  var modDatetime: String? = null,
                  var attachments: GoodsAttachments? = null,
                  var member: Member? = null,
                  var page: Page2? = null) : Parcelable {

    constructor(source: Parcel) : this(
            source.readValue(Long::class.java.classLoader) as Long?,
            source.readValue(Long::class.java.classLoader) as Long?,
            source.readValue(Long::class.java.classLoader) as Long?,
            source.readValue(Long::class.java.classLoader) as Long?,
            source.readValue(Long::class.java.classLoader) as Long?,
            source.readValue(Long::class.java.classLoader) as Long?,
            source.readParcelable<Goods>(Goods::class.java.classLoader),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readValue(Int::class.java.classLoader) as Int?,
            source.readString(),
            source.readString(),
            source.readParcelable<GoodsAttachments>(GoodsAttachments::class.java.classLoader),
            source.readParcelable<Member>(Member::class.java.classLoader),
            source.readParcelable<Page2>(Page2::class.java.classLoader)
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeValue(seqNo)
        writeValue(memberSeqNo)
        writeValue(goodsSeqNo)
        writeValue(pageSeqNo)
        writeValue(buyGoodsSeqNo)
        writeValue(buySeqNo)
        writeParcelable(goods, 0)
        writeString(review)
        writeString(reviewReply)
        writeString(reviewReplyDate)
        writeValue(eval)
        writeString(regDatetime)
        writeString(modDatetime)
        writeParcelable(attachments, 0)
        writeParcelable(member, 0)
        writeParcelable(page, 0)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<GoodsReview> = object : Parcelable.Creator<GoodsReview> {
            override fun createFromParcel(source: Parcel): GoodsReview = GoodsReview(source)
            override fun newArray(size: Int): Array<GoodsReview?> = arrayOfNulls(size)
        }
    }
}