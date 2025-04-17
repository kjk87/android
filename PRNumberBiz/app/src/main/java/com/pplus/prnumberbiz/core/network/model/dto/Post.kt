package com.pplus.prnumberbiz.core.network.model.dto

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by imac on 2018. 1. 2..
 */
class Post(var no: Long? = null,
           var subject: String? = null,
           var priority: Int? = null,
           var viewCount: Int? = null,
           var blind: Boolean = false,
           var type: String? = null,
           var commentCount: Int? = null,
           var imageCount: Int? = null,
           var contents: String? = null,
           var attachList: List<Attachment>? = null,
           var regDate: String? = null,
           var articleUrl: String? = null,
           var properties: PostProperties? = null,
           var author: User? = null,
           var board: No? = null,
           var page: Page? = null,
           var lastAdvertise: Advertise? = null) : Parcelable {

    override fun equals(other: Any?): Boolean {
        if (other == null) return false

        return if (other is Post) {
            other.no == no
        } else {
            false
        }
    }

    constructor(source: Parcel) : this(
            source.readValue(Long::class.java.classLoader) as Long?,
            source.readString(),
            source.readValue(Int::class.java.classLoader) as Int?,
            source.readValue(Int::class.java.classLoader) as Int?,
            1 == source.readInt(),
            source.readString(),
            source.readValue(Int::class.java.classLoader) as Int?,
            source.readValue(Int::class.java.classLoader) as Int?,
            source.readString(),
            source.createTypedArrayList(Attachment.CREATOR),
            source.readString(),
            source.readString(),
            source.readParcelable<PostProperties>(PostProperties::class.java.classLoader),
            source.readParcelable<User>(User::class.java.classLoader),
            source.readParcelable<No>(No::class.java.classLoader),
            source.readParcelable<Page>(Page::class.java.classLoader),
            source.readParcelable<Advertise>(Advertise::class.java.classLoader)
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeValue(no)
        writeString(subject)
        writeValue(priority)
        writeValue(viewCount)
        writeInt((if (blind) 1 else 0))
        writeString(type)
        writeValue(commentCount)
        writeValue(imageCount)
        writeString(contents)
        writeTypedList(attachList)
        writeString(regDate)
        writeString(articleUrl)
        writeParcelable(properties, 0)
        writeParcelable(author, 0)
        writeParcelable(board, 0)
        writeParcelable(page, 0)
        writeParcelable(lastAdvertise, 0)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Post> = object : Parcelable.Creator<Post> {
            override fun createFromParcel(source: Parcel): Post = Post(source)
            override fun newArray(size: Int): Array<Post?> = arrayOfNulls(size)
        }
    }
}