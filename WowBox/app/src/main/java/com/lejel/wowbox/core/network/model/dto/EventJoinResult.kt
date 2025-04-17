package com.lejel.wowbox.core.network.model.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by imac on 2018. 1. 2..
 */
@Parcelize
class EventJoinResult(var join: EventJoin? = null,
                      var win: EventWin? = null,
                      var joinDate: String? = null,
                      var joinTerm: Int? = null,
                      var remainSecond: Int? = null) : Parcelable {}