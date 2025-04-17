package com.pplus.prnumberuser.apps.common.mgmt

import android.content.Context
import android.content.Intent
import com.pplus.prnumberuser.Const
import com.pplus.prnumberuser.apps.bol.ui.BolConfigActivity
import com.pplus.prnumberuser.apps.bol.ui.TicketConfigActivity
import com.pplus.prnumberuser.apps.delivery.ui.DeliveryAddressSetActivity
import com.pplus.prnumberuser.apps.event.ui.EventDetailActivity
import com.pplus.prnumberuser.apps.event.ui.EventImpressionActivity
import com.pplus.prnumberuser.apps.menu.ui.OrderPurchaseHistoryDetailActivity
import com.pplus.prnumberuser.apps.menu.ui.TicketPurchaseHistoryDetailActivity
import com.pplus.prnumberuser.apps.page.ui.*
import com.pplus.prnumberuser.apps.prepayment.ui.PrepaymentPublishDetailActivity
import com.pplus.prnumberuser.apps.product.ui.ProductShipDetailActivity
import com.pplus.prnumberuser.apps.product.ui.PurchaseHistoryActivity
import com.pplus.prnumberuser.apps.setting.ui.AlarmContainerActivity
import com.pplus.prnumberuser.apps.setting.ui.NoticeDetailActivity
import com.pplus.prnumberuser.core.code.common.MoveType1Code
import com.pplus.prnumberuser.core.code.common.MoveType2Code
import com.pplus.prnumberuser.core.network.model.dto.*
import com.pplus.prnumberuser.core.util.PplusCommonUtil.Companion.openChromeWebView
import com.pplus.prnumberuser.push.PushReceiveData
import com.pplus.utils.part.logs.LogUtil
import com.pplus.utils.part.utils.StringUtils
import java.io.UnsupportedEncodingException
import java.net.URLDecoder
import java.util.*

/**
 * Created by ksh on 2016-10-18.
 */
