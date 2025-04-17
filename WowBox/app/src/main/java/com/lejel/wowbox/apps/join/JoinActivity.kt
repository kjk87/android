package com.lejel.wowbox.apps.join

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.UnderlineSpan
import android.view.View
import android.widget.CheckBox
import androidx.activity.result.contract.ActivityResultContracts
import com.google.firebase.analytics.FirebaseAnalytics
import com.lejel.wowbox.Const
import com.lejel.wowbox.R
import com.lejel.wowbox.apps.common.ui.base.BaseActivity
import com.lejel.wowbox.apps.terms.ui.TermsActivity
import com.lejel.wowbox.apps.verify.ui.SelectVerifyTypeActivity
import com.lejel.wowbox.apps.verify.ui.VerifySmsActivity
import com.lejel.wowbox.apps.verify.ui.VerifyWhatsAppActivity
import com.lejel.wowbox.core.network.ApiBuilder
import com.lejel.wowbox.core.network.model.dto.Member
import com.lejel.wowbox.core.network.model.dto.Terms
import com.lejel.wowbox.core.network.model.response.ListResultResponse
import com.lejel.wowbox.core.network.model.response.NewResultResponse
import com.lejel.wowbox.core.util.PplusCommonUtil
import com.lejel.wowbox.databinding.ActivityJoinBinding
import com.pplus.networks.common.PplusCallback
import com.pplus.utils.part.format.FormatUtil
import com.pplus.utils.part.resource.ResourceUtil
import com.pplus.utils.part.utils.StringUtils
import retrofit2.Call
import java.util.Locale


class JoinActivity : BaseActivity() {

    private lateinit var binding: ActivityJoinBinding

