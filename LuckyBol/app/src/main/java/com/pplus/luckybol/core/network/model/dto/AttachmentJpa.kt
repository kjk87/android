package com.pplus.luckybol.core.network.model.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by j2n on 2016. 7. 26..
 */
@Parcelize
class AttachmentJpa(var seqNo: Long? = null,
                    var id: String? = null,
                    var url: String? = null,
                    var targetType: String? = null) : Parcelable {

}