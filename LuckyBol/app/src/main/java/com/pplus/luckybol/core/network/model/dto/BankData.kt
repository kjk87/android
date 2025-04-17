package com.pplus.luckybol.core.network.model.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by kjk on 2017. 5. 15..
 */
@Parcelize
class BankData(var name: String? = null,
               var code: String? = null) : Parcelable {

    override fun toString(): String {
        return "BankData{name='$name', code='$code'}"
    }

}