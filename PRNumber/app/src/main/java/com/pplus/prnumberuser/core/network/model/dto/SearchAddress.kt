package com.pplus.prnumberuser.core.network.model.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by j2n on 2016. 7. 26..
 */
@Parcelize
class SearchAddress(var common: Common? = null,
                    var juso: List<SearchAddressJuso>? = null) : Parcelable {


    @Parcelize
    class Common(var errorMessage: String? = null,
                 var countPerPage: String? = null,
                 var totalCount: Int? = null,
                 var errorCode: Int? = null,
                 var currentPage: Int? = null) : Parcelable {
    }

}