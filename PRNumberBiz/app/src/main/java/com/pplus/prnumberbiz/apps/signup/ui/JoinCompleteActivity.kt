package com.pplus.prnumberbiz.apps.signup.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.pple.pplus.utils.part.utils.StringUtils
import com.pplus.prnumberbiz.Const
import com.pplus.prnumberbiz.R
import com.pplus.prnumberbiz.apps.common.mgmt.AppInfoManager
import com.pplus.prnumberbiz.apps.common.mgmt.LoginInfoManager
import com.pplus.prnumberbiz.apps.common.ui.base.BaseActivity
import com.pplus.prnumberbiz.apps.goods.ui.SellerApplyActivity
import com.pplus.prnumberbiz.core.network.ApiBuilder
import com.pplus.prnumberbiz.core.network.model.dto.Device
import com.pplus.prnumberbiz.core.network.model.dto.InstalledApp
import com.pplus.prnumberbiz.core.network.model.dto.User
import com.pplus.prnumberbiz.core.network.model.request.params.ParamsRegDevice
import com.pplus.prnumberbiz.core.network.model.response.NewResultResponse
import com.pplus.prnumberbiz.core.util.PplusCommonUtil
import kotlinx.android.synthetic.main.activity_join_complete.*
import network.common.PplusCallback
import retrofit2.Call
import java.util.*

class JoinCompleteActivity : BaseActivity() {
    override fun getPID(): String {
        return ""
    }

    override fun getLayoutRes(): Int {
        return R.layout.activity_join_complete
    }

    override fun initializeView(savedInstanceState: Bundle?) {
        text_join_complete_make.setOnClickListener {
            val params: MutableMap<String, String> = HashMap()
            params["loginId"] = LoginInfoManager.getInstance().user.loginId!!
            params["password"] = PplusCommonUtil.decryption(LoginInfoManager.getInstance().user.password!!)
            login(params)
        }
    }

    private fun login(params: Map<String, String>) {
        showProgress("")
        ApiBuilder.create().login(params).setCallback(object : PplusCallback<NewResultResponse<User>> {

            override fun onResponse(call: Call<NewResultResponse<User>>?, response: NewResultResponse<User>?) {
                if (isFinishing) {
                    return
                }
                val data = response!!.data
                data.password = PplusCommonUtil.encryption(params["password"]!!)
                hideProgress()
                LoginInfoManager.getInstance().user = data
                LoginInfoManager.getInstance().save()

                PplusCommonUtil.alertAlarmAgree(object : PplusCommonUtil.Companion.AlarmAgreeListener {
                    override fun result(pushActivate: Boolean, pushMask: String) {
                        registDevice(data.no, pushActivate, pushMask)
                    }
                })
            }

            override fun onFailure(call: Call<NewResultResponse<User>>?, t: Throwable?, response: NewResultResponse<User>?) {
                if (isFinishing) {
                    return
                }
                hideProgress()
                showAlert(R.string.login_fail_general)
            }
        }).build().call()
    }

    private fun registDevice(no: Long?, pushActivate: Boolean, pushMask: String) {

        val params = ParamsRegDevice()
        params.no = no

        val installedApp = InstalledApp()
        installedApp.appKey = packageName
        installedApp.version = AppInfoManager.getInstance().appVersion
        installedApp.isPushActivate = pushActivate
        if (StringUtils.isNotEmpty(pushMask)) {
            installedApp.pushMask = pushMask
        }

        installedApp.pushKey = AppInfoManager.getInstance().pushToken
        val device = Device()
        device.deviceId = PplusCommonUtil.getDeviceID()
        device.platform = "aos"
        device.installedApp = installedApp
        params.device = device
        showProgress("")
        ApiBuilder.create().registDevice(params).setCallback(object : PplusCallback<NewResultResponse<User>> {

            override fun onResponse(call: Call<NewResultResponse<User>>, response: NewResultResponse<User>) {

                hideProgress()
                saveDevice(response.data)
                AppInfoManager.getInstance().isAlimAgree = true

                val intent = Intent(this@JoinCompleteActivity, SellerApplyActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                startActivityForResult(intent, Const.REQ_APPLY)
            }

            override fun onFailure(call: Call<NewResultResponse<User>>, t: Throwable, response: NewResultResponse<User>) {

                hideProgress()
            }
        }).build().call()
    }

    private fun saveDevice(data: User) {

        LoginInfoManager.getInstance().user.device = data.device
        LoginInfoManager.getInstance().user.talkDenyDay = data.talkDenyDay
        LoginInfoManager.getInstance().user.talkDenyStartTime = data.talkDenyStartTime
        LoginInfoManager.getInstance().user.talkDenyEndTime = data.talkDenyEndTime
        LoginInfoManager.getInstance().user.modDate = data.modDate
        LoginInfoManager.getInstance().user.sessionKey = data.sessionKey
        LoginInfoManager.getInstance().save()
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                Const.REQ_APPLY -> {
                    setResult(Activity.RESULT_OK)
                    finish()
                }
            }
        }
    }

    override fun onBackPressed() {

    }
}
