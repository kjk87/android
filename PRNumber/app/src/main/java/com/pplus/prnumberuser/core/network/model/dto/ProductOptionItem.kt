package com.pplus.prnumberuser.core.network.model.dto


import android.os.Parcel
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by kjk on 2017. 6. 20..
 */
@Parcelize
class ProductOptionItem(var seqNo: Long? = null,
                        var productSeqNo: Long? = null,
                        var optionSeqNo: Long? = null,
                        var item: String? = null) : Parcelable {
}
