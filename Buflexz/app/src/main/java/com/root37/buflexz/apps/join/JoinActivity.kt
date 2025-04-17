package com.root37.buflexz.apps.join

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.LinearLayout
import androidx.activity.result.contract.ActivityResultContracts
import com.android.installreferrer.api.InstallReferrerClient
import com.android.installreferrer.api.InstallReferrerStateListener
import com.pplus.networks.common.PplusCallback
import com.pplus.utils.part.resource.ResourceUtil
import com.pplus.utils.part.format.FormatUtil
import com.pplus.utils.part.logs.LogUtil
import com.pplus.utils.part.utils.StringUtils
import com.root37.buflexz.Const
import com.root37.buflexz.R
import com.root37.buflexz.apps.common.builder.AlertBuilder
import com.root37.buflexz.apps.common.builder.OnAlertResultListener
import com.root37.buflexz.apps.common.mgmt.NationManager
import com.root37.buflexz.apps.common.ui.base.BaseActivity
import com.root37.buflexz.apps.terms.ui.TermsActivity
import com.root37.buflexz.core.network.ApiBuilder
import com.root37.buflexz.core.network.model.dto.Member
import com.root37.buflexz.core.network.model.dto.Terms
import com.root37.buflexz.core.network.model.response.ListResultResponse
import com.root37.buflexz.core.network.model.response.NewResultResponse
import com.root37.buflexz.core.util.PplusCommonUtil
import com.root37.buflexz.databinding.ActivityJoinBinding
import com.root37.buflexz.databinding.ItemTermsBinding
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

    var mCheckNickname:Boolean? = null
    var mNickname = ""
    var mNation = ""
    var mPlatformEmail = ""
    var mPlatformKey = ""

    private var mTermsList: MutableList<Terms>? = null
    private var mTermsAgreeMap: MutableMap<Long, Boolean>? = null
    private var mCheckBoxList: MutableList<CheckBox>? = null

    override fun initializeView(savedInstanceState: Bundle?) {

        mPlatformEmail = intent.getStringExtra(Const.PLATFORM_EMAIL)!!
        mPlatformKey = intent.getStringExtra(Const.PLATFORM_KEY)!!
        val joinType = intent.getStringExtra(Const.TYPE)

        val referrerClient = InstallReferrerClient.newBuilder(this).build()
        referrerClient.startConnection(object : InstallReferrerStateListener {

            override fun onInstallReferrerSetupFinished(responseCode: Int) {
                when (responseCode) {
                    InstallReferrerClient.InstallReferrerResponse.OK -> {
                        val response = referrerClient.installReferrer

                        LogUtil.e(LOG_TAG, "InstallReferrerResponse.OK : " + response.installReferrer)

                        val referrer = response.installReferrer
                        if (StringUtils.isNotEmpty(referrer)) {
                            val referrers = referrer.split("&").toTypedArray()
                            for (referrerValue in referrers) {
                                val keyValue = referrerValue.split("=").toTypedArray()
                                if (keyValue[0] == "recommendKey") {
                                    val recommendKey = keyValue[1]
                                    if (StringUtils.isNotEmpty(recommendKey)) {
                                        binding.editJoinRecommendKey.setText(recommendKey)
                                        LogUtil.e("recommendKey", "recommendKey : {}", recommendKey)
                                    }
                                }
                            }
                        }

                        referrerClient.endConnection()
                    }

                    InstallReferrerClient.InstallReferrerResponse.FEATURE_NOT_SUPPORTED -> { // API not available on the current Play Store app
                        LogUtil.e(LOG_TAG, "NOT_SUPPORTED")
                    }

                    InstallReferrerClient.InstallReferrerResponse.SERVICE_UNAVAILABLE -> { // Connection could not be established
                        LogUtil.e(LOG_TAG, "SERVICE_UNAVAILABLE")
                    }
                }
            }

            override fun onInstallReferrerServiceDisconnected() {
                LogUtil.e(LOG_TAG, "onInstallReferrerServiceDisconnected") // Try to restart the connection on the next request to
                // Google Play by calling the startConnection() method.
            }
        })
        binding.checkJoinTotalAgree.setOnCheckedChangeListener(checkListener)
        val country = Locale.getDefault().country
        if (StringUtils.isNotEmpty(country)) {
            binding.layoutJoinNation.setOnClickListener {}
            binding.imageJoinNationArrow.visibility = View.GONE
            mNation = country

            val nation = NationManager.getInstance().nationMap!![mNation]
            if (nation!!.code == "KR") {
                binding.textJoinNation.text = nation.name
            } else {
                binding.textJoinNation.text = nation.nameEn
            }

            getTerms()
        } else {
            val nationList = NationManager.getInstance().nationMap!!.values.toList()

            val countryList = arrayListOf<String>()
            val language = Locale.getDefault().language
            if(language == "ko"){
                for(nation in nationList){
                    countryList.add(nation.name!!)
                }
            }else{
                for(nation in nationList){
                    countryList.add(nation.nameEn!!)
                }
            }
            binding.imageJoinNationArrow.visibility = View.VISIBLE
            binding.layoutJoinNation.setOnClickListener {
                val builder = AlertBuilder.Builder()
                builder.setTitle(getString(R.string.word_select))
                builder.setStyle_alert(AlertBuilder.STYLE_ALERT.LIST_CENTER)
                builder.setContents(countryList)
                builder.setLeftText(getString(R.string.word_cancel)).setRightText(getString(R.string.word_confirm))
                builder.setOnAlertResultListener(object : OnAlertResultListener {
                    override fun onCancel() {}
                    override fun onResult(event_alert: AlertBuilder.EVENT_ALERT) {
                        when (event_alert) {
                            AlertBuilder.EVENT_ALERT.LIST -> {
                                binding.textJoinNation.text = countryList[event_alert.value - 1]
                                mNation = nationList[event_alert.value - 1].code!!.uppercase()
                                getTerms()
                            }
                            else -> {}
                        }
                    }
                }).builder().show(this)
            }
        }

        binding.textJoinNationTitle.text = PplusCommonUtil.fromHtml(getString(R.string.html_join_nation_title))
        binding.textJoinNicknameTitle.text = PplusCommonUtil.fromHtml(getString(R.string.html_join_nickname_title))
        binding.textJoinEmailTitle.text = PplusCommonUtil.fromHtml(getString(R.string.html_join_email_title))
        binding.editJoinNickname.setSingleLine()
        binding.editJoinEmail.setSingleLine()
        binding.editJoinRecommendKey.setSingleLine()

        binding.textJoinNicknameCheck.setOnClickListener {

            val nickname = binding.editJoinNickname.text.toString().trim()
            if (StringUtils.isEmpty(nickname)) {
                showAlert(R.string.msg_input_nickname)
                return@setOnClickListener
            }

            if (nickname.length < 2) {
                showAlert(R.string.msg_input_nickname_over_2)
                return@setOnClickListener
            }

            val params = HashMap<String, String>()
            params["nickname"] = nickname
            showProgress("")
            ApiBuilder.create().checkNickname(params).setCallback(object : PplusCallback<NewResultResponse<Boolean>> {
                override fun onResponse(call: Call<NewResultResponse<Boolean>>?, response: NewResultResponse<Boolean>?) {
                    hideProgress()
                    if (response?.result != null && response.result!!) {
                        binding.layoutJoinNickname.setBackgroundResource(R.drawable.bg_border_3px_48b778_232323_radius_33)
                        binding.textJoinNicknameDesc.setText(R.string.msg_usable_nickname)
                        binding.textJoinNicknameDesc.setTextColor(ResourceUtil.getColor(this@JoinActivity, R.color.color_48b778))
                        mCheckNickname = true
                        mNickname = nickname
                    } else {
                        binding.layoutJoinNickname.setBackgroundResource(R.drawable.bg_border_3px_ff5e5e_232323_radius_33)
                        binding.textJoinNicknameDesc.setText(R.string.msg_duplicate_nickname)
                        binding.textJoinNicknameDesc.setTextColor(ResourceUtil.getColor(this@JoinActivity, R.color.color_ff5e5e))
                        mCheckNickname = false
                        mNickname = ""
                    }
                }

                override fun onFailure(call: Call<NewResultResponse<Boolean>>?, t: Throwable?, response: NewResultResponse<Boolean>?) {
                    hideProgress()
                    binding.layoutJoinNickname.setBackgroundResource(R.drawable.bg_border_3px_ff5e5e_232323_radius_33)
                    binding.textJoinNicknameDesc.setText(R.string.msg_duplicate_nickname)
                    binding.textJoinNicknameDesc.setTextColor(ResourceUtil.getColor(this@JoinActivity, R.color.color_ff5e5e))
                    mCheckNickname = false
                    mNickname = ""
                }
            }).build().call()
        }

        binding.textJoin.setOnClickListener {
            val params = Member()
            params.platformEmail = mPlatformEmail
            params.platformKey = mPlatformKey
            params.joinType = joinType
            params.joinPlatform = "aos"
            params.language = Locale.getDefault().language
            params.device = PplusCommonUtil.getDeviceID()

            if(StringUtils.isEmpty(mNation)){
                showAlert(R.string.msg_select_nation)
                return@setOnClickListener
            }

            params.nation = mNation

            val nickname = binding.editJoinNickname.text.toString().trim()
            if (StringUtils.isEmpty(nickname)) {
                showAlert(R.string.msg_input_nickname)
                return@setOnClickListener
            }

            if (nickname.length < 2) {
                showAlert(getString(R.string.format_msg_input_over, 2))
                return@setOnClickListener
            }

            if(mCheckNickname == null || mNickname != nickname){
                showAlert(R.string.msg_check_duplicate_nickname)
                return@setOnClickListener
            }

            if (!mCheckNickname!!) {
                showAlert(R.string.msg_duplicate_nickname)
                return@setOnClickListener
            }
            params.nickname = nickname

            val email = binding.editJoinEmail.text.toString().trim()

            if (StringUtils.isEmpty(email)) {
                showAlert(R.string.msg_input_email)
                return@setOnClickListener
            }

            if(!FormatUtil.isEmailAddress(email)){
                showAlert(R.string.msg_valid_email)
                return@setOnClickListener
            }

            params.email = email

            val recommendKey = binding.editJoinRecommendKey.text.toString().trim()
            if(StringUtils.isNotEmpty(recommendKey)){
                params.recommendeeKey = recommendKey
            }

            for (i in mTermsList!!.indices) {
                if (mTermsList!![i].compulsory!! && (!mTermsAgreeMap!![mTermsList!![i].seqNo]!!)) {
                    showAlert(R.string.msg_agree_terms)
                    return@setOnClickListener
                }
            }

            val termsList = arrayListOf<Long>()
            for ((key, value) in mTermsAgreeMap!!) {
                if (value) {
                    termsList.add(key)
                }
            }

            var termsNo = ""

            for (i in 0 until termsList.size) {
                termsNo += termsList[i]
                if(i < termsList.size -1){
                    termsNo += ","
                }
            }

            params.termsNo = termsNo
            showProgress("")
            ApiBuilder.create().join(params).setCallback(object : PplusCallback<NewResultResponse<Member>>{
                override fun onResponse(call: Call<NewResultResponse<Member>>?, response: NewResultResponse<Member>?) {
                    hideProgress()

                    val intent = Intent(this@JoinActivity, JoinCompleteActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                    joinCompleteLauncher.launch(intent)
                }

                override fun onFailure(call: Call<NewResultResponse<Member>>?, t: Throwable?, response: NewResultResponse<Member>?) {
                    hideProgress()
                    if(response?.code == 587){
                        showAlert(R.string.msg_agree_terms)
                    }else if(response?.code == 504){
                        showAlert(R.string.msg_duplicate_nickname)
                    }else{
                        showAlert(R.string.msg_failed_join)
                    }
                }
            }).build().call()
        }
    }

    private fun getTerms(){
        val params = HashMap<String, String>()
        params["nation"] = mNation
        ApiBuilder.create().getTermsList(params).setCallback(object : PplusCallback<NewResultResponse<ListResultResponse<Terms>>> {
            override fun onResponse(call: Call<NewResultResponse<ListResultResponse<Terms>>>?, response: NewResultResponse<ListResultResponse<Terms>>?) {
                if (response?.result != null && response.result!!.list != null) {
                    mTermsList = response.result!!.list as MutableList<Terms>
                    mTermsAgreeMap = HashMap()
                    binding.layoutJoinTerms.removeAllViews()

                    mCheckBoxList = ArrayList()
                    for (i in mTermsList!!.indices) {
                        val terms = mTermsList!![i]
                        mTermsAgreeMap!![terms.seqNo!!] = false
                        val termsBinding = ItemTermsBinding.inflate(layoutInflater, LinearLayout(this@JoinActivity), false)
                        if (i != 0) {
                            val layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                            layoutParams.setMargins(0, resources.getDimensionPixelSize(R.dimen.height_50), 0, 0)
                            termsBinding.root.layoutParams = layoutParams
                        }
                        val checkTerms = termsBinding.checkTermsAgree
                        checkTerms.text = terms.title
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
                            binding.checkJoinTotalAgree.setOnCheckedChangeListener(checkListener)

                            mTermsAgreeMap!![terms.seqNo!!] = isChecked
                        }
                        mCheckBoxList!!.add(checkTerms)

                        termsBinding.textTermsContents.setOnClickListener {
                            val intent = Intent(this@JoinActivity, TermsActivity::class.java)
                            intent.putExtra(Const.DATA, terms)
                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                            startActivity(intent)
                        }
                        binding.layoutJoinTerms.addView(termsBinding.root)
                    }
                }
            }

            override fun onFailure(call: Call<NewResultResponse<ListResultResponse<Terms>>>?, t: Throwable?, response: NewResultResponse<ListResultResponse<Terms>>?) {

            }

        }).build().call()
    }

    val checkListener = CompoundButton.OnCheckedChangeListener { compoundButton, b ->
        if (mCheckBoxList != null) {
            for (checkBox in mCheckBoxList!!) {
                checkBox.isChecked = b
            }
        } else {
            binding.checkJoinTotalAgree.isChecked = !b
        }
    }

    val joinCompleteLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        val data = Intent()
        data.putExtra(Const.PLATFORM_KEY, mPlatformKey)
        data.putExtra(Const.PLATFORM_EMAIL, mPlatformEmail)
        setResult(RESULT_OK, data)
        finish()
    }
}