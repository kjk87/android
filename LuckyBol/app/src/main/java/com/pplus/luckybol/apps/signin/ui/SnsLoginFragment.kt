package com.pplus.luckybol.apps.signin.ui


import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.buzzvil.buzzad.benefit.BuzzAdBenefit
import com.buzzvil.buzzad.benefit.privacy.PrivacyPolicyEventListener
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.api.ApiException
import com.kakao.sdk.auth.LoginClient
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.user.UserApiClient
import com.navercorp.nid.NaverIdLoginSDK
import com.navercorp.nid.NaverIdLoginSDK.initialize
import com.navercorp.nid.oauth.NidOAuthLogin
import com.navercorp.nid.oauth.OAuthLoginCallback
import com.navercorp.nid.profile.NidProfileCallback
import com.navercorp.nid.profile.data.NidProfileResponse
import com.pplus.luckybol.Const
import com.pplus.luckybol.LuckyBolApplication
import com.pplus.luckybol.R
import com.pplus.luckybol.apps.common.builder.AlertBuilder
import com.pplus.luckybol.apps.common.builder.OnAlertResultListener
import com.pplus.luckybol.apps.common.builder.data.AlertData
import com.pplus.luckybol.apps.common.mgmt.AppInfoManager
import com.pplus.luckybol.apps.common.mgmt.LoginInfoManager
import com.pplus.luckybol.apps.common.mgmt.LoginResultManager2
import com.pplus.luckybol.apps.common.ui.base.BaseActivity
import com.pplus.luckybol.apps.common.ui.base.BaseFragment
import com.pplus.luckybol.apps.setting.ui.VerificationActivity
import com.pplus.luckybol.apps.signup.ui.JoinActivity
import com.pplus.luckybol.apps.signup.ui.VerificationMeActivity
import com.pplus.luckybol.core.Crypt
import com.pplus.luckybol.core.code.common.LoginStatus
import com.pplus.luckybol.core.code.common.SnsTypeCode
import com.pplus.luckybol.core.contact.ContactUtil
import com.pplus.luckybol.core.database.DBManager
import com.pplus.luckybol.core.network.ApiBuilder
import com.pplus.luckybol.core.network.model.dto.*
import com.pplus.luckybol.core.network.model.request.params.ParamsRegDevice
import com.pplus.luckybol.core.network.model.response.NewResultResponse
import com.pplus.luckybol.core.sns.google.GoogleUtil
import com.pplus.luckybol.core.sns.kakao.KakaoUtil
import com.pplus.luckybol.core.util.PplusCommonUtil
import com.pplus.luckybol.databinding.FragmentSnsLoginBinding
import com.pplus.networks.common.PplusCallback
import com.pplus.utils.part.logs.LogUtil
import com.pplus.utils.part.pref.PreferenceUtil
import com.pplus.utils.part.utils.StringUtils
import retrofit2.Call


