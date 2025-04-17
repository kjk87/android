package com.pplus.prnumberbiz.apps.cash.ui

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
import com.pplus.prnumberbiz.core.network.model.dto.Cash
import kotlinx.android.synthetic.main.activity_point_history_detail.*
import kotlinx.android.synthetic.main.item_point_detail.view.*
import java.text.SimpleDateFormat

class CashHistoryDetailActivity : BaseActivity(), ImplToolbar {
    override fun getPID(): String {
        return ""
    }

    override fun getLayoutRes(): Int {
        return R.layout.activity_point_history_detail
    }

    var mCash: Cash? = null

    override fun initializeView(savedInstanceState: Bundle?) {
        mCash = intent.getParcelableExtra(Const.DATA)

        text_point_history_mobile_gift_history_description.visibility = View.GONE

        when (mCash!!.secondaryType!!) {

            EnumData.CashType.useTargetPush.name, EnumData.CashType.usePush.name, EnumData.CashType.useSms.name, EnumData.CashType.useLBS.name, EnumData.CashType.useAdKeyword.name,
            EnumData.CashType.refundAdmin.name, EnumData.CashType.buyBol.name, EnumData.CashType.useAdvertise.name -> {
                //사용
                setTitle(getString(R.string.word_use_history_detail))
                text_point_history_detail_title1.setText(R.string.word_use_cash)
                text_point_history_detail_title2.setText(R.string.word_use_date)
                text_point_history_detail_contents1.setTextColor(ResourceUtil.getColor(this, R.color.color_ff0000))
            }
            EnumData.CashType.buy.name, EnumData.CashType.recvAdmin.name, EnumData.CashType.refundMsgFail.name, EnumData.CashType.cancelSendMsg.name, EnumData.CashType.refundAdvertise.name, EnumData.CashType.refundMsgFail.name -> {
                //적립
                setTitle(getString(R.string.word_charge_history_detail))
                text_point_history_detail_title1.setText(R.string.word_charge_cash2)
                text_point_history_detail_title2.setText(R.string.word_charge_date)
                text_point_history_detail_contents1.setTextColor(ResourceUtil.getColor(this, R.color.color_579ffb))
            }
        }

        text_point_history_detail_contents1.text = FormatUtil.getMoneyType(mCash!!.amount)
        try {
            val d = DateFormatUtils.PPLUS_DATE_FORMAT.parse(mCash!!.regDate)

            val output = SimpleDateFormat("yyyy.MM.dd")
            text_point_history_detail_contents2.text = output.format(d)

        } catch (e: Exception) {

        }

        val entries = mCash!!.properties?.entrySet()

        if (entries != null) {
            for (entry in entries) {
                val view = layoutInflater.inflate(R.layout.item_point_detail, LinearLayout(this))
                view.text_point_detail_title.text = entry.key
                view.text_point_detail_contents.text = entry.value.asString
                layout_point_history_item.addView(view)
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
