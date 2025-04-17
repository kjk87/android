package com.pplus.prnumberbiz.apps.signin.ui


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.view.View
import androidx.fragment.app.Fragment
import com.facebook.FacebookException
import com.facebook.GraphResponse
import com.pple.pplus.utils.part.logs.LogUtil
import com.pple.pplus.utils.part.pref.PreferenceUtil
import com.pple.pplus.utils.part.utils.StringUtils
import com.pplus.prnumberbiz.Const
import com.pplus.prnumberbiz.R
import com.pplus.prnumberbiz.apps.common.builder.AlertBuilder
import com.pplus.prnumberbiz.apps.common.builder.OnAlertResultListener
import com.pplus.prnumberbiz.apps.common.builder.data.AlertData
import com.pplus.prnumberbiz.apps.common.mgmt.AppInfoManager
import com.pplus.prnumberbiz.apps.common.mgmt.LoginInfoManager
import com.pplus.prnumberbiz.apps.common.mgmt.LoginResultManager2
import com.pplus.prnumberbiz.apps.common.ui.base.BaseActivity
import com.pplus.prnumberbiz.apps.common.ui.base.BaseFragment
import com.pplus.prnumberbiz.apps.goods.ui.SellerApplyActivity
import com.pplus.prnumberbiz.apps.signup.ui.JoinActivity
import com.pplus.prnumberbiz.apps.signup.ui.VerificationMeActivity
import com.pplus.prnumberbiz.core.code.common.EnumData
import com.pplus.prnumberbiz.core.code.common.LoginStatus
import com.pplus.prnumberbiz.core.code.common.SnsTypeCode
import com.pplus.prnumberbiz.core.contact.LoadContactTask
import com.pplus.prnumberbiz.core.database.DBManager
import com.pplus.prnumberbiz.core.network.ApiBuilder
import com.pplus.prnumberbiz.core.network.model.dto.*
import com.pplus.prnumberbiz.core.network.model.request.params.ParamsRegDevice
import com.pplus.prnumberbiz.core.network.model.response.NewResultResponse
import com.pplus.prnumberbiz.core.sns.facebook.FaceBookUtil
import com.pplus.prnumberbiz.core.sns.google.GoogleUtil
import com.pplus.prnumberbiz.core.util.PplusCommonUtil
import kotlinx.android.synthetic.main.fragment_login.*
import network.common.PplusCallback
import org.json.JSONObject
import retrofit2.Call
import java.util.*


/**
 * A simple [Fragment] subclass.
 * Use the [LoginFragment.newInstance] factory method to
 * create an instance of this mapFragment.
 */
class LoginFragment : BaseFragment<BaseActivity>() {

    override fun getPID(): String {
        return "Login"
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        if (arguments != null) {
//            mParam1 = arguments!!.getString(ARG_PARAM1)
//            mParam2 = arguments!!.getString(ARG_PARAM2)
        }
    }

    override fun getLayoutResourceId(): Int {
        return R.layout.fragment_login
    }

    override fun initializeView(container: View?) {

    }

    var mIsAutoSigIn = true

