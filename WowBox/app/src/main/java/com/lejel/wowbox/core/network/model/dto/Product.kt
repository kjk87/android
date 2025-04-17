package com.lejel.wowbox.core.network.model.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by imac on 2018. 1. 2..
 */
@Parcelize
class Product(var seqNo: Long? = null,
              var pageSeqNo: Long? = null,
              var marketType: String? = null,
              var salesType: Int? = null, //1:매장상품, 3:배송상품
              var status: Int? = null, //1 : 판매중, 0: 판매중단, 2 : 판매완료(sold out)
              var first: Long? = null,
              var second: Long? = null,
              var third: Long? = null,
              var name: String? = null,
              var priceMethod: String? = null,//free, fix
              var surtax: Boolean? = null,//true:과세, false : 면세
              var salesTerm: Boolean? = null,//판매기간설정 여부
              var startDate: String? = null,
              var endDate: String? = null,
              var contents: String? = null,
              var count: Int? = null,
              var soldCount: Int? = null,
              var useOption: Boolean? = null,
              var optionType: String? = null,// single, union
              var optionArray: String? = null,// 옵션 정렬순서 (등록순:regDate, 가나다:alphabet, 높은가격:highPrice, 낮은가격:lowPrice)
              var isKc: Boolean? = null,
              var nonKcMemo: String? = null,
              var noticeGroup: String? = null,
              var expireDatetime: String? = null,
              var blind: Boolean? = null,
              var reason: String? = null,
              var regDatetime: String? = null,
              var registerType: String? = null,
              var register: String? = null,
              var modDatetime: String? = null,
              var supplyPrice: Float? = null,
              var consumerPrice: Float? = null,
              var imageList: List<ProductImage>? = null,
              var deliveryType: Int? = null, // 1:무료, 2:유료, 3:조건부 무료
              var deliveryFee: Float? = null,
              var effectiveDate: String? = null) : Parcelable {
}