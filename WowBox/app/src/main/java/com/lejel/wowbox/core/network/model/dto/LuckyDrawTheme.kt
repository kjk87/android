package com.lejel.wowbox.core.network.model.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by 김종경 on 2016-10-04.
 */
@Parcelize
class LuckyDrawTheme(var seqNo: Long? = null,
                     var status: String? = null,
                     var aos: Boolean? = null,
                     var ios: Boolean? = null,
                     var title: String? = null,
                     var comment: String? = null,
                     var regDatetime: String? = null,
                     var array: Int? = null,
                     var groupList: List<LuckyDrawThemeGroup>? = null) : Parcelable {

}