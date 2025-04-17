package com.pplus.luckybol.core.network.model.dto


import android.os.Parcel
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by kjk on 2017. 6. 20..
 */
@Parcelize
class ProductOption(var seqNo: Long? = null,
                    var productSeqNo: Long? = null,
                    var name: String? = null,
                    var item: String? = null) : Parcelable {
}
