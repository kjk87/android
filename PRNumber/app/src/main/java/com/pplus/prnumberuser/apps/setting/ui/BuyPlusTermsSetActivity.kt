//package com.pplus.prnumberuser.apps.setting.ui
//
//import android.app.Activity
//import android.os.Bundle
//import android.widget.CompoundButton
//import com.pplus.prnumberuser.R
//import com.pplus.prnumberuser.apps.common.mgmt.LoginInfoManager
//import com.pplus.prnumberuser.apps.common.toolbar.ImplToolbar
//import com.pplus.prnumberuser.apps.common.toolbar.OnToolbarListener
//import com.pplus.prnumberuser.apps.common.toolbar.ToolbarOption
//import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
//import com.pplus.prnumberuser.apps.common.ui.custom.SafeSwitchCompat
//import com.pplus.prnumberuser.core.network.ApiBuilder
//import com.pplus.prnumberuser.core.network.model.response.NewResultResponse
//import kotlinx.android.synthetic.main.activity_buy_plus_terms_set.*
//import com.pplus.networks.common.PplusCallback
//import retrofit2.Call
//
//class BuyPlusTermsSetActivity : BaseActivity(), ImplToolbar {
//    override fun getPID(): String {
//        return ""
//    }
//
//    override fun getLayoutView(): Int {
//        return R.layout.activity_buy_plus_terms_set
//    }
//
//    override fun initializeView(savedInstanceState: Bundle?) {
//
//        var isCheck = false
//        if(LoginInfoManager.getInstance().user.buyPlusTerms != null){
//            isCheck = LoginInfoManager.getInstance().user.buyPlusTerms!!
//        }
//
//        switch_buy_plus_terms.setSafeCheck(isCheck, SafeSwitchCompat.IGNORE)
//        switch_buy_plus_terms.setOnSafeCheckedListener(object : SafeSwitchCompat.OnSafeCheckedListener{
//            override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
//                val params = HashMap<String, String>()
//                params["buyPlusTerms"] = isChecked.toString()
//                showProgress("")
//                ApiBuilder.create().updateBuyPlusTerms(params).setCallback(object : PplusCallback<NewResultResponse<Any>> {
//                    override fun onResponse(call: Call<NewResultResponse<Any>>?, response: NewResultResponse<Any>?) {
//                        hideProgress()
//                        LoginInfoManager.getInstance().user.buyPlusTerms = isChecked
//                        LoginInfoManager.getInstance().save()
//                    }
//
//                    override fun onFailure(call: Call<NewResultResponse<Any>>?, t: Throwable?, response: NewResultResponse<Any>?) {
//                        hideProgress()
//                    }
//                }).build().call()
//            }
//
//            override fun onAlwaysCalledListener(buttonView: CompoundButton?, isChecked: Boolean) {
//
//            }
//        })
//    }
//
//    override fun getToolbarOption(): ToolbarOption {
//
//        val toolbarOption = ToolbarOption(this)
//        toolbarOption.initializeDefaultToolbar(getString(R.string.word_setting_buy_plus_terms), ToolbarOption.ToolbarMenu.LEFT)
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