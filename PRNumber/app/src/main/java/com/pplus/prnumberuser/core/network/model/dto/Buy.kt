package com.pplus.prnumberuser.core.network.model.dto

import android.os.Parcel
import android.os.Parcelable
import com.pplus.prnumberuser.core.network.model.dto.Cart

/**
 * Created by imac on 2018. 1. 2..
 */
class Buy(var seqNo: Long? = null,
          var memberSeqNo: Long? = null,
          var pageSeqNo: Long? = null,
          var orderId: String? = null,
          var process: Int? = null,//0 :  대기, 1:승인 2:승인후 취소, 3:사용, 4:사용후환불, -1: 우리서버 에러, -2: PG 에러
          var payMethod: String? = null,
          var pg: String? = null,
          var pgTranId: String? = null,
          var pgAcceptId: String? = null,
          var carts: GoodsSeqNoList? = null,
          var title: String? = null,
          var buyerEmail: String? = null,
          var buyerName: String? = null,
          var buyerTel: String? = null,
          var buyerAddress: String? = null,
          var buyerPostcode: String? = null,
          var price: Long? = null,
          var vat: Long? = null,
          var regDatetime: String? = null,
          var modDatetime: String? = null,
          var buyGoods: Cart? = null,
          var buyGoodsList: List<BuyGoods>? = null,
          var buyGoodsSelectList: List<BuyGoods>? = null,
          var orderType: Int? = null,
          var orderProcess: Int? = null,
          var bookDatetime: String? = null,
          var clientAddress: String? = null,
          var memo: String? = null,
          var deliveryFee: Int? = null,
          var type: String? = null,
          var confirmDatetime: String? = null,
          var cancelDatetime: String? = null,
          var completeDatetime: String? = null,
          var page: Page2? = null,
          var isReviewExist: Boolean? = null,
          var installment: String? = null,
          var buycol: String? = null,
          var pointRatio: Float? = null,
          var commissionRatio: Float? = null,
          var savedPoint: Int? = null,
          var agentSeqNo: Long? = null,
          var isPaymentPoint: Boolean? = null,
          var payType: String? = null,
          var payDatetime: String? = null) : Parcelable {
    constructor(source: Parcel) : this(
            source.readValue(Long::class.java.classLoader) as Long?,
            source.readValue(Long::class.java.classLoader) as Long?,
            source.readValue(Long::class.java.classLoader) as Long?,
            source.readString(),
            source.readValue(Int::class.java.classLoader) as Int?,
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readParcelable<GoodsSeqNoList>(GoodsSeqNoList::class.java.classLoader),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readValue(Long::class.java.classLoader) as Long?,
            source.readValue(Long::class.java.classLoader) as Long?,
            source.readString(),
            source.readString(),
            source.readParcelable<Cart>(Cart::class.java.classLoader),
            source.createTypedArrayList(BuyGoods.CREATOR),
            source.createTypedArrayList(BuyGoods.CREATOR),
            source.readValue(Int::class.java.classLoader) as Int?,
            source.readValue(Int::class.java.classLoader) as Int?,
            source.readString(),
            source.readString(),
            source.readString(),
            source.readValue(Int::class.java.classLoader) as Int?,
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readParcelable<Page2>(Page2::class.java.classLoader),
            source.readValue(Boolean::class.java.classLoader) as Boolean?,
            source.readString(),
            source.readString(),
            source.readValue(Float::class.java.classLoader) as Float?,
            source.readValue(Float::class.java.classLoader) as Float?,
            source.readValue(Int::class.java.classLoader) as Int?,
            source.readValue(Long::class.java.classLoader) as Long?,
            source.readValue(Boolean::class.java.classLoader) as Boolean?,
            source.readString(),
            source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeValue(seqNo)
        writeValue(memberSeqNo)
        writeValue(pageSeqNo)
        writeString(orderId)
        writeValue(process)
        writeString(payMethod)
        writeString(pg)
        writeString(pgTranId)
        writeString(pgAcceptId)
        writeParcelable(carts, 0)
        writeString(title)
        writeString(buyerEmail)
        writeString(buyerName)
        writeString(buyerTel)
        writeString(buyerAddress)
        writeString(buyerPostcode)
        writeValue(price)
        writeValue(vat)
        writeString(regDatetime)
        writeString(modDatetime)
        writeParcelable(buyGoods, 0)
        writeTypedList(buyGoodsList)
        writeTypedList(buyGoodsSelectList)
        writeValue(orderType)
        writeValue(orderProcess)
        writeString(bookDatetime)
        writeString(clientAddress)
        writeString(memo)
        writeValue(deliveryFee)
        writeString(type)
        writeString(confirmDatetime)
        writeString(cancelDatetime)
        writeString(completeDatetime)
        writeParcelable(page, 0)
        writeValue(isReviewExist)
        writeString(installment)
        writeString(buycol)
        writeValue(pointRatio)
        writeValue(commissionRatio)
        writeValue(savedPoint)
        writeValue(agentSeqNo)
        writeValue(isPaymentPoint)
        writeString(payType)
        writeString(payDatetime)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Buy> = object : Parcelable.Creator<Buy> {
            override fun createFromParcel(source: Parcel): Buy = Buy(source)
            override fun newArray(size: Int): Array<Buy?> = arrayOfNulls(size)
        }
    }
}