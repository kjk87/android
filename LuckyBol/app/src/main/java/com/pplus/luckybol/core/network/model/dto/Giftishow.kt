package com.pplus.luckybol.core.network.model.dto

import android.os.Parcel
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by imac on 2018. 1. 15..
 */
@Parcelize
class Giftishow(var seqNo: Long? = null,
                var goodsCode: String? = null,
                var goodsNo: String? = null,
                var goodsName: String? = null,
                var brandCode: String? = null,
                var brandName: String? = null,
                var content: String? = null,
                var contentAddDesc: String? = null,
                var discountRate: Float? = null,
                var goodsTypeNm: String? = null,
                var goodsImgS: String? = null,
                var goodsImgB: String? = null,
                var goodsDescImgWeb: String? = null,
                var brandIconImg: String? = null,
                var mmsGoodsImg: String? = null,
                var discountPrice: String? = null,
                var realPrice: String? = null,
                var salePrice: String? = null,
                var srchKeyword: String? = null,
                var validPrdTypeCd: String? = null,
                var limitDay: String? = null,
                var validPrdDay: String? = null,
                var endDate: String? = null,
                var goodsComId: String? = null,
                var goodsComName: String? = null,
                var affiliateId: String? = null,
                var affiliate: String? = null,
                var exhGenderCd: String? = null,
                var exhAgeCd: String? = null,
                var mmsReserveFlag: String? = null,
                var goodsStateCd: String? = null,
                var mmsBarcdCreateYn: String? = null,
                var rmCntFlag: String? = null,
                var saleDateFlagCd: String? = null,
                var goodsTypeDtlNm: String? = null,
                var category1Seq: String? = null,
                var category2Seq: String? = null,
                var saleDateFlag: String? = null,
                var rmIdBuyCntFlagCd: String? = null,
                var mdCode: String? = null,
                var sale: Boolean? = null,
                var priority: Int? = null,
                var giftishowCategorySeqNo: Long? = null) : Parcelable {
    override fun equals(o: Any?): Boolean {

        if (o == null) return false

        return if (o is Giftishow) {
            o.seqNo == seqNo
        } else {
            false
        }
    }

}