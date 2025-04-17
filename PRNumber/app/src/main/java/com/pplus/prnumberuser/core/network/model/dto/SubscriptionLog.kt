package com.pplus.prnumberuser.core.network.model.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by imac on 2018. 1. 2..
 */
@Parcelize
class SubscriptionLog(var seqNo: Long? = null,
                      var subscriptionSeqNo: Long? = null,
                      var productPriceSeqNo: Long? = null,
                      var memberSeqNo: Long? = null,
                      var regDatetime: String? = null,
                      var usePrice: Int? = null,
                      var member: Member? = null,
                      var productPrice: ProductPrice? = null) : Parcelable {}