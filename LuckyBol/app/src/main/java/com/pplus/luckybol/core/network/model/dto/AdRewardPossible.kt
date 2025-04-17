package com.pplus.luckybol.core.network.model.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by imac on 2018. 1. 2..
 */
@Parcelize
class AdRewardPossible(var adRewardDatetime: String? = null,
                       var joinTerm: Int? = null) : Parcelable {}