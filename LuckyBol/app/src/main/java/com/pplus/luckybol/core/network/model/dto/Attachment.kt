package com.pplus.luckybol.core.network.model.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by j2n on 2016. 9. 29..
 */
@Parcelize
class Attachment(var no: Long? = null,
                 var targetNo: Long? = null,
                 var targetType: String? = null,
                 var originName: String? = null,
                 var filePath: String? = null,
                 var fileName: String? = null,
                 var fileSize: String? = null,
                 var extension: String? = null,
                 var url: String? = null,
                 var width: Int = 0,
                 var height: Int = 0,
                 var rotate: Int = 0,
                 var id: String? = null) : Parcelable {

    override fun toString(): String {
        return "Attachment{no=$no, targetNo=$targetNo, targetType='$targetType', originName='$originName', filePath='$filePath', fileName='$fileName', fileSize='$fileSize', extension='$extension', url='$url', width=$width, height=$height, rotate=$rotate}"
    }

    override fun describeContents(): Int {
        return 0
    }

}