    override fun getLayoutView(): View {
        binding = ActivityJoinBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun getPID(): String? {
        return ""
    }

    var mNation = ""
    var mPlatformEmail:String? = null
    var mPlatformKey:String? = null
    var mJoinType = ""

    private var mTermsList: MutableList<Terms>? = null
    private var mTermsAgreeMap: MutableMap<Long, Boolean>? = null
    private var mCheckBoxList: MutableList<CheckBox>? = null

    override fun initializeView(savedInstanceState: Bundle?) {

        mPlatformEmail = intent.getStringExtra(Const.PLATFORM_EMAIL)
        mPlatformKey = intent.getStringExtra(Const.PLATFORM_KEY)
        mJoinType = intent.getStringExtra(Const.TYPE)!!

//        val referrerClient = InstallReferrerClient.newBuilder(this).build()
//        referrerClient.startConnection(object : InstallReferrerStateListener {
//
//            override fun onInstallReferrerSetupFinished(responseCode: Int) {
//                when (responseCode) {
//                    InstallReferrerClient.InstallReferrerResponse.OK -> {
//                        val response = referrerClient.installReferrer
//
//                        LogUtil.e(LOG_TAG, "InstallReferrerResponse.OK : " + response.installReferrer)
//
//                        val referrer = response.installReferrer
//                        if (StringUtils.isNotEmpty(referrer)) {
//                            val referrers = referrer.split("&").toTypedArray()
//                            for (referrerValue in referrers) {
//                                val keyValue = referrerValue.split("=").toTypedArray()
//                                if (keyValue[0] == "recommendKey") {
//                                    val recommendKey = keyValue[1]
//                                    if (StringUtils.isNotEmpty(recommendKey)) {
////                                        binding.layoutJoinRecommendKey.visibility = View.VISIBLE
////                                        binding.textJoinRecommendKey.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_arrow_up, 0)
////                                        binding.textJoinRecommendKeyDesc2.visibility = View.GONE
//                                        binding.editJoinRecommendKey.setText(recommendKey)
//                                        LogUtil.e("recommendKey", "recommendKey : {}", recommendKey)
//                                    }
//                                }
//                            }
//                        }
//
//                        referrerClient.endConnection()
//                    }
//
//                    InstallReferrerClient.InstallReferrerResponse.FEATURE_NOT_SUPPORTED -> { // API not available on the current Play Store app
//                        LogUtil.e(LOG_TAG, "NOT_SUPPORTED")
//                    }
//
//                    InstallReferrerClient.InstallReferrerResponse.SERVICE_UNAVAILABLE -> { // Connection could not be established
//                        LogUtil.e(LOG_TAG, "SERVICE_UNAVAILABLE")
//                    }
//                }
//            }
//
//            override fun onInstallReferrerServiceDisconnected() {
//                LogUtil.e(LOG_TAG, "onInstallReferrerServiceDisconnected") // Try to restart the connection on the next request to
//                // Google Play by calling the startConnection() method.
//            }
//        })


        binding.editJoinMobileNumber.setSingleLine()
        if(mJoinType == "mobile" && StringUtils.isNotEmpty(mPlatformKey)){
            binding.editJoinMobileNumber.setText(mPlatformKey)
        }

        val country = Locale.getDefault().country
        mNation = country

        getTerms()
        binding.editJoinRecommendKey.setSingleLine()

        binding.textJoin.setOnClickListener {

            if (mTermsList == null) {
                return@setOnClickListener
            }

            val mobileNumber = binding.editJoinMobileNumber.text.toString().trim()
            if(StringUtils.isEmpty(mobileNumber)){
                showAlert(R.string.hint_input_mobile_number)
                return@setOnClickListener
            }

            if(!FormatUtil.isPhoneNumber(mobileNumber)){
                showAlert(R.string.msg_invalid_phone_number)
                return@setOnClickListener
            }
            checkJoinedMobile(mobileNumber)

        }
//        binding.textJoin2.setOnClickListener {
//            join()
//        }
    }

    private fun checkJoinedMobile(mobileNumber:String){
        val params = HashMap<String, String>()
        params["mobileNumber"] = mobileNumber
        showProgress("")
        ApiBuilder.create().checkJoinedMobileNumber(params).setCallback(object  : PplusCallback<NewResultResponse<Any>>{
            override fun onResponse(call: Call<NewResultResponse<Any>>?, response: NewResultResponse<Any>?) {
                hideProgress()
                showAlert(R.string.msg_alread_joined_mobile_number)
            }

            override fun onFailure(call: Call<NewResultResponse<Any>>?, t: Throwable?, response: NewResultResponse<Any>?) {
                hideProgress()
                val intent = Intent(this@JoinActivity, SelectVerifyTypeActivity::class.java)
                intent.putExtra(Const.MOOBILE_NUMBER, mobileNumber)
                selectVerifyTypeLauncher.launch(intent)

            }
        }).build().call()
    }

    private fun sendWhatsApp(member: Member){
        val params = HashMap<String, String>()
        params["mobileNumber"] = member.mobileNumber!!
        params["type"] = "join"
        showProgress("")
        ApiBuilder.create().sendWhatsapp(params).setCallback(object  : PplusCallback<NewResultResponse<Any>>{
            override fun onResponse(call: Call<NewResultResponse<Any>>?, response: NewResultResponse<Any>?) {
                hideProgress()

                val intent = Intent(this@JoinActivity, VerifyWhatsAppActivity::class.java)
                intent.putExtra(Const.MOOBILE_NUMBER, member.mobileNumber)
                intent.putExtra(Const.TYPE, "join")
                intent.putExtra(Const.MEMBER, member)
                joinCompleteLauncher.launch(intent)
            }

            override fun onFailure(call: Call<NewResultResponse<Any>>?, t: Throwable?, response: NewResultResponse<Any>?) {
                hideProgress()
            }
        }).build().call()
    }

    private fun sendSms(member: Member){
        val params = HashMap<String, String>()
        params["mobileNumber"] = member.mobileNumber!!
        params["type"] = "join"
        showProgress("")
        ApiBuilder.create().sendSms(params).setCallback(object  : PplusCallback<NewResultResponse<Any>>{
            override fun onResponse(call: Call<NewResultResponse<Any>>?, response: NewResultResponse<Any>?) {
                hideProgress()

                val intent = Intent(this@JoinActivity, VerifySmsActivity::class.java)
                intent.putExtra(Const.MOOBILE_NUMBER, member.mobileNumber)
                intent.putExtra(Const.TYPE, "join")
                intent.putExtra(Const.MEMBER, member)
                joinCompleteLauncher.launch(intent)
            }

            override fun onFailure(call: Call<NewResultResponse<Any>>?, t: Throwable?, response: NewResultResponse<Any>?) {
                hideProgress()
            }
        }).build().call()
    }

    private fun join(){
        val params = Member()
        params.platformKey = mPlatformKey
        params.joinType = mJoinType
        params.joinPlatform = "aos"
        params.language = Locale.getDefault().language
        params.device = PplusCommonUtil.getDeviceID()

        if (StringUtils.isEmpty(mNation)) {
            mNation = "ID"
        }

        params.nation = mNation
        if(StringUtils.isNotEmpty(mPlatformEmail)){
            params.platformEmail = mPlatformEmail
            params.nickname = mPlatformEmail!!.split("@")[0]
            params.email = mPlatformEmail
        }

        val recommendKey = binding.editJoinRecommendKey.text.toString().trim()
        if (StringUtils.isNotEmpty(recommendKey)) {
            params.recommendeeKey = recommendKey
        }

        if (mTermsList == null) {
            return
        }

        var termsNo = ""

        for (i in mTermsList!!.indices) {
            val terms = mTermsList!![i]

            termsNo += terms.seqNo
            if (i < mTermsList!!.size - 1) {
                termsNo += ","
            }
        }

        params.termsNo = termsNo
        showProgress("")
        ApiBuilder.create().join(params).setCallback(object : PplusCallback<NewResultResponse<Member>> {
            override fun onResponse(call: Call<NewResultResponse<Member>>?, response: NewResultResponse<Member>?) {
                hideProgress()
                setEvent(FirebaseAnalytics.Event.SIGN_UP)
                showAlert(R.string.msg_join_complete)
                if(response?.result != null){
                    setEvent(FirebaseAnalytics.Event.SIGN_UP)
                    val userKey = response.result!!.userKey
                    val data = Intent()
                    data.putExtra(Const.USER_KEY, userKey)
                    setResult(RESULT_OK, data)
                    finish()
                }
            }

            override fun onFailure(call: Call<NewResultResponse<Member>>?, t: Throwable?, response: NewResultResponse<Member>?) {
                hideProgress()
                if (response?.code == 587) {
                    showAlert(R.string.msg_agree_terms)
                } else if (response?.code == 504) {
                    showAlert(R.string.msg_duplicate_nickname)
                } else if (response?.code == 588) {
                    showAlert(R.string.msg_wrong_recommend_key)
                } else if (response?.code == 589) {
                    showAlert(R.string.msg_can_not_use_cursing)
                } else {
                    showAlert(R.string.msg_failed_join)
                }
            }
        }).build().call()
    }

    private fun getTerms() {
        val params = HashMap<String, String>()
        params["nation"] = mNation
        showProgress("")
        ApiBuilder.create().getTermsList(params).setCallback(object : PplusCallback<NewResultResponse<ListResultResponse<Terms>>> {
            override fun onResponse(call: Call<NewResultResponse<ListResultResponse<Terms>>>?, response: NewResultResponse<ListResultResponse<Terms>>?) {
                hideProgress()
                if (response?.result != null && response.result!!.list != null) {
                    mTermsList = response.result!!.list as MutableList<Terms>
                    mTermsAgreeMap = HashMap()
//                    binding.layoutJoinTerms.removeAllViews()

                    mCheckBoxList = ArrayList()
                    val termsTitle = StringBuilder()
                    for (i in mTermsList!!.indices) {
                        val terms = mTermsList!![i]
                        mTermsAgreeMap!![terms.seqNo!!] = false
                        termsTitle.append(terms.title)
                        if (i < mTermsList!!.size - 1) {
                            termsTitle.append(", ")
                        }
                    }

                    val spannable = SpannableString(termsTitle)
                    for (i in mTermsList!!.indices) {
                        val terms = mTermsList!![i]
                        val start = termsTitle.toString().indexOf(terms.title!!)
                        val clickableSpan = object : ClickableSpan() {
                            override fun onClick(p0: View) {
                                val intent = Intent(this@JoinActivity, TermsActivity::class.java)
                                intent.putExtra(Const.DATA, terms)
                                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                                startActivity(intent)
                            }

                            override fun updateDrawState(ds: TextPaint) {
                                ds.color = ResourceUtil.getColor(this@JoinActivity, R.color.color_ea5506)
                            }
                        }
                        spannable.setSpan(clickableSpan, start, start + terms.title!!.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                        spannable.setSpan(UnderlineSpan(), start, start + terms.title!!.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

                    }
                    binding.textJoinTerms.text = spannable
                    binding.textJoinTerms.movementMethod = LinkMovementMethod.getInstance()
                }
            }

            override fun onFailure(call: Call<NewResultResponse<ListResultResponse<Terms>>>?, t: Throwable?, response: NewResultResponse<ListResultResponse<Terms>>?) {
                hideProgress()
            }

        }).build().call()
    }

    val selectVerifyTypeLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val mobileNumber = result.data!!.getStringExtra(Const.MOOBILE_NUMBER)!!
            val verifyType = result.data!!.getStringExtra(Const.VERIFY_TYPE)!!

            if(mJoinType == "mobile"){
                mPlatformKey = mobileNumber
            }

            val member = Member()
            member.platformKey = mPlatformKey
            member.joinType = mJoinType
            member.joinPlatform = "aos"
            member.language = Locale.getDefault().language
            member.device = PplusCommonUtil.getDeviceID()
            member.mobileNumber = mobileNumber

            if (StringUtils.isEmpty(mNation)) {
                mNation = "ID"
            }

            member.nation = mNation

            if(StringUtils.isNotEmpty(mPlatformEmail)){
                member.platformEmail = mPlatformEmail
                member.nickname = mPlatformEmail!!.split("@")[0]
                member.email = mPlatformEmail
            }

//            val recommendKey = binding.editJoinRecommendKey.text.toString().trim()
//            if (StringUtils.isNotEmpty(recommendKey)) {
//                member.recommendeeKey = recommendKey
//            }

            if (mTermsList == null) {
                return@registerForActivityResult
            }

            var termsNo = ""

            for (i in mTermsList!!.indices) {
                val terms = mTermsList!![i]

                termsNo += terms.seqNo
                if (i < mTermsList!!.size - 1) {
                    termsNo += ","
                }
            }

            member.termsNo = termsNo

            when (verifyType) {
                "whatsapp" -> {
                    sendWhatsApp(member)
                }
                else -> {
                    sendSms(member)
                }
            }
        }
    }

    val joinCompleteLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if(result.resultCode == RESULT_OK){
            setResult(RESULT_OK)
            finish()
        }

    }
}