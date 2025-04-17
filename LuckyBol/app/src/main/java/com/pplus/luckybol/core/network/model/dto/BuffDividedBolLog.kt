package com.pplus.luckybol.core.network.model.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by imac on 2018. 1. 2..
 */
@Parcelize
class BuffDividedBolLog(var seqNo: Long? = null,
                        var buffSeqNo: Long? = null,
                        var memberSeqNo: Long? = null,
                        var type: String? = null, //lotto, shopping
                        var moneyType: String? = null, //bol, point
                        var eventSeqNo: Long? = null,
                        var shoppingSeqNo: Long? = null,
                        var amount: Float? = null,
                        var regDatetime: String? = null,
                        var member: Member? = null) : Parcelable {


}