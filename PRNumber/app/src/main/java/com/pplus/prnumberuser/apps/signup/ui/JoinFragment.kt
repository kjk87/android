package com.pplus.prnumberuser.apps.signup.ui


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.LinearLayout
import com.android.installreferrer.api.InstallReferrerClient
import com.android.installreferrer.api.InstallReferrerStateListener
import com.igaworks.v2.core.AdBrixRm
import com.pplus.networks.common.PplusCallback
import com.pplus.prnumberuser.Const
import com.pplus.prnumberuser.R
import com.pplus.prnumberuser.apps.common.mgmt.LoginInfoManager
import com.pplus.prnumberuser.apps.common.ui.base.BaseFragment
import com.pplus.prnumberuser.apps.common.ui.common.WebViewActivity
import com.pplus.prnumberuser.core.Crypt
import com.pplus.prnumberuser.core.code.common.SnsTypeCode
import com.pplus.prnumberuser.core.network.ApiBuilder
import com.pplus.prnumberuser.core.network.model.dto.No
import com.pplus.prnumberuser.core.network.model.dto.Terms
import com.pplus.prnumberuser.core.network.model.dto.User
import com.pplus.prnumberuser.core.network.model.response.NewResultResponse
import com.pplus.prnumberuser.core.util.PplusCommonUtil
import com.pplus.prnumberuser.databinding.FragmentJoinBinding
import com.pplus.prnumberuser.databinding.ItemTermsBinding
import com.pplus.utils.part.apps.resource.ResourceUtil
import com.pplus.utils.part.logs.LogUtil
import com.pplus.utils.part.pref.PreferenceUtil
import com.pplus.utils.part.utils.StringUtils
import retrofit2.Call
import java.util.*

/**
 * 회원가입 입력화면
 */
class JoinFragment : BaseFragment<JoinActivity>(), CompoundButton.OnCheckedChangeListener {

    private var mTermsList: ArrayList<Terms>? = null
    private var mTermsAgreeMap: MutableMap<Long, Boolean>? = null
    private var mIsCheckId: Boolean = false
    //    private var mIsCheckNickName: Boolean = false
    private var mSignedId: String? = null
//    private var mSignedNickName: String? = null

    private var mCheckBoxList: MutableList<CheckBox>? = null
    private var paramsJoin: User? = null

    override fun getPID(): String {

        return "Sign_account information"
    }

