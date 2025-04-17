package com.pplus.luckybol.apps.buff.ui

import android.os.Bundle
import android.view.View
import android.widget.CompoundButton
import com.pplus.luckybol.R
import com.pplus.luckybol.apps.common.mgmt.LoginInfoManager
import com.pplus.luckybol.apps.common.toolbar.ImplToolbar
import com.pplus.luckybol.apps.common.toolbar.OnToolbarListener
import com.pplus.luckybol.apps.common.toolbar.ToolbarOption
import com.pplus.luckybol.apps.common.ui.base.BaseActivity
import com.pplus.luckybol.apps.common.ui.custom.SafeSwitchCompat
import com.pplus.luckybol.core.network.ApiBuilder
import com.pplus.luckybol.core.network.model.response.NewResultResponse
import com.pplus.luckybol.databinding.ActivityBuffPostHiddenBinding
import com.pplus.networks.common.PplusCallback
import com.pplus.utils.part.apps.resource.ResourceUtil
import retrofit2.Call

class BuffPostHiddenActivity : BaseActivity(), ImplToolbar {

    private lateinit var binding: ActivityBuffPostHiddenBinding

    override fun getLayoutView(): View {
        binding = ActivityBuffPostHiddenBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun getPID(): String? {
        return ""
    }

    override fun initializeView(savedInstanceState: Bundle?) {

        val buffPostPublic = LoginInfoManager.getInstance().user!!.buffPostPublic
        if(buffPostPublic == null ||  buffPostPublic){
            binding.switchBuffPostHidden.setSafeCheck(true, SafeSwitchCompat.IGNORE)
            binding.textBuffPostHidden.text = getString(R.string.word_on_en)
            binding.textBuffPostHidden.setTextColor(ResourceUtil.getColor(this, R.color.color_fc5c57))
        }else{
            binding.switchBuffPostHidden.setSafeCheck(false, SafeSwitchCompat.IGNORE)
            binding.textBuffPostHidden.text = getString(R.string.word_off_en)
            binding.textBuffPostHidden.setTextColor(ResourceUtil.getColor(this, R.color.color_a9b0b7))
        }

        binding.switchBuffPostHidden.onSafeCheckedListener = object : SafeSwitchCompat.OnSafeCheckedListener{
            override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
                updateBuffPostPublic(isChecked)
            }

            override fun onAlwaysCalledListener(buttonView: CompoundButton?, isChecked: Boolean) {

            }
        }
    }

    private fun updateBuffPostPublic(isPublic:Boolean){
        val params = HashMap<String, String>()
        params["buffPostPublic"] = isPublic.toString()
        showProgress("")
        ApiBuilder.create().updateBuffPostPublic(params).setCallback(object : PplusCallback<NewResultResponse<Any>>{
            override fun onResponse(call: Call<NewResultResponse<Any>>?,
                                    response: NewResultResponse<Any>?) {
                hideProgress()
            }

            override fun onFailure(call: Call<NewResultResponse<Any>>?,
                                   t: Throwable?,
                                   response: NewResultResponse<Any>?) {
                hideProgress()
            }
        }).build().call()
    }

    override fun getToolbarOption(): ToolbarOption {
        val toolbarOption = ToolbarOption(this)
        toolbarOption.initializeDefaultToolbar(getString(R.string.word_buff_post_hidden_title), ToolbarOption.ToolbarMenu.LEFT)

        return toolbarOption
    }

    override fun getOnToolbarClickListener(): OnToolbarListener {

        return OnToolbarListener { v, toolbarMenu, tag ->
            when (toolbarMenu) {
                ToolbarOption.ToolbarMenu.LEFT -> if (tag == 1) {
                    onBackPressed()
                }
                else -> {}
            }
        }
    }
}