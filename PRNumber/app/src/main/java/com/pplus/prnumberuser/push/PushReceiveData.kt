package com.pplus.prnumberuser.push

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by imac on 2018. 1. 2..
 */
class PushReceiveData(var msgNo: String? = null,
                      var title: String? = null,
                      var contents: String? = null,
                      var moveType1: String? = null,
                      var moveType2: String? = null,
                      var moveTarget: String? = null,
                      var move_target_string: String? = null,
                      var image_path: String? = null,
                      var image_path1: String? = null) : Parcelable {
    constructor(source: Parcel) : this(
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(msgNo)
        writeString(title)
        writeString(contents)
        writeString(moveType1)
        writeString(moveType2)
        writeString(moveTarget)
        writeString(move_target_string)
        writeString(image_path)
        writeString(image_path1)
    }

    override fun toString(): String {
        return "PushReceiveData(msgNo=$msgNo, title=$title, contents=$contents, moveType1=$moveType1, moveType2=$moveType2, moveTarget=$moveTarget, move_target_string=$move_target_string, image_path=$image_path, image_path1=$image_path1)"
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<PushReceiveData> = object : Parcelable.Creator<PushReceiveData> {
            override fun createFromParcel(source: Parcel): PushReceiveData = PushReceiveData(source)
            override fun newArray(size: Int): Array<PushReceiveData?> = arrayOfNulls(size)
        }
    }
}