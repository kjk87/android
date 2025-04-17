package com.lejel.wowbox.push

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by imac on 2018. 1. 2..
 */
@Parcelize
class PushReceiveData(var msgNo: String? = null,
                      var title: String? = null,
                      var contents: String? = null,
                      var moveType1: String? = null,
                      var moveType2: String? = null,
                      var moveTarget: String? = null,
                      var move_target_string: String? = null,
                      var image_path: String? = null,
                      var image_path1: String? = null) : Parcelable {
}