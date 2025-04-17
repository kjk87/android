package com.root37.buflexz.core.network.model.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by 김종경 on 2016-10-04.
 */
@Parcelize
class LotteryJoin(var seqNo: String? = null,
                  var lotteryRound: Int? = null,
                  var userKey: String? = null,
                  var no1: Int? = null,
                  var no2: Int? = null,
                  var no3: Int? = null,
                  var no4: Int? = null,
                  var no5: Int? = null,
                  var no6: Int? = null,
                  var joinType: String? = null,
                  var regDatetime: String? = null) : Parcelable {

}