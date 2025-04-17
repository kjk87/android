package com.root37.buflexz.apps.luckydraw.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import com.pplus.networks.common.PplusCallback
import com.pplus.utils.part.format.FormatUtil
import com.root37.buflexz.Const
import com.root37.buflexz.R
import com.root37.buflexz.apps.common.builder.AlertBuilder
import com.root37.buflexz.apps.common.builder.data.AlertData
import com.root37.buflexz.apps.common.mgmt.LoginInfoManager
import com.root37.buflexz.apps.common.toolbar.ImplToolbar
import com.root37.buflexz.apps.common.toolbar.OnToolbarListener
import com.root37.buflexz.apps.common.toolbar.ToolbarOption
import com.root37.buflexz.apps.common.ui.base.BaseActivity
import com.root37.buflexz.apps.luckyball.ui.AlertLuckyBallLackActivity
import com.root37.buflexz.apps.luckydraw.data.SelectLuckyDrawNumberAdapter
import com.root37.buflexz.core.network.ApiBuilder
import com.root37.buflexz.core.network.model.dto.LuckyDraw
import com.root37.buflexz.core.network.model.dto.LuckyDrawNumber
import com.root37.buflexz.core.network.model.response.NewResultResponse
import com.root37.buflexz.core.util.AdmobUtil
import com.root37.buflexz.core.util.PplusCommonUtil
import com.root37.buflexz.databinding.ActivityLuckyDrawJoinBinding
import com.yy.mobile.rollingtextview.CharOrder
import com.yy.mobile.rollingtextview.strategy.Strategy
import retrofit2.Call

class LuckyDrawJoinActivity : BaseActivity(), ImplToolbar {
    private lateinit var binding: ActivityLuckyDrawJoinBinding

