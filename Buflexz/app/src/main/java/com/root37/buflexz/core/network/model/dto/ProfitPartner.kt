package com.root37.buflexz.core.network.model.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by imac on 2018. 1. 15..
 */
@Parcelize
class ProfitPartner(var seqNo: Long? = null,
                    var userKey: String? = null,
                    var calculateMonth: String? = null,
                    var partnerType: String? = null,//partner1, partner2
                    var status: String? = null,//pending, transfer, complete
                    var transferedBonusProfit: Float? = null,
                    var transferedAdProfit: Float? = null,
                    var transferedBallProfit: Float? = null,
                    var bonusProfit: Float? = null,
                    var adProfit: Float? = null,
                    var ballProfit: Float? = null,
                    var regDatetime: String? = null,
                    var changeDatetime: String? = null,
                    var comment: String? = null,
                    var parentsPartner: String? = null,
                    var memberTotal: Member? = null) : Parcelable {

}