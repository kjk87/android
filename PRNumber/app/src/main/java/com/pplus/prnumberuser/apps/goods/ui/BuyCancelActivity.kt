//package com.pplus.prnumberuser.apps.goods.ui
//
//import android.app.Activity
//import android.os.Bundle
//import com.pplus.utils.part.utils.StringUtils
//import com.pplus.prnumberuser.Const
//import com.pplus.prnumberuser.R
//import com.pplus.prnumberuser.apps.common.toolbar.ImplToolbar
//import com.pplus.prnumberuser.apps.common.toolbar.OnToolbarListener
//import com.pplus.prnumberuser.apps.common.toolbar.ToolbarOption
//import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
//import com.pplus.prnumberuser.core.code.common.EnumData
//import com.pplus.prnumberuser.core.network.ApiBuilder
//import com.pplus.prnumberuser.core.network.model.dto.BuyGoods
//import com.pplus.prnumberuser.core.network.model.response.NewResultResponse
//import kotlinx.android.synthetic.main.activity_buy_cancel.*
//import com.pplus.networks.common.PplusCallback
//import retrofit2.Call
//
//class BuyCancelActivity : BaseActivity(), ImplToolbar {
//    override fun getPID(): String {
//        return ""
//    }
//
//    override fun getLayoutRes(): Int {
//        return R.layout.activity_buy_cancel
//    }
//
//    override fun initializeView(savedInstanceState: Bundle?) {
//
//        val buyGoods = intent.getParcelableExtra<BuyGoods>(Const.DATA)
//
//        text_buy_cancel.setOnClickListener {
//            val memo = edit_buy_cancel_memo.text.toString().trim()
//            if(StringUtils.isEmpty(memo)){
//                showAlert(R.string.msg_input_buy_cancel_memo)
//                return@setOnClickListener
//            }
//
//            if(memo.length < 10){
//                showAlert(getString(R.string.format_msg_input_over, 10))
//                return@setOnClickListener
//            }
//
//            process(EnumData.BuyGoodsProcess.CANCEL_WAIT.process, buyGoods.seqNo!!, memo)
//        }
//
//    }
//
//    private fun process(process: Int, seqNo: Long, memo:String) {
//        val params = HashMap<String, String>()
//        params["seqNo"] = seqNo.toString()
//        params["process"] = process.toString()
//        showProgress("")
//        ApiBuilder.create().buyGoodsProcess(params).setCallback(object : PplusCallback<NewResultResponse<Any>> {
//            override fun onResponse(call: Call<NewResultResponse<Any>>?, response: NewResultResponse<Any>?) {
//                hideProgress()
//                showAlert(R.string.msg_complete_cancel_apply)
//                setResult(Activity.RESULT_OK)
//                finish()
//            }
//
//            override fun onFailure(call: Call<NewResultResponse<Any>>?, t: Throwable?, response: NewResultResponse<Any>?) {
//                hideProgress()
//            }
//        }).build().call()
//    }
//
//    override fun getToolbarOption(): ToolbarOption {
//
//        val toolbarOption = ToolbarOption(this)
//        toolbarOption.initializeDefaultToolbar(getString(R.string.word_apply_buy_cancel), ToolbarOption.ToolbarMenu.LEFT)
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