    override fun getLayoutView(): View {
        binding = ActivityLuckyDrawJoinBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun getPID(): String? {
        return ""
    }

    private lateinit var mLuckyDraw: LuckyDraw
    private lateinit var mSelectAdapter: SelectLuckyDrawNumberAdapter

    override fun initializeView(savedInstanceState: Bundle?) {

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val params = HashMap<String, String>()
                params["luckyDrawSeqNo"] = mLuckyDraw.seqNo.toString()
                var winNumbers = ""
                for (i in 0 until mSelectAdapter.mDataList!!.size) {
                    winNumbers += mSelectAdapter.mDataList!![i].winNumber
                    if (i < mSelectAdapter.mDataList!!.size - 1) {
                        winNumbers += ","
                    }
                }
                params["winNumbers"] = winNumbers
                ApiBuilder.create().luckyDrawDeleteNumber(params).setCallback(object : PplusCallback<NewResultResponse<Any>> {
                    override fun onResponse(call: Call<NewResultResponse<Any>>?, response: NewResultResponse<Any>?) {

                    }

                    override fun onFailure(call: Call<NewResultResponse<Any>>?, t: Throwable?, response: NewResultResponse<Any>?) {

                    }
                }).build().call()
                finish()
            }
        })

        mLuckyDraw = PplusCommonUtil.getParcelableExtra(intent, Const.DATA, LuckyDraw::class.java)!!

        binding.textLuckyDrawJoinFirst.addCharOrder(CharOrder.Number)
        binding.textLuckyDrawJoinSecond.addCharOrder(CharOrder.Number)
        binding.textLuckyDrawJoinThird.addCharOrder(CharOrder.Number)
        binding.textLuckyDrawJoinFirst.animationDuration = 1000L
        binding.textLuckyDrawJoinSecond.animationDuration = 1000L
        binding.textLuckyDrawJoinThird.animationDuration = 1000L
        binding.textLuckyDrawJoinFirst.charStrategy = Strategy.CarryBitAnimation()
        binding.textLuckyDrawJoinSecond.charStrategy = Strategy.CarryBitAnimation()
        binding.textLuckyDrawJoinThird.charStrategy = Strategy.CarryBitAnimation()
        binding.textLuckyDrawJoinFirst.animationInterpolator = AccelerateDecelerateInterpolator()
        binding.textLuckyDrawJoinSecond.animationInterpolator = AccelerateDecelerateInterpolator()
        binding.textLuckyDrawJoinThird.animationInterpolator = AccelerateDecelerateInterpolator()

        mSelectAdapter = SelectLuckyDrawNumberAdapter()
        binding.recyclerLuckyDrawJoinSelect.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerLuckyDrawJoinSelect.adapter = mSelectAdapter

        mSelectAdapter.listener = object : SelectLuckyDrawNumberAdapter.OnItemListener {
            override fun delete(item: LuckyDrawNumber) {
                val params = HashMap<String, String>()
                params["luckyDrawSeqNo"] = mLuckyDraw.seqNo.toString()

                params["winNumbers"] = item.winNumber!!
                ApiBuilder.create().luckyDrawDeleteNumber(params).setCallback(object : PplusCallback<NewResultResponse<Any>> {
                    override fun onResponse(call: Call<NewResultResponse<Any>>?, response: NewResultResponse<Any>?) {

                    }

                    override fun onFailure(call: Call<NewResultResponse<Any>>?, t: Throwable?, response: NewResultResponse<Any>?) {

                    }
                }).build().call()
                setEngagePrice()
            }
        }

        binding.textLuckyDrawJoinWinRate.setOnClickListener {
            val intent = Intent(this, AlertLuckyDrawWinRateActivity::class.java)
            intent.putExtra(Const.DATA, mLuckyDraw)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            defaultLauncher.launch(intent)
        }

        binding.textLuckyDrawJoinSelect.setOnClickListener {

            if (mLuckyDraw.engageNumber!! > 0) {
                val totalJoinCount = mJoinCount + mSelectAdapter.mDataList!!.size
                if (totalJoinCount >= mLuckyDraw.engageNumber!!) {
                    showAlert(R.string.msg_over_max_join_count)
                    return@setOnClickListener
                }
            }

            binding.textLuckyDrawJoinSelect.isClickable = false
            binding.textLuckyDrawJoinSelect.isEnabled = false
            val params = HashMap<String, String>()
            params["luckyDrawSeqNo"] = mLuckyDraw.seqNo.toString()
            showProgress("")
            ApiBuilder.create().luckyDrawSelectNumber(params).setCallback(object : PplusCallback<NewResultResponse<LuckyDrawNumber>> {
                override fun onResponse(call: Call<NewResultResponse<LuckyDrawNumber>>?, response: NewResultResponse<LuckyDrawNumber>?) {
                    hideProgress()
                    if (response?.result != null) {
                        val number = response.result!!
                        when (mLuckyDraw.winType) { //1:ABC, 2:AC, 3:A
                            1 -> {
                                binding.textLuckyDrawJoinFirst.setText(number.first!!)
                                binding.textLuckyDrawJoinSecond.setText(number.second!!)
                                binding.textLuckyDrawJoinThird.setText(number.third!!)
                            }

                            2 -> {
                                binding.textLuckyDrawJoinFirst.setText(number.first!!)
                                binding.textLuckyDrawJoinThird.setText(number.third!!)
                            }

                            3 -> {
                                binding.textLuckyDrawJoinFirst.setText(number.first!!)
                            }
                        }
                        val handler = Handler(Looper.myLooper()!!)
                        handler.postDelayed({
                            mSelectAdapter.add(number)
                            binding.textLuckyDrawJoinSelect.isClickable = true
                            binding.textLuckyDrawJoinSelect.isEnabled = true
                            setEngagePrice()
                        }, 1000)

                    } else {
                        binding.textLuckyDrawJoinSelect.isClickable = true
                        binding.textLuckyDrawJoinSelect.isEnabled = true
                        showAlert(R.string.msg_not_exist_selectable_number)
                    }
                }

                override fun onFailure(call: Call<NewResultResponse<LuckyDrawNumber>>?, t: Throwable?, response: NewResultResponse<LuckyDrawNumber>?) {
                    hideProgress()
                    binding.textLuckyDrawJoinSelect.isClickable = true
                    binding.textLuckyDrawJoinSelect.isEnabled = true
                    if (response?.code == 404) {
                        showAlert(R.string.msg_not_exist_selectable_number)
                    }
                }
            }).build().call()
        }

        binding.textLuckyDrawNumberGuide.setOnClickListener {
            val builder = AlertBuilder.Builder()
            builder.setTitle(getString(R.string.word_lucky_draw_number))
            builder.setStyle_alert(AlertBuilder.STYLE_ALERT.MESSAGE)
            builder.addContents(AlertData.MessageData(getString(R.string.msg_alert_lucky_draw_number_desc), AlertBuilder.MESSAGE_TYPE.TEXT, 3))
            builder.setRightText(getString(R.string.word_confirm))
            builder.builder().show(this)
        }

        binding.layoutLuckyDrawJoin.setOnClickListener {
            if (mSelectAdapter.mDataList!!.isEmpty()) {
                showAlert(R.string.msg_select_lucky_draw_number)
                return@setOnClickListener
            }

            if (mLuckyDraw.engageType == "ball" && mTotalPrice > LoginInfoManager.getInstance().member!!.ball!!) {
                val lackBall = mTotalPrice - LoginInfoManager.getInstance().member!!.ball!!
                val intent = Intent(this, AlertLuckyBallLackActivity::class.java)
                intent.putExtra(Const.DATA, lackBall)
                intent.putParcelableArrayListExtra(Const.NUMBER, mSelectAdapter.mDataList!! as ArrayList)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                defaultLauncher.launch(intent)
                return@setOnClickListener
            }

            if (mLuckyDraw.engageNumber!! > 0) {
                val totalJoinCount = mJoinCount + mSelectAdapter.mDataList!!.size
                if (totalJoinCount >= mLuckyDraw.engageNumber!!) {
                    showAlert(R.string.msg_over_max_join_count)
                    return@setOnClickListener
                }
            }

            //            if(mLuckyDraw.engageType == "free"){
            //                val consentInformation = UserMessagingPlatform.getConsentInformation(this)
            //                if (!consentInformation.canRequestAds()) {
            //                    // Create a ConsentRequestParameters object.
            //                    val params = ConsentRequestParameters.Builder()
            //                        .build()
            //
            //                    consentInformation.requestConsentInfoUpdate(this, params, ConsentInformation.OnConsentInfoUpdateSuccessListener {
            //                        UserMessagingPlatform.loadAndShowConsentFormIfRequired(this, ConsentForm.OnConsentFormDismissedListener { loadAndShowError -> // Consent gathering failed.
            //                            LogUtil.e(LOG_TAG, String.format("%s: %s", loadAndShowError?.errorCode, loadAndShowError?.message))
            //
            //                            // Consent has been gathered.
            //                            if (consentInformation.canRequestAds()) {
            //                                AdmobUtil.getInstance(this).initAdMob()
            //                            }
            //
            //                        })
            //                    }, ConsentInformation.OnConsentInfoUpdateFailureListener { requestConsentError -> // Consent gathering failed.
            //                        LogUtil.e(LOG_TAG, String.format("%s: %s", requestConsentError.errorCode, requestConsentError.message))
            //                    })
            //                    return@setOnClickListener
            //                }
            //            }

            val intent = Intent(this, AlertLuckyDrawJoinActivity::class.java)
            intent.putExtra(Const.DATA, mLuckyDraw)
            intent.putParcelableArrayListExtra(Const.NUMBER, mSelectAdapter.mDataList!! as ArrayList)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            joinLauncher.launch(intent)
        }

        reloadSession()
        getLuckyDraw()
    }

    private var mTotalPrice = 0

    private fun setEngagePrice() {
        if (mLuckyDraw.engageType == "ball") {
            mTotalPrice = mSelectAdapter.mDataList!!.size * mLuckyDraw.engageBall!!
            if (mTotalPrice > 0) {
                binding.textLuckyDrawJoinEngageBall.visibility = View.VISIBLE
                binding.textLuckyDrawJoinEngageBall.text = getString(R.string.format_ball_unit, FormatUtil.getMoneyType(mTotalPrice.toString()))
            } else {
                binding.textLuckyDrawJoinEngageBall.visibility = View.GONE
            }

        } else {
            binding.textLuckyDrawJoinEngageBall.visibility = View.GONE
        }
    }

    private fun getLuckyDraw() {
        showProgress("")
        ApiBuilder.create().getLuckyDraw(mLuckyDraw.seqNo!!).setCallback(object : PplusCallback<NewResultResponse<LuckyDraw>> {
            override fun onResponse(call: Call<NewResultResponse<LuckyDraw>>?, response: NewResultResponse<LuckyDraw>?) {
                hideProgress()
                if (response?.result != null) {
                    mLuckyDraw = response.result!!
                    binding.textLuckyDrawJoinDrawTitle.text = mLuckyDraw.title
                    if(mLuckyDraw.engageType == "ball"){
                        binding.textLuckyDrawJoinBall.text = getString(R.string.format_ball_unit, FormatUtil.getMoneyType(mLuckyDraw.engageBall!!.toInt().toString()))
                        binding.textLuckyDrawJoinBall.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_lucky_draw_ball, 0, 0, 0)
                    }else if (mLuckyDraw.engageType == "free") {
                        binding.textLuckyDrawJoinBall.text = getString(R.string.word_free)
                        binding.textLuckyDrawJoinBall.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
                    }


                    when (mLuckyDraw.winType) { //1:ABC, 2:AC, 3:A
                        1 -> {
                            binding.layoutLuckyDrawJoinFirst.visibility = View.VISIBLE
                            binding.layoutLuckyDrawJoinSecond.visibility = View.VISIBLE
                            binding.layoutLuckyDrawJoinThird.visibility = View.VISIBLE
                            binding.textLuckyDrawJoinDesc.setText(R.string.msg_lucky_draw_join_desc1)
                        }

                        2 -> {
                            binding.layoutLuckyDrawJoinFirst.visibility = View.VISIBLE
                            binding.layoutLuckyDrawJoinSecond.visibility = View.GONE
                            binding.layoutLuckyDrawJoinThird.visibility = View.VISIBLE
                            binding.textLuckyDrawJoinDesc.setText(R.string.msg_lucky_draw_join_desc2)
                        }

                        3 -> {
                            binding.layoutLuckyDrawJoinFirst.visibility = View.VISIBLE
                            binding.layoutLuckyDrawJoinSecond.visibility = View.GONE
                            binding.layoutLuckyDrawJoinThird.visibility = View.GONE
                            binding.textLuckyDrawJoinDesc.setText(R.string.msg_lucky_draw_join_desc3)
                        }
                    }

                    getRemainCount()
                }

            }

            override fun onFailure(call: Call<NewResultResponse<LuckyDraw>>?, t: Throwable?, response: NewResultResponse<LuckyDraw>?) {
                hideProgress()
            }
        }).build().call()
    }

    private fun getRemainCount() {
        val params = HashMap<String, String>()
        params["luckyDrawSeqNo"] = mLuckyDraw.seqNo.toString()
        showProgress("")
        ApiBuilder.create().getLuckyDrawNumberRemainCount(params).setCallback(object : PplusCallback<NewResultResponse<Int>> {
            override fun onResponse(call: Call<NewResultResponse<Int>>?, response: NewResultResponse<Int>?) {
                hideProgress()
                if (response?.result != null) {
                    binding.textLuckyDrawJoinRemainCount.text = getString(R.string.format_lucky_draw_remain_count, FormatUtil.getMoneyType(response.result.toString()))
                } else {
                    binding.textLuckyDrawJoinRemainCount.text = getString(R.string.format_lucky_draw_remain_count, "0")
                }
                if (mLuckyDraw.engageNumber!! > 0) {
                    getJoinCount()
                }
            }

            override fun onFailure(call: Call<NewResultResponse<Int>>?, t: Throwable?, response: NewResultResponse<Int>?) {
                hideProgress()
            }
        }).build().call()
    }

    private var mJoinCount = 0

    private fun getJoinCount() {
        val params = HashMap<String, String>()
        params["luckyDrawSeqNo"] = mLuckyDraw.seqNo.toString()
        showProgress("")
        ApiBuilder.create().getLuckyDrawJoinCount(params).setCallback(object : PplusCallback<NewResultResponse<Int>> {
            override fun onResponse(call: Call<NewResultResponse<Int>>?, response: NewResultResponse<Int>?) {
                hideProgress()
                if (response?.result != null) {
                    mJoinCount = response.result!!
                }

            }

            override fun onFailure(call: Call<NewResultResponse<Int>>?, t: Throwable?, response: NewResultResponse<Int>?) {
                hideProgress()
            }
        }).build().call()
    }

    private fun reloadSession() {
        PplusCommonUtil.reloadSession(object : PplusCommonUtil.Companion.ReloadListener {

            override fun reload() {

                binding.textLuckyDrawJoinRetentionBall.text = getString(R.string.format_ball_unit, FormatUtil.getMoneyType(LoginInfoManager.getInstance().member!!.ball!!.toInt().toString()))
            }
        })
    }

    val joinLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            if (mLuckyDraw.engageType == "free") {

                if (!AdmobUtil.getInstance(this).mIsLoaded) {
                    return@registerForActivityResult
                }

                if (AdmobUtil.getInstance(this).mAdmobInterstitialAd != null) {
                    AdmobUtil.getInstance(this).mAdmobInterstitialAd!!.show(this)
                    AdmobUtil.getInstance(this).mIsLoaded = false
                    AdmobUtil.getInstance(this).initAdMob()
                    setResult(Activity.RESULT_OK)
                    finish()
                } else {
                    AdmobUtil.getInstance(this).mIsLoaded = false
                    AdmobUtil.getInstance(this).initAdMob()
                    setResult(Activity.RESULT_OK)
                    finish()
                }
            } else {
                setResult(RESULT_OK)
                finish()
            }

        }
    }

    val defaultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        reloadSession()
    }


    override fun getToolbarOption(): ToolbarOption {

        val toolbarOption = ToolbarOption(this)
        toolbarOption.initializeDefaultToolbar(getString(R.string.word_select_lucky_draw_number), ToolbarOption.ToolbarMenu.LEFT)
        return toolbarOption
    }

    override fun getOnToolbarClickListener(): OnToolbarListener {

        return object : OnToolbarListener {
            override fun onClick(v: View?, toolbarMenu: ToolbarOption.ToolbarMenu?, tag: Any?) {
                when (toolbarMenu) {
                    ToolbarOption.ToolbarMenu.LEFT -> if (tag == 1) {
                        onBackPressedDispatcher.onBackPressed()
                    }

                    else -> {}
                }
            }
        }
    }
}