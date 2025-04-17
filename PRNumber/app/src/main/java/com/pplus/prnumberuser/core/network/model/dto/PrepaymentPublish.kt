package com.pplus.prnumberuser.core.network.model.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by imac on 2018. 1. 2..
 */
@Parcelize
class PrepaymentPublish(var seqNo: Long? = null,
                        var prepaymentSeqNo: Long? = null,
                        var pageSeqNo: Long? = null,
                        var memberSeqNo: Long? = null,
                        var agentSeqNo: Long? = null,
                        var status: String? = null,
                        var expireDate: String? = null,
                        var price: Float? = null,
                        var addPrice: Float? = null,
                        var totalPrice: Float? = null,
                        var notice: String? = null,
                        var regDatetime: String? = null,
                        var completedDatetime: String? = null,
                        var havePrice: Float? = null,
                        var usePrice: Float? = null,
                        var wholesaleCode: String? = null,
                        var distributorCode: String? = null,
                        var pageCommissionRatio: Float? = null,
                        var wholesaleCommissionRatio: Float? = null,
                        var distributorCommissionRatio: Float? = null,
                        var pageCommission: Float? = null,
                        var wholesaleCommission: Float? = null,
                        var distributorCommission: Float? = null,
                        var prepayment: Prepayment? = null,
                        var page:Page2? = null) : Parcelable {}