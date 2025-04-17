package com.root37.buflexz.core.network.model.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by 김종경 on 2016-10-04.
 */
@Parcelize
class ProductCategory(var seqNo: Long? = null,
                      var title: String? = null,
                      var status: String? = null,
                      var array: Int? = null,
                      var regDatetime: String? = null) : Parcelable {

}