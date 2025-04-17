package com.pplus.prnumberuser.core.network.model.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by imac on 2018. 1. 2..
 */
@Parcelize
class VirtualNumberManage(var seqNo: Long? = null,
                          var type: String? = null,
                          var status: String? = null,
                          var virtualNumber: String? = null,
                          var digit: Int? = null,
                          var groupName: String? = null,
                          var eventCode: String? = null,
                          var url: String? = null,
                          var startDatetime: String? = null,
                          var endDatetime: String? = null,
                          var nbook: Boolean? = null,
                          var thumbnail: String? = null,
                          var reason: String? = null,
                          var removeReason: String? = null,
                          var regDatetime: String? = null,
                          var removeDatetime: String? = null,
                          var productType: String? = null,
                          var itemList: List<VirtualNumberManageGroupItem>? = null) : Parcelable {
}