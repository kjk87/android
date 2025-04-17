package com.root37.buflexz.core.network.model.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by 김종경 on 2016-10-04.
 */
@Parcelize
class LuckyDrawGift(var seqNo: Long? = null,
                    var luckyDrawSeqNo: Long? = null,
                    var type: String? = null, //goods, ball, point, candy, coin
                    var grade: Int? = null,
                    var title: String? = null,
                    var notice: String? = null,
                    var amount: Int? = null,
                    var price: Float? = null,
                    var image: String? = null,
                    var giftLink: String? = null,
                    var reviewPresent: Boolean? = null) : Parcelable {

}