    private var _binding: FragmentJoinBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        _binding = FragmentJoinBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        paramsJoin = requireArguments().getParcelable(Const.JOIN)

    }

    var isCpa = false
    var cpaType = ""
    var cpaId = ""
    var mPageSeqNo:Long? = null
    var mVisitPageSeqNo:Long? = null
    var mVisitBenefitPageSeqNo:Long? = null
    var mEventSeqNo:Long? = null
    var mAdCode:String? = null
    var mJoinType:String? = null
    var mSubscriptionProductSeqNo:Long? = null

    override fun init() {
        isCpa = false
        mPageSeqNo = null
        mVisitPageSeqNo = null
        val referrerClient = InstallReferrerClient.newBuilder(activity).build()
        referrerClient.startConnection(object : InstallReferrerStateListener {

            override fun onInstallReferrerSetupFinished(responseCode: Int) {
                when (responseCode) {
                    InstallReferrerClient.InstallReferrerResponse.OK -> {
                        val response = referrerClient.installReferrer

                        Log.e(LOG_TAG, "InstallReferrerResponse.OK : " + response.installReferrer)

                        val referrer = response.installReferrer
                        if (StringUtils.isNotEmpty(referrer)) {
                            val referrers = referrer.split("&").toTypedArray()
                            for (referrerValue in referrers) {
                                val keyValue = referrerValue.split("=").toTypedArray()
                                if (keyValue[0] == "participateID") {
                                    val participateID = keyValue[1]

                                    if (StringUtils.isNotEmpty(participateID)) {
                                        isCpa = true
                                        cpaType = "zzal"
                                        cpaId = participateID
//                                        cpaReport("zzal", participateID)
                                        LogUtil.e("participateID", "participateID : {}", participateID)
                                    }
                                } else if (keyValue[0] == "adType") {
                                    val adType = keyValue[1]
                                    if (StringUtils.isNotEmpty(adType)) {
                                        isCpa = true
                                        cpaType = adType
                                        cpaId = PplusCommonUtil.getDeviceID()
                                        LogUtil.e("adType", "adType : {}", adType)
                                    }
                                } else if (keyValue[0] == "recommendKey") {
                                    val recommendKey = keyValue[1]
                                    if (StringUtils.isNotEmpty(recommendKey)) {
                                        isCpa = true
                                        cpaType = "invite"
                                        cpaId = PplusCommonUtil.getDeviceID()
                                        binding.editJoinRecommendCode.setText(recommendKey)
                                        LogUtil.e("recommendKey", "recommendKey : {}", recommendKey)
                                    }
                                } else if (keyValue[0] == "gad_tracking_id") {
                                    val gad_tracking_id = keyValue[1]
                                    if (StringUtils.isNotEmpty(gad_tracking_id)) {
                                        isCpa = true
                                        cpaType = "gpa"
                                        cpaId = gad_tracking_id
                                    }
                                } else if (keyValue[0] == "plusInfo") {
                                    if (StringUtils.isNotEmpty(keyValue[1])) {
                                        mPageSeqNo = keyValue[1].toLong()
                                    }
                                } else if (keyValue[0] == "visit") {
                                    if (StringUtils.isNotEmpty(keyValue[1])) {
                                        mVisitPageSeqNo = keyValue[1].toLong()
                                    }
                                }
                                else if (keyValue[0] == "visitBenefit") {
                                    if (StringUtils.isNotEmpty(keyValue[1])) {
                                        mVisitBenefitPageSeqNo = keyValue[1].toLong()
                                    }
                                }
                                else if (keyValue[0] == "subscription") {
                                    if (StringUtils.isNotEmpty(keyValue[1])) {
                                        mSubscriptionProductSeqNo = keyValue[1].toLong()
                                    }
                                }
                                else if (keyValue[0] == "eventDetail") {
                                    if (StringUtils.isNotEmpty(keyValue[1])) {
                                        mEventSeqNo = keyValue[1].toLong()
                                    }

                                }
                                else if (keyValue[0] == "adCode") {
                                    if (StringUtils.isNotEmpty(keyValue[1])) {
                                        mAdCode = keyValue[1]
                                    }

                                }
                                else if (keyValue[0] == "joinType") {
                                    if (StringUtils.isNotEmpty(keyValue[1])) {
                                        mJoinType = keyValue[1]
                                    }

                                }
                            }
                        }

                        referrerClient.endConnection()
                    }
                    InstallReferrerClient.InstallReferrerResponse.FEATURE_NOT_SUPPORTED -> {
                        // API not available on the current Play Store app
                        Log.e(LOG_TAG, "FEATURE_NOT_SUPPORTED")
                    }
                    InstallReferrerClient.InstallReferrerResponse.SERVICE_UNAVAILABLE -> {
                        // Connection could not be established
                        Log.e(LOG_TAG, "SERVICE_UNAVAILABLE")
                    }
                }
            }

            override fun onInstallReferrerServiceDisconnected() {
                Log.e(LOG_TAG, "onInstallReferrerServiceDisconnected")
                // Try to restart the connection on the next request to
                // Google Play by calling the startConnection() method.
            }
        })

        if (paramsJoin!!.accountType == SnsTypeCode.pplus.name) {
            binding.layoutJoinIdPw.visibility = View.VISIBLE
        } else {
            binding.layoutJoinIdPw.visibility = View.GONE
        }

        binding.textJoinIdTitle.text = PplusCommonUtil.fromHtml(getString(R.string.html_id))
        binding.textJoinPasswordTitle.text = PplusCommonUtil.fromHtml(getString(R.string.html_password))
//        text_sign_up_nickname_title.text = PplusCommonUtil.fromHtml(getString(R.string.html_nickname))

        binding.editJoinRecommendCode.setSingleLine()
//        val recommend = PreferenceUtil.getDefaultPreference(activity).getString(Const.RECOMMEND)
//        if (StringUtils.isNotEmpty(recommend)) {
//            edit_sign_up_recommend_code.setText(recommend)
//        }

        binding.editJoinId.setSingleLine()
        binding.editJoinId.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val id = s.toString().trim()
                if (id.length > 3) {

                    val regex = Regex(".*[ㄱ-ㅎㅏ-ㅣ가-힣]+.*")
                    if (id.matches(regex)) {
                        binding.textJoinIdState.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_login_reject, 0, 0, 0)
                        binding.textJoinIdState.setTextColor(ResourceUtil.getColor(activity, R.color.color_ff4646))
                        binding.textJoinIdState.setText(R.string.msg_input_valid_id)
                        binding.textJoinIdState.visibility = View.VISIBLE
                        return
                    }
                    val params = HashMap<String, String>()
                    params["loginId"] = id
                    ApiBuilder.create().userCheck(params).setCallback(object : PplusCallback<NewResultResponse<String>> {
                        override fun onResponse(call: Call<NewResultResponse<String>>?, response: NewResultResponse<String>?) {
                            if (response != null) {
                                val result = response.data
                                when (result) {
                                    "success" -> {
                                        mIsCheckId = true
                                        mSignedId = id

                                        binding.textJoinIdState.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_login_ok, 0, 0, 0)
                                        binding.textJoinIdState.setTextColor(ResourceUtil.getColor(activity, R.color.color_579ffb))
                                        binding.textJoinIdState.setText(R.string.msg_usable_id)
                                        binding.textJoinIdState.visibility = View.VISIBLE
                                    }
                                    "fail" -> {
                                        mIsCheckId = false
                                        binding.textJoinIdState.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_login_reject, 0, 0, 0)
                                        binding.textJoinIdState.setTextColor(ResourceUtil.getColor(activity, R.color.color_ff4646))
                                        binding.textJoinIdState.setText(R.string.msg_duplicate_id)
                                        binding.textJoinIdState.visibility = View.VISIBLE
                                    }
                                }
                            }
                        }

                        override fun onFailure(call: Call<NewResultResponse<String>>?, t: Throwable?, response: NewResultResponse<String>?) {

                        }
                    }).build().call()
                } else {
                    binding.textJoinIdState.visibility = View.INVISIBLE
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        binding.editJoinPassword.setSingleLine()
        binding.editJoinPassword.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD


//        edit_sign_up_nickname.setSingleLine()
//        edit_sign_up_nickname.addTextChangedListener(object : TextWatcher {
//            override fun afterTextChanged(s: Editable?) {
//                val nickname = s.toString().trim()
//                if (nickname.length > 1) {
//                    val params = HashMap<String, String>()
//                    params["nickname"] = nickname
//                    ApiBuilder.create().existsUser(params).setCallback(object : PplusCallback<NewResultResponse<Any>> {
//
//                        override fun onResponse(call: Call<NewResultResponse<Any>>, response: NewResultResponse<Any>) {
//
//                            hideProgress()
//                            mIsCheckNickName = false
//                            text_sign_up_nickname_state.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_login_reject, 0, 0, 0)
//                            text_sign_up_nickname_state.setTextColor(ResourceUtil.getColor(activity, R.color.color_ff4646))
//                            text_sign_up_nickname_state.setText(R.string.msg_duplicate_nickname)
//                            text_sign_up_nickname_state.visibility = View.VISIBLE
//                        }
//
//                        override fun onFailure(call: Call<NewResultResponse<Any>>, t: Throwable, response: NewResultResponse<Any>) {
//                            hideProgress()
//                            mIsCheckNickName = true
//                            mSignedNickName = nickname
//
//                            text_sign_up_nickname_state.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_login_ok, 0, 0, 0)
//                            text_sign_up_nickname_state.setTextColor(ResourceUtil.getColor(activity, R.color.color_579ffb))
//                            text_sign_up_nickname_state.setText(R.string.msg_usable_nickname)
//                            text_sign_up_nickname_state.visibility = View.VISIBLE
//                        }
//                    }).build().call()
//                } else {
//                    text_sign_up_nickname_state.visibility = View.INVISIBLE
//                }
//            }
//
//            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
//
//            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
//        })

        ApiBuilder.create().getActiveTermsAll(Const.APP_TYPE).setCallback(object : PplusCallback<NewResultResponse<Terms>> {

            override fun onResponse(call: Call<NewResultResponse<Terms>>, response: NewResultResponse<Terms>) {

                if (!isAdded) {
                    return
                }

                mTermsList = response.datas as ArrayList<Terms>
                mTermsAgreeMap = HashMap()
                binding.layoutJoinTerms.removeAllViews()
                mCheckBoxList = ArrayList()
                for (i in mTermsList!!.indices) {
                    val terms = mTermsList!![i]
                    mTermsAgreeMap!![terms.no!!] = false
                    val termsBinding = ItemTermsBinding.inflate(LayoutInflater.from(activity), LinearLayout(activity), false)
                    if (i != 0) {
                        val layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                        layoutParams.setMargins(0, resources.getDimensionPixelSize(R.dimen.height_50), 0, 0)
                        termsBinding.root.layoutParams = layoutParams
                    }
                    val checkTerms = termsBinding.checkTermsAgree
                    checkTerms.text = terms.subject
                    checkTerms.setOnCheckedChangeListener { buttonView, isChecked ->
                        var isAll = true
                        for (checkBox in mCheckBoxList!!) {
                            if (!checkBox.isChecked) {
                                isAll = false
                                break
                            }
                        }

                        binding.checkJoinTotalAgree.setOnCheckedChangeListener(null)
                        binding.checkJoinTotalAgree.isChecked = isAll
                        binding.checkJoinTotalAgree.setOnCheckedChangeListener(this@JoinFragment)

                        mTermsAgreeMap!![terms.no!!] = isChecked
                    }
                    mCheckBoxList!!.add(checkTerms)

                    termsBinding.imageTermsStory.setOnClickListener {
                        val intent = Intent(activity, WebViewActivity::class.java)
                        intent.putExtra(Const.TITLE, terms.subject)
                        intent.putExtra(Const.TOOLBAR_RIGHT_ARROW, true)
                        intent.putExtra(Const.WEBVIEW_URL, terms.url)
                        intent.putExtra(Const.TERMS_LIST, mTermsList)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                        startActivity(intent)
                    }
                    binding.layoutJoinTerms.addView(termsBinding.root)
                }
            }

            override fun onFailure(call: Call<NewResultResponse<Terms>>, t: Throwable, response: NewResultResponse<Terms>) {

            }
        }).build().call()

        binding.checkJoinTotalAgree.setOnCheckedChangeListener(this)

        binding.textJoinConfirm.setOnClickListener {
            signUp()
        }
    }

    override fun onCheckedChanged(compoundButton: CompoundButton, b: Boolean) {

        if (mCheckBoxList != null) {
            for (checkBox in mCheckBoxList!!) {
                checkBox.isChecked = b
            }
        } else {
            binding.checkJoinTotalAgree.isChecked = !b
        }
    }

    private fun signUp() {

        if (paramsJoin!!.accountType == SnsTypeCode.pplus.name) {
            val loginId = binding.editJoinId.text.toString().trim()

            if (StringUtils.isEmpty(loginId)) {
                showAlert(R.string.msg_input_id)
                return
            }

            if (loginId.length < 4) {
                showAlert(getString(R.string.format_msg_input_over, 4))
                return
            }

            val regex = Regex(".*[ㄱ-ㅎㅏ-ㅣ가-힣]+.*")

            if (loginId.matches(regex)) {
                showAlert(R.string.msg_input_valid_id);
                return
            }

            if (!mIsCheckId || mSignedId != loginId) {
                showAlert(R.string.msg_duplicate_id)
                return
            }

            val pw = binding.editJoinPassword.text.toString().trim()
            if (StringUtils.isEmpty(pw)) {
                binding.scrollJoin.smoothScrollTo(0, binding.editJoinPassword.top)
                binding.editJoinPassword.requestFocus()
                showAlert(R.string.msg_input_password)
                return
            }

            if (pw.length < 4) {
                binding.scrollJoin.smoothScrollTo(0, binding.editJoinPassword.top)
                binding.editJoinPassword.requestFocus()
                showAlert(getString(R.string.to_password) + " " + getString(R.string.format_msg_input_over_number, 4))
                return
            }

            paramsJoin!!.loginId = mSignedId
            paramsJoin!!.nickname = mSignedId
            paramsJoin!!.password = Crypt.encrypt(pw)
            paramsJoin!!.encrypted = true
            paramsJoin!!.accountType = SnsTypeCode.pplus.name
        } else {
            if (!StringUtils.isNotEmpty(paramsJoin!!.nickname)) {
                val nickname = paramsJoin!!.loginId!!.split("@")[0]
                LogUtil.e(LOG_TAG, nickname)
                paramsJoin!!.nickname = nickname
            }
        }

        val recommendationCode = binding.editJoinRecommendCode.text.toString().trim()
        if (StringUtils.isNotEmpty(recommendationCode)) {
            paramsJoin!!.recommendationCode = recommendationCode
        } else {
            paramsJoin!!.recommendationCode = null
        }

        for (i in mTermsList!!.indices) {
            if (mTermsList!![i].isCompulsory && (!mTermsAgreeMap!![mTermsList!![i].no]!!)) {
                showAlert(R.string.msg_agree_terms)
                return
            }
        }

        val termsList = ArrayList<No>()
        for ((key, value) in mTermsAgreeMap!!) {
            if (value) {
                termsList.add(No(key))
            }
        }
        paramsJoin!!.termsList = termsList
        paramsJoin!!.appType = Const.APP_TYPE

        if(StringUtils.isNotEmpty(mJoinType)){
            paramsJoin!!.joinType = mJoinType
        }

        if(StringUtils.isNotEmpty(mAdCode)){
            val adCodes = mAdCode!!.split(":")
            paramsJoin!!.adCompany = adCodes[0]
            if(adCodes.size > 1){
                paramsJoin!!.adCode = adCodes[1]
            }
        }

        showProgress("")

        ApiBuilder.create().join(paramsJoin!!).setCallback(object : PplusCallback<NewResultResponse<User>> {

            override fun onResponse(call: Call<NewResultResponse<User>>, response: NewResultResponse<User>) {
                if (!isAdded) {
                    return
                }

                hideProgress()
                LoginInfoManager.getInstance().user = response.data
                try {
                    LoginInfoManager.getInstance().user.password = PplusCommonUtil.encryption(paramsJoin!!.password!!)
                } catch (e: Exception) {

                }

                LoginInfoManager.getInstance().save()
                val eventAttr = AdBrixRm.AttrModel()
                eventAttr.setAttrs("loginId", response.data.loginId)
                if(LoginInfoManager.getInstance().user.accountType != "pplus"){

                    AdBrixRm.event("snsJoin", eventAttr)
                }else{
                    AdBrixRm.event("prnumberJoin", eventAttr)
                }


                if (isCpa) {
                    cpaReport(cpaType, cpaId)
                }

                if(mPageSeqNo != null){
                    PreferenceUtil.getDefaultPreference(activity).put(Const.PLUS_INFO_PAGE_SEQ_NO, mPageSeqNo!!)
                }

                if(mVisitPageSeqNo != null){
                    PreferenceUtil.getDefaultPreference(activity).put(Const.VISIT_PAGE_SEQ_NO, mVisitPageSeqNo!!)
                }

                if(mVisitBenefitPageSeqNo != null){
                    PreferenceUtil.getDefaultPreference(activity).put(Const.BENEFIT_PAGE_SEQ_NO, mVisitBenefitPageSeqNo!!)
                }

                if(mSubscriptionProductSeqNo != null){
                    PreferenceUtil.getDefaultPreference(activity).put(Const.SUBSCRIPTION_PRODUCT_SEQ_NO, mSubscriptionProductSeqNo!!)
                }

                if(mEventSeqNo != null){
                    PreferenceUtil.getDefaultPreference(activity).put(Const.EVENT_DETAIL_SEQ_NO, mEventSeqNo!!)
                }

                activity!!.setResult(Activity.RESULT_OK)
                activity!!.finish()
            }

            override fun onFailure(call: Call<NewResultResponse<User>>, t: Throwable, response: NewResultResponse<User>) {
                if (!isAdded) {
                    return
                }
                hideProgress()
                when (response.resultCode) {
                    587 -> showAlert(getString(R.string.msg_need_agree_terms))
                    583 -> showAlert(getString(R.string.msg_duplicate_id))
                    503 -> showAlert(getString(R.string.msg_invalid_recommendKey))
                    else -> showAlert(getString(R.string.msg_failed_join))
                }
            }
        }).build().call()
    }

    private fun cpaReport(type: String, id: String) {

        val params = HashMap<String, String>()
        params["type"] = type
        params["id"] = id
        params["actionType"] = "join"
        ApiBuilder.create().cpaReport(params).setCallback(object : PplusCallback<NewResultResponse<Any>> {
            override fun onResponse(call: Call<NewResultResponse<Any>>?, response: NewResultResponse<Any>?) {

            }

            override fun onFailure(call: Call<NewResultResponse<Any>>?, t: Throwable?, response: NewResultResponse<Any>?) {

            }
        }).build().call()
    }

    companion object {

        fun newInstance(params: User): JoinFragment {

            val fragment = JoinFragment()
            val args = Bundle()
            args.putParcelable(Const.JOIN, params)
            fragment.arguments = args
            return fragment
        }
    }
}
