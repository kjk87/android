package com.pplus.prnumberuser.core.network.model.dto

import android.net.Uri
import android.os.Parcel
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class GalleryData(var id: Long = 0,
//                  var imageUrl: String? = null,
                  var imageUri: Uri? = null,
                  var imageType: String? = null,
                  var folder: String? = null,
                  var mimeType: String? = null,
                  var size:Int? = null,
                  var orientation:Int? = null,
                  var checked: Boolean? = null,
                  var isBroken: Boolean? = null) : Parcelable {


    init {
        checked = false
        isBroken = false
    }

}