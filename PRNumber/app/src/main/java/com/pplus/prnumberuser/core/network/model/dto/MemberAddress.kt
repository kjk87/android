package com.pplus.prnumberuser.core.network.model.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by imac on 2018. 1. 2..
 */
@Parcelize
class MemberAddress(var seqNo: Long? = null,
                    var memberSeqNo: Long? = null,
                    var name: String? = null,
                    var postCode: String? = null,
                    var address: String? = null,
                    var addressDetail: String? = null,
                    var tel: String? = null) : Parcelable {}