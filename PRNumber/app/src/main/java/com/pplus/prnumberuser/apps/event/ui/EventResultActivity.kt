package com.pplus.prnumberuser.apps.event.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.activity.result.contract.ActivityResultContracts
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.pplus.prnumberuser.Const
import com.pplus.prnumberuser.R
import com.pplus.prnumberuser.apps.common.component.MyBounceInterpolator
import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
import com.pplus.prnumberuser.apps.product.ui.ProductActivity
import com.pplus.prnumberuser.apps.product.ui.ProductShipDetailActivity
import com.pplus.prnumberuser.core.code.common.EventType
import com.pplus.prnumberuser.core.network.model.dto.Event
import com.pplus.prnumberuser.core.network.model.dto.EventResult
import com.pplus.prnumberuser.core.network.model.dto.ProductPrice
import com.pplus.prnumberuser.core.util.PplusCommonUtil
import com.pplus.prnumberuser.databinding.ActivityEventResultBinding
import com.pplus.utils.part.format.FormatUtil
import com.pplus.utils.part.logs.LogUtil
import com.pplus.utils.part.utils.StringUtils


class EventResultActivity : BaseActivity() {
    override fun getPID(): String {
        return ""
    }

    private lateinit var binding:ActivityEventResultBinding

    override fun getLayoutView(): View {
        binding = ActivityEventResultBinding.inflate(layoutInflater)
        return binding.root
    }

    var mEventResult: EventResult? = null

    private var mInterstitialAd: InterstitialAd? = null
    private var mEvent: Event? = null
    var isAdShow = false

