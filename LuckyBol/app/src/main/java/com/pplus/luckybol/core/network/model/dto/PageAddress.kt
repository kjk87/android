package com.pplus.luckybol.core.network.model.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by imac on 2018. 1. 2..
 */
@Parcelize
class PageAddress(var seqNo: Long? = null,
                  var pageSeqNo: Long? = null,
                  var name: String? = null,
                  var postcode: String? = null,
                  var addr1: String? = null,
                  var addr2: String? = null,
                  var tel: String? = null,
                  var type: Int? = null, // 1:대표출고지, 2:대표반품교환지, 3:일반주소
                  var regDatetime: String? = null) : Parcelable {}