class SchemaManager {
    fun moveToSchema(type1: String?, type2: String?, moveTarget: String?, moveTargetString: String?, msgNo: String?, isPush: Boolean) {

        //null일 경우 crash발생하여 예외처리함.
        if (StringUtils.isEmpty(type1)) {
            return
        }
        val moveType1Code = MoveType1Code.valueOf(type1!!)
        var moveType2Code: MoveType2Code? = null
        if (type2 != null) {
            try {
                moveType2Code = MoveType2Code.valueOf(type2)
            } catch (e: Exception) {
            }
        }
        var intent: Intent? = null // 내부로 연결시
        if (moveType1Code == MoveType1Code.inner) {
            if (moveType2Code != null) {
                when (moveType2Code) {
                    MoveType2Code.main -> {
                    }
                    MoveType2Code.pageDetail -> {
                        intent = Intent(mContext, PageActivity::class.java)
                        val page = Page()
                        page.no = moveTarget!!.toLong()
                        intent.putExtra(Const.PAGE, page)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                        mContext!!.startActivity(intent)
                    }
                    MoveType2Code.productDetail -> {
                        intent = Intent(mContext, ProductShipDetailActivity::class.java)
                        val productPrice = ProductPrice()
                        productPrice.seqNo = moveTarget!!.toLong()
                        intent.putExtra(Const.DATA, productPrice)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                        mContext!!.startActivity(intent)
                    } //                    MoveType2Code.newsDetail -> {
                    //                        intent = Intent(mContext, NewsDetailActivity::class.java)
                    //                        val news = News()
                    //                        news.seqNo = java.lang.Long.valueOf(moveTarget!!)
                    //                        intent.putExtra(Const.DATA, news)
                    //                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                    //                        mContext!!.startActivity(intent)
                    //                    }
                    MoveType2Code.plus -> {
                    }
                    MoveType2Code.bolHistory -> {
                        intent = Intent(mContext, BolConfigActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                        mContext!!.startActivity(intent)
                    }
                    MoveType2Code.ticketHistory -> {
                        intent = Intent(mContext, TicketConfigActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                        mContext!!.startActivity(intent)
                    }
                    MoveType2Code.cashHistory -> {
                    }
                    MoveType2Code.msgbox -> {
                        intent = Intent(mContext, AlarmContainerActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                        mContext!!.startActivity(intent)
                    }
                    MoveType2Code.eventWin -> {
                        intent = Intent(mContext, EventImpressionActivity::class.java)
                        val event = Event()
                        event.no = moveTarget!!.toLong()
                        intent.putExtra(Const.DATA, event)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                        mContext!!.startActivity(intent)
                    }
                    MoveType2Code.eventWinPage -> {
                        intent = Intent(mContext, PageEventAnnounceActivity::class.java)
                        val event = Event()
                        event.no = moveTarget!!.toLong()
                        intent.putExtra(Const.DATA, event)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                        mContext!!.startActivity(intent)
                    }
                    MoveType2Code.eventDetail -> {
                        intent = Intent(mContext, EventDetailActivity::class.java)
                        val event = Event()
                        event.no = moveTarget!!.toLong()
                        intent.putExtra(Const.DATA, event)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                        mContext!!.startActivity(intent)
                    }
                    MoveType2Code.noticeDetail -> {
                        intent = Intent(mContext, NoticeDetailActivity::class.java)
                        val notice = Notice()
                        notice.no = moveTarget!!.toLong()
                        intent.putExtra(Const.NOTICE, notice)
                        mContext!!.startActivity(intent)
                    }
                    MoveType2Code.bolDetail -> {
                        intent = Intent(mContext, BolConfigActivity::class.java)
                        val bol = Bol()
                        bol.no = moveTarget!!.toLong()
                        intent.putExtra(Const.DATA, bol)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                        mContext!!.startActivity(intent)
                    }
                    MoveType2Code.buyCancel, MoveType2Code.buyHistory -> {
                        intent = Intent(mContext, PurchaseHistoryActivity::class.java)
                        intent.putExtra(Const.KEY, Const.ORDER)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                        mContext!!.startActivity(intent)
                    }
                    MoveType2Code.plusInfo -> {
                        intent = Intent(mContext, PageActivity::class.java)
                        val page = Page()
                        page.no = moveTarget!!.toLong()
                        intent.putExtra(Const.PAGE, page)
                        intent.putExtra(Const.KEY, Const.PLUS_INFO)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                        mContext!!.startActivity(intent)

                    }
                    MoveType2Code.visit -> {
                        if (LoginInfoManager.getInstance().isMember) { //                            val intent = Intent(mContext, PageCashBackActivity::class.java)
                            val intent = Intent(mContext, PageAttendanceActivity::class.java)
                            val page = Page()
                            page.no = moveTarget!!.toLong()
                            intent.putExtra(Const.DATA, page)
                            mContext!!.startActivity(intent)
                        }
                    }
                    MoveType2Code.visitBenefit -> {
                        if (LoginInfoManager.getInstance().isMember) {
                            val intent = Intent(mContext, PageFirstBenefitActivity::class.java)
                            val page = Page2()
                            page.seqNo = moveTarget!!.toLong()
                            intent.putExtra(Const.DATA, page)
                            mContext!!.startActivity(intent)
                        }
                    }
                    MoveType2Code.prepaymentPublish, MoveType2Code.prepaymentLog -> {
                        if (LoginInfoManager.getInstance().isMember) {
                            val intent = Intent(mContext, PrepaymentPublishDetailActivity::class.java)
                            val prepaymentPublish = PrepaymentPublish()
                            prepaymentPublish.seqNo = moveTarget!!.toLong()
                            intent.putExtra(Const.DATA, prepaymentPublish)
                            intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
                            mContext!!.startActivity(intent)
                        }
                    }
                    MoveType2Code.orderDetail -> {
                        if (LoginInfoManager.getInstance().isMember) {
                            val intent = Intent(mContext, OrderPurchaseHistoryDetailActivity::class.java)
                            val orderPurchase = OrderPurchase()
                            orderPurchase.seqNo = moveTarget!!.toLong()
                            intent.putExtra(Const.DATA, orderPurchase)
                            mContext!!.startActivity(intent)
                        }
                    }
                    MoveType2Code.ticketDetail -> {
                        if (LoginInfoManager.getInstance().isMember) {
                            val intent = Intent(mContext, TicketPurchaseHistoryDetailActivity::class.java)
                            val orderPurchase = OrderPurchase()
                            orderPurchase.seqNo = moveTarget!!.toLong()
                            intent.putExtra(Const.DATA, orderPurchase)
                            mContext!!.startActivity(intent)
                        }
                    }
                    MoveType2Code.menu -> {
                        val deliveryAddressList = DeliveryAddressManager.getInstance().deliveryAddressList
                        if(deliveryAddressList != null && deliveryAddressList.isNotEmpty()){
                            val intent = Intent(mContext, PageDeliveryMenuActivity::class.java)
                            val page = Page2()
                            page.seqNo = moveTarget!!.toLong()
                            intent.putExtra(Const.DATA, page)
                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                            mContext!!.startActivity(intent)
                        }else{
                            val intent = Intent(mContext, DeliveryAddressSetActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                            mContext!!.startActivity(intent)
                        }
                    }
                }
            }
        } else {
            if (StringUtils.isNotEmpty(moveTargetString)) {
                openChromeWebView(mContext!!, moveTargetString!!)
            }
        }
    }

    fun moveToSchema(url: String?, isPush: Boolean) {
        if (url == null) return
        LogUtil.d(TAG, "url = $url")
        var resultData: Map<String?, MutableList<String?>?>? = null
        if (url.contains(SCHEMA_PRNUMBER)) {
            try {
                resultData = splitQuery(url)
            } catch (e: UnsupportedEncodingException) {
                e.printStackTrace()
            }
            var type1: String? = null
            var type2: String? = null
            var moveTarget: String? = null
            var moveTargetString: String? = null
            var msgNo: String? = null
            if (resultData != null && resultData[SCHEMA_MOVE_TYPE1] != null && resultData[SCHEMA_MOVE_TYPE1]!!.size > 0) {
                type1 = resultData[SCHEMA_MOVE_TYPE1]!![0]
            }
            if (resultData != null && resultData[SCHEMA_MOVE_TYPE2] != null && resultData[SCHEMA_MOVE_TYPE2]!!.size > 0) {
                type2 = resultData[SCHEMA_MOVE_TYPE2]!![0]
            }
            if (resultData != null && resultData[SCHEMA_MOVE_TARGET] != null && resultData[SCHEMA_MOVE_TARGET]!!.size > 0) {
                moveTarget = resultData[SCHEMA_MOVE_TARGET]!![0]
            }
            if (resultData != null && resultData[SCHEMA_MOVE_TARGET_STRING] != null && resultData[SCHEMA_MOVE_TARGET_STRING]!!.size > 0) {
                moveTargetString = resultData[SCHEMA_MOVE_TARGET_STRING]!![0]
            }
            if (resultData != null && resultData[MSG_NO] != null && resultData[MSG_NO]!!.size > 0) {
                msgNo = resultData[MSG_NO]!![0]
            }
            if (StringUtils.isNotEmpty(type1)) {
                moveToSchema(type1, type2, moveTarget, moveTargetString, msgNo, isPush)
            }
        }
    }

    fun setSchemaData(data: PushReceiveData?): String? {
        if (data != null) {
            val builder = StringBuilder()
            builder.append(SCHEMA_PRNUMBER)
            if (data.moveType1 != null) {
                builder.append(SCHEMA_MOVE_TYPE1 + "=" + data.moveType1)
            }
            if (data.moveType2 != null) {
                builder.append("&" + SCHEMA_MOVE_TYPE2 + "=" + data.moveType2)
            }
            if (data.moveTarget != null) {
                builder.append("&" + SCHEMA_MOVE_TARGET + "=" + data.moveTarget)
            }
            if (data.move_target_string != null) {
                builder.append("&" + SCHEMA_MOVE_TARGET_STRING + "=" + data.move_target_string)
            }
            if (data.msgNo != null) {
                builder.append("&" + MSG_NO + "=" + data.msgNo)
            }
            return builder.toString()
        }
        return null
    } /*
    public String getSearchData(){

        String schemaData = PPlusApplication.getSchemaData();
        String type3 = null;
        if(StringUtils.isNotEmpty(schemaData) && schemaData.contains(Const.SCHEMA_SEARCH)) {
            try {
                Map<String, List<String>> resultData = SchemaManager.splitQuery(new URL(schemaData));
                if(resultData.get(SCHEMA_MOVE_TARGET) != null && resultData.get(SCHEMA_MOVE_TARGET).size() > 0) {
                    type3 = resultData.get(SCHEMA_MOVE_TARGET).get(0);
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
        return type3;
    }

    public void clearSchemaData() {
        PPlusApplication.setSchemaData(null);
    }
*/

    companion object {
        private val TAG = SchemaManager::class.java.simpleName
        private var mSchemaManager: SchemaManager? = null
        private var mContext: Context? = null
        const val SCHEMA_PRNUMBER = "prnumber://"
        const val SCHEMA_PPLUS = "pplus://qr"
        const val SCHEMA_MOVE_TYPE1 = "moveType1"
        const val SCHEMA_MOVE_TYPE2 = "moveType2"
        const val SCHEMA_MOVE_TARGET = "moveTarget"
        const val SCHEMA_MOVE_TARGET_STRING = "moveTargetString"
        const val MSG_NO = "msgNo"
        fun getInstance(context: Context?): SchemaManager {
            if (mSchemaManager == null) {
                mSchemaManager = SchemaManager()
            }
            mContext = context
            return mSchemaManager!!
        }

        @Throws(UnsupportedEncodingException::class)
        fun splitQuery(url: String): Map<String?, MutableList<String?>?> {
            val params: MutableMap<String?, MutableList<String?>?> = HashMap()
            val urlParts = url.split("\\?".toRegex()).toTypedArray()
            var query: String? = null
            if (urlParts.size > 1) {
                query = urlParts[1]
                for (param in query.split("&".toRegex()).toTypedArray()) {
                    val pair = param.split("=".toRegex()).toTypedArray()
                    val key = URLDecoder.decode(pair[0], "UTF-8")
                    var value: String? = ""
                    if (pair.size > 1) {
                        value = URLDecoder.decode(pair[1], "UTF-8")
                    }
                    var values = params[key]
                    if (values == null) {
                        values = ArrayList()
                        params[key] = values
                    }
                    values.add(value)
                }
            } else {
                query = if (url.contains(SCHEMA_PRNUMBER)) {
                    url.replace(SCHEMA_PRNUMBER.toRegex(), "")
                } else {
                    url
                }
            }
            for (param in query.split("&".toRegex()).toTypedArray()) {
                val pair = param.split("=".toRegex()).toTypedArray()
                val key = URLDecoder.decode(pair[0], "UTF-8")
                var value: String? = ""
                if (pair.size > 1) {
                    value = URLDecoder.decode(pair[1], "UTF-8")
                }
                var values = params[key]
                if (values == null) {
                    values = ArrayList()
                    params[key] = values
                }
                values.add(value)
            }
            return params
        }
    }
}