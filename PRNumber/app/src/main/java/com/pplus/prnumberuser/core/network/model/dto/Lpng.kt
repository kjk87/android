package com.pplus.prnumberuser.core.network.model.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by imac on 2018. 1. 2..
 */
@Parcelize
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

}