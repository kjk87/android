package com.lejel.wowbox.apps.event.ui

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.lejel.wowbox.Const
import com.lejel.wowbox.R
import com.lejel.wowbox.apps.common.component.MyBounceInterpolator
import com.lejel.wowbox.apps.common.ui.base.BaseActivity
import com.lejel.wowbox.core.network.ApiBuilder
import com.lejel.wowbox.core.network.model.dto.Event
import com.lejel.wowbox.core.network.model.dto.EventJoin
import com.lejel.wowbox.core.network.model.dto.EventWin
import com.lejel.wowbox.core.network.model.response.NewResultResponse
import com.lejel.wowbox.core.util.AdmobUtil
import com.lejel.wowbox.core.util.PplusCommonUtil
import com.lejel.wowbox.databinding.ActivityEventResultBinding
import com.pplus.networks.common.PplusCallback
import com.pplus.utils.part.format.FormatUtil
import com.pplus.utils.part.utils.StringUtils
import retrofit2.Call


class EventResultActivity : BaseActivity() {
    override fun getPID(): String {
        return ""
    }

    private lateinit var binding: ActivityEventResultBinding

    override fun getLayoutView(): View {
        binding = ActivityEventResultBinding.inflate(layoutInflater)
        return binding.root
    }

    private var mEventWin: EventWin? = null
    private var mEventJoin: EventJoin? = null
    private var mEvent: Event? = null
    private var isAdShow = false

    override fun initializeView(savedInstanceState: Bundle?) {

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
            }
        })

        mEventWin = PplusCommonUtil.getParcelableExtra(intent, Const.EVENT_WIN, EventWin::class.java)
        mEventJoin = PplusCommonUtil.getParcelableExtra(intent, Const.EVENT_JOIN, EventJoin::class.java)
        mEvent = PplusCommonUtil.getParcelableExtra(intent, Const.EVENT, Event::class.java)

