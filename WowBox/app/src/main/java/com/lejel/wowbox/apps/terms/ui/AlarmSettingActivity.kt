package com.lejel.wowbox.apps.terms.ui

import android.os.Bundle
import android.view.View
import android.widget.CompoundButton
import com.pplus.networks.common.PplusCallback
import com.pplus.utils.part.pref.PreferenceUtil
import com.lejel.wowbox.Const
import com.lejel.wowbox.R
import com.lejel.wowbox.apps.common.mgmt.LoginInfoManager
import com.lejel.wowbox.apps.common.toolbar.ImplToolbar
import com.lejel.wowbox.apps.common.toolbar.OnToolbarListener
import com.lejel.wowbox.apps.common.toolbar.ToolbarOption
import com.lejel.wowbox.apps.common.ui.base.BaseActivity
import com.lejel.wowbox.apps.common.ui.custom.SafeSwitchCompat
import com.lejel.wowbox.core.network.ApiBuilder
import com.lejel.wowbox.core.network.model.dto.Device
import com.lejel.wowbox.core.network.model.response.NewResultResponse
import com.lejel.wowbox.core.util.PplusCommonUtil
import com.lejel.wowbox.core.util.ToastUtil
import com.lejel.wowbox.databinding.ActivityAlarmSettingBinding
import retrofit2.Call

class AlarmSettingActivity : BaseActivity(), ImplToolbar {

    private lateinit var binding: ActivityAlarmSettingBinding

    override fun getLayoutView(): View {
        binding = ActivityAlarmSettingBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun getPID(): String? {
        return ""
    }

    var mDevice: Device? = null

    override fun initializeView(savedInstanceState: Bundle?) {

        binding.switchAlarmSettingPush.onSafeCheckedListener = object : SafeSwitchCompat.OnSafeCheckedListener {
            override fun onCheckedChanged(p0: CompoundButton?, isChecked: Boolean) {
                postDevice(isChecked)
            }

            override fun onAlwaysCalledListener(buttonView: CompoundButton?, isChecked: Boolean) {

            }
        }

//        if(LoginInfoManager.getInstance().isMember()){
//            binding.layoutAlarmSettingMarketingAgree.visibility = View.VISIBLE
//            val marketingReceiving = LoginInfoManager.getInstance().member!!.marketingReceiving != null && LoginInfoManager.getInstance().member!!.marketingReceiving!!
//            binding.switchAlarmSettingMarketing.setSafeCheck(marketingReceiving, SafeSwitchCompat.IGNORE)
//
//            binding.switchAlarmSettingMarketing.onSafeCheckedListener = object : SafeSwitchCompat.OnSafeCheckedListener {
//                override fun onCheckedChanged(p0: CompoundButton?, isChecked: Boolean) {
//                    updateMarketingService(isChecked)
//                }
//
//                override fun onAlwaysCalledListener(buttonView: CompoundButton?, isChecked: Boolean) {
//
//                }
//            }
//        }else{
//            binding.layoutAlarmSettingMarketingAgree.visibility = View.GONE
//        }

        getDevice()

    }

    private fun getDevice() {
        val params = HashMap<String, String>()
        params["deviceId"] = PplusCommonUtil.getDeviceID()
        showProgress("")
        ApiBuilder.create().getDevice(params).setCallback(object : PplusCallback<NewResultResponse<Device>> {
            override fun onResponse(call: Call<NewResultResponse<Device>>?, response: NewResultResponse<Device>?) {
                hideProgress()
                if (response?.result != null) {
                    mDevice = response.result!!
                    mDevice!!.pushId = PreferenceUtil.getDefaultPreference(this@AlarmSettingActivity).getString(Const.PUSH_TOKEN)
                    binding.switchAlarmSettingPush.setSafeCheck(mDevice!!.pushActivate!!, SafeSwitchCompat.IGNORE)
                }


            }

            override fun onFailure(call: Call<NewResultResponse<Device>>?, t: Throwable?, response: NewResultResponse<Device>?) {

            }
        }).build().call()
    }

    private fun postDevice(pushActivate:Boolean){
        mDevice!!.pushActivate = pushActivate
        showProgress("")
        ApiBuilder.create().postDevice(mDevice!!).setCallback(object : PplusCallback<NewResultResponse<Device>> {
            override fun onResponse(call: Call<NewResultResponse<Device>>?, response: NewResultResponse<Device>?) {
                hideProgress()
                if(pushActivate){
                    ToastUtil.show(this@AlarmSettingActivity, R.string.word_service_alarm_on)
                }else{
                    ToastUtil.show(this@AlarmSettingActivity, R.string.word_service_alarm_off)
                }
            }

            override fun onFailure(call: Call<NewResultResponse<Device>>?, t: Throwable?, response: NewResultResponse<Device>?) {

            }
        }).build().call()
    }

    private fun updateMarketingService(isChecked:Boolean){
        val params = HashMap<String, String>()

        val marketingReceiving:String
        if(isChecked){
            marketingReceiving = "1"
        }else{
            marketingReceiving = "0"
        }
        params["marketingReceiving"] = marketingReceiving
        showProgress("")
        ApiBuilder.create().updateMarketingReceiving(params).setCallback(object : PplusCallback<NewResultResponse<Any>> {
            override fun onResponse(call: Call<NewResultResponse<Any>>?, response: NewResultResponse<Any>?) {
                hideProgress()
                LoginInfoManager.getInstance().member!!.marketingReceiving = isChecked
                LoginInfoManager.getInstance().save()

                if(isChecked){
                    ToastUtil.show(this@AlarmSettingActivity, R.string.word_marketing_alarm_on)
                }else{
                    ToastUtil.show(this@AlarmSettingActivity, R.string.word_marketing_alarm_off)
                }
            }

            override fun onFailure(call: Call<NewResultResponse<Any>>?, t: Throwable?, response: NewResultResponse<Any>?) {
                hideProgress()
            }
        }).build().call()
    }

    override fun getToolbarOption(): ToolbarOption {

        val toolbarOption = ToolbarOption(this)
        toolbarOption.initializeDefaultToolbar(getString(R.string.word_alarm_setting), ToolbarOption.ToolbarMenu.LEFT)
        return toolbarOption
    }

    override fun getOnToolbarClickListener(): OnToolbarListener {

        return object : OnToolbarListener {
            override fun onClick(v: View?, toolbarMenu: ToolbarOption.ToolbarMenu?, tag: Any?) {
                when (toolbarMenu) {
                    ToolbarOption.ToolbarMenu.LEFT -> if (tag == 1) {
                        onBackPressedDispatcher.onBackPressed()
                    }

                    else -> {}
                }
            }
        }
    }
}