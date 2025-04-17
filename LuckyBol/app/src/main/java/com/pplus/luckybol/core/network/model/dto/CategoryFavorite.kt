package com.pplus.luckybol.core.network.model.dto


import android.os.Parcel
import android.os.Parcelable

/**
 * Created by 김종경 on 2016-10-04.
 */

class CategoryFavorite(var id: Long? = null,
                       var memberSeqNo: Long? = null,
                       var categoryMinorSeqNo: Long? = null,
                       var categoryMajorSeqNo: Long? = null,
                       var categoryMinor: CategoryMinor? = null) : Parcelable {
    override fun equals(other: Any?): Boolean {
        if (other == null) return false

        return if (other is CategoryFavorite) {
            other.id == id
        } else {
            false
        }
    }

    constructor(source: Parcel) : this(
            source.readValue(Long::class.java.classLoader) as Long?,
            source.readValue(Long::class.java.classLoader) as Long?,
            source.readValue(Long::class.java.classLoader) as Long?,
            source.readValue(Long::class.java.classLoader) as Long?,
            source.readParcelable<CategoryMinor>(CategoryMinor::class.java.classLoader)
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeValue(id)
        writeValue(memberSeqNo)
        writeValue(categoryMinorSeqNo)
        writeValue(categoryMajorSeqNo)
        writeParcelable(categoryMinor, 0)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<CategoryFavorite> = object : Parcelable.Creator<CategoryFavorite> {
            override fun createFromParcel(source: Parcel): CategoryFavorite = CategoryFavorite(source)
            override fun newArray(size: Int): Array<CategoryFavorite?> = arrayOfNulls(size)
        }
    }
}
