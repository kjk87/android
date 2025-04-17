package com.pplus.prnumberuser.core.network.model.dto

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by imac on 2018. 1. 2..
 */
class UserHashTag(var memberSeqNo: Long? = null,
                  var hashtag: String? = null) : Parcelable {

    constructor(source: Parcel) : this(
            source.readValue(Long::class.java.classLoader) as Long?,
            source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeValue(memberSeqNo)
        writeString(hashtag)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<UserHashTag> = object : Parcelable.Creator<UserHashTag> {
            override fun createFromParcel(source: Parcel): UserHashTag = UserHashTag(source)
            override fun newArray(size: Int): Array<UserHashTag?> = arrayOfNulls(size)
        }
    }
}