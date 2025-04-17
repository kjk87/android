package com.pplus.prnumberuser.core.network.model.dto

import android.os.Parcel
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by imac on 2018. 1. 2..
 */
@Parcelize
class PurchaseProductOption(var seqNo: Long? = null,
                            var purchaseSeqNo: Long? = null,
                            var purchaseProductSeqNo: Long? = null,
                            var productSeqNo: Long? = null,
                            var productOptionDetailSeqNo: Long? = null,
                            var amount: Int? = null,
                            var price: Int? = null,
                            var depth1: String? = null,
                            var depth2: String? = null) : Parcelable {
}