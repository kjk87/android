package com.pplus.luckybol.core.network.model.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by imac on 2018. 1. 2..
 */
@Parcelize
class Coin(var balance: String? = null,
           var available: String? = null,
           var inOrders: String? = null) : Parcelable {

}