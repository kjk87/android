package com.pplus.luckybol.core.network.model.dto

import android.os.Parcel
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by imac on 2018. 1. 2..
 */
@Parcelize
class Goods(var seqNo: Long? = null,
            var pageSeqNo: Long? = null,
            var categorySeqNo: Long? = null,
            var pageCategorySeqNo: Long? = null,
            var name: String? = null,
            var hashtag: String? = null,
            var description: String? = null,
            var count: Int? = null,
            var soldCount: Int? = null,
            var status: Int? = null, //1 : 판매중, 0: 판매중단, 2 : 판매완료(sold out)
            var main: Int? = null,
            var price: Long? = null,
            var originPrice: Long? = null,
            var regDatetime: String? = null,
            var modDatetime: String? = null,
            var expireDatetime: String? = null,
            var attachments: GoodsAttachments? = null,
            var page: Page2? = null,
            var reviewCount: Int? = null,
            var distance: Double? = null,
            var avgEval: Double? = null,
            var rewardLuckybol: Long? = null,
            var isHotdeal: Boolean? = null,
            var isPlus: Boolean? = null,
            var category: GoodsCategory? = null,
            var type: String? = null,
            var startTime: String? = null,
            var endTime: String? = null,
            var rewardPrLink: Long? = null,
            var registerType: String? = null,
            var register: String? = null,
            var blind: Boolean? = null,
            var represent: Boolean? = null,
            var note: String? = null,
            var reason: String? = null,
            var serviceCondition: String? = null,
            var timeOption: String? = null,
            var isDeleted: Boolean? = null,
            var discountRatio: Float? = null,
            var isCoupon: Boolean? = null,
            var allDays: Boolean? = null,
            var allWeeks: Boolean? = null,
            var dayOfWeeks: String? = null,
            var goodsImageList: List<GoodsImage>? = null,
            var salesType: Int? = null, //1:매장상품, 3:배송상품
            var isPacking: Boolean? = null,
            var isStore: Boolean? = null,
            var deliveryFee: Int? = null,
            var refundDeliveryFee: Int? = null,
            var deliveryAddFee: Int? = null,
            var deliveryMinPrice: Int? = null,
            var reservationMinNumber: Int? = null,
            var reservationMaxNumber: Int? = null,
            var isDeliveryPlus: Boolean? = null,
            var isDeliveryHotdeal: Boolean? = null,
            var externalUrl: String? = null,
            var optionType: Int? = null, //0:없음, 1:필수, 2:선택
            var buyableCount: Int? = null,
            var detailType: String? = null,
            var reviewPoint: Int? = null,
            var recommend: Boolean? = null,
            var salesTypes: String? = null,
            var marketType: String? = null,
            var categoryFirst: CategoryFirst? = null,
            var categorySecond: CategoryFirst? = null,
            var categoryThird: CategoryFirst? = null) : Parcelable {
}