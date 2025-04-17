package com.pplus.luckybol.core.network.model.dto


import android.os.Parcel
import android.os.Parcelable

/**
 * Created by 김종경 on 2016-10-04.
 */

class CategoryMinor(var seqNo: Long? = null,
                    var major: Long? = null,
                    var name: String? = null,
                    var status: String? = null,
                    var array: Int? = null,
                    var regDatetime: String? = null,
                    var modDatetime: String? = null,
                    var register: String? = null,
                    var updater: String? = null) : Parcelable {

    override fun equals(other: Any?): Boolean {
        if (other == null) return false

        return if (other is CategoryMinor) {
            other.seqNo == seqNo
        } else {
            false
        }
    }

    constructor(source: Parcel) : this(
            source.readValue(Long::class.java.classLoader) as Long?,
            source.readValue(Long::class.java.classLoader) as Long?,
            source.readString(),
            source.readString(),
            source.readValue(Int::class.java.classLoader) as Int?,
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeValue(seqNo)
        writeValue(major)
        writeString(name)
        writeString(status)
        writeValue(array)
        writeString(regDatetime)
        writeString(modDatetime)
        writeString(register)
        writeString(updater)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<CategoryMinor> = object : Parcelable.Creator<CategoryMinor> {
            override fun createFromParcel(source: Parcel): CategoryMinor = CategoryMinor(source)
            override fun newArray(size: Int): Array<CategoryMinor?> = arrayOfNulls(size)
        }
    }
}
