package com.lejel.wowbox.core.network.model.dto


import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by kjk on 2017. 6. 20..
 */
@Parcelize
class ProductOption(var seqNo: Long? = null,
                    var productSeqNo: Long? = null,
                    var name: String? = null,
                    var item: String? = null,
                    var items: List<ProductOptionItem>? = null) : Parcelable {
}
