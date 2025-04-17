package com.lejel.wowbox.apps

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.android.installreferrer.api.InstallReferrerClient
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.messaging.FirebaseMessaging
import com.lejel.wowbox.Const
import com.lejel.wowbox.R
import com.lejel.wowbox.WowBoxApplication
import com.lejel.wowbox.apps.common.builder.AlertBuilder
import com.lejel.wowbox.apps.common.builder.OnAlertResultListener
import com.lejel.wowbox.apps.common.builder.data.AlertData
import com.lejel.wowbox.apps.common.mgmt.LoginInfoManager
import com.lejel.wowbox.apps.common.ui.base.BaseActivity
import com.lejel.wowbox.apps.login.WithdrawalCancelAuthActivity
import com.lejel.wowbox.apps.main.ui.MainActivity
import com.lejel.wowbox.apps.terms.ui.AddTermsAgreeActivity
import com.lejel.wowbox.core.network.ApiBuilder
import com.lejel.wowbox.core.network.ApiController
import com.lejel.wowbox.core.network.model.dto.App
import com.lejel.wowbox.core.network.model.dto.Member
import com.lejel.wowbox.core.network.model.dto.ServerStatus
import com.lejel.wowbox.core.network.model.dto.Terms
import com.lejel.wowbox.core.network.model.response.ListResultResponse
import com.lejel.wowbox.core.network.model.response.NewResultResponse
import com.lejel.wowbox.core.util.PplusCommonUtil
import com.lejel.wowbox.databinding.ActivityLauncherScreenBinding
import com.lejel.wowbox.push.PushReceiveData
import com.pplus.networks.common.PplusCallback
import com.pplus.utils.Config
import com.pplus.utils.part.logs.LogUtil
import com.pplus.utils.part.pref.PreferenceUtil
import com.pplus.utils.part.utils.StringUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LauncherScreenActivity : BaseActivity() {


    private lateinit var binding: ActivityLauncherScreenBinding

    override fun getLayoutView(): View {
        binding = ActivityLauncherScreenBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun getPID(): String? {
        return ""
    }

    private lateinit var referrerClient: InstallReferrerClient

    override fun initializeView(savedInstanceState: Bundle?) {

        enableEdgeToEdge()

        setEvent(FirebaseAnalytics.Event.APP_OPEN)
        if (Const.DEBUG_MODE) {
            Config.setLogEnable(Config.CONFIG.ON)
        }

        val handler = Handler(Looper.myLooper()!!)
        handler.postDelayed({ appStart() }, 1000)
    }

    private fun appStart() {
        if (!PplusCommonUtil.hasNetworkConnection()) {
            val builder = AlertBuilder.Builder()
            builder.setTitle(getString(R.string.word_notice_alert))
            builder.setStyle_alert(AlertBuilder.STYLE_ALERT.MESSAGE)
            builder.addContents(AlertData.MessageData(getString(R.string.msg_disconnected_network), AlertBuilder.MESSAGE_TYPE.TEXT, 1))
            builder.addContents(AlertData.MessageData(getString(R.string.msg_check_network_status), AlertBuilder.MESSAGE_TYPE.TEXT, 2))
            builder.setRightText(getString(R.string.word_confirm))
            builder.setOnAlertResultListener(object : OnAlertResultListener {

                override fun onCancel() {

                    finish()
                }

                override fun onResult(event_alert: AlertBuilder.EVENT_ALERT) {

                    finish()
                }
            })
            builder.builder().show(this)
            return
        }

        val data = intent.data

        if (data != null) {
            WowBoxApplication.schemaData = data.toString()
        }

        if (checkPlayServices()) {
            LoginInfoManager.getInstance()

            FirebaseMessaging.getInstance().token.addOnSuccessListener { token ->
                LogUtil.e(LOG_TAG, "new token : {}", token)
                PreferenceUtil.getDefaultPreference(this@LauncherScreenActivity).put(Const.PUSH_TOKEN, token)
                serverStatus()
            }
        }
    }

    //    private fun getDevice() {
    //        val params = HashMap<String, String>()
    //        params["deviceId"] = PplusCommonUtil.getDeviceID()
    //        ApiBuilder.create().getDevice(params).setCallback(object : PplusCallback<NewResultResponse<Device>> {
    //            override fun onResponse(call: Call<NewResultResponse<Device>>?, response: NewResultResponse<Device>?) {
    //                val device: Device
    //                if (response?.result != null) {
    //                    device = response.result!!
    //                    device.pushId = PreferenceUtil.getDefaultPreference(this@LauncherScreenActivity).getString(Const.PUSH_TOKEN)
    //
    //                } else {
    //                    device = Device()
    //                    device.deviceId = PplusCommonUtil.getDeviceID()
    //                    device.pushId = PreferenceUtil.getDefaultPreference(this@LauncherScreenActivity).getString(Const.PUSH_TOKEN)
    //                    device.pushActivate = false
    //                }
    //
    //                ApiBuilder.create().postDevice(device).setCallback(object : PplusCallback<NewResultResponse<Device>> {
    //                    override fun onResponse(call: Call<NewResultResponse<Device>>?, response: NewResultResponse<Device>?) {
    //
    //                    }
    //
    //                    override fun onFailure(call: Call<NewResultResponse<Device>>?, t: Throwable?, response: NewResultResponse<Device>?) {
    //
    //                    }
    //                }).build().call()
    //            }
    //
    //            override fun onFailure(call: Call<NewResultResponse<Device>>?, t: Throwable?, response: NewResultResponse<Device>?) {
    //
    //            }
    //        }).build().call()
    //    }

    private fun serverStatus() {
        ApiController.cdnApi.getServerStatus(System.currentTimeMillis().toString()).enqueue(object : Callback<ServerStatus> {
            override fun onResponse(call: Call<ServerStatus>, response: Response<ServerStatus>) {
                val status = response.body()
                if (status != null) {
                    if (status.code == 200) {
                        checkAppVersion()
                    } else {
                        if (StringUtils.isNotEmpty(status.message)) {
                            showAlert(status.message)
                        } else {
                            showAlert(R.string.msg_server_checking)
                        }

                        finish()
                    }
                } else {
                    showAlert(R.string.msg_server_checking)
                    finish()
                }

            }

            override fun onFailure(call: Call<ServerStatus>, t: Throwable) {
                showAlert(R.string.msg_server_checking)
                finish()
            }
        })
    }

    private fun checkAppVersion() {
        val params = HashMap<String, String>()
        params["version"] = PplusCommonUtil.getAppVersion(this)
        params["platform"] = "aos"

        showProgress("")
        ApiBuilder.create().getApp(params).setCallback(object : PplusCallback<NewResultResponse<App>> {
            override fun onResponse(call: Call<NewResultResponse<App>>?, response: NewResultResponse<App>?) {
                hideProgress()
                if (response?.result != null) {
                    val app = response.result!!


                    var isUpdate = PplusCommonUtil.isVersionUpdate(this@LauncherScreenActivity, app.version)

                    if (isUpdate != -1 && app.isVital != null && app.isVital!!) {
                        isUpdate = 1
                    }

                    if (app.isOpen == null || !app.isOpen!!) {
                        isUpdate = -1
                    }

                    if (isUpdate == 0) {

                        val builder = AlertBuilder.Builder()
                        builder.setTitle(getString(R.string.word_notice_alert))
                        builder.addContents(AlertData.MessageData(getString(R.string.msg_minor_update), AlertBuilder.MESSAGE_TYPE.TEXT, 2))
                        builder.setLeftText(getString(R.string.word_cancel)).setRightText(getString(R.string.word_update))
                        builder.setOnAlertResultListener(object : OnAlertResultListener {

                            override fun onCancel() {

                                isUpdate = -1
                                userLoginCheck()
                            }

                            override fun onResult(event_alert: AlertBuilder.EVENT_ALERT) {

                                when (event_alert) {
                                    AlertBuilder.EVENT_ALERT.RIGHT -> { // Creates instance of the manager.
                                        val it = Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=$packageName"))
                                        startActivity(it)

                                    }

                                    AlertBuilder.EVENT_ALERT.LEFT -> {
                                        isUpdate = -1
                                        userLoginCheck()
                                    }

                                    else -> {}
                                }
                            }
                        }).builder().show(this@LauncherScreenActivity)
                    } else if (isUpdate == 1) {

                        val builder = AlertBuilder.Builder()
                        builder.setTitle(getString(R.string.word_notice_alert))
                        builder.addContents(AlertData.MessageData(getString(R.string.msg_major_update), AlertBuilder.MESSAGE_TYPE.TEXT, 3))
                        builder.setAutoCancel(false)
                        builder.setBackgroundClickable(false)
                        builder.setRightText(getString(R.string.word_update))
                        builder.setOnAlertResultListener(object : OnAlertResultListener {

                            override fun onCancel() {

                            }

                            override fun onResult(event_alert: AlertBuilder.EVENT_ALERT) {

                                when (event_alert) {
                                    AlertBuilder.EVENT_ALERT.SINGLE -> {

                                        val it = Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=$packageName"))
                                        startActivity(it)
                                    }

                                    else -> {}
                                }
                            }
                        }).builder().show(this@LauncherScreenActivity)
                    } else {
                        userLoginCheck()
                    }
                }
            }

            override fun onFailure(call: Call<NewResultResponse<App>>?, t: Throwable?, response: NewResultResponse<App>?) {
                hideProgress()
            }
        }).build().call()
    }

    fun userLoginCheck() {
        LogUtil.e(LOG_TAG, "userLoginCheck()")
        if (LoginInfoManager.getInstance().isMember()) {
            login()
        } else {
            LoginInfoManager.getInstance().clear()
            try {
                if(isDestroyed){
                    return
                }

                val isFirst = PreferenceUtil.getDefaultPreference(this).get(Const.IS_GUIDE_FIRST, true)
                if (isFirst) {
                    val guideIntent = Intent(this, GuideActivity::class.java)
                    guideIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                    loginLauncher.launch(guideIntent)
                }else{
                    goMainActivity()
                }


            } catch (e: Exception) {
                LogUtil.e(LOG_TAG, e.toString())
                finish()
            }
        }
    }

    private fun login() {
        val params = HashMap<String, String>()
        params["userKey"] = LoginInfoManager.getInstance().member!!.userKey!!
        params["device"] = PplusCommonUtil.getDeviceID()
        showProgress("")
        ApiBuilder.create().login(params).setCallback(object : PplusCallback<NewResultResponse<Member>> {
            override fun onResponse(call: Call<NewResultResponse<Member>>?, response: NewResultResponse<Member>?) {
                hideProgress()
                if (isFinishing) {
                    return
                }

                if (response?.result != null) {

                    if (response.result!!.status == "active") {
                        LoginInfoManager.getInstance().member = response.result
                        LoginInfoManager.getInstance().save()
                        getNotSignedTermsList()
                    } else if (response.result!!.status == "waitingLeave") {
                        val builder = AlertBuilder.Builder()
                        builder.setTitle(getString(R.string.msg_alert_waiting_leave))
                        builder.addContents(AlertData.MessageData(getString(R.string.msg_alert_waiting_leave_desc), AlertBuilder.MESSAGE_TYPE.TEXT, 2))
                        builder.setLeftText(getString(R.string.word_cancel)).setRightText(getString(R.string.word_withdrawal_cancel))
                        builder.setOnAlertResultListener(object : OnAlertResultListener {

                            override fun onCancel() {

                            }

                            override fun onResult(event_alert: AlertBuilder.EVENT_ALERT) {
                                when (event_alert) {
                                    AlertBuilder.EVENT_ALERT.RIGHT -> {
                                        val intent = Intent(this@LauncherScreenActivity, WithdrawalCancelAuthActivity::class.java)
                                        intent.putExtra(Const.DATA, response.result)
                                        startActivity(intent)
                                    }

                                    else -> {

                                    }
                                }
                            }
                        }).builder().show(this@LauncherScreenActivity)
                    }

                } else {
                    LoginInfoManager.getInstance().clear()
                    goMainActivity()
                }
            }

            override fun onFailure(call: Call<NewResultResponse<Member>>?, t: Throwable?, response: NewResultResponse<Member>?) {
                LoginInfoManager.getInstance().clear()
                goMainActivity()
            }
        }).build().call()
    }

    private fun getNotSignedTermsList() {
        showProgress("")
        ApiBuilder.create().getNotSignedList().setCallback(object : PplusCallback<NewResultResponse<ListResultResponse<Terms>>> {
            override fun onResponse(call: Call<NewResultResponse<ListResultResponse<Terms>>>?, response: NewResultResponse<ListResultResponse<Terms>>?) {
                hideProgress()
                if (response?.result != null && response.result!!.list != null) {
                    for (terms in response.result!!.list!!) {
                        if (terms.compulsory!!) {
                            val intent = Intent(this@LauncherScreenActivity, AddTermsAgreeActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
                            termsAgreeLauncher.launch(intent)
                            return
                        }
                    }
                }
                goMainActivity()
            }

            override fun onFailure(call: Call<NewResultResponse<ListResultResponse<Terms>>>?, t: Throwable?, response: NewResultResponse<ListResultResponse<Terms>>?) {
                hideProgress()
            }
        }).build().call()
    }

    private fun goMainActivity() {
        val recvIntent = intent
        val data = recvIntent.data //        recvIntent.setClass(this@LauncherScreenActivity, MainActivity::class.java)
        recvIntent.setClass(this@LauncherScreenActivity, MainActivity::class.java)
        if (data != null) { // 스키마로 들어온 경우 처리 예정
            recvIntent.putExtra("schemaUrl", data.toString())
            recvIntent.putExtra(Const.PUSH_DATA, PplusCommonUtil.getParcelableExtra(intent, Const.PUSH_DATA, PushReceiveData::class.java))

        }
        recvIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        startActivity(recvIntent)
        finish()
    }

    fun checkPlayServices(): Boolean {

        val apiAvailability = GoogleApiAvailability.getInstance()
        val resultCode = apiAvailability.isGooglePlayServicesAvailable(this)
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, 9000)?.show()
            } else {
                LogUtil.e(LOG_TAG, "This device is not supported.")
                finish()
            }
            return false
        }
        return true
    }

    private fun checkSelfPermission() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val checkPermission = ContextCompat.checkSelfPermission(this@LauncherScreenActivity, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_DENIED
            val isFirst = PreferenceUtil.getDefaultPreference(this).get(Const.IS_FIRST_PERMISSION, true)
            if (checkPermission && isFirst) {
                PreferenceUtil.getDefaultPreference(this).put(Const.IS_FIRST_PERMISSION, false)
                permissionLauncher.launch(arrayOf(Manifest.permission.POST_NOTIFICATIONS))
                return
            }
        }
        val handler = Handler(Looper.myLooper()!!)
        handler.postDelayed({ appStart() }, 1000)
    }

    private val loginLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        goMainActivity()
    }

    val termsAgreeLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        getNotSignedTermsList()
    }

    private val permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { results ->
        var denied = false
        results.forEach {
            if (!it.value) {
                denied = true
            }
        }

        if(denied){
            val builder = AlertBuilder.Builder()
            builder.setStyle_alert(AlertBuilder.STYLE_ALERT.MESSAGE)
            builder.addContents(AlertData.MessageData(getString(R.string.msg_alert_notification_permission_denied), AlertBuilder.MESSAGE_TYPE.TEXT, 4))
            builder.setRightText(getString(R.string.word_confirm))
            builder.setOnAlertResultListener(object : OnAlertResultListener {

                override fun onCancel() {

                    appStart()
                }

                override fun onResult(event_alert: AlertBuilder.EVENT_ALERT) {

                    appStart()
                }
            })
            builder.builder().show(this)
        }else{
            appStart()
        }

    }

    val updateLauncher = registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { result ->
        if (result.resultCode != RESULT_OK) {
            finish()
        }
    }
}