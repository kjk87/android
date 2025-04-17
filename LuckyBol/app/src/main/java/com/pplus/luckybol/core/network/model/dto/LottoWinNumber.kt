package com.pplus.luckybol.core.network.model.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by imac on 2018. 1. 2..
 */
@Parcelize
class LottoWinNumber(var seqNo: Long? = null,
                     var eventSeqNo: Long? = null,
                     var lottoNumber: Int? = null,
                     var regDatetime: String? = null) : Parcelable {}