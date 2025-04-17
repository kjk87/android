package com.pplus.prnumberuser.core.network.model.dto

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import kotlinx.parcelize.Parcelize

/**
 * Created by imac on 2018. 1. 2..
 */
@Parcelize
class Bol(var no: Long? = null,
          var user: User? = null,
          var primaryType: String? = null,
          var secondaryType: String? = null,
          var amount: String? = null,
          var regDate: String? = null,
          var subject: String? = null,
          var properties: HashMap<String, String>? = null,
          var likeCount: Int? = null,
          var targetList: List<User>? = null) : Parcelable {

}