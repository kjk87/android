package com.pplus.luckybol.core.network.model.dto


import android.os.Parcel
import android.os.Parcelable

/**
 * Created by 김종경 on 2016-10-04.
 */

class Category(var no: Long? = null,
               var depth: Int = 0,
               var name: String? = null,
               var iconUrl: String? = null,
               var priority: Long? = null,
               var iconImageUrl: String? = null,
               var parent: No? = null,
               var status: String? = null,
               var type: String? = null,
               var uuid: String? = null) : Parcelable {
    override fun equals(o: Any?): Boolean {

        if (o == null) return false

        return if (o is Category) {
            o.no == no
        } else {
            false
        }
    }

    constructor(source: Parcel) : this(
            source.readValue(Long::class.java.classLoader) as Long?,
            source.readInt(),
            source.readString(),
            source.readString(),
            source.readValue(Long::class.java.classLoader) as Long?,
            source.readString(),
            source.readParcelable<No>(No::class.java.classLoader),
            source.readString(),
            source.readString(),
            source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeValue(no)
        writeInt(depth)
        writeString(name)
        writeString(iconUrl)
        writeValue(priority)
        writeString(iconImageUrl)
        writeParcelable(parent, 0)
        writeString(status)
        writeString(type)
        writeString(uuid)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Category> = object : Parcelable.Creator<Category> {
            override fun createFromParcel(source: Parcel): Category = Category(source)
            override fun newArray(size: Int): Array<Category?> = arrayOfNulls(size)
        }
    }
}
