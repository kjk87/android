package com.pplus.prnumberuser.core.network.model.dto


import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by kjk on 2017. 6. 20..
 */
@Parcelize
class ProductOptionDetail(var seqNo: Long? = null,
                          var productSeqNo: Long? = null,
                          var optionSeqNo: Long? = null,
                          var depth1ItemSeqNo: Long? = null,
                          var depth2ItemSeqNo: Long? = null,
                          var amount: Int? = null,
                          var soldCount: Int? = null,
                          var price: Int? = null,
                          var flag: Boolean? = null,
                          var status: Int? = null,// '상품상태 1:판매중, 0:판매완료 soldout, -1:판매종료(expire), -2:판매중지, -999: 삭제
                          var usable: Boolean? = null,// 사용여부
                          var item1: ProductOptionItem? = null,
                          var item2: ProductOptionItem? = null,
                          var option: ProductOption? = null) : Parcelable {}
