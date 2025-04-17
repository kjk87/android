package com.pplus.prnumberuser.core.network.model.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by imac on 2018. 1. 2..
 */
@Parcelize
class RiderFee(var result_code: String? = null,
               var result_message: String? = null,
               var delivery_distance: String? = null,
               var is_run: String? = null,
               var pick_lst: String? = null,
               var run_status: String? = null,
               var delivery_fee: String? = null,
               var manage_fee: String? = null,
               var extra_addr: String? = null,
               var extra_distance: String? = null,
               var extra_price: String? = null,
               var extra_rain: String? = null,
               var extra_snow: String? = null,
               var extra_freeze: String? = null,
               var extra_weekend: String? = null,
               var extra_holiday: String? = null,
               var extra_busytime: String? = null,
               var extra_night: String? = null) : Parcelable {

}