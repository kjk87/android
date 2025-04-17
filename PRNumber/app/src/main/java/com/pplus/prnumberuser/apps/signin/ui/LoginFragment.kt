package com.pplus.prnumberuser.apps.signin.ui


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.pplus.networks.common.PplusCallback
import com.pplus.prnumberuser.Const
import com.pplus.prnumberuser.R
import com.pplus.prnumberuser.apps.common.builder.AlertBuilder
import com.pplus.prnumberuser.apps.common.builder.OnAlertResultListener
import com.pplus.prnumberuser.apps.common.builder.data.AlertData
import com.pplus.prnumberuser.apps.common.mgmt.AppInfoManager
import com.pplus.prnumberuser.apps.common.mgmt.LoginInfoManager
import com.pplus.prnumberuser.apps.common.mgmt.LoginResultManager2
import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
import com.pplus.prnumberuser.apps.common.ui.base.BaseFragment
import com.pplus.prnumberuser.apps.event.ui.EventDetailActivity
import com.pplus.prnumberuser.apps.page.ui.PageActivity
import com.pplus.prnumberuser.apps.page.ui.PageAttendanceActivity
import com.pplus.prnumberuser.apps.page.ui.PageFirstBenefitActivity
import com.pplus.prnumberuser.apps.setting.ui.VerificationActivity
import com.pplus.prnumberuser.apps.signup.ui.JoinActivity
import com.pplus.prnumberuser.apps.signup.ui.VerificationMeActivity
import com.pplus.prnumberuser.apps.subscription.ui.SubscriptionDetailActivity
import com.pplus.prnumberuser.core.Crypt
import com.pplus.prnumberuser.core.code.common.LoginStatus
import com.pplus.prnumberuser.core.code.common.SnsTypeCode
import com.pplus.prnumberuser.core.database.DBManager
import com.pplus.prnumberuser.core.network.ApiBuilder
import com.pplus.prnumberuser.core.network.model.dto.*
import com.pplus.prnumberuser.core.network.model.request.params.ParamsRegDevice
import com.pplus.prnumberuser.core.network.model.response.NewResultResponse
import com.pplus.prnumberuser.core.util.PplusCommonUtil
import com.pplus.prnumberuser.databinding.FragmentLoginBinding
import com.pplus.utils.part.logs.LogUtil
import com.pplus.utils.part.pref.PreferenceUtil
import com.pplus.utils.part.utils.StringUtils
import retrofit2.Call
import java.util.*
import kotlin.collections.HashMap


