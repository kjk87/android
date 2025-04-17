package com.pplus.luckybol.core.network.model.dto

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by imac on 2018. 1. 2..
 */
class FTLink(var shopcode: String? = null,
             var loginId: String? = null,
             var autokey: String? = null,
             var order_req_amt: String? = null,
             var order_goodsname: String? = null,//0 :  대기, 1:승인 2:승인후 취소, 3:사용, 4:사용후환불, -1: 우리서버 에러, -2: PG 에러
             var order_name: String? = null,
             var order_hp: String? = null,
             var order_email: String? = null,
             var req_installment: String? = null,
             var req_cardcode: String? = null,
             var comp_orderno: String? = null,
             var comp_memno: String? = null,
             var comp_temp1: String? = null,
             var comp_temp2: String? = null,
             var comp_temp3: String? = null,
             var comp_temp4: String? = null,
             var comp_temp5: String? = null,
             var errCode: String? = null,
             var errMessage: String? = null,
             var serverType: String? = null,
             var roomId: String? = null) : Parcelable {
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
        writeString(autokey)
        writeString(order_req_amt)
        writeString(order_goodsname)
        writeString(order_name)
        writeString(order_hp)
        writeString(order_email)
        writeString(req_installment)
        writeString(req_cardcode)
        writeString(comp_orderno)
        writeString(comp_memno)
        writeString(comp_temp1)
        writeString(comp_temp2)
        writeString(comp_temp3)
        writeString(comp_temp4)
        writeString(comp_temp5)
        writeString(errCode)
        writeString(errMessage)
        writeString(serverType)
        writeString(roomId)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<FTLink> = object : Parcelable.Creator<FTLink> {
            override fun createFromParcel(source: Parcel): FTLink = FTLink(source)
            override fun newArray(size: Int): Array<FTLink?> = arrayOfNulls(size)
        }
    }
}