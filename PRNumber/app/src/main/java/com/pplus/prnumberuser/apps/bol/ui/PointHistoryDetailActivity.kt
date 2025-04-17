//package com.pplus.prnumberuser.apps.bol.ui
//
//import android.os.Bundle
//import android.view.View
//import android.widget.LinearLayout
//import com.pplus.utils.part.apps.resource.ResourceUtil
//import com.pplus.utils.part.format.FormatUtil
//import com.pplus.utils.part.utils.time.DateFormatUtils
//import com.pplus.prnumberuser.Const
//import com.pplus.prnumberuser.R
//import com.pplus.prnumberuser.apps.common.toolbar.ImplToolbar
//import com.pplus.prnumberuser.apps.common.toolbar.OnToolbarListener
//import com.pplus.prnumberuser.apps.common.toolbar.ToolbarOption
//import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
//import com.pplus.prnumberuser.core.code.common.EnumData
//import com.pplus.prnumberuser.core.network.ApiBuilder
//import com.pplus.prnumberuser.core.network.model.dto.Bol
//import com.pplus.prnumberuser.core.network.model.response.NewResultResponse
//import kotlinx.android.synthetic.main.activity_point_history_detail.*
//import kotlinx.android.synthetic.main.item_point_detail.view.*
//import com.pplus.networks.common.PplusCallback
//import retrofit2.Call
//import java.text.SimpleDateFormat
//import java.util.*
//
//class PointHistoryDetailActivity : BaseActivity(), ImplToolbar {
//    override fun getPID(): String {
//        return ""
//    }
//
//    override fun getLayoutRes(): Int {
//        return R.layout.activity_point_history_detail
//    }
//
//    var mBol: Bol? = null
//
//    override fun initializeView(savedInstanceState: Bundle?) {
//        mBol = intent.getParcelableExtra(Const.DATA)
//
//        text_point_history_mobile_gift_history_description.visibility = View.GONE
//        getHistory()
//    }
//
//    fun getHistory() {
//        val params = HashMap<String, String>()
//        params["no"] = mBol!!.no.toString()
//        showProgress("")
//        ApiBuilder.create().getBolHistoryWithTarget(params).setCallback(object : PplusCallback<NewResultResponse<Bol>> {
//            override fun onResponse(call: Call<NewResultResponse<Bol>>?, response: NewResultResponse<Bol>?) {
//                hideProgress()
//                if (isFinishing) {
//                    return
//                }
//
//                mBol = response!!.data
//                if(mBol != null){
//                    setData()
//                }
//            }
//
//            override fun onFailure(call: Call<NewResultResponse<Bol>>?, t: Throwable?, response: NewResultResponse<Bol>?) {
//                hideProgress()
//            }
//        }).build().call()
//    }
//
//    fun setData() {
//        when (EnumData.BolType.valueOf(mBol!!.secondaryType!!)) {
//
//            EnumData.BolType.buyCancel, EnumData.BolType.giftBol, EnumData.BolType.giftBols, EnumData.BolType.exchange, EnumData.BolType.buyMobileGift, EnumData.BolType.sendPush,
//            EnumData.BolType.rewardComment, EnumData.BolType.reqExchange, EnumData.BolType.purchaseMobileGift, EnumData.BolType.useAdvertise, EnumData.BolType.joinReduceEvent, EnumData.BolType.refundAdmin -> {
//                //사용
//                setTitle(getString(R.string.word_use_history_detail))
//                text_point_history_detail_title1.setText(R.string.word_use_point)
//                if(mBol!!.secondaryType == EnumData.BolType.exchange.name){
//                    text_point_history_detail_title2.setText(R.string.word_req_date)
//                }else{
//                    text_point_history_detail_title2.setText(R.string.word_use_date)
//                }
//                text_point_history_detail_contents1.setTextColor(ResourceUtil.getColor(this, R.color.color_ff0000))
//            }
//            EnumData.BolType.buy, EnumData.BolType.winEvent, EnumData.BolType.invite, EnumData.BolType.invitee, EnumData.BolType.recvPush, EnumData.BolType.rewardReview,
//            EnumData.BolType.review, EnumData.BolType.comment, EnumData.BolType.recvGift, EnumData.BolType.denyExchange, EnumData.BolType.denyRecv, EnumData.BolType.winAdvertise, EnumData.BolType.refundAdvertise,
//            EnumData.BolType.joinEvent, EnumData.BolType.adpcReward, EnumData.BolType.winRecommend, EnumData.BolType.joinMember, EnumData.BolType.refundJoinEvent, EnumData.BolType.admin,
//            EnumData.BolType.refundMobileGift, EnumData.BolType.buyGoodsUse -> {
//                //적립
//                setTitle(getString(R.string.word_charge_history_detail))
//                text_point_history_detail_title1.setText(R.string.word_reward_point)
//                text_point_history_detail_title2.setText(R.string.word_earn_date)
//                text_point_history_detail_contents1.setTextColor(ResourceUtil.getColor(this, R.color.color_579ffb))
//            }
//        }
//
//        text_point_history_detail_contents1.text = getString(R.string.format_bol_unit, FormatUtil.getMoneyType(mBol!!.amount))
//        try {
//            val d = DateFormatUtils.PPLUS_DATE_FORMAT.parse(mBol!!.regDate)
//
//            val output = SimpleDateFormat("yyyy.MM.dd")
//            text_point_history_detail_contents2.text = output.format(d)
//
//        } catch (e: Exception) {
//            text_point_history_detail_contents2.text = ""
//        }
//
//        val entries = mBol!!.properties?.entrySet()
//
//        if (entries != null) {
//            for (entry in entries) {
//                val view = layoutInflater.inflate(R.layout.item_point_detail, LinearLayout(this))
//                view.text_point_detail_title.text = entry.key
//                view.text_point_detail_contents.text = entry.value.asString
//                layout_point_history_item.addView(view)
//            }
//        }
//
//        when (EnumData.BolType.valueOf(mBol!!.secondaryType!!)) {
//            EnumData.BolType.purchaseMobileGift -> {
//                text_point_history_mobile_gift_history_description.visibility = View.VISIBLE
//            }
//        }
//    }
//
//    override fun getToolbarOption(): ToolbarOption {
//
//        val toolbarOption = ToolbarOption(this)
//        toolbarOption.initializeDefaultToolbar(getString(R.string.word_point_config), ToolbarOption.ToolbarMenu.LEFT)
//        return toolbarOption
//    }
//
//    override fun getOnToolbarClickListener(): OnToolbarListener {
//
//        return OnToolbarListener { v, toolbarMenu, tag ->
//            when (toolbarMenu) {
//                ToolbarOption.ToolbarMenu.LEFT -> if (tag == 1) {
//                    onBackPressed()
//                }
//            }
//        }
//    }
//}
