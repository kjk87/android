package com.lejel.wowbox.core.network.model.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by imac on 2018. 1. 2..
 */
@Parcelize
class LuckyBoxPurchaseItemOption(var seqNo: Long? = null,
                                 var luckyboxPurchaseItemSeqNo: Long? = null,
                                 var productSeqNo: Long? = null,
                                 var productOptionDetailSeqNo: Long? = null,
                                 var quantity: Int? = null,
                                 var price: Int? = null,
                                 var depth1: String? = null,
                                 var depth2: String? = null) : Parcelable {
}