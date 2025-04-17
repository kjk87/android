package com.pplus.prnumberuser.core.network.model.dto

import android.os.Parcel
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by j2n on 2016. 9. 29..
 */ //{"no":1003332,"targetType":"postList","originName":"temp_20180205_170010.png","filePath":"/storage/pplus/postList/2018/07/30","fileName":"1532938588016_447969.png","fileSize":446372,"extension":"png","url":"http://thc.mindwareworks.com/files/pplus/postList/2018/07/30/1532938588016_447969.png"}
@Parcelize
class Attachment : Parcelable {
    var no: Long? = null
    var targetNo: Long? = null
    var targetType: String? = null
    var originName: String? = null
    var filePath: String? = null
    var fileName: String? = null
    var fileSize: String? = null
    var extension: String? = null
    var url: String? = null
    var width = 0
    var height = 0
    var rotate = 0
    var id: String? = null
    override fun toString(): String {
        return "Attachment{no=$no, targetNo=$targetNo, targetType='$targetType', originName='$originName', filePath='$filePath', fileName='$fileName', fileSize='$fileSize', extension='$extension', url='$url', width=$width, height=$height, rotate=$rotate}"
    }

}