package com.pplus.prnumberbiz.core.network.model.dto

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by imac on 2018. 1. 2..
 */
class Lpng(var shopcode: String? = null,
           var SERVICECODE: String? = null,
           var orderno: String?=null,
           var order_req_amt: String? = null,
           var order_goodsname: String? = null,//0 :  대기, 1:승인 2:승인후 취소, 3:사용, 4:사용후환불, -1: 우리서버 에러, -2: PG 에러
           var order_name: String? = null,
           var order_hp: String? = null,
           var order_email: String? = null,
           var comp_orderno: String? = null,
           var comp_memno: String? = null,
           var req_install : String? = null) : Parcelable {

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
            source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(shopcode)
        writeString(SERVICECODE)
        writeString(orderno)
        writeString(order_req_amt)
        writeString(order_goodsname)
        writeString(order_name)
        writeString(order_hp)
        writeString(order_email)
        writeString(comp_orderno)
        writeString(comp_memno)
        writeString(req_install)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Lpng> = object : Parcelable.Creator<Lpng> {
            override fun createFromParcel(source: Parcel): Lpng = Lpng(source)
            override fun newArray(size: Int): Array<Lpng?> = arrayOfNulls(size)
        }
    }
}