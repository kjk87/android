package com.pplus.luckybol.core.network.model.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by kjk on 2017. 6. 22..
 */
@Parcelize
class Cash(var no: Long? = null,
           var user: No? = null,
           var primaryType: String? = null,
           var secondaryType: String? = null,
           var amount: String? = null,
           var regDate: String? = null,
           var subject: String? = null,
           var properties: CashProperties? = null,
           var paymentProperties: CashBillingProperties? = null,
           var target: No? = null) : Parcelable {

    override fun toString(): String {
        return "Cash{no=$no, user=$user, primaryType='$primaryType', secondaryType='$secondaryType', amount='$amount', regDate='$regDate', subject='$subject', properties=$properties, target=$target}"
    }

}