package com.pplus.prnumberuser.core.network.model.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by imac on 2018. 1. 15..
 */
@Parcelize
class Plus(var plusNo: Long? = null,
           var no: Long? = null,
           var status: String? = null,
           var nickname: String? = null,
           var type: String? = null,
           var name: String? = null,
           var phone: String? = null,
           var openBound: String? = null,
           var homepageLink: String? = null,
           var catchphrase: String? = null,
           var categoryText: String? = null,
           var todayViewCount: Int? = null,
           var totalViewCount: Int? = null,
           var blind: Boolean = false,
           var talkRecvBound: String? = null,
           var talkDenyDay: String? = null,
           var talkDenyStartTime: String? = null,
           var talkDenyEndTime: String? = null,
           var latitude: Double? = null,
           var longitude: Double? = null,
           var distance: Double? = null,
           var customerCount: Int? = null,
           var plusCount: Int? = null,
           var valuationCount: Int? = null,
           var valuationPoint: Int? = null,
           var prBoard: No? = null,
           var reviewBoard: No? = null,
           var profileImage: ImgUrl? = null,
           var backgroundImage: ImgUrl? = null,
           var address: Address? = null,
           var properties: PageProperties? = null,
           var numberList: List<PRNumber>? = null,
           var searchKeyword: String? = null,
           var introduction: String? = null,
           var code: String? = null,
           var mainMovieUrl: String? = null,
           var cooperation: No? = null,
           var coopStatus: String? = null,
           var user: User? = null,
           var authCode: String? = null,
           var plus: Boolean? = null,
           var mainGoodsSeqNo: Long? = null,
           var reviewCount: Int? = null,
           var goodsCount: Int? = null,
           var avgEval: Double? = null,
           var isLink: Boolean? = null,
           var hashtag: String? = null,
           var virtualPage: Boolean? = null,
           var isShopOrderable: Boolean? = null,
           var isPackingOrderable: Boolean? = null,
           var isDeliveryOrderable: Boolean? = null,
           var management: PageManagement? = null,
           var isParkingAvailable: Boolean? = null,
           var isValetParkingAvailable: Boolean? = null,
           var usePrnumber: Boolean? = null,
           var distributorAgentCode: String? = null,
           var isSeller: Boolean? = null,
           var isBrand: Boolean? = null,
           var bank: String? = null,
           var bankAccount: String? = null,
           var depositor: String? = null,
           var reason: String? = null,
           var email: String? = null,
           var licenseImage: String? = null,
           var ableNfc: Boolean? = null,
           var thumbnail: String? = null,
           var goodsNotiType: String? = null,
           var goodsNotification: String? = null,
           var point: Float? = null,
           var woodongyi: Boolean? = null,
           var qrImage: String? = null,
           var categoryMinorSeqNo: Long? = null,
           var categoryMajorSeqNo: Long? = null,
           var buyCount: Int? = null,
           var lastBuyDatetime: String? = null,
           var pushActivate: Boolean? = null,
           var agreement: Boolean? = null,
           var plusImage: String? = null,
           var plusInfo: String? = null,
           var plusGiftReceived: Boolean? = null,
           var plusGiftReceivedDatetime:String? = null,
           var mainProductPriceSeqNo:Long? = null) : Parcelable {
    override fun equals(other: Any?): Boolean {
        if (other == null) return false

        return if (other is Plus) {
            other.no == no
        } else {
            false
        }
    }

}