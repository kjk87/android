package com.pplus.luckybol.core.network.model.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by imac on 2018. 1. 2..
 */
@Parcelize
class EventResult(var join: EventJoin? = null,
                  var win: EventWin? = null,
                  var pageView: EventPv? = null,
                  var userView: EventUv? = null,
                  var joinDate: String? = null,
                  var joinTerm: Int? = null,
                  var remainSecond: Int? = null) : Parcelable {}