package com.pplus.prnumberbiz.apps.pages.ui

import android.os.Bundle
import android.widget.CompoundButton
import com.pplus.prnumberbiz.R
import com.pplus.prnumberbiz.apps.common.mgmt.LoginInfoManager
import com.pplus.prnumberbiz.apps.common.toolbar.ImplToolbar
import com.pplus.prnumberbiz.apps.common.toolbar.OnToolbarListener
import com.pplus.prnumberbiz.apps.common.toolbar.ToolbarOption
import com.pplus.prnumberbiz.apps.common.ui.base.BaseActivity
import com.pplus.prnumberbiz.apps.common.ui.custom.SafeSwitchCompat
import com.pplus.prnumberbiz.core.code.common.PageOpenBoundsCode
import com.pplus.prnumberbiz.core.network.ApiBuilder
import com.pplus.prnumberbiz.core.network.model.dto.Page
import com.pplus.prnumberbiz.core.network.model.response.NewResultResponse
import kotlinx.android.synthetic.main.activity_secret_mode.*
import network.common.PplusCallback
import retrofit2.Call

class SecretModeActivity : BaseActivity(), ImplToolbar {
    override fun getPID(): String {
        return ""
    }

    override fun getLayoutRes(): Int {
        return R.layout.activity_secret_mode
    }

    val mPage = LoginInfoManager.getInstance().user.page

    override fun initializeView(savedInstanceState: Bundle?) {

        when(mPage!!.openBound){
            PageOpenBoundsCode.everybody.name ->{
                switch_secret_mode.setSafeCheck(false, SafeSwitchCompat.IGNORE)
                text_secret_mode.isSelected = false
            }
            else ->{
                switch_secret_mode.setSafeCheck(true, SafeSwitchCompat.IGNORE)
                text_secret_mode.isSelected = true
            }
        }

        switch_secret_mode.onSafeCheckedListener = object : SafeSwitchCompat.OnSafeCheckedListener{
            override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
                if(isChecked){
                    mPage.openBound = PageOpenBoundsCode.nobody.name
                }else{
                    mPage.openBound = PageOpenBoundsCode.everybody.name
                }
                update()
            }

            override fun onAlwaysCalledListener(buttonView: CompoundButton?, isChecked: Boolean) {

            }
        }
    }

    private fun update(){
        showProgress("")
        ApiBuilder.create().updatePage(mPage).setCallback(object : PplusCallback<NewResultResponse<Page>> {

            override fun onResponse(call: Call<NewResultResponse<Page>>, response: NewResultResponse<Page>) {

                hideProgress()
                LoginInfoManager.getInstance().user.page = response.data
                LoginInfoManager.getInstance().save()
                showAlert(R.string.msg_saved)
            }

            override fun onFailure(call: Call<NewResultResponse<Page>>, t: Throwable, response: NewResultResponse<Page>) {

                hideProgress()
            }
        }).build().call()
    }

    override fun getToolbarOption(): ToolbarOption {
        val toolbarOption = ToolbarOption(this)
        toolbarOption.initializeDefaultToolbar(getString(R.string.word_setting_secret_mode), ToolbarOption.ToolbarMenu.LEFT)
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
