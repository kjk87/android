package com.pplus.prnumberuser.apps.prepayment.ui

import android.os.Bundle
import android.view.View
import com.pplus.prnumberuser.Const
import com.pplus.prnumberuser.R
import com.pplus.prnumberuser.apps.common.toolbar.ImplToolbar
import com.pplus.prnumberuser.apps.common.toolbar.OnToolbarListener
import com.pplus.prnumberuser.apps.common.toolbar.ToolbarOption
import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
import com.pplus.prnumberuser.core.network.model.dto.PrepaymentPublish
import com.pplus.prnumberuser.core.util.PplusCommonUtil
import com.pplus.prnumberuser.databinding.ActivityPrepaymentUseBinding
import com.pplus.utils.part.format.FormatUtil

class PrepaymentUseActivity : BaseActivity(), ImplToolbar {
    private lateinit var binding: ActivityPrepaymentUseBinding

    override fun getLayoutView(): View {
        binding = ActivityPrepaymentUseBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun getPID(): String? {
        return ""
    }

    override fun initializeView(savedInstanceState: Bundle?) {
        val prepaymentPublish = intent.getParcelableExtra<PrepaymentPublish>(Const.DATA)
        val usePrice = intent.getIntExtra(Const.PRICE, 0)

        binding.textPrepaymentUseConfirm.setOnClickListener {
            setResult(RESULT_OK)
            finish()
        }

        binding.textPrepaymentUsePrice.text = getString(R.string.format_money_unit, FormatUtil.getMoneyType(usePrice.toString()))

        val remainPrice = prepaymentPublish!!.havePrice!!.toInt() - usePrice
        binding.textPrepaymentUseRemainPrice.text = PplusCommonUtil.fromHtml(getString(R.string.html_remain_price_after_use_price, FormatUtil.getMoneyType(remainPrice.toString())))

//        val params = HashMap<String, String>()
//        params["prepaymentPublishSeqNo"] = prepaymentPublish.seqNo.toString()
//        params["usePrice"] = usePrice.toString()
//        ApiBuilder.create().prepaymentUse(params).setCallback(object : PplusCallback<NewResultResponse<Any>> {
//            override fun onResponse(call: Call<NewResultResponse<Any>>?, response: NewResultResponse<Any>?) {
//
//            }
//
//            override fun onFailure(call: Call<NewResultResponse<Any>>?, t: Throwable?, response: NewResultResponse<Any>?) {
//                if (response?.resultCode == 662) {
//                    showAlert(R.string.msg_exceed_use_price)
//                    setResult(RESULT_OK)
//                    finish()
//                }else if (response?.resultCode == 505) {
//                    showAlert(R.string.msg_not_permission_user)
//                    setResult(RESULT_OK)
//                    finish()
//                }else if (response?.resultCode == 661) {
//                    showAlert(R.string.msg_exist_request_log)
//                    setResult(RESULT_OK)
//                    finish()
//                }
//            }
//        }).build().call()
    }

    override fun getToolbarOption(): ToolbarOption {

        val toolbarOption = ToolbarOption(this)
        toolbarOption.initializeDefaultToolbar("", ToolbarOption.ToolbarMenu.LEFT)
        return toolbarOption
    }

    override fun getOnToolbarClickListener(): OnToolbarListener {

        return object : OnToolbarListener {
            override fun onClick(v: View?, toolbarMenu: ToolbarOption.ToolbarMenu?, tag: Any?) {
                when (toolbarMenu) {
                    ToolbarOption.ToolbarMenu.LEFT -> if (tag == 1) {
                        onBackPressed()
                    }
                }
            }
        }
    }
}