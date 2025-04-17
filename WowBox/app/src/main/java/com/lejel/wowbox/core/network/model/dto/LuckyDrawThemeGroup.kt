package com.lejel.wowbox.core.network.model.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by 김종경 on 2016-10-04.
 */
@Parcelize
class LuckyDrawThemeGroup(var seqNo: Long? = null,
                          var luckyDrawThemeSeqNo: Long? = null,
                          var luckyDrawGroupSeqNo: Long? = null,
                          var array: Int? = null,
                          var luckyDrawGroup:LuckyDrawGroup? = null) : Parcelable {

}