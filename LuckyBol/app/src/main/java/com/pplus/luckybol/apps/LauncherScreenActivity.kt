package com.pplus.luckybol.apps

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.WindowManager
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.android.installreferrer.api.InstallReferrerClient
import com.android.installreferrer.api.InstallReferrerStateListener
import com.buzzvil.buzzad.benefit.BuzzAdBenefit
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability
import com.google.firebase.messaging.FirebaseMessaging
import com.pplus.luckybol.Const
import com.pplus.luckybol.Const.DEBUG_MODE
import com.pplus.luckybol.LuckyBolApplication
import com.pplus.luckybol.R
import com.pplus.luckybol.apps.common.builder.AlertBuilder
import com.pplus.luckybol.apps.common.builder.OnAlertResultListener
import com.pplus.luckybol.apps.common.builder.data.AlertData
import com.pplus.luckybol.apps.common.mgmt.*
import com.pplus.luckybol.apps.common.ui.base.BaseActivity
import com.pplus.luckybol.apps.main.ui.AppMainActivity
import com.pplus.luckybol.apps.setting.ui.FAQActivity
import com.pplus.luckybol.apps.signin.ui.AddInfoActivity
import com.pplus.luckybol.core.Crypt
import com.pplus.luckybol.core.code.common.SnsTypeCode
import com.pplus.luckybol.core.contact.ContactUtil
import com.pplus.luckybol.core.database.DBManager
import com.pplus.luckybol.core.network.ApiBuilder
import com.pplus.luckybol.core.network.ApiController
import com.pplus.luckybol.core.network.model.dto.*
import com.pplus.luckybol.core.network.model.request.params.ParamsRegDevice
import com.pplus.luckybol.core.network.model.response.NewResultResponse
import com.pplus.luckybol.core.util.DebugConfig
import com.pplus.luckybol.core.util.PplusCommonUtil
import com.pplus.luckybol.databinding.ActivityLauncherScreenBinding
import com.pplus.luckybol.push.PushReceiveData
import com.pplus.networks.common.PplusCallback
import com.pplus.utils.part.logs.LogUtil
import com.pplus.utils.part.pref.PreferenceUtil
import com.pplus.utils.part.utils.StringUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LauncherScreenActivity : BaseActivity() {

    private var splashStatus = SPLASH_STATUS.READY

    private var isUpdate = -1 // -1이면 동일, 0: minor, 1: major update

    enum class SPLASH_STATUS {
        READY, REQUEST, RESPONSE_OK, RESPONSE_ERROR, SUCCESS
    }

    override fun getPID(): String {

        return ""
    }

    private lateinit var binding: ActivityLauncherScreenBinding

    override fun getLayoutView(): View {
        binding = ActivityLauncherScreenBinding.inflate(layoutInflater)
        return binding.root
    }

    private lateinit var referrerClient: InstallReferrerClient

    override fun initializeView(savedInstanceState: Bundle?) {

        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        setActionbarColor(Color.TRANSPARENT)

        DebugConfig.setDebugMode(DEBUG_MODE)
        DebugConfig.init(this)

        //        val participateID = PreferenceUtil.getDefaultPreference(this).getString(Const.PARTICIPATEID)
        //        val participateID = "b2d3425ea6af6fde0bcebf859575e50e"

        val isFirst = PreferenceUtil.getDefaultPreference(this).get(Const.IS_FIRST, true)
        if (isFirst) {
            referrerClient = InstallReferrerClient.newBuilder(this).build()
            referrerClient.startConnection(object : InstallReferrerStateListener {

                override fun onInstallReferrerSetupFinished(responseCode: Int) {
                    when (responseCode) {
                        InstallReferrerClient.InstallReferrerResponse.OK -> {
                            val response = referrerClient.installReferrer

                            Log.e(LOG_TAG, "response.installReferrer : " + response.installReferrer)

                            val referrer = response.installReferrer
                            if (StringUtils.isNotEmpty(referrer)) {
                                val referrers = referrer.split("&").toTypedArray()
                                for (referrerValue in referrers) {
                                    val keyValue = referrerValue.split("=").toTypedArray()
                                    if (keyValue[0] == "gad_tracking_id") {
                                        val gad_tracking_id = keyValue[1]
                                        if (StringUtils.isNotEmpty(gad_tracking_id)) {
                                            gpaCompAction(gad_tracking_id)
                                            cpeReport("gpa", gad_tracking_id)
                                            Log.e("gad_tracking_id", "gad_tracking_id : $gad_tracking_id")
                                        }
                                    } else if (keyValue[0] == "adType") {
                                        val adType = keyValue[1]
                                        if (StringUtils.isNotEmpty(adType)) {
                                            cpeReport(adType, PplusCommonUtil.getDeviceID())
                                            Log.e("adType", "adType : $adType")
                                        }
                                    } else if (keyValue[0] == "recommendKey") {
                                        val recommendKey = keyValue[1]
                                        if (StringUtils.isNotEmpty(recommendKey)) {
                                            PreferenceUtil.getDefaultPreference(this@LauncherScreenActivity).put(Const.RECOMMEND, recommendKey)
                                            Log.e("recommendKey", "recommendKey : $recommendKey")
                                        }
                                    } else if (keyValue[0] == "participateID") {
                                        val participateID = keyValue[1]

                                        if (StringUtils.isNotEmpty(participateID)) {
                                            cpeReport("zzal", participateID)
                                            LogUtil.e("participateID", "participateID : {}", participateID)
                                        }
                                    }
                                }
                            }

                            PreferenceUtil.getDefaultPreference(this@LauncherScreenActivity).put(Const.IS_FIRST, false)
                            referrerClient.endConnection()
                        }
                        InstallReferrerClient.InstallReferrerResponse.FEATURE_NOT_SUPPORTED -> {
                            // API not available on the current Play Store app
                            LogUtil.e(LOG_TAG, "NOT_SUPPORTED")
                        }
                        InstallReferrerClient.InstallReferrerResponse.SERVICE_UNAVAILABLE -> {
                            // Connection could not be established
                            LogUtil.e(LOG_TAG, "SERVICE_UNAVAILABLE")
                        }
                    }
                }

                override fun onInstallReferrerServiceDisconnected() {
                    LogUtil.e(LOG_TAG, "onInstallReferrerServiceDisconnected")
                    // Try to restart the connection on the next request to
                    // Google Play by calling the startConnection() method.
                }
            })
        }

        if (checkSelfPermission()) {
            return
        }

        //        text_launcher.animateText(getString(R.string.word_pplus_cash))

        val handler = Handler(Looper.myLooper()!!)
        handler.postDelayed({ appStart() }, 1000)
    }

    private fun cpeReport(type: String, id: String) {

        val params = HashMap<String, String>()
        params["type"] = type
        params["id"] = id
        ApiBuilder.create().cpeReport(params).setCallback(object : PplusCallback<NewResultResponse<Any>> {
            override fun onResponse(call: Call<NewResultResponse<Any>>?,
                                    response: NewResultResponse<Any>?) {

            }

            override fun onFailure(call: Call<NewResultResponse<Any>>?,
                                   t: Throwable?,
                                   response: NewResultResponse<Any>?) {

            }
        }).build().call()
    }

    private fun gpaCompAction(gad_tracking_id: String) {
        val params = HashMap<String, String>()
        params["gad_tracking_id"] = gad_tracking_id
        ApiController.gpaApi.gpaComp(params).enqueue(object : Callback<Any> {
            override fun onFailure(call: Call<Any>, t: Throwable) {
                Log.e(LOG_TAG, "GpaFailure")
            }

            override fun onResponse(call: Call<Any>, response: Response<Any>) {
                if (response.isSuccessful) {
                    Log.e(LOG_TAG, "GpaApi : ${response.body().toString()}")
                } else {
                    Log.e(LOG_TAG, "GpaError : ${response.errorBody().toString()}")
                }

            }
        })
    }

    private fun appStart() {

        if (!PplusCommonUtil.hasNetworkConnection()) {
            val builder = AlertBuilder.Builder()
            builder.setTitle(getString(R.string.word_notice_alert))
            builder.setStyle_alert(AlertBuilder.STYLE_ALERT.MESSAGE)
            builder.addContents(AlertData.MessageData(getString(R.string.msg_disconnected_network), AlertBuilder.MESSAGE_TYPE.TEXT, 1))
            builder.addContents(AlertData.MessageData(getString(R.string.msg_check_network_status), AlertBuilder.MESSAGE_TYPE.TEXT, 2))
            builder.setLeftText(getString(R.string.word_confirm))
            builder.setOnAlertResultListener(object : OnAlertResultListener {

                override fun onCancel() {

                    finish()
                }

                override fun onResult(event_alert: AlertBuilder.EVENT_ALERT) {

                    finish()
                }
            })
            builder.builder().show(this, true)
            return
        }

        val data = intent.data

        if (data != null) {
            LuckyBolApplication.setSchemaData(data.toString())
        }

        if (checkPlayServices()) {
            // UserInfo init
            LoginInfoManager.getInstance()
            ApiController.pRNumberService.updateHeaders()

//            FirebaseInstanceId.getInstance().instanceId.addOnSuccessListener { instanceIdResult ->
//                LogUtil.e(LOG_TAG, "new token : {}", instanceIdResult.token)
//                AppInfoManager.getInstance().pushToken = instanceIdResult.token
//                serverStatus()
//            }

            FirebaseMessaging.getInstance().token.addOnSuccessListener { token ->
                LogUtil.e(LOG_TAG, "new token : {}", token)
                AppInfoManager.getInstance().pushToken = token
                serverStatus()
            }
        }
    }

    private fun serverStatus() {
        ApiController.cdnApi.getServerStatus(System.currentTimeMillis().toString()).enqueue(object : Callback<ServerStatus> {
            override fun onResponse(call: Call<ServerStatus>, response: Response<ServerStatus>) {
                val status = response.body()

                if (status != null) {
                    if (status.resultCode == 200) {
                        serverRequest()
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

    val permissionLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            appStart()
        } else {
            finish()
        }
    }

    val restrictionLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        goMainActivity()
    }

    val configLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        successLogin()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            Const.REQ_UPDATE->{
                if (resultCode != RESULT_OK) {
                    finish()
                }
            }
            PLAY_SERVICES_RESOLUTION_REQUEST -> appStart()
        }
    }

    fun checkPlayServices(): Boolean {

        val apiAvailability = GoogleApiAvailability.getInstance()
        val resultCode = apiAvailability.isGooglePlayServicesAvailable(this)
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)?.show()
            } else {
                LogUtil.e(LOG_TAG, "This device is not supported.")
                finish()
            }
            return false
        }
        return true
    }

    private fun checkSelfPermission(): Boolean {

        val contactPermission = ContextCompat.checkSelfPermission(this@LauncherScreenActivity, Manifest.permission.READ_CONTACTS)
        //        int outgoingPermission = ContextCompat.checkSelfPermission(LauncherScreenActivity.this, Manifest.permission.PROCESS_OUTGOING_CALLS);
        val locationPermission = ContextCompat.checkSelfPermission(this@LauncherScreenActivity, Manifest.permission.ACCESS_FINE_LOCATION)
        //        int callPhonePermission = ContextCompat.checkSelfPermission(LauncherScreenActivity.this, Manifest.permission.CALL_PHONE);

        val checkPermission: Boolean
        //        if(Const.API_URL.startsWith("https://api")){
        //            checkPermission = (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && contactPermission == PackageManager.PERMISSION_DENIED);
        //        }else{
        //            checkPermission = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && (contactPermission == PackageManager.PERMISSION_DENIED || locationPermission == PackageManager.PERMISSION_DENIED);
        //        }
        checkPermission = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && (contactPermission == PackageManager.PERMISSION_DENIED || locationPermission == PackageManager.PERMISSION_DENIED)
        if (checkPermission) {
            val intent = Intent(this, GrantPermissionActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            permissionLauncher.launch(intent)
            return true
        }

        return false
    }

    private fun serverRequest() {

        LogUtil.d(LOG_TAG, "serverRequest splashStatus = $splashStatus")

        if (splashStatus != SPLASH_STATUS.READY) {
            return
        }

        splashStatus = SPLASH_STATUS.REQUEST


        ApiBuilder.create().countryConfigAll.setCallback(object : PplusCallback<NewResultResponse<CountryConfig>> {

            override fun onResponse(call: Call<NewResultResponse<CountryConfig>>,
                                    response: NewResultResponse<CountryConfig>) {

                if(response.datas != null && response.datas!!.isNotEmpty()){
                    CountryConfigManager.getInstance().config = response.datas!![0]
                    CountryConfigManager.getInstance().save()
                    if (isFinishing) {
                        return
                    }
                    checkAppVersion()
                }
            }

            override fun onFailure(call: Call<NewResultResponse<CountryConfig>>,
                                   t: Throwable,
                                   response: NewResultResponse<CountryConfig>) {
                if (isFinishing) {
                    return
                }
                checkAppVersion()
            }
        }).build().call()

    }

    private fun checkAppVersion() {

        val params = HashMap<String, String>()
        params["appKey"] = packageName
        params["version"] = AppInfoManager.getInstance().appVersion!!

        ApiBuilder.create().appVersion(params).setCallback(object : PplusCallback<NewResultResponse<AppVersion>> {

            override fun onResponse(call: Call<NewResultResponse<AppVersion>>,
                                    response: NewResultResponse<AppVersion>) {

                if (isFinishing) {
                    return
                }

                val appVersion = response.data!!

                if (appVersion.versionProp == null) {
                    userLoginCheck()
                    return
                }

                val newVersion = appVersion.versionProp?.lastVersion

                isUpdate = AppInfoManager.getInstance().isVersionUpdate(newVersion)

                // 서버의 값이 강제업데이트인 경우에는 1로 변경
                if (isUpdate != -1 && appVersion.versionProp?.isMustUpdate!!) {
                    isUpdate = 1
                }

                // 선택 업데이트인 경우
                if (isUpdate == 0) {
                    //                        stopAnimation();

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
                                AlertBuilder.EVENT_ALERT.RIGHT -> {
                                    // Creates instance of the manager.
                                    val appUpdateManager = AppUpdateManagerFactory.create(this@LauncherScreenActivity)
                                    //                                    val appUpdateManager = FakeAppUpdateManager(this@LauncherScreenActivity)

                                    // Returns an intent object that you use to check for an update.
                                    val appUpdateInfoTask = appUpdateManager.appUpdateInfo

                                    // Checks that the platform will allow the specified type of update.
                                    appUpdateInfoTask.addOnSuccessListener { appUpdateInfo ->
                                        if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                                            // For a flexible update, use AppUpdateType.FLEXIBLE
                                            && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)
                                        ) {
                                            // Request the update.
                                            appUpdateManager.startUpdateFlowForResult(
                                                // Pass the intent that is returned by 'getAppUpdateInfo()'.
                                                appUpdateInfo,
                                                // Or 'AppUpdateType.FLEXIBLE' for flexible updates.
                                                AppUpdateType.IMMEDIATE,
                                                // The current activity making the update request.
                                                this@LauncherScreenActivity,
                                                // Include a request code to later monitor this update request.
                                                Const.REQ_UPDATE)

                                        }else{
                                            finish()
                                            val it = Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$packageName"))
                                            startActivity(it)
                                        }
                                    }

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
                    //                        stopAnimation();

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

                                    // Creates instance of the manager.
                                    val appUpdateManager = AppUpdateManagerFactory.create(this@LauncherScreenActivity)
//                                    val appUpdateManager = FakeAppUpdateManager(this@LauncherScreenActivity)

                                    // Returns an intent object that you use to check for an update.
                                    val appUpdateInfoTask = appUpdateManager.appUpdateInfo

                                    // Checks that the platform will allow the specified type of update.
                                    appUpdateInfoTask.addOnSuccessListener { appUpdateInfo ->
                                        if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                                            // For a flexible update, use AppUpdateType.FLEXIBLE
                                            && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)
                                        ) {
                                            // Request the update.
                                            appUpdateManager.startUpdateFlowForResult(
                                                // Pass the intent that is returned by 'getAppUpdateInfo()'.
                                                appUpdateInfo,
                                                // Or 'AppUpdateType.FLEXIBLE' for flexible updates.
                                                AppUpdateType.IMMEDIATE,
                                                // The current activity making the update request.
                                                this@LauncherScreenActivity,
                                                // Include a request code to later monitor this update request.
                                                Const.REQ_UPDATE)

                                        }else{
                                            finish()
                                            val it = Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$packageName"))
                                            startActivity(it)
                                        }
                                    }
                                }
                                else -> {}
                            }
                        }
                    }).builder().show(this@LauncherScreenActivity)
                } else {
                    userLoginCheck()
                } // 강제 업데이트
            }

            override fun onFailure(call: Call<NewResultResponse<AppVersion>>,
                                   t: Throwable,
                                   response: NewResultResponse<AppVersion>) {

            }

        }).build().call()
    }

    fun userLoginCheck() {

        LogUtil.e(LOG_TAG, "userLoginCheck times..4")

        splashStatus = SPLASH_STATUS.SUCCESS

        //        BaseAddressManager.getInstance().listCall(BaseAddressManager.LEVEL_TYPE.LEVEL1, null, object : BaseAddressManager.OnCategoryResultListener {
        //
        //            override fun onResult(Level: BaseAddressManager.LEVEL_TYPE, addressList: List<StepAddress>) {
        //
        //                BaseAddressManager.getInstance().addressList = addressList
        //                BaseAddressManager.getInstance().save()
        //            }
        //
        //            override fun onFailed(message: String) {
        //
        //            }
        //        })
        getCategoryMajorOnly()

        ApiBuilder.create().categoryFirstList.setCallback(object : PplusCallback<NewResultResponse<CategoryFirst>> {

            override fun onResponse(call: Call<NewResultResponse<CategoryFirst>>?,
                                    response: NewResultResponse<CategoryFirst>?) {

                if (response?.datas != null) {
                    CategoryFirstManager.getInstance().categoryFirstList = response.datas
                    CategoryFirstManager.getInstance().save()
                    if (LoginInfoManager.getInstance().isMember) {
                        if (AppInfoManager.getInstance().isAutoSingIn) {
                            login()
                        } else {
                            logOutAndSignIn()
                        }
                    } else {
                        logOutAndSignIn()
                    }
                }
            }

            override fun onFailure(call: Call<NewResultResponse<CategoryFirst>>?,
                                   t: Throwable?,
                                   response: NewResultResponse<CategoryFirst>?) {
                if (LoginInfoManager.getInstance().isMember) {
                    if (AppInfoManager.getInstance().isAutoSingIn) {
                        login()
                    } else {
                        logOutAndSignIn()
                    }
                } else {
                    logOutAndSignIn()
                }
            }
        }).build().call()


    }

    private fun getCategoryMajorOnly() {
        ApiBuilder.create().categoryMajorOnly.setCallback(object : PplusCallback<NewResultResponse<CategoryMajor>> {
            override fun onResponse(call: Call<NewResultResponse<CategoryMajor>>?,
                                    response: NewResultResponse<CategoryMajor>?) {
                if (response?.datas != null) {
                    CategoryInfoManager.getInstance().categoryList = response.datas
                    CategoryInfoManager.getInstance().save()
                }
            }

            override fun onFailure(call: Call<NewResultResponse<CategoryMajor>>?,
                                   t: Throwable?,
                                   response: NewResultResponse<CategoryMajor>?) {
            }
        }).build().call()
    }

    private fun login() {

        val params = HashMap<String, String>()

        if (StringUtils.isEmpty(LoginInfoManager.getInstance().user.loginId) || StringUtils.isEmpty(LoginInfoManager.getInstance().user.password)) {
            logOutAndSignIn()
            return
        }

        params["loginId"] = LoginInfoManager.getInstance().user.loginId!!

        if (LoginInfoManager.getInstance().user.accountType == SnsTypeCode.pplus.name) {
            params["password"] = Crypt.encrypt(PplusCommonUtil.decryption(LoginInfoManager.getInstance().user.password!!))
        } else {
            params["password"] = Crypt.encrypt(LoginInfoManager.getInstance().user.accountType + "-" + LoginInfoManager.getInstance().user.loginId!!)
        }
        params["appType"] = Const.APP_TYPE
        params["encrypted"] = "true"
        showProgress("")
        ApiBuilder.create().login(params).setCallback(object : PplusCallback<NewResultResponse<User>> {

            override fun onResponse(call: Call<NewResultResponse<User>>,
                                    response: NewResultResponse<User>) {
                hideProgress()
                if (isFinishing) {
                    return
                }
                val data = response.data

                if (data != null) {
                    data.password = PplusCommonUtil.encryption(Crypt.decrypt(params["password"]!!))
                    LoginInfoManager.getInstance().user = data
                    LoginInfoManager.getInstance().save()

                    existsDevice(data.no)
                }

            }

            override fun onFailure(call: Call<NewResultResponse<User>>,
                                   t: Throwable,
                                   response: NewResultResponse<User>) {

                logOutAndSignIn()

            }
        }).build().call()
    }

    private fun existsDevice(no: Long?) {

        showProgress("")
        val params = HashMap<String, String>()
        params["no"] = no.toString()
        params["device.deviceId"] = PplusCommonUtil.getDeviceID()
        params["device.installedApp.appKey"] = packageName
        params["device.installedApp.version"] = AppInfoManager.getInstance().appVersion!!
        ApiBuilder.create().existsDevice(params).setCallback(object : PplusCallback<NewResultResponse<User>> {

            override fun onResponse(call: Call<NewResultResponse<User>>,
                                    response: NewResultResponse<User>) {

                hideProgress()
                if (isFinishing) {
                    return
                }
                if (response.data == null || response.data!!.device == null || response.data!!.device!!.installedApp == null) {
                    PplusCommonUtil.alertAlarmAgree(object : PplusCommonUtil.Companion.AlarmAgreeListener {

                        override fun result(pushActivate: Boolean, pushMask: String) {

                            registDevice(no, pushActivate, pushMask)
                        }
                    })
                } else {
                    saveDevice(response.data!!)
                    if(LoginInfoManager.getInstance().user != null){
                        if (StringUtils.isEmpty(response.data!!.device!!.installedApp.pushKey) || AppInfoManager.getInstance().pushToken != response.data!!.device!!.installedApp.pushKey) {
                            registDevice(no, LoginInfoManager.getInstance().user.device!!.installedApp.isPushActivate, LoginInfoManager.getInstance().user.device!!.installedApp.pushMask)
                        } else {
                            loginCheck(LoginInfoManager.getInstance().user)
                        }
                    }
                }

            }

            override fun onFailure(call: Call<NewResultResponse<User>>,
                                   t: Throwable,
                                   response: NewResultResponse<User>) {

                hideProgress()
                if (isFinishing) {
                    return
                }
                PplusCommonUtil.alertAlarmAgree(object : PplusCommonUtil.Companion.AlarmAgreeListener {

                    override fun result(pushActivate: Boolean, pushMask: String) {

                        registDevice(no, pushActivate, pushMask)
                    }
                })
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

            override fun onResponse(call: Call<NewResultResponse<User>>,
                                    response: NewResultResponse<User>) {

                hideProgress()
                if (isFinishing) {
                    return
                }

                saveDevice(response.data!!)
                loginCheck(LoginInfoManager.getInstance().user)
                AppInfoManager.getInstance().isAlimAgree = true
            }

            override fun onFailure(call: Call<NewResultResponse<User>>,
                                   t: Throwable,
                                   response: NewResultResponse<User>) {

                hideProgress()
            }
        }).build().call()
    }

    private fun saveDevice(data: User) {

        if(LoginInfoManager.getInstance().user != null){
            LoginInfoManager.getInstance().user.device = data.device
            LoginInfoManager.getInstance().user.talkDenyDay = data.talkDenyDay
            LoginInfoManager.getInstance().user.talkDenyStartTime = data.talkDenyStartTime
            LoginInfoManager.getInstance().user.talkDenyEndTime = data.talkDenyEndTime
            LoginInfoManager.getInstance().user.modDate = data.modDate
            LoginInfoManager.getInstance().user.sessionKey = data.sessionKey
            LoginInfoManager.getInstance().save()
        }
    }

    private fun loginCheck(data: User) {

        LoginResultManager2.getInstance().success(data) { state ->
            when (state) {
                LoginResultManager2.loginState.Success -> successLogin()
                LoginResultManager2.loginState.FAQActivity -> {
                    val intent = Intent(this@LauncherScreenActivity, FAQActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                    restrictionLauncher.launch(intent)
                }
                LoginResultManager2.loginState.SecessionCancelActivity -> logOutAndSignIn()
                LoginResultManager2.loginState.UnVerifiedUserActivity -> {
                }
                LoginResultManager2.loginState.Cancel_btn -> {

                    logOutAndSignIn()
                }
                LoginResultManager2.loginState.Exile->{
                    logOut()
                    try{
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("kakaoplus://plusfriend/home/@luckybol"))
                        startActivity(intent)
                    }catch (e: Exception){
                        PplusCommonUtil.openChromeWebView(this, "http://pf.kakao.com/_xcXKrK")
                    }

                    for (activity in LuckyBolApplication.getActivityList()) {
                        if (!activity.isFinishing) {
                            activity.finish()
                        }

                    }
                }
                else -> {}
            }
        }
    }

    private fun successLogin() {

        if (StringUtils.isNotEmpty(LoginInfoManager.getInstance().user.nickname)) {
            ApiBuilder.create().getNotSignedActiveTermsAll(Const.APP_TYPE).setCallback(object : PplusCallback<NewResultResponse<Terms>> {

                override fun onResponse(call: Call<NewResultResponse<Terms>>,
                                        response: NewResultResponse<Terms>) {

                    if (isFinishing) {
                        return
                    }

                    for (terms in response.datas!!) {
                        if (terms.isCompulsory) {
                            val intent = Intent(this@LauncherScreenActivity, AddInfoActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
                            configLauncher.launch(intent)
                            return
                        }
                    }
                    ContactUtil.getInstance().load(this@LauncherScreenActivity)

                    if(BuzzAdBenefit.getPrivacyPolicyManager()!!.isConsentGranted()){
                       PplusCommonUtil.setBuzvilProfileData()
                    }
                    requestSearchMain()

                }

                override fun onFailure(call: Call<NewResultResponse<Terms>>,
                                       t: Throwable,
                                       response: NewResultResponse<Terms>) {

                }
            }).build().call()

        } else {
            logOutAndSignIn()
        }
    }

    private fun logOut(){
        val contactDao = DBManager.getInstance(LuckyBolApplication.getContext()).session.contactDao
        contactDao.deleteAll()
        LoginInfoManager.getInstance().clear() // 초기화
        AppInfoManager.getInstance().setAutoSignIn(false)
    }

    private fun logOutAndSignIn() {
        setEvent("notLoginRun")
        logOut()

        goMainActivity()
    }

    private fun requestSearchMain() {
        setEvent("autoLogin")
        goMainActivity()
    }

    private fun goMainActivity() {

        val recvIntent = intent
        val data = recvIntent.data
        //        recvIntent.setClass(this@LauncherScreenActivity, MainActivity::class.java)
        recvIntent.setClass(this@LauncherScreenActivity, AppMainActivity::class.java)
        if (data != null) {
            // 스키마로 들어온 경우 처리 예정
            recvIntent.putExtra("schemaUrl", data.toString())
            recvIntent.putExtra(Const.PUSH_DATA, intent.getParcelableExtra<PushReceiveData>(Const.PUSH_DATA))
        }
        recvIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        startActivity(recvIntent)
        finish()
    }

    companion object {

        private val PLAY_SERVICES_RESOLUTION_REQUEST = 9000
    }
}