    override fun init() {
        edit_login_id.setSingleLine()
        edit_login_pw.setSingleLine()
        edit_login_pw.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD

        text_login.setOnClickListener {
            val id = edit_login_id.getText().toString().trim()

            if (StringUtils.isEmpty(id)) {
                showAlert(R.string.msg_input_id)
                return@setOnClickListener
            }

            val password = edit_login_pw.getText().toString().trim()

            if (StringUtils.isEmpty(password)) {
                showAlert(R.string.msg_input_password)
                return@setOnClickListener
            }

            val params = HashMap<String, String>()
            params["loginId"] = id
            params["password"] = password
            params["accountType"] = SnsTypeCode.pplus.name
            login(params)
        }

        text_login_signUp.setOnClickListener {
            goSignUp(null)
        }

        text_login_auto.setOnClickListener {

            mIsAutoSigIn = !mIsAutoSigIn
            text_login_auto.isSelected = mIsAutoSigIn
        }

        text_login_auto.isSelected = mIsAutoSigIn

        text_login_find_id.setOnClickListener {
            findId()
        }

        text_login_find_password.setOnClickListener {
            findPw()
        }

        layout_login_facebook.setOnClickListener {
            FaceBookUtil.registerCallback(object : FaceBookUtil.FaceBookCallbackListener {

                override fun onSuccess(loginResult: com.facebook.login.LoginResult) {
                    //로그인 성공후 프로필 정보 요청
                    hideProgress()
                    mIsAutoSigIn = true
                    val params = HashMap<String, String>()
                    params["loginId"] = loginResult.accessToken.userId
                    params["password"] = loginResult.accessToken.token
                    params["accountType"] = SnsTypeCode.facebook.name
                    login(params)
                }

                override fun onCancel() {

                    hideProgress()
                }

                override fun onError(exception: FacebookException) {

                    hideProgress()
                }

                override fun onCompleted(`object`: JSONObject, response: GraphResponse) {

                    hideProgress()

                    if (!isAdded) {
                        return
                    }

                    //프로필 정보 요청 성공
                    LogUtil.e(LOG_TAG, `object`.toString())
//                    val id = `object`.optString("id")
//                    val token = AccessToken.getCurrentAccessToken().token

//                    GraphRequest(
//                            AccessToken.getCurrentAccessToken(),
//                            "/$id/ids_for_business",
//                            null,
//                            HttpMethod.GET,
//                            GraphRequest.Callback { /* handle the result */
//
//                                LogUtil.e(LOG_TAG, it.jsonObject.toString())
//                            }
//                    ).executeAsync()


                }
            })
            FaceBookUtil.logIn(this)
            showProgress("")
        }

        layout_login_google.setOnClickListener {
            showProgress("")
            GoogleUtil.init(activity) { result ->
                hideProgress()

                if (result.isSuccess) {
                    val acct = result.signInAccount
                    val id = acct!!.id
                    val token = acct.idToken

                    LogUtil.e("id", "" + id!!)
                    LogUtil.e("token", "" + token!!)
                    mIsAutoSigIn = true
                    val params = HashMap<String, String>()
                    params["loginId"] = id
                    params["password"] = token
                    params["accountType"] = SnsTypeCode.google.name
                    login(params)
                }
            }
        }
    }

