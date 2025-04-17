package com.pplus.prnumberuser.core.network.model.dto

import android.os.Parcel
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by imac on 2018. 1. 2..
 */

@Parcelize
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
}