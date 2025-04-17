package com.pplus.luckybol.core.network.model.dto


import android.os.Parcel
import android.os.Parcelable

/**
 * Created by 김종경 on 2016-10-04.
 */

class CategoryMajor(var seqNo: Long? = null,
                    var name: String? = null,
                    var type: String? = null,
                    var status: String? = null,
                    var array: Int? = null,
                    var regDatetime: String? = null,
                    var modDatetime: String? = null,
                    var register: String? = null,
                    var updater: String? = null,
                    var minorList: List<CategoryMinor>? = null) : Parcelable {
    constructor(source: Parcel) : this(
            source.readValue(Long::class.java.classLoader) as Long?,
            source.readString(),
            source.readString(),
            source.readString(),
            source.readValue(Int::class.java.classLoader) as Int?,
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.createTypedArrayList(CategoryMinor.CREATOR)
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeValue(seqNo)
        writeString(name)
        writeString(type)
        writeString(status)
        writeValue(array)
        writeString(regDatetime)
        writeString(modDatetime)
        writeString(register)
        writeString(updater)
        writeTypedList(minorList)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<CategoryMajor> = object : Parcelable.Creator<CategoryMajor> {
            override fun createFromParcel(source: Parcel): CategoryMajor = CategoryMajor(source)
            override fun newArray(size: Int): Array<CategoryMajor?> = arrayOfNulls(size)
        }
    }
}
