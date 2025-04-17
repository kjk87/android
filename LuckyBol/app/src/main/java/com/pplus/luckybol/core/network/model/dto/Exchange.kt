package com.pplus.luckybol.core.network.model.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by imac on 2018. 1. 2..
 */
@Parcelize
class Exchange(var seqNo: Long? = null,
               var memberSeqNo: Long? = null,
               var bankTransferSeqNo: Long? = null,
//               var bol: Long? = null,
               var point: Long? = null,
               var cash: Long? = null,
               var refundCash: Long? = null,
               var status: Int? = null,
               var bankName: String? = null,
               var bankAccountId: String? = null,
               var bankAccountHolderName: String? = null,
               var regDatetime: String? = null,
               var modDatetime: String? = null) : Parcelable {

}