package com.pplus.prnumberuser.core.network.model.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by j2n on 2016. 7. 26..
 */
@Parcelize
class PRNumber(var number: String? = null,
               var type: String? = null,
               var reserved: String? = null,
               var duration: Duration? = null,
               var openBound: String? = null,
               var actSrc: String? = null,
               var actor: User? = null,
               var actDate: String? = null,
               var note: String? = null,
               var batch: No? = null,
               var country: No? = null,
               var reservedTitle: String? = null,
               var reservedReason: String? = null,
               var reservedDesc: String? = null,
               var reservedDate: String? = null) : Parcelable {

}