    private fun login(params: Map<String, String>) {
        if (!isAdded) {
            return
        }
        showProgress("")
        ApiBuilder.create().login(params).setCallback(object : PplusCallback<NewResultResponse<User>> {

            override fun onResponse(call: Call<NewResultResponse<User>>?, response: NewResultResponse<User>?) {
                if (!isAdded) {
                    return
                }
                val data = response!!.data
                data.password = PplusCommonUtil.encryption(params["password"]!!)
                hideProgress()
                LoginInfoManager.getInstance().user = data
                LoginInfoManager.getInstance().save()

                if (data.useStatus != null && data.useStatus == LoginStatus.waitingToLeave.name) {//탈퇴대기
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
                                    val intent = Intent(activity, VerificationMeActivity::class.java)
                                    intent.putExtra(Const.KEY, Const.CANCEL_LEAVE)
                                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                                    activity!!.startActivityForResult(intent, Const.REQ_LEAVE_CANCEL)
                                }
                            }
                        }
                    }).builder().show(activity)
                } else if (data.useStatus != null && data.useStatus == LoginStatus.leave.name) {
                    val builder = AlertBuilder.Builder()
                    builder.setTitle(getString(R.string.word_notice_alert))
                    builder.addContents(AlertData.MessageData(getString(R.string.msg_not_user_go_sign_up), AlertBuilder.MESSAGE_TYPE.TEXT, 2))
                    builder.setLeftText(getString(R.string.word_cancel)).setRightText(getString(R.string.word_confirm))
                    builder.setOnAlertResultListener(object : OnAlertResultListener {

                        override fun onCancel() {

                        }

                        override fun onResult(event_alert: AlertBuilder.EVENT_ALERT) {

                            when (event_alert) {
                                AlertBuilder.EVENT_ALERT.RIGHT -> {
                                    if (StringUtils.isNotEmpty(params["accountType"]) && !params["accountType"].equals(SnsTypeCode.pplus.name)) {
                                        goSignUp(params)
                                    } else {
                                        goSignUp(null)
                                    }
                                }
                            }
                        }
                    }).builder().show(activity)
                } else if (data.useStatus != null && data.useStatus == LoginStatus.pending.name) {
                    val builder = AlertBuilder.Builder()
                    builder.setTitle(getString(R.string.word_notice_alert))
                    builder.setStyle_alert(AlertBuilder.STYLE_ALERT.MESSAGE)
                    builder.addContents(AlertData.MessageData(getString(R.string.msg_verification_pending_user1), AlertBuilder.MESSAGE_TYPE.TEXT, 3))
                    builder.addContents(AlertData.MessageData(getString(R.string.msg_verification_pending_user2), AlertBuilder.MESSAGE_TYPE.TEXT, 2))
                    builder.setLeftText(getString(R.string.word_cancel)).setRightText(getString(R.string.word_confirm))
                    builder.setBackgroundClickable(false).setAutoCancel(false)
                    builder.setOnAlertResultListener(object : OnAlertResultListener {

                        override fun onCancel() {}

                        override fun onResult(event_alert: AlertBuilder.EVENT_ALERT) {

                            when (event_alert) {
                                AlertBuilder.EVENT_ALERT.RIGHT -> {
                                    val intent = Intent(activity, JoinActivity::class.java)
                                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                                    intent.putExtra(Const.KEY, Const.PENDING)
                                    activity!!.startActivityForResult(intent, Const.REQ_JOIN)
                                }
                            }
                        }
                    }).builder().show(activity)
                } else {
                    existsDevice(data.no)
                }
            }

            override fun onFailure(call: Call<NewResultResponse<User>>?, t: Throwable?, response: NewResultResponse<User>?) {
                hideProgress()
                if (!isAdded) {
                    return
                }
                if (response != null) {
                    if (response.resultCode == 503) {
                        val builder = AlertBuilder.Builder()
                        builder.setTitle(getString(R.string.word_notice_alert))
                        builder.addContents(AlertData.MessageData(getString(R.string.msg_not_user_go_sign_up), AlertBuilder.MESSAGE_TYPE.TEXT, 2))
                        builder.setLeftText(getString(R.string.word_cancel)).setRightText(getString(R.string.word_confirm))
                        builder.setOnAlertResultListener(object : OnAlertResultListener {

                            override fun onCancel() {

                            }

                            override fun onResult(event_alert: AlertBuilder.EVENT_ALERT) {

                                when (event_alert) {
                                    AlertBuilder.EVENT_ALERT.RIGHT -> {
                                        if (StringUtils.isNotEmpty(params["accountType"]) && !params["accountType"].equals(SnsTypeCode.pplus.name)) {
                                            goSignUp(params)
                                        } else {
                                            goSignUp(null)
                                        }
                                    }
                                }
                            }
                        }).builder().show(activity)
                    } else {
                        LoginResultManager2.getInstance().fail(response.data) { state ->
                            when (state) {

                                LoginResultManager2.loginState.AuthActivity -> findPw()
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
        params["device.installedApp.appKey"] = activity!!.packageName
        params["device.installedApp.version"] = AppInfoManager.getInstance().appVersion
        ApiBuilder.create().existsDevice(params).setCallback(object : PplusCallback<NewResultResponse<User>> {

            override fun onResponse(call: Call<NewResultResponse<User>>, response: NewResultResponse<User>) {
                if (!isAdded) {
                    return
                }
                hideProgress()
                LogUtil.e(LOG_TAG, "onResponse")
                saveDevice(response.data)
                if (StringUtils.isNotEmpty(AppInfoManager.getInstance().pushToken) && (response.data == null || response.data.device == null || response.data.device!!.installedApp == null || StringUtils.isEmpty(response.data.device!!.installedApp.pushKey) || AppInfoManager.getInstance().pushToken != response.data.device!!.installedApp.pushKey)) {
                    registDevice(no, LoginInfoManager.getInstance().user.device!!.installedApp.isPushActivate, LoginInfoManager.getInstance().user.device!!.installedApp.pushMask)
                } else {
                    loginCheck(LoginInfoManager.getInstance().user)
                }
            }

            override fun onFailure(call: Call<NewResultResponse<User>>, t: Throwable, response: NewResultResponse<User>) {
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
        installedApp.appKey = activity!!.packageName
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
                loginCheck(LoginInfoManager.getInstance().user)
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

    private fun loginCheck(data: User) {

        if (data.certificationLevel != null && data.certificationLevel!! < 11) {
            val intent = Intent(activity, JoinActivity::class.java)
            intent.putExtra(Const.MOBILE_NUMBER, LoginInfoManager.getInstance().user.mobile)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            intent.putExtra(Const.KEY, Const.LEVEL_UP)
            activity!!.startActivityForResult(intent, Const.REQ_JOIN)
        } else {
            val page = LoginInfoManager.getInstance().user.page
            if (page != null) {
                AppInfoManager.getInstance().setAutoSignIn(mIsAutoSigIn)
                if (page.status == EnumData.PageStatus.normal.name) {
                    LoginResultManager2.getInstance().success(data) { state ->
                        when (state) {
                            LoginResultManager2.loginState.Success -> {
                                LogUtil.e(LOG_TAG, "loginCheck Success")
                                successLogin()
                            }
                            LoginResultManager2.loginState.UnVerifiedUserActivity -> {
                                val intent = Intent(activity, VerificationMeActivity::class.java)
                                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                                intent.putExtra(Const.KEY, Const.MOBILE_NUMBER)
                                activity!!.startActivityForResult(intent, Const.REQ_CHANGE_PHONE)
                            }
                            LoginResultManager2.loginState.Exile // 영구정지
                            -> {
                                activity!!.setResult(Activity.RESULT_CANCELED)
                                activity!!.finish()
                            }
                            LoginResultManager2.loginState.AuthActivity -> findPw()
                        }
                    }
                } else if (page.status == EnumData.PageStatus.ready.name) { //미승인
                    val intent = Intent(activity, SellerApplyActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                    activity?.startActivityForResult(intent, Const.REQ_APPLY)
                } else if (page.status == EnumData.PageStatus.pending.name || page.status == EnumData.PageStatus.redemand.name) { //승인대기
                    val intent = Intent(activity, PageStatusActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                    activity?.startActivityForResult(intent, Const.REQ_APPLY)
//                activatePage();
                } else if (page.status == "return") {//승인거절
                    val intent = Intent(activity, PageStatusActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                    activity?.startActivityForResult(intent, Const.REQ_APPLY)
//                activatePage();
                }else if (page.status == EnumData.PageStatus.refuse.name) {
                    val intent = Intent(activity, AlertRefuseActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                    activity?.startActivityForResult(intent, Const.REQ_REFUSE)
                }
            }else{
                val intent = Intent(activity, JoinActivity::class.java)
                intent.putExtra(Const.MOBILE_NUMBER, LoginInfoManager.getInstance().user.mobile)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                intent.putExtra(Const.KEY, Const.LEVEL_UP)
                activity!!.startActivityForResult(intent, Const.REQ_JOIN)
            }

        }

    }

    private fun successLogin() {

        // 자동로그인여부 저장
        AppInfoManager.getInstance().setAutoSignIn(mIsAutoSigIn)

        ApiBuilder.create().getNotSignedActiveTermsAll(context!!.packageName).setCallback(object : PplusCallback<NewResultResponse<Terms>> {
            override fun onResponse(call: Call<NewResultResponse<Terms>>?, response: NewResultResponse<Terms>?) {

                for (terms in response!!.datas) {
                    if (terms.isCompulsory) {
                        val intent = Intent(activity, AddTermsActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
                        activity!!.startActivityForResult(intent, Const.REQ_CONFIG)
                        return
                    }
                }

                val task = LoadContactTask(activity, LoadContactTask.OnResultListener {

                })
                task.execute()

                activity!!.setResult(Activity.RESULT_OK)
                activity!!.finish()
            }

            override fun onFailure(call: Call<NewResultResponse<Terms>>?, t: Throwable?, response: NewResultResponse<Terms>?) {

            }
        }).build().call()

    }

    private fun findId() {
        val intent = Intent(activity, FindIdActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        activity!!.startActivityForResult(intent, Const.REQ_FIND_ID)
    }

    private fun findPw() {

        val intent = Intent(activity, FindPWActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        activity!!.startActivityForResult(intent, Const.REQ_FIND_PW)
    }

    private fun goSignUp(params: Map<String, String>? = null) {

        if (PplusCommonUtil.getCountryCode().equals("kr", ignoreCase = true)) {
            val intent = Intent(activity, JoinActivity::class.java)
            if (params != null) {
                val user = User()
                user.loginId = params["loginId"]
                user.password = params["password"]
                user.accountType = params["accountType"]
                intent.putExtra(Const.USER, user)
//                intent.putExtra(Const.ID, params["loginId"])
//                intent.putExtra(Const.PASSWORD, params["password"])
//                intent.putExtra(Const.ACCOUNT_TYPE, params["accountType"])
            }

            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            activity!!.startActivityForResult(intent, Const.REQ_JOIN)
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        FaceBookUtil.onActivityResult(requestCode, resultCode, data)
        GoogleUtil.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            Const.REQ_REFUSE->{
                val contactDao = DBManager.getInstance(activity).session.contactDao
                contactDao.deleteAll()
                LoginInfoManager.getInstance().clear() // 초기화
                AppInfoManager.getInstance().setAutoSignIn(false)
            }
            Const.REQ_CHANGE_PHONE -> {
                if (resultCode == Activity.RESULT_OK && data != null) {
                    val verification: Verification = data.getParcelableExtra(Const.VERIFICATION)
                    val mobile = data.getStringExtra(Const.MOBILE_NUMBER)
                    changeMobile(verification, mobile)
                }
            }
            Const.REQ_SET_PAGE -> {
                if (resultCode == Activity.RESULT_OK) {
                    loginCheck(LoginInfoManager.getInstance().user)
                }

            }
            Const.REQ_CONFIG -> {
                if (resultCode == Activity.RESULT_OK) {
                    successLogin()
                }
            }
            Const.REQ_JOIN -> {
                if (resultCode == Activity.RESULT_OK) {
                    val params: MutableMap<String, String> = HashMap()
                    params["loginId"] = LoginInfoManager.getInstance().user.loginId!!
                    params["password"] = PplusCommonUtil.decryption(LoginInfoManager.getInstance().user.password!!)
                    PreferenceUtil.getDefaultPreference(activity).put(Const.SIGN_UP, true)
                    login(params)
                }
            }
            Const.REQ_FIND_ID -> {
                if (resultCode == Activity.RESULT_OK && data != null && data.getStringExtra(Const.KEY) == Const.FIND_PW) {
                    findPw()
                }
            }
            Const.REQ_FIND_PW -> {
            }
            Const.REQ_LEAVE_CANCEL -> {
                if (resultCode == Activity.RESULT_OK && data != null) {
                    val verification = data.getParcelableExtra<Verification>(Const.VERIFICATION)

                    val params = HashMap<String, String>()
                    params["number"] = verification.number
                    params["token"] = verification.token
                    params["media"] = verification.media
                    params["mobile"] = data.getStringExtra(Const.MOBILE_NUMBER)
                    params["loginId"] = LoginInfoManager.getInstance().user.loginId!!
                    showProgress("")
                    ApiBuilder.create().cancelLeave(params).setCallback(object : PplusCallback<NewResultResponse<Any>> {

                        override fun onResponse(call: Call<NewResultResponse<Any>>, response: NewResultResponse<Any>) {

                            hideProgress()
                            successLogin()
                        }

                        override fun onFailure(call: Call<NewResultResponse<Any>>, t: Throwable, response: NewResultResponse<Any>) {

                            val contactDao = DBManager.getInstance(activity).session.contactDao
                            contactDao.deleteAll()
                            LoginInfoManager.getInstance().clear() // 초기화
                            AppInfoManager.getInstance().setAutoSignIn(false)
                            hideProgress()
                        }
                    }).build().call()
                }
            }
            Const.REQ_APPLY, Const.REQ_RE_REG_NUMBER -> {
                if (resultCode == Activity.RESULT_OK){
                    val params: MutableMap<String, String> = HashMap()
                    params["loginId"] = LoginInfoManager.getInstance().user.loginId!!
                    params["password"] = PplusCommonUtil.decryption(LoginInfoManager.getInstance().user.password!!)
//                    params.put("accountType", LoginInfoManager.getInstance().user.accountType)
                    login(params)
                }

            }
        }
    }

    private fun changeMobile(verification: Verification, mobile: String) {
        val params: MutableMap<String, String> = HashMap()
        params["mobile"] = mobile
        params["number"] = verification.number
        params["token"] = verification.token
        showProgress("")
        ApiBuilder.create().updateMobileByVerification(params).setCallback(object : PplusCallback<NewResultResponse<Any?>> {
            override fun onResponse(call: Call<NewResultResponse<Any?>>, response: NewResultResponse<Any?>) {
                hideProgress()
                val params: MutableMap<String, String> = HashMap()
                params["loginId"] = LoginInfoManager.getInstance().user.loginId!!
                params["password"] = PplusCommonUtil.decryption(LoginInfoManager.getInstance().user.password!!)
                PreferenceUtil.getDefaultPreference(activity).put(Const.SIGN_UP, true)
                login(params)
            }

            override fun onFailure(call: Call<NewResultResponse<Any?>>, t: Throwable, response: NewResultResponse<Any?>) {
                hideProgress()
                if (response.resultCode == 504) {
                    showAlert(R.string.msg_exist_mobile)
                } else {
                    showAlert(R.string.msg_can_not_use_mobile)
                }
            }
        }).build().call()
    }

    companion object {

        fun newInstance(): LoginFragment {

            val fragment = LoginFragment()
            val args = Bundle()
//            args.putString(ARG_PARAM1, param1)
//            args.putString(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }
    }

}// Required empty public constructor
