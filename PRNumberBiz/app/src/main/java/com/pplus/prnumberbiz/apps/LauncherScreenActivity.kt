package com.pplus.prnumberbiz.apps

import android.Manifest
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Parcelable
import android.view.WindowManager
import androidx.core.content.ContextCompat
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.firebase.iid.FirebaseInstanceId
import com.pple.pplus.utils.part.logs.LogUtil
import com.pple.pplus.utils.part.pref.PreferenceUtil
import com.pple.pplus.utils.part.utils.StringUtils
import com.pple.pplus.utils.part.utils.time.DateFormatUtils
import com.pplus.prnumberbiz.Const
import com.pplus.prnumberbiz.PRNumberBizApplication
import com.pplus.prnumberbiz.R
import com.pplus.prnumberbiz.apps.common.builder.AlertBuilder
import com.pplus.prnumberbiz.apps.common.builder.AlertBuilder.EVENT_ALERT
import com.pplus.prnumberbiz.apps.common.builder.OnAlertResultListener
import com.pplus.prnumberbiz.apps.common.builder.data.AlertData.MessageData
import com.pplus.prnumberbiz.apps.common.mgmt.*
import com.pplus.prnumberbiz.apps.common.mgmt.CategoryInfoManager.CATEGORY_TYPE
import com.pplus.prnumberbiz.apps.common.mgmt.CategoryInfoManager.OnCategoryResultListener
import com.pplus.prnumberbiz.apps.common.mgmt.LoginResultManager2.loginState
import com.pplus.prnumberbiz.apps.common.ui.base.BaseActivity
import com.pplus.prnumberbiz.apps.goods.ui.SellerApplyActivity
import com.pplus.prnumberbiz.apps.main.ui.BizMainActivity
import com.pplus.prnumberbiz.apps.signin.ui.AlertRefuseActivity
import com.pplus.prnumberbiz.apps.signin.ui.NotSignedTermsActivity
import com.pplus.prnumberbiz.apps.signin.ui.PageStatusActivity
import com.pplus.prnumberbiz.apps.signin.ui.SnsLoginActivity
import com.pplus.prnumberbiz.core.code.common.EnumData
import com.pplus.prnumberbiz.core.code.common.SnsTypeCode
import com.pplus.prnumberbiz.core.contact.LoadContactTask
import com.pplus.prnumberbiz.core.database.DBManager
import com.pplus.prnumberbiz.core.network.ApiBuilder
import com.pplus.prnumberbiz.core.network.ApiController
import com.pplus.prnumberbiz.core.network.model.dto.*
import com.pplus.prnumberbiz.core.network.model.request.params.ParamsRegDevice
import com.pplus.prnumberbiz.core.network.model.response.NewResultResponse
import com.pplus.prnumberbiz.core.util.DebugConfig
import com.pplus.prnumberbiz.core.util.PplusCommonUtil.Companion.AlarmAgreeListener
import com.pplus.prnumberbiz.core.util.PplusCommonUtil.Companion.alertAlarmAgree
import com.pplus.prnumberbiz.core.util.PplusCommonUtil.Companion.decryption
import com.pplus.prnumberbiz.core.util.PplusCommonUtil.Companion.encryption
import com.pplus.prnumberbiz.core.util.PplusCommonUtil.Companion.getDeviceID
import com.pplus.prnumberbiz.core.util.PplusCommonUtil.Companion.hasNetworkConnection
import kotlinx.android.synthetic.main.activity_launcher_screen.*
import network.common.PplusCallback
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class LauncherScreenActivity : BaseActivity() {
    private var isUpdate = -1 // -1이면 동일, 0: minor, 1: major update
    override fun getPID(): String {
        return ""
    }

    override fun getLayoutRes(): Int {
        return R.layout.activity_launcher_screen
    }

    override fun initializeView(savedInstanceState: Bundle?) {
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        setActionbarColor(Color.TRANSPARENT)
        DebugConfig.setDebugMode(Const.DEBUG_MODE)
        DebugConfig.init(this)
        if (checkSelfPermission()) {
            return
        }
        text_launcher.animateText(getString(R.string.msg_launcher_description))
        val handler = Handler()
        handler.postDelayed({ appStart() }, 1500)
    }

    override fun onResume() {
        super.onResume()
    }

    private fun appStart() {
        if (!hasNetworkConnection()) {
            val builder = AlertBuilder.Builder()
            builder.setTitle(getString(R.string.word_notice_alert))
            builder.setStyle_alert(AlertBuilder.STYLE_ALERT.MESSAGE)
            builder.addContents(MessageData(getString(R.string.msg_disconnected_network), AlertBuilder.MESSAGE_TYPE.TEXT, 1))
            builder.addContents(MessageData(getString(R.string.msg_check_network_status), AlertBuilder.MESSAGE_TYPE.TEXT, 2))
            builder.setLeftText(getString(R.string.word_confirm))
            builder.setOnAlertResultListener(object : OnAlertResultListener {
                override fun onCancel() {
                    finish()
                }

                override fun onResult(event_alert: EVENT_ALERT) {
                    finish()
                }
            })
            builder.builder().show(this, true)
            return
        }
        val data = intent.data
        if (data != null) {
            PRNumberBizApplication.setSchemaData(data.toString())
        }
        if (checkPlayServices()) {
            // UserInfo init
            LoginInfoManager.getInstance()
            ApiController.getPRNumberService().updateHeaders()
            FirebaseInstanceId.getInstance().instanceId.addOnSuccessListener { instanceIdResult ->
                LogUtil.e(LOG_TAG, "new token : {}", instanceIdResult.token)
                AppInfoManager.getInstance().pushToken = instanceIdResult.token
                serverStatus()
            }
        }
    }

    private fun serverStatus() {
        ApiController.getCdnApi().getServerSatus(System.currentTimeMillis().toString()).enqueue(object : Callback<ServerStatus?> {
            override fun onResponse(call: Call<ServerStatus?>, response: Response<ServerStatus?>) {
                val status = response.body()
                if (status!!.resultCode == 200) {
                    serverRequest()
                } else {
                    showAlert(status.message)
                    finish()
                }
            }

            override fun onFailure(call: Call<ServerStatus?>, t: Throwable) {
                showAlert(R.string.msg_server_checking)
                finish()
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            Const.REQ_REFUSE->{
                logOutAndSignIn()
            }
            Const.REQ_SET_PAGE -> if (resultCode != Activity.RESULT_OK) {
                finish()
            } else {
                existsDevice(LoginInfoManager.getInstance().user.no)
            }
            Const.REQ_PERMISSION -> if (resultCode == Activity.RESULT_OK) {
                appStart()
            } else {
                finish()
            }
            Const.REQ_SIGN_IN -> if (resultCode == Activity.RESULT_OK) {
                goMainActivity()
            } else {
                finish()
            }
            Const.REQ_RESTRICTION -> goMainActivity()
            Const.REQ_RE_REG_NUMBER -> if (resultCode == Activity.RESULT_OK) {
                successLogin()
            } else {
                logOutAndSignIn()
            }
            Const.REQ_APPLY -> if (resultCode == Activity.RESULT_OK) {
                userLoginCheck()
            }
        }
    }

    fun checkPlayServices(): Boolean {
        val apiAvailability = GoogleApiAvailability.getInstance()
        val resultCode = apiAvailability.isGooglePlayServicesAvailable(this)
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST).show()
            } else {
                LogUtil.e(LOG_TAG, "This device is not supported.")
                finish()
            }
            return false
        }
        return true
    }

    private fun checkSelfPermission(): Boolean {

//        int findLocationPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        val contactPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)
        //        int outgoingPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.PROCESS_OUTGOING_CALLS);

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && (findLocationPermission == PackageManager.PERMISSION_DENIED || contactPermission == PackageManager.PERMISSION_DENIED)) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && contactPermission == PackageManager.PERMISSION_DENIED) {
            val intent = Intent(this, GrantPermissionActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivityForResult(intent, Const.REQ_PERMISSION)
            return true
        }
        return false
    }

    private fun categoryStore() {
        CategoryInfoManager.getInstance().categoryListCall(CATEGORY_TYPE.LEVEL1, "store", null, object : OnCategoryResultListener {
            override fun onResult(Level: CATEGORY_TYPE, categories: List<Category>) {
                CategoryInfoManager.getInstance().categoryListStore = categories
                CategoryInfoManager.getInstance().save()
                categoryPerson()
            }

            override fun onFailed(message: String) {}
        })
    }

    private fun categoryPerson() {
        CategoryInfoManager.getInstance().categoryListCall(CATEGORY_TYPE.LEVEL1, "person", null, object : OnCategoryResultListener {
            override fun onResult(Level: CATEGORY_TYPE, categories: List<Category>) {
                CategoryInfoManager.getInstance().categoryListPerson = categories
                CategoryInfoManager.getInstance().save()
                categoryShop()
            }

            override fun onFailed(message: String) {}
        })
    }

    private fun categoryShop() {
        CategoryInfoManager.getInstance().categoryListCall(CATEGORY_TYPE.LEVEL1, "shop", null, object : OnCategoryResultListener {
            override fun onResult(Level: CATEGORY_TYPE, categories: List<Category>) {
                CategoryInfoManager.getInstance().categoryListShop = categories
                CategoryInfoManager.getInstance().save()
            }

            override fun onFailed(message: String) {}
        })
    }

    private fun getPrefixNumber() {
        ApiBuilder.create().prefixNumber.setCallback(object : PplusCallback<NewResultResponse<String>> {
            override fun onResponse(call: Call<NewResultResponse<String>>?, response: NewResultResponse<String>?) {
                PreferenceUtil.getDefaultPreference(this@LauncherScreenActivity).put(Const.FREE_NUMBER_PREFIX, response!!.data)
            }

            override fun onFailure(call: Call<NewResultResponse<String>>?, t: Throwable?, response: NewResultResponse<String>?) {
            }
        }).build().call()
    }

    private fun serverRequest() {
        getPrefixNumber()
        categoryStore()
        ApiBuilder.create().countryConfigAll.setCallback(object : PplusCallback<NewResultResponse<CountryConfig>> {
            override fun onResponse(call: Call<NewResultResponse<CountryConfig>>?, response: NewResultResponse<CountryConfig>?) {
                var updateDate: String? = ""
                if (CountryConfigManager.getInstance().config != null && CountryConfigManager.getInstance().config.properties != null && StringUtils.isNotEmpty(CountryConfigManager.getInstance().config.properties.sysTypeLastUpdate)) {
                    updateDate = CountryConfigManager.getInstance().config.properties.sysTypeLastUpdate
                }
                CountryConfigManager.getInstance().config = response!!.datas[0]
                CountryConfigManager.getInstance().save()
                if (StringUtils.isNotEmpty(updateDate) && SystemBoardManager.getInstance().board != null && SystemBoardManager.getInstance().board.cashList != null && SystemBoardManager.getInstance().board.cashList.size > 0) {
                    try {
                        val latestUpdateDate = response.datas[0].properties.sysTypeLastUpdate
                        val d1 = DateFormatUtils.PPLUS_DATE_FORMAT.parse(latestUpdateDate)
                        val d2 = DateFormatUtils.PPLUS_DATE_FORMAT.parse(updateDate)
                        if (d1.after(d2)) {
                            callCashTerms()
                        }
                    } catch (e: Exception) {
                        LogUtil.e(LOG_TAG, e.toString())
                    }
                } else {
                    callCashTerms()
                }
                checkAppVersion()
            }

            override fun onFailure(call: Call<NewResultResponse<CountryConfig>>?, t: Throwable?, response: NewResultResponse<CountryConfig>?) {
                checkAppVersion()
            }
        }).build().call()
    }

    private fun callCashTerms() {
        val params: MutableMap<String, String> = HashMap()
        params["boardNo"] = "" + CountryConfigManager.getInstance().config.properties.sysTypeBoard
        params["pg"] = "1"
        params["sz"] = "10000"
        params["filter"] = "charge_alert,refund_rule"
        ApiBuilder.create().getBoardPostList(params).setCallback(object : PplusCallback<NewResultResponse<Post>> {
            override fun onResponse(call: Call<NewResultResponse<Post>>?, response: NewResultResponse<Post>?) {
                val board = SystemBoard()
                board.cashList = response!!.datas
                SystemBoardManager.getInstance().board = board
                SystemBoardManager.getInstance().save()
            }


            override fun onFailure(call: Call<NewResultResponse<Post>>?, t: Throwable?, response: NewResultResponse<Post>?) {

            }
        }).build().call()
    }

    private fun checkAppVersion() {
        val params: MutableMap<String, String> = HashMap()
        params["appKey"] = packageName
        params["version"] = AppInfoManager.getInstance().appVersion
        ApiBuilder.create().appVersion(params).setCallback(object : PplusCallback<NewResultResponse<AppVersion>> {
            override fun onResponse(call: Call<NewResultResponse<AppVersion>>?, response: NewResultResponse<AppVersion>?) {
                val appVersion = response!!.data
                if (appVersion.versionProp == null) {
                    userLoginCheck()
                    return
                }
                val newVersion = appVersion.versionProp.lastVersion
                isUpdate = AppInfoManager.getInstance().isVersionUpdate(newVersion)

                // 서버의 값이 강제업데이트인 경우에는 1로 변경
                if (isUpdate != -1 && appVersion.versionProp.isMustUpdate) {
                    isUpdate = 1
                }
                val downloadUrl = appVersion.versionProp.downloadUrl

                // 선택 업데이트인 경우
                if (isUpdate == 0) {
                    //                        stopAnimation();
                    val builder = AlertBuilder.Builder()
                    builder.setTitle(getString(R.string.word_notice_alert))
                    builder.setStyle_alert(AlertBuilder.STYLE_ALERT.MESSAGE)
                    builder.addContents(MessageData(getString(R.string.msg_minor_update), AlertBuilder.MESSAGE_TYPE.TEXT, 3))
                    builder.setLeftText(getString(R.string.word_cancel)).setRightText(getString(R.string.word_update))
                    builder.setOnAlertResultListener(object : OnAlertResultListener {
                        override fun onCancel() {
                            isUpdate = -1
                            userLoginCheck()
                        }

                        override fun onResult(event_alert: EVENT_ALERT) {
                            when (event_alert) {
                                EVENT_ALERT.RIGHT -> {
                                    val it = Intent(Intent.ACTION_VIEW, Uri.parse(downloadUrl))
                                    try {
                                        startActivity(it)
                                    } catch (e: ActivityNotFoundException) {
                                    }
                                    finish()
                                }
                                EVENT_ALERT.LEFT -> {
                                    isUpdate = -1
                                    userLoginCheck()
                                }
                            }
                        }
                    }).builder().show(this@LauncherScreenActivity)
                } else if (isUpdate == 1) {
                    //                        stopAnimation();
                    val builder = AlertBuilder.Builder()
                    builder.setTitle(getString(R.string.word_notice_alert))
                    builder.setStyle_alert(AlertBuilder.STYLE_ALERT.MESSAGE)
                    builder.addContents(MessageData(getString(R.string.msg_major_update), AlertBuilder.MESSAGE_TYPE.TEXT, 3))
                    builder.setAutoCancel(false)
                    builder.setBackgroundClickable(false)
                    builder.setRightText(getString(R.string.word_update))
                    builder.setOnAlertResultListener(object : OnAlertResultListener {
                        override fun onCancel() {}
                        override fun onResult(event_alert: EVENT_ALERT) {
                            when (event_alert) {
                                EVENT_ALERT.SINGLE -> {
                                    val it = Intent(Intent.ACTION_VIEW, Uri.parse(downloadUrl))
                                    try {
                                        startActivity(it)
                                    } catch (e: ActivityNotFoundException) {
                                    }
                                    finish()
                                }
                            }
                        }
                    }).builder().show(this@LauncherScreenActivity)
                } else {
                    userLoginCheck()
                }
            }

            override fun onFailure(call: Call<NewResultResponse<AppVersion>>?, t: Throwable?, response: NewResultResponse<AppVersion>?) {
                showAlert(getString(R.string.server_error_default))
                finish()
            }
        }).build().call()
    }

    fun userLoginCheck() {
        LogUtil.e(LOG_TAG, "userLoginCheck times..4")

        //        splashStatus = SPLASH_STATUS.SUCCESS;
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

    private fun login() {
        val params: MutableMap<String, String?> = HashMap()
        if (StringUtils.isEmpty(LoginInfoManager.getInstance().user.loginId) || StringUtils.isEmpty(LoginInfoManager.getInstance().user.password)) {
            logOutAndSignIn()
            return
        }
        params["loginId"] = LoginInfoManager.getInstance().user.loginId
        if(LoginInfoManager.getInstance().user.accountType == SnsTypeCode.pplus.name){
            params["password"] = decryption(LoginInfoManager.getInstance().user.password!!)
        }else{
            params["password"] =  LoginInfoManager.getInstance().user.accountType + "-" + LoginInfoManager.getInstance().user.loginId!!
        }
        showProgress("")
        ApiBuilder.create().login(params).setCallback(object : PplusCallback<NewResultResponse<User>> {
            override fun onResponse(call: Call<NewResultResponse<User>>?, response: NewResultResponse<User>?) {
                val data = response!!.data
                hideProgress()
                data.password = encryption(params["password"]!!)
                LoginInfoManager.getInstance().user = data
                LoginInfoManager.getInstance().save()
                if (data.certificationLevel!! < 11) {
                    logOutAndSignIn()
                } else {
//                    Page page = LoginInfoManager.getInstance().getUser().getPage();
//                    if (page != null && page.getProperties() != null && StringUtils.isNotEmpty(page.getProperties().getEmptyNumberCause()) && page.getProperties().getEmptyNumberCause().equals("removeByAdmin")) {
//                        logOutAndSignIn();
//                    } else {
//
//                    }
                    existsDevice(data.no)
                }
            }

            override fun onFailure(call: Call<NewResultResponse<User>>?, t: Throwable?, response: NewResultResponse<User>?) {
                logOutAndSignIn()
            }
        }).build().call()
    }

    private fun existsDevice(no: Long?) {
        showProgress("")
        val params: MutableMap<String, String> = HashMap()
        params["no"] = "" + no
        params["device.deviceId"] = getDeviceID()
        params["device.installedApp.appKey"] = packageName
        params["device.installedApp.version"] = AppInfoManager.getInstance().appVersion
        ApiBuilder.create().existsDevice(params).setCallback(object : PplusCallback<NewResultResponse<User>> {
            override fun onResponse(call: Call<NewResultResponse<User>>?, response: NewResultResponse<User>?) {
                hideProgress()
                LogUtil.e(LOG_TAG, "onResponse")
                saveDevice(response!!.data)
                if (response.data == null || response.data!!.device == null || response.data!!.device!!.installedApp == null || StringUtils.isEmpty(response.data!!.device!!.installedApp.pushKey) || AppInfoManager.getInstance().pushToken != response.data!!.device!!.installedApp.pushKey) {
                    registDevice(no, LoginInfoManager.getInstance().user.device!!.installedApp.isPushActivate, LoginInfoManager.getInstance().user.device!!.installedApp.pushMask)
                } else {
                    loginCheck(LoginInfoManager.getInstance().user)
                }
            }

            override fun onFailure(call: Call<NewResultResponse<User>>?, t: Throwable?, response: NewResultResponse<User>?) {
                hideProgress()
                alertAlarmAgree(object : AlarmAgreeListener {
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
        device.deviceId = getDeviceID()
        device.platform = "aos"
        device.installedApp = installedApp
        params.device = device
        showProgress("")
        ApiBuilder.create().registDevice(params).setCallback(object : PplusCallback<NewResultResponse<User>> {
            override fun onResponse(call: Call<NewResultResponse<User>>?, response: NewResultResponse<User>?) {
                hideProgress()
                saveDevice(response!!.data)
                loginCheck(LoginInfoManager.getInstance().user)
                AppInfoManager.getInstance().isAlimAgree = true
            }

            override fun onFailure(call: Call<NewResultResponse<User>>?, t: Throwable?, response: NewResultResponse<User>?) {
                hideProgress()
            }

        }).build().call()
    }

    private fun saveDevice(data: User?) {
        LoginInfoManager.getInstance().user.device = data!!.device
        LoginInfoManager.getInstance().user.talkDenyDay = data.talkDenyDay
        LoginInfoManager.getInstance().user.talkDenyStartTime = data.talkDenyStartTime
        LoginInfoManager.getInstance().user.talkDenyEndTime = data.talkDenyEndTime
        LoginInfoManager.getInstance().user.modDate = data.modDate
        LoginInfoManager.getInstance().user.sessionKey = data.sessionKey
        LoginInfoManager.getInstance().save()
    }

    private fun loginCheck(data: User) {
        val page = LoginInfoManager.getInstance().user.page
        if (page != null) {
            if (page.status == EnumData.PageStatus.normal.name) {
                LoginResultManager2.getInstance().success(data) { state ->
                    when (state) {
                        loginState.Success -> successLogin()
                        loginState.SecessionCancelActivity -> logOutAndSignIn()
                        loginState.UnVerifiedUserActivity -> {
                        }
                        loginState.Cancel_btn, loginState.Exile -> logOutAndSignIn()
                    }
                }
            } else if (page.status == EnumData.PageStatus.ready.name) { //미승인
                val intent = Intent(this, SellerApplyActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                startActivityForResult(intent, Const.REQ_APPLY)
            } else if (page.status == EnumData.PageStatus.pending.name || page.status == EnumData.PageStatus.redemand.name) { //승인대기
                val intent = Intent(this, PageStatusActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                startActivityForResult(intent, Const.REQ_APPLY)
//                activatePage();
            } else if (page.status == "return") {//승인거절
                val intent = Intent(this, PageStatusActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                startActivityForResult(intent, Const.REQ_APPLY)
//                activatePage();
            }else if (page.status == EnumData.PageStatus.refuse.name) {
                val intent = Intent(this, AlertRefuseActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                startActivityForResult(intent, Const.REQ_REFUSE)
            }
        }
    }

    private fun successLogin() {
        val task = LoadContactTask(this@LauncherScreenActivity, null)
        task.execute()
        ApiBuilder.create().getNotSignedActiveTermsAll(packageName).setCallback(object : PplusCallback<NewResultResponse<Terms>> {

            override fun onResponse(call: Call<NewResultResponse<Terms>>?, response: NewResultResponse<Terms>?) {
                val termsList = response!!.datas as ArrayList<Terms>
                if (termsList == null || termsList.size == 0) {
                    for (terms in termsList) {
                        if (terms.isCompulsory) {
                            val intent = Intent(this@LauncherScreenActivity, NotSignedTermsActivity::class.java)
                            intent.putExtra(Const.KEY, Const.CANCEL_LEAVE)
                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                            startActivityForResult(intent, Const.REQ_SIGN_IN)
                            break
                        }
                    }
                    goMainActivity()
                } else {
                    goMainActivity()
                }
            }

            override fun onFailure(call: Call<NewResultResponse<Terms>>?, t: Throwable?, response: NewResultResponse<Terms>?) {
                goMainActivity()
            }
        }).build().call()
    }

    private fun logOutAndSignIn() {
        val contactDao = DBManager.getInstance(PRNumberBizApplication.getContext()).session.contactDao
        contactDao.deleteAll()
        LoginInfoManager.getInstance().clear() // 초기화
        AppInfoManager.getInstance().setAutoSignIn(false)
        val intent = Intent(this, SnsLoginActivity::class.java)
        startActivityForResult(intent, Const.REQ_SIGN_IN)
    }

    private fun goMainActivity() {
        val recvIntent = intent
        val data = recvIntent.data
        //                        Intent intent = new Intent(this, BizMainActivity.class);

//        if (LoginInfoManager.getInstance().getUser().getPage().getType().equals(EnumData.PageTypeCode.person.name()) && !LoginInfoManager.getInstance().getUser().getPage().isSeller()) {
////        if (!LoginInfoManager.getInstance().getUser().getPage().isSeller()) {
//            recvIntent.setClass(this, PersonMainActivity.class);
//        } else {
//            recvIntent.setClass(this, BizMainActivity.class);
//        }
        recvIntent.setClass(this, BizMainActivity::class.java)
        if (data != null) {
            // 스키마로 들어온 경우 처리 예정
            recvIntent.putExtra("schemaUrl", data.toString())
            recvIntent.putExtra(Const.PUSH_DATA, intent.getParcelableExtra<Parcelable>(Const.PUSH_DATA))
        }
        recvIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        startActivity(recvIntent)
        finish()
    }

    companion object {
        private const val PLAY_SERVICES_RESOLUTION_REQUEST = 9000
    }
}