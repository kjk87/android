package com.pplus.luckybol.core.network.model.dto

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by imac on 2018. 1. 2..
 */
class Lpng(var shopcode:String? = null,
           var loginId: String? = null,
           var order_req_amt: String? = null,
           var comp_orderno: String? = null,
           var order_goodsname: String? = null,//0 :  대기, 1:승인 2:승인후 취소, 3:사용, 4:사용후환불, -1: 우리서버 에러, -2: PG 에러
           var order_name: String? = null,
           var order_hp: String? = null,
           var order_email: String? = null,
           var req_cardNo: String? = null,
           var req_cardMonth: String? = null,
           var req_cardYear: String? = null,
           var req_identity: String? = null,
           var req_cardPwd: String? = null,
           var req_installment: String? = null,
           var comp_memno: String? = null,
           var errCode: String? = null,
           var errMessage: String? = null) : Parcelable {

    constructor(source: Parcel) : this(
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(shopcode)
        writeString(loginId)
        writeString(order_req_amt)
        writeString(comp_orderno)
        writeString(order_goodsname)
        writeString(order_name)
        writeString(order_hp)
        writeString(order_email)
        writeString(req_cardNo)
        writeString(req_cardMonth)
        writeString(req_cardYear)
        writeString(req_identity)
        writeString(req_cardPwd)
        writeString(req_installment)
        writeString(comp_memno)
        writeString(errCode)
        writeString(errMessage)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Lpng> = object : Parcelable.Creator<Lpng> {
            override fun createFromParcel(source: Parcel): Lpng = Lpng(source)
            override fun newArray(size: Int): Array<Lpng?> = arrayOfNulls(size)
        }
    }
}