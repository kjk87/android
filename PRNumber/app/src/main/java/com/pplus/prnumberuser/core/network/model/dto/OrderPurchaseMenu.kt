package com.pplus.prnumberuser.core.network.model.dto

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Created by imac on 2018. 1. 2..
 */
@Parcelize
class OrderPurchaseMenu(var seqNo: Long? = null,
                        var orderPurchaseSeqNo: Long? = null,
                        var orderMenuSeqNo: Long? = null,
                        var price: Float? = null,
                        var optionPrice: Float? = null,
                        var amount: Int? = null,
                        var title: String? = null,
                        var expireDatetime: String? = null,
                        var orderMenu: OrderMenu? = null,
                        var orderPurchaseMenuOptionList: List<OrderPurchaseMenuOption>? = null) : Parcelable {}