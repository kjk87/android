package com.pplus.luckybol.core.network.model.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by imac on 2018. 1. 2..
 */
@Parcelize
class PointBuy(var seqNo: Long? = null,
               var memberSeqNo: Long? = null,
               var orderId: String? = null,
               var receiptId: String? = null,
               var cash: Int? = null,
               var status: Int? = null,
               var pg: String? = null,
               var cardName: String? = null,
               var cardNo: String? = null,
               var cardQuota: String? = null,
               var cardAuthNo: String? = null,
               var payMethod: String? = null,
               var bankName: String? = null,
               var accountHolder: String? = null,
               var account: String? = null,
               var expireDate: String? = null,
               var userName: String? = null,
               var cashResult: String? = null,
               var regDatetime: String? = null,
               var modDatetime: String? = null,
               var count: Int? = null,
               var eventSeqNo: Long? = null,
               var autoKey: String? = null,
               var cardCode: String? = null,
               var installment: String? = null) : Parcelable {}