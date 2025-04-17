package com.pplus.prnumberuser.core.network.model.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by imac on 2018. 1. 2..
 */
@Parcelize
class OrderPurchaseMenuOption(var seqNo: Long? = null,
                              var orderPurchaseMenuSeqNo: Long? = null,
                              var menuOptionDetailSeqNo: Long? = null,
                              var type: Int? = null, // 1:필수, 2:선택
                              var price: Float? = null,
                              var title: String? = null,
                              var menuOptionDetail: MenuOptionDetail? = null) : Parcelable {
}