//        isAdShow =  mEvent!!.primaryType == "goodluck" && (mEvent!!.reward == null || mEvent!!.reward!! >= 0)
        isAdShow = true

        binding.imageEventResult2Rto.setImageResource(R.drawable.ic_event_popup_character)

        binding.textEventResultCancel.visibility = View.GONE
        binding.textEventResultBar.visibility = View.GONE

        binding.textEventResultImpression.setOnClickListener {
            if (mEventWin != null) {
                if (mEventWin!!.eventGift!!.giftType == "ball" || mEventWin!!.eventGift!!.giftType == "point" || mEventWin!!.eventGift!!.giftType == "lotto") {
                    val params = HashMap<String, String>()
                    params["seqNo"] = mEventWin!!.seqNo.toString()
                    params["impression"] = mEventWin!!.eventGift!!.title!!
                    showProgress("")
                    ApiBuilder.create().writeImpression(params).setCallback(object : PplusCallback<NewResultResponse<Any>> {
                        override fun onResponse(call: Call<NewResultResponse<Any>>?,
                                                response: NewResultResponse<Any>?) {
                            hideProgress()
                            setResult(Activity.RESULT_OK)
                            finish()
                        }

                        override fun onFailure(call: Call<NewResultResponse<Any>>?,
                                               t: Throwable?,
                                               response: NewResultResponse<Any>?) {
                            hideProgress()
                        }
                    }).build().call()
                } else {
                    winImpression()
                }

            } else {
                if (StringUtils.isEmpty(mEvent!!.moveTargetString)) {
                    if (isAdShow) {
                        if (!AdmobUtil.getInstance(this).mIsLoaded) {
                            return@setOnClickListener
                        }

                        showAdmob()
                    } else {
                        val data = Intent()
                        data.putExtra(Const.DATA, mEvent)
                        setResult(Activity.RESULT_OK, data)
                        finish()
                    }
                } else {
                    if (mEventJoin != null) {
                        moveToTarget()
                    }
                }
            }
        }

        if (mEventWin != null) {
            binding.layoutEventResult1.visibility = View.VISIBLE
            binding.layoutEventResult2.visibility = View.GONE
            binding.imageEventResultLoose.visibility = View.GONE
            binding.textEventResultTitle.setPadding(0, 0, 0, 0)
            binding.textEventResultDescription1.setPadding(0, 0, 0, 0)
            binding.textEventResultTitle.setText(R.string.msg_celebration)
            binding.textEventResultTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimensionPixelSize(R.dimen.textSize_84pt).toFloat())
            binding.imageEventResult.visibility = View.VISIBLE
            binding.textEventResultDescription2.visibility = View.GONE
            binding.textEventResultDescription2.setText(R.string.msg_write_winner_description)
            if (mEventWin!!.eventGift!!.giftType == "ball" || mEventWin!!.eventGift!!.giftType == "point" || mEventWin!!.eventGift!!.giftType == "lotto") {
                binding.textEventResultImpression.setText(R.string.word_confirm)
            } else {
                binding.textEventResultImpression.setText(R.string.msg_write_winner_impression)
            }

            if(StringUtils.isNotEmpty(mEventWin!!.eventGift!!.alert)){
                binding.textEventResultDescription1.text = PplusCommonUtil.fromHtml(mEventWin!!.eventGift!!.alert!!)
            }

            Glide.with(this).load(mEventWin!!.eventGift!!.giftImageUrl).apply(RequestOptions().centerCrop()).into(binding.imageEventResult)

        } else {

            if (mEvent!!.winAnnounceType.equals("immediately")) {
                binding.layoutEventResult1.visibility = View.VISIBLE
                binding.layoutEventResult2.visibility = View.GONE

                binding.imageEventResultLoose.visibility = View.VISIBLE
                Glide.with(this).load(R.drawable.crying).into(binding.imageEventResultLoose)

                binding.textEventResultTitle.setPadding(0, resources.getDimensionPixelSize(R.dimen.height_60), 0, 0)
                binding.textEventResultDescription1.setPadding(0, 0, 0, 0)
                binding.textEventResultTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimensionPixelSize(R.dimen.textSize_64pt).toFloat())
                binding.imageEventResult.visibility = View.GONE
                binding.textEventResultDescription2.visibility = View.GONE

                if (mEvent!!.reward != null && mEvent!!.reward!! > 0) {
                    binding.textEventResultDescription2.visibility = View.VISIBLE
                    binding.textEventResultDescription2.text = getString(R.string.format_bol_reward, FormatUtil.getMoneyTypeFloat(mEvent!!.reward!!.toString()))
                }

                binding.textEventResultTitle.setText(R.string.msg_next_time_event)

                if (mEvent!!.primaryType.equals("goodluck")) {
                    binding.textEventResultDescription1.text = PplusCommonUtil.fromHtml(mEvent!!.winnerAlert!!)
                    binding.textEventResultImpression.setText(R.string.word_confirm)

                } else {
                    binding.textEventResultDescription1.text = PplusCommonUtil.fromHtml(mEvent!!.winnerAlert!!)
                    binding.textEventResultImpression.setText(R.string.word_confirm)
                }

            } else {
                if (StringUtils.isEmpty(mEvent!!.moveTargetString)) {

                    if (isAdShow && !AdmobUtil.getInstance(this).mIsLoaded) {
                        AdmobUtil.getInstance(this).listener = object : AdmobUtil.OnLoadListener {
                            override fun onLoaded() {
                                hideProgress()
                                setEventResult()
                            }
                        }
                        showProgress(getString(R.string.msg_event_join_progress))
                        AdmobUtil.getInstance(this).initAdMob()
                    } else {
                        setEventResult()
                    }

                    binding.layoutEventResult.setOnClickListener {
                        if (isAdShow) {
                            if (!AdmobUtil.getInstance(this).mIsLoaded) {
                                return@setOnClickListener
                            }
                            showAdmob()
                        } else {
                            setResult(Activity.RESULT_OK)
                            finish()
                        }
                    }
                } else {
                    setEventResult()
                    binding.layoutEventResult.setOnClickListener {
                        if (mEventJoin != null) {
                            moveToTarget()
                        }
                    }
                }
            }

        }
    }

    private fun setEventResult() {
        binding.layoutEventResult1.visibility = View.GONE
        binding.layoutEventResult2.visibility = View.VISIBLE
        binding.textEventResult2Alert.text = PplusCommonUtil.fromHtml(mEvent!!.winnerAlert!!)

        binding.textEventResult2Alert.visibility = View.INVISIBLE
        binding.textEventResult2Confirm.visibility = View.INVISIBLE

        val fadeInAnim = AnimationUtils.loadAnimation(this, android.R.anim.fade_in)
        val anim = AnimationUtils.loadAnimation(this, R.anim.bounce)
        anim.duration = 1000
        val interpolator = MyBounceInterpolator(0.2, 20.0)
        anim.interpolator = interpolator
        anim.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationRepeat(animation: Animation?) {

            }

            override fun onAnimationEnd(animation: Animation?) {
                binding.textEventResult2Alert.visibility = View.VISIBLE
                binding.textEventResult2Confirm.visibility = View.VISIBLE
                binding.textEventResult2Alert.startAnimation(fadeInAnim)
                binding.textEventResult2Confirm.startAnimation(fadeInAnim)
            }

            override fun onAnimationStart(animation: Animation?) {

            }
        })
        binding.imageEventResult2Rto.post{
            binding.imageEventResult2Rto.startAnimation(anim)
        }
    }