    override fun initializeView(savedInstanceState: Bundle?) {

        mEventResult = intent.getParcelableExtra(Const.EVENT_RESULT)

        if (mEventResult!!.win != null) {
            mEvent = mEventResult!!.win!!.event
        } else {
            mEvent = mEventResult!!.join!!.event
        }

        isAdShow = !(mEvent!!.primaryType == EventType.PrimaryType.lotto.name || mEvent!!.primaryType == EventType.PrimaryType.lottoPlaybol.name || mEvent!!.primaryType == EventType.PrimaryType.page.name || mEvent!!.primaryType == EventType.PrimaryType.tune.name)

        if(mEvent!!.primaryType == EventType.PrimaryType.number.name){
            binding.imageEventResult2Rto.setImageResource(R.drawable.ic_number_event_popup_character)
        }else if(mEvent!!.primaryType == EventType.PrimaryType.lotto.name || mEvent!!.primaryType == EventType.PrimaryType.lottoPlaybol.name){
            binding.imageEventResult2Rto.setImageResource(R.drawable.img_lotto_event_popup_character)
        }else{
            binding.imageEventResult2Rto.setImageResource(R.drawable.ic_event_popup_character)
        }


        if (mEventResult!!.win != null) {
            binding.layoutEventResult1.visibility = View.VISIBLE
            binding.layoutEventResult2.visibility = View.GONE
            binding.textEventResultCancel.visibility = View.GONE
            binding.textEventResultImpression.visibility = View.VISIBLE
            binding.imageEventResultLoose.visibility = View.GONE
            binding.textEventResultTitle.setPadding(0, 0, 0, 0)
            binding.textEventResultDescription1.setPadding(0, 0, 0, 0)
            binding.textEventResultTitle.setText(R.string.msg_celebration)
            binding.textEventResultTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimensionPixelSize(R.dimen.textSize_84pt).toFloat())
            binding.imageEventResult.visibility = View.VISIBLE
            binding.textEventResultDescription2.visibility = View.GONE
            binding.textEventResultDescription2.setText(R.string.msg_write_winner_description)
            binding.textEventResultImpression.setText(R.string.msg_write_winner_impression)
            binding.textEventResultDescription1.text = mEventResult!!.win!!.gift!!.alert

            if(mEvent!!.primaryType.equals(EventType.PrimaryType.number.name)){
                binding.imageEventResult.setImageResource(R.drawable.img_lotto_event_gift)
            }else{
                if(mEventResult!!.win!!.gift != null){
                    Glide.with(this).load(mEventResult!!.win!!.gift!!.giftImageUrl).apply(RequestOptions().centerCrop().placeholder(R.drawable.img_lotto_event_gift).error(R.drawable.img_lotto_event_gift)).into(binding.imageEventResult)
                }else{
                    binding.imageEventResult.setImageResource(R.drawable.img_lotto_event_gift)
                }

            }

        } else {
            if (mEvent!!.winAnnounceType.equals(EventType.WinAnnounceType.immediately.name)) {
                binding.layoutEventResult1.visibility = View.VISIBLE
                binding.layoutEventResult2.visibility = View.GONE

                binding.imageEventResultLoose.visibility = View.VISIBLE
                Glide.with(this).load(R.drawable.crying).into(binding.imageEventResultLoose)

                binding.textEventResultTitle.setPadding(0, resources.getDimensionPixelSize(R.dimen.height_60), 0, 0)
                binding.textEventResultDescription1.setPadding(0, 0, 0, resources.getDimensionPixelSize(R.dimen.height_80))
                binding.textEventResultTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimensionPixelSize(R.dimen.textSize_64pt).toFloat())
                binding.imageEventResult.visibility = View.GONE
                binding.textEventResultDescription2.visibility = View.GONE

                if(mEvent!!.reward != null && mEvent!!.reward!! > 0){
                    binding.textEventResultDescription2.visibility = View.VISIBLE
                    binding.textEventResultDescription2.text = getString(R.string.format_bol_reward, FormatUtil.getMoneyType(mEvent!!.reward!!.toString()))
                }

                binding.textEventResultTitle.setText(R.string.msg_next_time_event)

                if (mEvent!!.primaryType.equals(EventType.PrimaryType.goodluck.name)) {
                    binding.textEventResultCancel.visibility = View.VISIBLE
                    binding.textEventResultImpression.visibility = View.GONE

                    if (mEvent!!.reward!! < 0) {
                        binding.textEventResultDescription1.visibility = View.VISIBLE
                        binding.textEventResultDescription1.text = getString(R.string.format_use_bol2, FormatUtil.getMoneyType(Math.abs(mEvent!!.reward!!).toString()))
                    } else {
//                        binding.textEventResultDescription1.text = "(${getString(R.string.word_free)})"
                        binding.textEventResultDescription1.visibility = View.GONE
                    }

//                    text_event_result_impression.setText(R.string.word_retry)
                    binding.textEventResultCancel.setText(R.string.word_confirm)

                    binding.textEventResultCancel.setOnClickListener {
                        if (mEventResult!!.join != null) {
                            moveToTarget()
                        }
                    }

                } else {
                    binding.textEventResultDescription1.setText(R.string.msg_join_other_event)

                    binding.textEventResultImpression.setText(R.string.word_confirm)
                }

            } else {

                binding.layoutEventResult1.visibility = View.GONE
                binding.layoutEventResult2.visibility = View.VISIBLE
                if(StringUtils.isNotEmpty(mEvent!!.winnerAlert)){
                    binding.textEventResult2Alert.text = mEvent!!.winnerAlert
                }else{
                    binding.textEventResult2Alert.text = getString(R.string.msg_event_join_complete)
                }


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

                        binding.layoutEventResult.setOnClickListener {

                            if (mEventResult!!.join != null) {
                                moveToTarget()
                            }
                        }
                    }

