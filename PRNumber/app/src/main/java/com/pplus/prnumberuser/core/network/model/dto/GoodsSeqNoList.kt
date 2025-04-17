package com.pplus.prnumberuser.core.network.model.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by imac on 2018. 1. 2..
 */
@Parcelize
class GoodsSeqNoList(var seqNoList: List<Long>? = null,
                     var buyGoodsList: List<BuyGoods>? = null) : Parcelable {
}