/**
 * A simple [Fragment] subclass.
 * Use the [LoginFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LoginFragment : BaseFragment<BaseActivity>() {

    override fun getPID(): String {
        return "Login"
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        if (arguments != null) { //            mParam1 = arguments!!.getString(ARG_PARAM1)
            //            mParam2 = arguments!!.getString(ARG_PARAM2)
        }
    }

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    var mIsAutoSigIn = true

    override fun init() {
        binding.editLoginId.setSingleLine()
        binding.editLoginPw.setSingleLine()
        binding.editLoginPw.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD

        binding.textLogin.setOnClickListener {
            val id = binding.editLoginId.text.toString().trim()

            if (StringUtils.isEmpty(id)) {
                showAlert(R.string.msg_input_id)
                return@setOnClickListener
            }

            val password = binding.editLoginPw.text.toString().trim()

            if (StringUtils.isEmpty(password)) {
                showAlert(R.string.msg_input_password)
                return@setOnClickListener
            }

            val params = HashMap<String, String>()
            params["loginId"] = id
            params["password"] = Crypt.encrypt(password)
            params["accountType"] = SnsTypeCode.pplus.name
            params["encrypted"] = "true"
            login(params)
        }

        binding.textLoginSignUp.setOnClickListener {
            goSignUp(null)
        }

        binding.textLoginAuto.setOnClickListener {

            mIsAutoSigIn = !mIsAutoSigIn
            binding.textLoginAuto.isSelected = mIsAutoSigIn
        }

        binding.textLoginAuto.isSelected = mIsAutoSigIn

        binding.textLoginFindId.setOnClickListener {
            findId()
        }

        binding.textLoginFindPassword.setOnClickListener {
            findPw()
        }

    }

    private fun login(params: HashMap<String, String>) {
        params["appType"] = Const.APP_TYPE
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
                                }
                            }
                        }).builder().show(activity)
                    }

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
                                    goSignUp(null)
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
                                        goSignUp(null)
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
        params["device.installedApp.appKey"] = requireActivity().packageName
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
        installedApp.appKey = requireActivity().packageName
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
                LoginResultManager2.loginState.Exile // 영구정지
                -> {
                    requireActivity().setResult(Activity.RESULT_CANCELED)
                    requireActivity().finish()
                }
                LoginResultManager2.loginState.AuthActivity -> findPw()
            }
        }

    }

    private fun successLogin() {

        // 자동로그인여부 저장
        AppInfoManager.getInstance().setAutoSignIn(mIsAutoSigIn)

        if (StringUtils.isNotEmpty(LoginInfoManager.getInstance().user.nickname)) {

            ApiBuilder.create().getNotSignedActiveTermsAll(Const.APP_TYPE).setCallback(object : PplusCallback<NewResultResponse<Terms>> {
                override fun onResponse(call: Call<NewResultResponse<Terms>>?, response: NewResultResponse<Terms>?) {

                    for (terms in response!!.datas) {
                        if (terms.isCompulsory) {
                            val intent = Intent(activity, AddInfoActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
                            configLauncher.launch(intent)
                            return
                        }
                    }

                    //                    val task = LoadContactTask(activity, LoadContactTask.OnResultListener {
                    //                        //                        if (isAdded) {
                    ////                            hideProgress()
                    ////                            activity!!.setResult(Activity.RESULT_OK)
                    ////                            activity!!.finish()
                    ////                        }
                    //                    })
                    //                    task.execute()
                    if (PreferenceUtil.getDefaultPreference(activity).get(Const.SIGN_UP, false)) {
                        PreferenceUtil.getDefaultPreference(activity).put(Const.SIGN_UP, false)

                        val pageSeqNo = PreferenceUtil.getDefaultPreference(activity).get(Const.PLUS_INFO_PAGE_SEQ_NO, -1L)
                        if (pageSeqNo != -1L) {

                            PreferenceUtil.getDefaultPreference(activity).put(Const.PLUS_INFO_PAGE_SEQ_NO, -1L)

                            val intent = Intent(activity!!, PageActivity::class.java)
                            val page = Page()
                            page.no = pageSeqNo
                            intent.putExtra(Const.PAGE, page)
                            intent.putExtra(Const.KEY, Const.PLUS_INFO)
                            startActivity(intent)
                        }

                        val visitPageSeqNo = PreferenceUtil.getDefaultPreference(activity).get(Const.VISIT_PAGE_SEQ_NO, -1L)
                        if (visitPageSeqNo != -1L) {

                            PreferenceUtil.getDefaultPreference(activity).put(Const.VISIT_PAGE_SEQ_NO, -1L)

                            //                            val intent = Intent(activity!!, PageCashBackActivity::class.java)
                            val intent = Intent(activity!!, PageAttendanceActivity::class.java)
                            val page = Page()
                            page.no = visitPageSeqNo
                            intent.putExtra(Const.DATA, page)
                            startActivity(intent)
                        }

                        val benefitPageSeqNo = PreferenceUtil.getDefaultPreference(activity).get(Const.BENEFIT_PAGE_SEQ_NO, -1L)
                        if (benefitPageSeqNo != -1L) {

                            PreferenceUtil.getDefaultPreference(activity).put(Const.BENEFIT_PAGE_SEQ_NO, -1L)

                            val intent = Intent(activity!!, PageFirstBenefitActivity::class.java)
                            val page = Page2()
                            page.seqNo = benefitPageSeqNo
                            intent.putExtra(Const.DATA, page)
                            startActivity(intent)
                        }

                        val subscriptionProductSeqNo = PreferenceUtil.getDefaultPreference(activity).get(Const.SUBSCRIPTION_PRODUCT_SEQ_NO, -1L)
                        if (subscriptionProductSeqNo != -1L) {

                            PreferenceUtil.getDefaultPreference(activity).put(Const.SUBSCRIPTION_PRODUCT_SEQ_NO, -1L)

                            val intent = Intent(activity!!, SubscriptionDetailActivity::class.java)
                            val productPrice = ProductPrice()
                            productPrice.seqNo = subscriptionProductSeqNo
                            intent.putExtra(Const.DATA, productPrice)
                            intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
                            startActivity(intent)
                        }

                        val eventSeqNo = PreferenceUtil.getDefaultPreference(activity).get(Const.EVENT_DETAIL_SEQ_NO, -1L)
                        if (eventSeqNo != -1L) {

                            PreferenceUtil.getDefaultPreference(activity).put(Const.EVENT_DETAIL_SEQ_NO, -1L)

                            val intent = Intent(activity!!, EventDetailActivity::class.java)
                            val event = Event()
                            event.no = eventSeqNo
                            intent.putExtra(Const.DATA, event)
                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                            startActivity(intent)
                        }
                    }
                    activity?.setResult(Activity.RESULT_OK)
                    activity?.finish()

                    //                    val pPlusPermission = PPlusPermission(activity)
                    //                    pPlusPermission.addPermission(Permission.PERMISSION_KEY.CONTACTS)
                    //                    pPlusPermission.setPermissionListener(object : PermissionListener {
                    //
                    //                        override fun onPermissionGranted() {
                    //
                    //                            showProgress(getString(R.string.msg_contact_loading))
                    //                            val task = LoadContactTask(activity, LoadContactTask.OnResultListener {
                    //                                if (isAdded) {
                    //                                    hideProgress()
                    //                                    activity!!.setResult(Activity.RESULT_OK)
                    //                                    activity!!.finish()
                    //                                }
                    //                            })
                    //                            task.execute()
                    //
                    //                        }
                    //
                    //                        override fun onPermissionDenied(deniedPermissions: ArrayList<String>) {
                    //
                    //                            if (isAdded) {
                    //                                activity!!.setResult(Activity.RESULT_OK)
                    //                                activity!!.finish()
                    //                            }
                    //
                    //                        }
                    //                    })
                    //                    pPlusPermission.checkPermission()
                }

                override fun onFailure(call: Call<NewResultResponse<Terms>>?, t: Throwable?, response: NewResultResponse<Terms>?) {

                }
            }).build().call()

        } else {
            val intent = Intent(activity, AddInfoActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
            configLauncher.launch(intent)
        }

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
                intent.putExtra(Const.NICKNAME, params["NICKNAME"])
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
        val params = HashMap<String, String>()
        params["mobile"] = mobile
        params["number"] = verification.number
        params["token"] = verification.token
        params["appType"] = Const.APP_TYPE
        showProgress("")
        ApiBuilder.create().updateMobileByVerification(params).setCallback(object : PplusCallback<NewResultResponse<Any>> {
            override fun onResponse(call: Call<NewResultResponse<Any>>, response: NewResultResponse<Any>) {
                hideProgress()
                val params = HashMap<String, String>()
                params["loginId"] = LoginInfoManager.getInstance().user.loginId!!
                params["password"] = Crypt.encrypt(PplusCommonUtil.decryption(LoginInfoManager.getInstance().user.password!!))
                params["encrypted"] = "true"
                PreferenceUtil.getDefaultPreference(activity).put(Const.SIGN_UP, true)
                login(params)
            }

            override fun onFailure(call: Call<NewResultResponse<Any>>, t: Throwable, response: NewResultResponse<Any>) {
                hideProgress()
                if (response.resultCode == 504) {
                    showAlert(R.string.msg_exist_mobile)
                } else {
                    showAlert(R.string.msg_can_not_use_mobile)
                }
            }
        }).build().call()
    }

    val joinLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) { // There are no request codes
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
        if (result.resultCode == Activity.RESULT_OK) { // There are no request codes
            val data = result.data
            if (data != null && data.getStringExtra(Const.KEY) == Const.FIND_PW) {
                findPw()
            }
        }
    }

    val changeMobileLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) { // There are no request codes
            val data = result.data
            if (data != null) {
                val verification = data.getParcelableExtra<Verification>(Const.VERIFICATION)
                val mobile = data.getStringExtra(Const.MOBILE_NUMBER)
                changeMobile(verification!!, mobile!!)
            }
        }
    }

    val configLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        successLogin()
    }

    val leaveCancelLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) { // There are no request codes
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

        fun newInstance(): LoginFragment {

            val fragment = LoginFragment()
            val args = Bundle() //            args.putString(ARG_PARAM1, param1)
            //            args.putString(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }
    }

} // Required empty public constructor
