package com.pplus.prnumberuser.core.network.model.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by imac on 2018. 1. 2..
 */
@Parcelize
class Cart(var seqNo: Long? = null,
           var pageSeqNo: Long? = null,
           var memberSeqNo: Long? = null,
           var orderMenuSeqNo: Long? = null,
           var salesType: Int? = null, // 1:매장, 2:배달, 3:배송, 4:예약, 5:포장
           var amount: Int? = null,
           var regDatetime: String? = null,
           var modDatetime: String? = null,
           var page: Page2? = null,
           var orderMenu: OrderMenu? = null,
           var cartOptionList: List<CartOption>? = null) : Parcelable {
}