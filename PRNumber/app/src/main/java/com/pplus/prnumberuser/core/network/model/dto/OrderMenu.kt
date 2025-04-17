package com.pplus.prnumberuser.core.network.model.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by imac on 2018. 1. 2..
 */
@Parcelize
class OrderMenu(var seqNo: Long? = null,
                var pageSeqNo: Long? = null,
                var groupSeqNo: Long? = null,
                var title: String? = null,
                var menuInfo: String? = null,
                var price: Float? = null,
                var isSoldOut: Boolean? = null,
                var delegate: Boolean? = null,
                var soldOutDate: String? = null,
                var regDatetime: String? = null,
                var deleted: Boolean? = null,
                var originPrice: Float? = null,
                var discount: Float? = null,
                var expireType: String? = null,//date, number
                var expireDate: String? = null,
                var remainDate: Int? = null,
                var type: String? = null,//menu, ticket
                var imageList: List<OrderMenuImage>? = null,
                var optionList: List<OrderMenuOption>? = null) : Parcelable {
}