package com.pplus.prnumberbiz.core.network.model.dto

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by imac on 2018. 1. 15..
 */
class Member(var seqNo: Long? = null,
             var memberName: String? = null,
             var profileAttachment: Attachment? = null,
             var nickname: String? = null) : Parcelable {
    override fun equals(o: Any?): Boolean {

        if (o == null) return false

        return if (o is Member) {
            o.seqNo == seqNo
        } else {
            false
        }
    }

    constructor(source: Parcel) : this(
            source.readValue(Long::class.java.classLoader) as Long?,
            source.readString(),
            source.readParcelable<Attachment>(Attachment::class.java.classLoader),
            source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeValue(seqNo)
        writeString(memberName)
        writeParcelable(profileAttachment, 0)
        writeString(nickname)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Member> = object : Parcelable.Creator<Member> {
            override fun createFromParcel(source: Parcel): Member = Member(source)
            override fun newArray(size: Int): Array<Member?> = arrayOfNulls(size)
        }
    }
}