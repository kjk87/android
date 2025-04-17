package com.pplus.luckybol.core.network.model.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by imac on 2018. 2. 9..
 */

@Parcelize
class Lotto(var seqNo: Long? = null,
            var lottoTimes: String? = null,
            var lottoPrevTimes: String? = null,
            var winCode: String? = null,
            var joinLuckybol: Int? = null,
            var lottoLuckybol: Int? = null,
            var url1: String? = null,
            var selector1: String? = null,
            var success1: Boolean? = null,
            var url2: String? = null,
            var selector2: String? = null,
            var success2: Boolean? = null,
            var url3: String? = null,
            var selector3: String? = null,
            var success3: Boolean? = null,
            var modDatetime: String? = null) : Parcelable {
}