                    override fun onAnimationStart(animation: Animation?) {

                    }
                })
                binding.imageEventResult2Rto.post(Runnable {
                    binding.imageEventResult2Rto.startAnimation(anim)
                })

            }
        }

        binding.textEventResultImpression.setOnClickListener {
            if (mEventResult!!.win != null) {
                moveEventWinImpression()
            } else {

                if (mEventResult!!.join != null) {
                    moveToTarget()
                }
            }
        }

        if (StringUtils.isEmpty(mEvent!!.moveTargetString)) {

            binding.layoutEventResult.isEnabled = false
            binding.textEventResultCancel.isEnabled = false
            if(isAdShow){
                initAdMob()
            }else{
                binding.layoutEventResult.isEnabled = true
                binding.textEventResultCancel.isEnabled = true
            }

        }else{
            binding.layoutEventResult.setOnClickListener {
                if (mEventResult!!.join != null) {
                    moveToTarget()
                }
            }
        }
    }

    fun initAdMob() {
        MobileAds.initialize(this)

        val adRequest = AdRequest.Builder().build()

        InterstitialAd.load(this,Const.ADMOB_INTERSTITIAL_ID, adRequest, object : InterstitialAdLoadCallback() {
            override fun onAdFailedToLoad(adError: LoadAdError) {
                LogUtil.e(LOG_TAG, "onAdFailedToLoad : {}", adError?.message)
                mInterstitialAd = null
                binding.layoutEventResult.isEnabled = true
                binding.textEventResultCancel.isEnabled = true
            }

            override fun onAdLoaded(interstitialAd: InterstitialAd) {
                LogUtil.e(LOG_TAG, "onAdLoaded")
                mInterstitialAd = interstitialAd
                binding.layoutEventResult.isEnabled = true
                binding.textEventResultCancel.isEnabled = true
            }
        })
    }

    var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            // There are no request codes
            val data = result.data

            if (mEventResult!!.join != null) {
                if (mEventResult!!.join!!.event!!.winAnnounceType.equals(EventType.WinAnnounceType.immediately.name)) {
                    val intent = Intent(this, EventImpressionActivity::class.java)
                    intent.putExtra(Const.DATA, mEventResult!!.join!!.event)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                    startActivity(intent)
                    moveToTarget()
                }else{
                    setResult(Activity.RESULT_OK)
                    finish()
                }
            }else{
                setResult(Activity.RESULT_OK)
                finish()
            }
        }else{
            moveEventWinImpression()
        }
    }

    private fun moveEventWinImpression(){
        val intent = Intent(this, EventWinImpressionActivity::class.java)
        intent.putExtra(Const.EVENT_WIN, mEventResult!!.win)
        resultLauncher.launch(intent)
    }

    fun moveToTarget() {

        LogUtil.e(LOG_TAG, "moveTargetNumber : "+mEventResult!!.join!!.event!!.moveTargetNumber)
        if (!mEventResult!!.join!!.event!!.primaryType.equals(EventType.PrimaryType.move.name)) {

            val targetString = mEventResult!!.join!!.event!!.moveTargetString
            if(mEventResult!!.join!!.event!!.moveType == "inner"){
                val moveTargetNumber = mEventResult!!.join!!.event!!.moveTargetNumber
                when(moveTargetNumber){
                    4, 5 -> {//상품
                        if (StringUtils.isNotEmpty(targetString)) {
                            val intent = Intent(this, ProductShipDetailActivity::class.java)
                            val productPrice = ProductPrice()
                            productPrice.seqNo = targetString!!.toLong()
                            intent.putExtra(Const.DATA, productPrice)
                            startActivity(intent)
                        }
                    }
                    6 -> {
                        val intent = Intent(this, ProductActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                        startActivity(intent)
                    }
                }

                setResult(Activity.RESULT_OK)
                finish()
            }else if(mEventResult!!.join!!.event!!.moveType == "external"){

                if (StringUtils.isNotEmpty(targetString)) {
                    PplusCommonUtil.openChromeWebView(this, targetString!!)
                }

                setResult(Activity.RESULT_OK)
                finish()

            }else{
                if(isAdShow){
                    showAdmob()
                }else{
                    setResult(Activity.RESULT_OK)
                    finish()
                }
            }
        }
    }
    fun showAdmob() {
        if (mInterstitialAd != null) {
            mInterstitialAd!!.show(this)

            setResult(Activity.RESULT_OK)
            finish()
        }else{
            setResult(Activity.RESULT_OK)
            finish()
        }

    }

    override fun onBackPressed() {
    }
}
