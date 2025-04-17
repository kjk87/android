package com.pplus.prnumberbiz.core.network.model.dto

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by imac on 2018. 1. 2..
 */
class LpngRes(var returncode: String? = null,
              var errormsg: String? = null,
              var shopcode: String? = null,
              var orderno: String? = null,//0 :  대기, 1:승인 2:승인후 취소, 3:사용, 4:사용후환불, -1: 우리서버 에러, -2: PG 에러
              var orderstatus: String? = null,
              var orderexpiredt: String? = null,
              var order_req_amt: String? = null,
              var comp_orderno: String? = null) : Parcelable {

    constructor(source: Parcel) : this(
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
        writeString(returncode)
        writeString(errormsg)
        writeString(shopcode)
        writeString(orderno)
        writeString(orderstatus)
        writeString(orderexpiredt)
        writeString(order_req_amt)
        writeString(comp_orderno)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<LpngRes> = object : Parcelable.Creator<LpngRes> {
            override fun createFromParcel(source: Parcel): LpngRes = LpngRes(source)
            override fun newArray(size: Int): Array<LpngRes?> = arrayOfNulls(size)
        }
    }
}