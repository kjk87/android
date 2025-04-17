package com.pplus.prnumberbiz.apps.bol.ui

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import com.pple.pplus.utils.part.apps.resource.ResourceUtil
import com.pple.pplus.utils.part.format.FormatUtil
import com.pple.pplus.utils.part.utils.time.DateFormatUtils
import com.pplus.prnumberbiz.Const
import com.pplus.prnumberbiz.R
import com.pplus.prnumberbiz.apps.common.toolbar.ImplToolbar
import com.pplus.prnumberbiz.apps.common.toolbar.OnToolbarListener
import com.pplus.prnumberbiz.apps.common.toolbar.ToolbarOption
import com.pplus.prnumberbiz.apps.common.ui.base.BaseActivity
import com.pplus.prnumberbiz.core.code.common.EnumData
import com.pplus.prnumberbiz.core.network.model.dto.Bol
import kotlinx.android.synthetic.main.activity_point_history_detail.*
import kotlinx.android.synthetic.main.item_point_detail.view.*
import java.text.SimpleDateFormat

class PointHistoryDetailActivity : BaseActivity(), ImplToolbar {
    override fun getPID(): String {
        return ""
    }

    override fun getLayoutRes(): Int {
        return R.layout.activity_point_history_detail
    }

    var mBol: Bol? = null

    override fun initializeView(savedInstanceState: Bundle?) {
        mBol = intent.getParcelableExtra(Const.DATA)

        text_point_history_mobile_gift_history_description.visibility = View.GONE

        when (EnumData.BolType.valueOf(mBol!!.secondaryType!!)) {

            EnumData.BolType.giftBol, EnumData.BolType.giftBols, EnumData.BolType.exchange, EnumData.BolType.buyMobileGift, EnumData.BolType.sendPush,
            EnumData.BolType.rewardReview, EnumData.BolType.rewardComment, EnumData.BolType.reqExchange, EnumData.BolType.purchaseMobileGift, EnumData.BolType.useAdvertise, EnumData.BolType.joinReduceEvent, EnumData.BolType.refundAdmin -> {
                //사용
                setTitle(getString(R.string.word_use_history_detail))
                text_point_history_detail_title1.setText(R.string.word_use_point)
                if(mBol!!.secondaryType == EnumData.BolType.exchange.name){
                    text_point_history_detail_title2.setText(R.string.word_req_date)
                }else{
                    text_point_history_detail_title2.setText(R.string.word_use_date)
                }

                text_point_history_detail_contents1.setTextColor(ResourceUtil.getColor(this, R.color.color_ff0000))
            }
            EnumData.BolType.buy, EnumData.BolType.winEvent, EnumData.BolType.invite, EnumData.BolType.invitee, EnumData.BolType.recvPush,
            EnumData.BolType.review, EnumData.BolType.comment, EnumData.BolType.recvGift, EnumData.BolType.denyExchange, EnumData.BolType.denyRecv, EnumData.BolType.winAdvertise, EnumData.BolType.refundAdvertise
                , EnumData.BolType.joinEvent, EnumData.BolType.adpcReward, EnumData.BolType.winRecommend, EnumData.BolType.joinMember, EnumData.BolType.refundJoinEvent, EnumData.BolType.admin -> {
                //적립
                setTitle(getString(R.string.word_earn_history_detail))
                text_point_history_detail_title1.setText(R.string.word_reward_point)
                text_point_history_detail_title2.setText(R.string.word_earn_date)
                text_point_history_detail_contents1.setTextColor(ResourceUtil.getColor(this, R.color.color_579ffb))
            }
        }

        text_point_history_detail_contents1.text = FormatUtil.getMoneyType(mBol!!.amount)
        try {
            val d = DateFormatUtils.PPLUS_DATE_FORMAT.parse(mBol!!.regDate)

            val output = SimpleDateFormat("yyyy.MM.dd")
            text_point_history_detail_contents2.text = output.format(d)

        } catch (e: Exception) {

        }

        val entries = mBol!!.properties?.entrySet()

        if (entries != null) {
            for (entry in entries) {
                val view = layoutInflater.inflate(R.layout.item_point_detail, LinearLayout(this))
                view.text_point_detail_title.text = entry.key
                view.text_point_detail_contents.text = entry.value.asString
                layout_point_history_item.addView(view)
            }
        }

        when (EnumData.BolType.valueOf(mBol!!.secondaryType!!)) {
            EnumData.BolType.purchaseMobileGift -> {
                text_point_history_mobile_gift_history_description.visibility = View.VISIBLE
            }
        }
    }

    override fun getToolbarOption(): ToolbarOption {

        val toolbarOption = ToolbarOption(this)
        toolbarOption.initializeDefaultToolbar(getString(R.string.word_point_config), ToolbarOption.ToolbarMenu.LEFT)
        return toolbarOption
    }

    override fun getOnToolbarClickListener(): OnToolbarListener {

        return OnToolbarListener { v, toolbarMenu, tag ->
            when (toolbarMenu) {
                ToolbarOption.ToolbarMenu.LEFT -> if (tag == 1) {
                    onBackPressed()
                }
            }
        }
    }
}