/**
 * A simple [Fragment] subclass.
 * Use the [SnsLoginFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SnsLoginFragment : BaseFragment<BaseActivity>() {

    override fun getPID(): String {
        return ""
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        if (arguments != null) {
//            mParam1 = arguments!!.getString(ARG_PARAM1)
//            mParam2 = arguments!!.getString(ARG_PARAM2)
        }
    }

    private var _binding: FragmentSnsLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        _binding = FragmentSnsLoginBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    var mIsAutoSigIn = true

    var mGoogleSignListener = object : GoogleUtil.GoogleSignListener{
        override fun handleSignInResult(account: GoogleSignInAccount?) {
            hideProgress()
            if(account != null){
                val id = account.email
                val token = account.idToken

                GoogleUtil.signOut()

                LogUtil.e("id", "" + id!!)
                LogUtil.e("token", "" + token!!)
                mIsAutoSigIn = true
                val params = HashMap<String, String>()
                params["loginId"] = id
                //                    params["password"] = token
                params["password"] = Crypt.encrypt(SnsTypeCode.google.name + "-" + Const.APP_TYPE + "##" + id)
                params["accountType"] = SnsTypeCode.google.name
                params["nickname"] = id.split("@")[0]
                params["encrypted"] = "true"
                login(params)
            }

        }
    }

    val mGoogleLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->

        if(result.resultCode == Activity.RESULT_OK){
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            mGoogleSignListener.handleSignInResult(task.getResult(ApiException::class.java))
        }else{
            hideProgress()
        }
    }

    override fun init() {

        binding.textSnsLoginJoin.setOnClickListener {
            goSignUp(null)
        }

        GoogleUtil.init(requireActivity(), mGoogleSignListener)

        binding.layoutSnsLoginGoogle.setOnClickListener {
            showProgress("")
            GoogleUtil.launch(mGoogleLauncher)
        }

        binding.layoutSnsLoginKakao.setOnClickListener {
            KakaoUtil.login(it.context, callback)
        }

        binding.layoutSnsLoginNaver.setOnClickListener {
            initialize(requireActivity(), getString(R.string.naver_client_id), getString(R.string.naver_client_secret), getString(R.string.app_name2))
            NaverIdLoginSDK.authenticate(requireActivity(), oauthLoginCallback)
        }

        binding.layoutSnsLoginPplus.setOnClickListener {
            val intent = Intent(activity, LoginActivity::class.java)
            signInLauncher.launch(intent)
        }
    }

    val oauthLoginCallback = object : OAuthLoginCallback {
        override fun onSuccess() {
            // 네이버 로그인 인증이 성공했을 때 수행할 코드 추가
            NidOAuthLogin().callProfileApi(profileCallback)
        }
        override fun onFailure(httpStatus: Int, message: String) {
            val errorCode = NaverIdLoginSDK.getLastErrorCode().code
            val errorDescription = NaverIdLoginSDK.getLastErrorDescription()
            showAlert(errorDescription)
        }
        override fun onError(errorCode: Int, message: String) {
            onFailure(errorCode, message)
        }
    }

    val profileCallback = object : NidProfileCallback<NidProfileResponse> {
        override fun onSuccess(response: NidProfileResponse) {
            Toast.makeText(context,"$response",Toast.LENGTH_SHORT).show()
            val email = response.profile?.email
            if(StringUtils.isNotEmpty(email)){
                val params = HashMap<String, String>()
                params["loginId"] = email!!
                //                    params["password"] = token
                params["password"] = Crypt.encrypt(SnsTypeCode.naver.name + "-" + Const.APP_TYPE + "##" + email)
                params["accountType"] = SnsTypeCode.naver.name
                params["nickname"] = email.split("@")[0]
                params["encrypted"] = "true"
                login(params)
            }else{
                showAlert(R.string.msg_cannot_get_email)
            }
        }
        override fun onFailure(httpStatus: Int, message: String) {
            val errorCode = NaverIdLoginSDK.getLastErrorCode().code
            val errorDescription = NaverIdLoginSDK.getLastErrorDescription()
            showAlert(errorDescription)
        }
        override fun onError(errorCode: Int, message: String) {
            onFailure(errorCode, message)
        }
    }

    val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
        if (error != null) {
            LogUtil.e(LOG_TAG, "로그인 실패 : {}", error)
        }
        else if (token != null) {
            LogUtil.e(LOG_TAG, "로그인 성공 ${token.accessToken}")

            UserApiClient.instance.me { user, error ->
                if (error != null) {
                    LogUtil.e(LOG_TAG, "사용자 정보 요청 실패 : {}", error)
                }
                else if (user != null) {

                    if(!isAdded){
                        return@me
                    }
                    val scopes = listOf("account_email")

                    LoginClient.instance.loginWithNewScopes(requireActivity(), scopes) { token, error ->
                        if (error != null) {
                            LogUtil.e(LOG_TAG, "이메일 제공 동의 실패", error)
                        } else {
                            LogUtil.e(LOG_TAG, "allowed scopes: ${token!!.scopes}")

                            // 사용자 정보 재요청
                            UserApiClient.instance.me { user, error ->
                                if (error != null) {
                                    LogUtil.e(LOG_TAG, "사용자 정보 요청 실패", error)
                                    showAlert(R.string.msg_cannot_get_kakao_account)
                                }
                                else if (user != null) {

                                    if(StringUtils.isNotEmpty(user.kakaoAccount?.email)){
                                        val params = HashMap<String, String>()
                                        params["loginId"] = user.kakaoAccount?.email!!
                                        params["password"] = Crypt.encrypt(SnsTypeCode.kakao.name + "-" + Const.APP_TYPE + "##" + user.kakaoAccount?.email)
                                        params["accountType"] = SnsTypeCode.kakao.name
                                        params["encrypted"] = "true"

                                        val nickname = user.kakaoAccount?.profile?.nickname

                                        if (StringUtils.isNotEmpty(nickname)) {
                                            params["nickname"] = nickname!!
                                        }
                                        KakaoUtil.logout()
                                        login(params)
                                    }


                                }
                            }
                        }
                    }
                }
            }

        }
    }

    private fun login(params: HashMap<String, String>) {
        params["appType"] = Const.APP_TYPE
        if (!isAdded) {
            return
        }
        showProgress("")
        ApiBuilder.create().login(params).setCallback(object : PplusCallback<NewResultResponse<User>> {

            override fun onResponse(call: Call<NewResultResponse<User>>?,
                                    response: NewResultResponse<User>?) {
                if (!isAdded) {
                    return
                }
                val data = response!!.data!!
                data.password = PplusCommonUtil.encryption(Crypt.decrypt(params["password"]!!))
                hideProgress()
                LoginInfoManager.getInstance().user = data
                LoginInfoManager.getInstance().save()

                if (data.useStatus != null && data.useStatus == LoginStatus.waitingToLeave.name) { //탈퇴대기
                    if (LoginInfoManager.getInstance().user.verification!!.media == "external") {
                        val builder = AlertBuilder.Builder()
                        builder.setTitle(getString(R.string.word_notice_alert))
                        builder.setStyle_alert(AlertBuilder.STYLE_ALERT.MESSAGE)
                        builder.addContents(AlertData.MessageData(getString(R.string.msg_leave_cancel_description1), AlertBuilder.MESSAGE_TYPE.TEXT, 3))
                        builder.addContents(AlertData.MessageData(getString(R.string.msg_leave_cancel_description2), AlertBuilder.MESSAGE_TYPE.TEXT, 2))
                        builder.setLeftText(getString(R.string.word_cancel)).setRightText(getString(R.string.word_confirm))
                        builder.setBackgroundClickable(false).setAutoCancel(false)
                        builder.setOnAlertResultListener(object : OnAlertResultListener {

                            override fun onCancel() {}

                            override fun onResult(event_alert: AlertBuilder.EVENT_ALERT) {

                                when (event_alert) {
                                    AlertBuilder.EVENT_ALERT.RIGHT -> {
                                        val intent = Intent(activity, VerificationMeActivity::class.java)
                                        intent.putExtra(Const.KEY, Const.CANCEL_LEAVE)
                                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                                        leaveCancelLauncher.launch(intent)
                                    }
                                    else -> {}
                                }
                            }
                        }).builder().show(activity)
                    } else {
                        val builder = AlertBuilder.Builder()
                        builder.setTitle(getString(R.string.word_notice_alert))
                        builder.setStyle_alert(AlertBuilder.STYLE_ALERT.MESSAGE)
                        builder.addContents(AlertData.MessageData(getString(R.string.msg_leave_cancel_description3), AlertBuilder.MESSAGE_TYPE.TEXT, 3))
                        builder.addContents(AlertData.MessageData(getString(R.string.msg_leave_cancel_description4), AlertBuilder.MESSAGE_TYPE.TEXT, 2))
                        builder.setLeftText(getString(R.string.word_cancel)).setRightText(getString(R.string.word_confirm))
                        builder.setBackgroundClickable(false).setAutoCancel(false)
                        builder.setOnAlertResultListener(object : OnAlertResultListener {

                            override fun onCancel() {}

                            override fun onResult(event_alert: AlertBuilder.EVENT_ALERT) {

                                when (event_alert) {
                                    AlertBuilder.EVENT_ALERT.RIGHT -> {
                                        val intent = Intent(activity, VerificationActivity::class.java)
                                        intent.putExtra(Const.KEY, Const.CANCEL_LEAVE)
                                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                                        leaveCancelLauncher.launch(intent)
                                    }
                                    else -> {}
                                }
                            }
                        }).builder().show(activity)
                    }

                } else if (data.useStatus != null && data.useStatus == LoginStatus.leave.name) {
                    if (StringUtils.isNotEmpty(params["accountType"]) && !params["accountType"].equals(SnsTypeCode.pplus.name)) {
                        if(params["accountType"].equals(SnsTypeCode.kakao.name)){
                            showAlert(R.string.msg_can_not_join_kakao)
                        }else{
                            goSignUp(params)
                        }
                    } else {
                        goSignUp(null)
                    }
                } else {
                    existsDevice(data.no)
                }


            }

            override fun onFailure(call: Call<NewResultResponse<User>>?,
                                   t: Throwable?,
                                   response: NewResultResponse<User>?) {
                hideProgress()
                if (!isAdded) {
                    return
                }
                if (response != null) {
                    if (response.resultCode == 503) {
                        if (StringUtils.isNotEmpty(params["accountType"]) && !params["accountType"].equals(SnsTypeCode.pplus.name)) {
                            if(params["accountType"].equals(SnsTypeCode.kakao.name)){
                                showAlert(R.string.msg_can_not_join_kakao)
                            }else{
                                goSignUp(params)
                            }
                        } else {
                            goSignUp(null)
                        }
                    } else {
                        LoginResultManager2.getInstance().fail(response.data) { state ->
                            when (state) {
                                LoginResultManager2.loginState.AuthActivity -> findPw()
                                else -> {}
                            }
                        }
                    }
                } else {
                    showAlert(R.string.login_fail_general)
                }
            }
        }).build().call()
    }

    private fun existsDevice(no: Long?) {

        showProgress("")
        val params = HashMap<String, String>()
        params["no"] = "" + no!!
        params["device.deviceId"] = PplusCommonUtil.getDeviceID()
        params["device.installedApp.appKey"] = requireActivity().packageName
        params["device.installedApp.version"] = AppInfoManager.getInstance().appVersion
        ApiBuilder.create().existsDevice(params).setCallback(object : PplusCallback<NewResultResponse<User>> {

            override fun onResponse(call: Call<NewResultResponse<User>>,
                                    response: NewResultResponse<User>) {
                if (!isAdded) {
                    return
                }
                hideProgress()
                LogUtil.e(LOG_TAG, "onResponse")
                saveDevice(response.data!!)
                if (StringUtils.isNotEmpty(AppInfoManager.getInstance().pushToken) && (response.data == null || response.data!!.device == null || response.data!!.device!!.installedApp == null || StringUtils.isEmpty(response.data!!.device!!.installedApp.pushKey) || AppInfoManager.getInstance().pushToken != response.data!!.device!!.installedApp.pushKey)) {
                    registDevice(no, LoginInfoManager.getInstance().user.device!!.installedApp.isPushActivate, LoginInfoManager.getInstance().user.device!!.installedApp.pushMask)
                } else {
                    loginCheck(LoginInfoManager.getInstance().user)
                }
            }

            override fun onFailure(call: Call<NewResultResponse<User>>,
                                   t: Throwable,
                                   response: NewResultResponse<User>) {
                if (!isAdded) {
                    return
                }
                hideProgress()
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
        installedApp.appKey = activity?.packageName
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
                saveDevice(response.data!!)
                AppInfoManager.getInstance().isAlimAgree = true
                loginCheck(LoginInfoManager.getInstance().user)
            }

            override fun onFailure(call: Call<NewResultResponse<User>>,
                                   t: Throwable,
                                   response: NewResultResponse<User>) {

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

    private fun loginCheck(data: User) {

        LoginResultManager2.getInstance().success(data) { state ->
            when (state) {
                LoginResultManager2.loginState.Success -> {
                    LogUtil.e(LOG_TAG, "loginCheck Success")
                    successLogin()
                }
                LoginResultManager2.loginState.UnVerifiedUserActivity -> {
                    val intent = Intent(activity, VerificationActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                    intent.putExtra(Const.KEY, Const.MOBILE_NUMBER)
                    changeMobileLauncher.launch(intent)
                }
                LoginResultManager2.loginState.AuthActivity -> findPw()
                LoginResultManager2.loginState.Cancel_btn -> {

                    logOut()
                }
                LoginResultManager2.loginState.Exile->{
                    logOut()
                    try{
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("kakaoplus://plusfriend/home/@luckybol"))
                        startActivity(intent)
                    }catch (e: Exception){
                        PplusCommonUtil.openChromeWebView(requireActivity(), "http://pf.kakao.com/_xcXKrK")
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

    private fun logOut(){
        val contactDao = DBManager.getInstance(LuckyBolApplication.getContext()).session.contactDao
        contactDao.deleteAll()
        LoginInfoManager.getInstance().clear() // 초기화
        AppInfoManager.getInstance().setAutoSignIn(false)
    }


    private fun successLogin() {

        // 자동로그인여부 저장
        AppInfoManager.getInstance().setAutoSignIn(mIsAutoSigIn)

        if (StringUtils.isNotEmpty(LoginInfoManager.getInstance().user.nickname)) {

            ApiBuilder.create().getNotSignedActiveTermsAll(Const.APP_TYPE).setCallback(object : PplusCallback<NewResultResponse<Terms>> {
                override fun onResponse(call: Call<NewResultResponse<Terms>>?,
                                        response: NewResultResponse<Terms>?) {

                    if(!isAdded){
                        return
                    }

                    for (terms in response!!.datas!!) {
                        if (terms.isCompulsory) {
                            val intent = Intent(activity, AddInfoActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
                            configLauncher.launch(intent)
                            return
                        }
                    }
                    ContactUtil.getInstance().load(requireActivity())

                    if(!BuzzAdBenefit.getPrivacyPolicyManager()!!.isConsentGranted()){
                        BuzzAdBenefit.getPrivacyPolicyManager()?.showConsentUI(activity!!, object : PrivacyPolicyEventListener {
                            override fun onUpdated(accepted: Boolean) {
                                if(accepted){
                                    PplusCommonUtil.setBuzvilProfileData()
                                }
                                checkSignUp()
                            }
                        })
                    }else{
                        PplusCommonUtil.setBuzvilProfileData()
                        checkSignUp()
                    }

                }

                override fun onFailure(call: Call<NewResultResponse<Terms>>?,
                                       t: Throwable?,
                                       response: NewResultResponse<Terms>?) {

                }
            }).build().call()

        } else {
            val intent = Intent(activity, AddInfoActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
            configLauncher.launch(intent)
        }

    }

    private fun checkSignUp(){
        setEvent(requireActivity(), "login")
        activity?.setResult(Activity.RESULT_OK)
        activity?.finish()
    }

    private fun findId() {
        val intent = Intent(activity, FindIdActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        findIdLauncher.launch(intent)
    }

    private fun findPw() {

        val intent = Intent(activity, FindPWActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        startActivity(intent)
    }

    private fun goSignUp(params: Map<String, String>? = null) {

        if (PplusCommonUtil.getCountryCode().equals("kr", ignoreCase = true)) {
            val intent = Intent(activity, JoinActivity::class.java)
            if (params != null) {
                intent.putExtra(Const.ID, params["loginId"])
                intent.putExtra(Const.PASSWORD, Crypt.decrypt(params["password"]))
                intent.putExtra(Const.ACCOUNT_TYPE, params["accountType"])
                intent.putExtra(Const.NICKNAME, params["nickname"])
            }

            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            joinLauncher.launch(intent)
        } else {
            val builder = AlertBuilder.Builder()
            builder.setTitle(getString(R.string.word_notice_alert))
            builder.addContents(AlertData.MessageData(getString(R.string.msg_do_not_service_country_alert1), AlertBuilder.MESSAGE_TYPE.TEXT, 2))
            builder.addContents(AlertData.MessageData(getString(R.string.msg_do_not_service_country_alert2), AlertBuilder.MESSAGE_TYPE.TEXT, 2))
            builder.setLeftText(getString(R.string.word_confirm))
            builder.setOnAlertResultListener(object : OnAlertResultListener {

                override fun onCancel() {

                }

                override fun onResult(event_alert: AlertBuilder.EVENT_ALERT) {

                }
            }).builder().show(activity)
        }
    }

    private fun changeMobile(verification: Verification, mobile: String) {
        val params: MutableMap<String, String> = HashMap()
        params["mobile"] = mobile
        params["number"] = verification.number
        params["token"] = verification.token
        params["appType"] = Const.APP_TYPE
        showProgress("")
        ApiBuilder.create().updateMobileByVerification(params).setCallback(object : PplusCallback<NewResultResponse<Any>> {
            override fun onResponse(call: Call<NewResultResponse<Any>>?,
                                    response: NewResultResponse<Any>?) {
                hideProgress()
                val params = HashMap<String, String>()
                params["loginId"] = LoginInfoManager.getInstance().user.loginId!!
                params["password"] = Crypt.encrypt(PplusCommonUtil.decryption(LoginInfoManager.getInstance().user.password!!))
                params["encrypted"] = "true"
                PreferenceUtil.getDefaultPreference(activity).put(Const.SIGN_UP, true)
                login(params)
            }

            override fun onFailure(call: Call<NewResultResponse<Any>>?,
                                   t: Throwable?,
                                   response: NewResultResponse<Any>?) {
                hideProgress()
                if (response?.resultCode == 504) {
                    showAlert(R.string.msg_exist_mobile)
                } else {
                    showAlert(R.string.msg_can_not_use_mobile)
                }
            }
        }).build().call()
    }

    private fun checkSignIn(){
        activity?.setResult(Activity.RESULT_OK)
        activity?.finish()
    }

    val joinLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            // There are no request codes
            val data = result.data
            val params = HashMap<String, String>()
            params["loginId"] = LoginInfoManager.getInstance().user.loginId!!
            params["password"] = Crypt.encrypt(PplusCommonUtil.decryption(LoginInfoManager.getInstance().user.password!!))
            params["encrypted"] = "true"
            PreferenceUtil.getDefaultPreference(activity).put(Const.SIGN_UP, true)
            login(params)
        }
    }

    val findIdLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            // There are no request codes
            val data = result.data
            if (data != null && data.getStringExtra(Const.KEY) == Const.FIND_PW) {
                findPw()
            }
        }
    }

    val changeMobileLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            // There are no request codes
            val data = result.data
            if(data != null){
                val verification = data.getParcelableExtra<Verification>(Const.VERIFICATION)
                val mobile = data.getStringExtra(Const.MOBILE_NUMBER)
                changeMobile(verification!!, mobile!!)
            }
        }
    }

    val signInLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            // There are no request codes
            val data = result.data
            checkSignIn()
        }
    }

    val configLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        successLogin()
    }

    val leaveCancelLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            // There are no request codes
            val data = result.data
            if (data != null) {
                val verification = data.getParcelableExtra<Verification>(Const.VERIFICATION)
                val params = HashMap<String, String>()
                params["number"] = verification!!.number
                params["token"] = verification.token
                params["media"] = verification.media
                params["mobile"] = data.getStringExtra(Const.MOBILE_NUMBER)!!
                params["loginId"] = LoginInfoManager.getInstance().user.loginId!!
                params["appType"] = Const.APP_TYPE
                showProgress("")
                ApiBuilder.create().cancelLeave(params).setCallback(object : PplusCallback<NewResultResponse<Any>> {

                    override fun onResponse(call: Call<NewResultResponse<Any>>, response: NewResultResponse<Any>) {

                        hideProgress()
                        val params = HashMap<String, String>()
                        params["loginId"] = LoginInfoManager.getInstance().user.loginId!!
                        params["password"] = Crypt.encrypt(PplusCommonUtil.decryption(LoginInfoManager.getInstance().user.password!!))
                        params["encrypted"] = "true"
                        login(params)
                    }

                    override fun onFailure(call: Call<NewResultResponse<Any>>, t: Throwable, response: NewResultResponse<Any>) {
                        LogUtil.e(LOG_TAG, "onFailure")
                        val contactDao = DBManager.getInstance(requireActivity()).session.contactDao
                        contactDao.deleteAll()
                        LoginInfoManager.getInstance().clear() // 초기화
                        AppInfoManager.getInstance().setAutoSignIn(false)
                        hideProgress()
                    }
                }).build().call()
            }
        }
    }


    companion object {

        fun newInstance(): SnsLoginFragment {

            val fragment = SnsLoginFragment()
            val args = Bundle()
//            args.putString(ARG_PARAM1, param1)
//            args.putString(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }
    }

}// Required empty public constructor