//    fun initAdMob() {
//        showProgress(getString(R.string.msg_event_join_progress))
//        MobileAds.initialize(this)
//
//        val adRequest = AdRequest.Builder().build()
//
//        InterstitialAd.load(this, Const.ADMOB_INTERSTITIAL_ID, adRequest, object : InterstitialAdLoadCallback() {
//            override fun onAdFailedToLoad(adError: LoadAdError) {
//                hideProgress()
//                LogUtil.e(LOG_TAG, "onAdFailedToLoad : {}", adError.message)
//                mAdmobInterstitialAd = null
//                isAdLoaded = true
//                setEventResult()
//            }
//
//            override fun onAdLoaded(interstitialAd: InterstitialAd) {
//                LogUtil.e(LOG_TAG, "onAdLoaded")
//                hideProgress()
//                mAdmobInterstitialAd = interstitialAd
//                isAdLoaded = true
//                setEventResult()
//            }
//        })
//    }

    private fun winImpression() {
        val intent = Intent(this, EventWinImpressionActivity::class.java)
        intent.putExtra(Const.EVENT_WIN, mEventWin)
        resultLauncher.launch(intent)
    }

    val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            if (mEventJoin != null) {
                if (mEvent!!.winAnnounceType.equals("immediately")) {
                    val intent = Intent(this, EventImpressionActivity::class.java)
                    intent.putExtra(Const.DATA, mEvent)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                    startActivity(intent)
                    moveToTarget()
                } else {
                    setResult(Activity.RESULT_OK)
                    finish()
                }
            } else {
                setResult(Activity.RESULT_OK)
                finish()
            }
        } else {
            winImpression()
        }
    }

    private fun moveToTarget() {
        if (!mEvent!!.primaryType.equals("move")) {
            val targetString = mEvent!!.moveTargetString
            if (mEvent!!.moveType == "none" || StringUtils.isEmpty(targetString)) {
                if (isAdShow) {
                    showAdmob()
                } else {
                    setResult(Activity.RESULT_OK)
                    finish()
                }
            } else {
                val moveTargetNumber = mEvent!!.moveTargetNumber
                if (mEvent!!.moveType == "inner" && moveTargetNumber != null) {
//                    when (moveTargetNumber) {
//                        5 -> { //배송상품
//                            val intent = Intent(this, ProductShipDetailActivity::class.java)
//                            val productPrice = ProductPrice()
//                            productPrice.seqNo = targetString!!.toLong()
//                            intent.putExtra(Const.DATA, productPrice)
//                            startActivity(intent)
//                        }
//                    }
                } else {
                    PplusCommonUtil.openChromeWebView(this, targetString!!)
                }

                setResult(Activity.RESULT_OK)
                finish()
            }
        }
    }

    fun showAdmob() {
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
    }
}
