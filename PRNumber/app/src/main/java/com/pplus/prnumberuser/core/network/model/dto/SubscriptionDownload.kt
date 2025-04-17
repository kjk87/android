package com.pplus.prnumberuser.core.network.model.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.time.LocalDate

/**
 * Created by imac on 2018. 1. 2..
 */
@Parcelize
class SubscriptionDownload(var seqNo: Long? = null,
                           var productPriceSeqNo: Long? = null,
                           var memberSeqNo: Long? = null,
                           var status: Int? = null, // 1:사용중, 2:사용완료, 3:기간만료
                           var expireDate: String? = null,
                           var haveCount:Int? = null,
                           var useCount:Int? = null,
                           var regDatetime: String? = null,
                           var completeDatetime: String? = null,
                           var name: String? = null,
                           var useCondition: String? = null,
                           var type: String? = null,
                           var havePrice:Int? = null,
                           var usePrice:Int? = null,
                           var member: Member? = null,
                           var productPrice: ProductPrice? = null) : Parcelable {}