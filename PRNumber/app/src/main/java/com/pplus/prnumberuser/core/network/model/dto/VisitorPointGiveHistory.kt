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
class VisitorPointGiveHistory(var seqNo: Long? = null,
                              var pageSeqNo: Long? = null,
                              var senderSeqNo: Long? = null,
                              var receiverSeqNo: Long? = null,
                              var type: String? = null,
                              var regDatetime: String? = null,
                              var price: Int? = null,
                              var isPayment: Boolean? = null,
                              var member:Member? = null,
                              var authCode:String? = null,
                              var echossId:String? = null,
                              var token:String? = null) : Parcelable {

}