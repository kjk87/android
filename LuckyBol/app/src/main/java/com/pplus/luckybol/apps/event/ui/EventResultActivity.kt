package com.pplus.luckybol.apps.event.ui

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
import com.pplus.luckybol.Const
import com.pplus.luckybol.R
import com.pplus.luckybol.apps.common.component.MyBounceInterpolator
import com.pplus.luckybol.apps.common.ui.base.BaseActivity
import com.pplus.luckybol.apps.product.ui.ProductShipDetailActivity
import com.pplus.luckybol.core.code.common.EventType
import com.pplus.luckybol.core.network.ApiBuilder
import com.pplus.luckybol.core.network.model.dto.Event
import com.pplus.luckybol.core.network.model.dto.EventResult
import com.pplus.luckybol.core.network.model.dto.ProductPrice
import com.pplus.luckybol.core.network.model.response.NewResultResponse
import com.pplus.luckybol.core.util.PplusCommonUtil
import com.pplus.luckybol.databinding.ActivityEventResultBinding
import com.pplus.networks.common.PplusCallback
import com.pplus.utils.part.format.FormatUtil
import com.pplus.utils.part.logs.LogUtil
import com.pplus.utils.part.utils.StringUtils
import retrofit2.Call
import java.util.*


class EventResultActivity : BaseActivity() {
    override fun getPID(): String {
        return ""
    }

    private lateinit var binding: ActivityEventResultBinding

    override fun getLayoutView(): View {
        binding = ActivityEventResultBinding.inflate(layoutInflater)
        return binding.root
    }

    var mEventResult: EventResult? = null

    //    private var mMobonSDK: MobonSDK? = null
    private var mAdmobInterstitialAd: InterstitialAd? = null
//    private var mFaceBookInterstitialAd: com.facebook.ads.InterstitialAd? = null
    private var mEvent: Event? = null
    var isAdShow = false
    var isAdLoaded = false
//    var mAdsList2 = arrayOf("google", "facebook")
//    var mShowAds = "google"
//    var mAdItem: AdItem? = null

    override fun initializeView(savedInstanceState: Bundle?) {

        mEventResult = intent.getParcelableExtra(Const.EVENT_RESULT)

        if (mEventResult!!.win != null) {
            mEvent = mEventResult!!.win!!.event
        } else {
            mEvent = mEventResult!!.join!!.event
        }

        isAdShow = !(mEvent!!.primaryType == EventType.PrimaryType.lotto.name || mEvent!!.primaryType == EventType.PrimaryType.lottoPlaybol.name || mEvent!!.primaryType == EventType.PrimaryType.randomluck.name)
//        isAdShow = ((mEvent!!.primaryType == EventType.PrimaryType.goodluck.name && (mEvent!!.reward == null || mEvent!!.reward!! >= 0)) || mEvent!!.primaryType == EventType.PrimaryType.number.name)

        if (mEvent!!.primaryType == EventType.PrimaryType.number.name) {
            binding.imageEventResult2Rto.setImageResource(R.drawable.ic_number_event_popup_character)
        } else if (mEvent!!.primaryType == EventType.PrimaryType.lotto.name || mEvent!!.primaryType == EventType.PrimaryType.lottoPlaybol.name) {
            binding.imageEventResult2Rto.setImageResource(R.drawable.img_lotto_event_popup_character)
        } else {
            binding.imageEventResult2Rto.setImageResource(R.drawable.ic_event_popup_character)
        }

        binding.textEventResultCancel.visibility = View.GONE
        binding.textEventResultBar.visibility = View.GONE
        if (mEventResult!!.win != null) {
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
            if(mEventResult!!.win!!.gift!!.type == "bol" || mEventResult!!.win!!.gift!!.type == "point"){
                binding.textEventResultImpression.setText(R.string.word_confirm)
            }else{
                binding.textEventResultImpression.setText(R.string.msg_write_winner_impression)
            }

            binding.textEventResultDescription1.text = mEventResult!!.win!!.gift!!.alert

            if (mEvent!!.primaryType.equals(EventType.PrimaryType.number.name)) {
                binding.imageEventResult.setImageResource(R.drawable.img_lotto_event_gift)
            } else {
                if (mEventResult!!.win!!.gift != null) {
                    Glide.with(this).load(mEventResult!!.win!!.gift!!.giftImageUrl).apply(RequestOptions().centerCrop().placeholder(R.drawable.img_lotto_event_gift).error(R.drawable.img_lotto_event_gift)).into(binding.imageEventResult)
                } else {
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

                if (mEvent!!.reward != null && mEvent!!.reward!! > 0) {
                    binding.textEventResultDescription2.visibility = View.VISIBLE
                    binding.textEventResultDescription2.text = getString(R.string.format_bol_reward, FormatUtil.getMoneyTypeFloat(mEvent!!.reward!!.toString()))
                }

                binding.textEventResultTitle.setText(R.string.msg_next_time_event)

                if (mEvent!!.primaryType.equals(EventType.PrimaryType.goodluck.name)) {
                    binding.textEventResultCancel.visibility = View.VISIBLE
                    binding.textEventResultBar.visibility = View.VISIBLE
                    binding.textEventResultDescription1.setText(R.string.msg_retry)

                    if (mEvent!!.reward!! < 0) {
                        binding.textEventResultDescription1.text = getString(R.string.format_use_bol2, FormatUtil.getMoneyTypeFloat(Math.abs(mEvent!!.reward!!).toString()))
                    } else {
                        binding.textEventResultDescription1.text = "(${getString(R.string.word_free)})"
                    }

                    binding.textEventResultImpression.setText(R.string.word_retry)

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
                binding.textEventResult2Alert.text = mEvent!!.winnerAlert

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
                binding.imageEventResult2Rto.post(Runnable {
                    binding.imageEventResult2Rto.startAnimation(anim)
                })

            }
        }

        binding.textEventResultImpression.setOnClickListener {
            if (mEventResult!!.win != null) {
                if(mEventResult!!.win!!.gift!!.type == "bol" || mEventResult!!.win!!.gift!!.type == "point"){
                    val params = HashMap<String, String>()
                    params["event.no"] = mEvent!!.no.toString()
                    params["winNo"] = mEventResult!!.win!!.winNo.toString()
                    params["id"] = mEventResult!!.win!!.id.toString()

                    params["impression"] = mEventResult!!.win!!.gift!!.alert!!
                    showProgress("")
                    ApiBuilder.create().writeImpression(params).setCallback(object : PplusCallback<NewResultResponse<Any>> {
                        override fun onResponse(call: Call<NewResultResponse<Any>>?, response: NewResultResponse<Any>?) {
                            hideProgress()

                            setResult(Activity.RESULT_OK)
                            finish()
                        }

                        override fun onFailure(call: Call<NewResultResponse<Any>>?, t: Throwable?, response: NewResultResponse<Any>?) {
                            hideProgress()
                        }
                    }).build().call()
                }else{
                    winImpression()
                }

            } else {

                if (mEvent!!.primaryType.equals(EventType.PrimaryType.goodluck.name)) {
                    val data = Intent()
                    data.putExtra(Const.DATA, mEvent)
                    setResult(Activity.RESULT_OK, data)
                    finish()
                } else {
                    if (StringUtils.isEmpty(mEvent!!.moveTargetString)) {
                        if (isAdShow) {
                            if (!isAdLoaded) {
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
                        if (mEventResult!!.join != null) {
                            moveToTarget()
                        }
                    }


                }
            }
        }

        if (StringUtils.isEmpty(mEvent!!.moveTargetString)) {

            if (isAdShow) {
                val random = Random()
//                mShowAds = mAdsList2[random.nextInt(mAdsList2.size)]
//                LogUtil.e(LOG_TAG, mShowAds)
//                when (mShowAds) {
//                    "google" -> {
//                        initAdMob()
//                    }
//                    "facebook"->{
//                        initFaceBookAd()
//                    }
//                    "tnk" -> {
//                        initTnk()
//                    }
//                }

                initAdMob()

            }

            binding.layoutEventResult.setOnClickListener {

                if (isAdShow) {

                    if (!isAdLoaded) {
                        return@setOnClickListener
                    }

                    showAdmob()

                } else {
                    setResult(Activity.RESULT_OK)
                    finish()
                }
            }

        } else {
            binding.layoutEventResult.setOnClickListener {
                if (mEventResult!!.join != null) {
                    moveToTarget()
                }
            }
        }
    }

//    fun initTnk() {
//        val interstitialAdItem = InterstitialAdItem(this, "interstitial1")
//        interstitialAdItem.setListener(object : com.tnkfactory.ad.AdListener() {
//            override fun onLoad(adItem: AdItem) {
//                Log.e(LOG_TAG, "tnk onLoad")
//                isAdLoaded = true
//                mAdItem = adItem
//            }
//
//            override fun onError(adItem: AdItem?, error: com.tnkfactory.ad.AdError?) {
//                super.onError(adItem, error)
//                Log.e(LOG_TAG, "tnk onError : " + error!!.message)
//                if (mShowAds == "tnk") {
//                    initAdMob()
//                }
//            }
//
//            override fun onClose(adItem: AdItem?, type: Int) {
//                super.onClose(adItem, type)
//                setResult(RESULT_OK)
//                finish()
//            }
//        })
//        // 전면 광고 로드
//        interstitialAdItem.load()
//
//    }

//    fun initFaceBookAd() {
//        mFaceBookInterstitialAd = com.facebook.ads.InterstitialAd(this, "333186467783343_333189017783088")
//        val interstitialAdListener: InterstitialAdListener = object : InterstitialAdListener {
//            override fun onInterstitialDisplayed(ad: Ad) {
//                // Interstitial ad displayed callback
//                Log.e(LOG_TAG, "Interstitial ad displayed.")
//            }
//
//            override fun onInterstitialDismissed(ad: Ad) {
//                // Interstitial dismissed callback
//                Log.e(LOG_TAG, "Interstitial ad dismissed.")
//                setResult(Activity.RESULT_OK)
//                finish()
//            }
//
//            override fun onError(ad: Ad?, adError: AdError) {
//                // Ad error callback
//                Log.e(LOG_TAG, "Interstitial ad failed to load: " + adError.errorMessage)
//                if(mShowAds == "facebook"){
//                    initAdMob()
//                }else {
//                    isAdLoaded = true
//                }
//            }
//
//            override fun onAdLoaded(ad: Ad) {
//                // Interstitial ad is loaded and ready to be displayed
//                Log.d(LOG_TAG, "Interstitial ad is loaded and ready to be displayed!")
//                // Show the ad
//                isAdLoaded = true
//                //                mFaceBookInterstitialAd!!.show()
//            }
//
//            override fun onAdClicked(ad: Ad) {
//                // Ad clicked callback
//                Log.d(LOG_TAG, "Interstitial ad clicked!")
//            }
//
//            override fun onLoggingImpression(ad: Ad) {
//                // Ad impression logged callback
//                Log.d(LOG_TAG, "Interstitial ad impression logged!")
//            }
//        }
//
//        mFaceBookInterstitialAd!!.loadAd(mFaceBookInterstitialAd!!.buildLoadAdConfig().withAdListener(interstitialAdListener).build());
//    }

    fun initAdMob() {

        MobileAds.initialize(this)

        val adRequest = AdRequest.Builder().build()

        InterstitialAd.load(this,Const.ADMOB_INTERSTITIAL_ID, adRequest, object : InterstitialAdLoadCallback() {
            override fun onAdFailedToLoad(adError: LoadAdError) {
                LogUtil.e(LOG_TAG, "onAdFailedToLoad : {}", adError.message)
                mAdmobInterstitialAd = null
                isAdLoaded = true
            }

            override fun onAdLoaded(interstitialAd: InterstitialAd) {
                LogUtil.e(LOG_TAG, "onAdLoaded")
                mAdmobInterstitialAd = interstitialAd
                isAdLoaded = true
            }
        })
    }

    private fun winImpression(){
        val intent = Intent(this, EventWinImpressionActivity::class.java)
        intent.putExtra(Const.EVENT_WIN, mEventResult!!.win)
        resultLauncher.launch(intent)
    }

    val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            if (mEventResult!!.join != null) {
                if (mEventResult!!.join!!.event!!.winAnnounceType.equals(EventType.WinAnnounceType.immediately.name)) {
                    val intent = Intent(this, EventImpressionActivity::class.java)
                    intent.putExtra(Const.DATA, mEventResult!!.join!!.event)
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

    fun moveToTarget() {
        if (!mEventResult!!.join!!.event!!.primaryType.equals(EventType.PrimaryType.move.name)) {
            val targetString = mEventResult!!.join!!.event!!.moveTargetString
            if (mEventResult!!.join!!.event!!.moveType == "none" || StringUtils.isEmpty(targetString)) {
                if (isAdShow) {
                    if (mEvent!!.primaryType == EventType.PrimaryType.goodluck.name || mEvent!!.primaryType == EventType.PrimaryType.number.name) {
//                        when (mShowAds) {
//                            "google" -> {
//                                showAdmob()
//                            }
//                            "facebook" -> {
//                                showFaceBookAd()
//                            }
//                        }
                        showAdmob()
                    } else {
                        showAdmob()
                        //                        showMoboon()
                    }
                } else {
                    setResult(Activity.RESULT_OK)
                    finish()
                }
            } else {
                val moveTargetNumber = mEventResult!!.join!!.event!!.moveTargetNumber
                if (mEventResult!!.join!!.event!!.moveType == "inner" && moveTargetNumber != null) {
                    when (moveTargetNumber) {
                        5 -> { //배송상품
                            val intent = Intent(this, ProductShipDetailActivity::class.java)
                            val productPrice = ProductPrice()
                            productPrice.seqNo = targetString!!.toLong()
                            intent.putExtra(Const.DATA, productPrice)
                            startActivity(intent)
                        }
                    }
                } else {
                    PplusCommonUtil.openChromeWebView(this, targetString!!)
                }

                setResult(Activity.RESULT_OK)
                finish()
            }
        }
    }

//    fun showTnk() {
//        if (mAdItem != null && mAdItem!!.isLoaded) {
//            mAdItem!!.show()
//        } else {
//            if (mShowAds == "tnk") {
//                showAdmob()
//            } else {
//                setResult(Activity.RESULT_OK)
//                finish()
//            }
//
//        }
//
//    }

//    fun showFaceBookAd() {
//        if (mFaceBookInterstitialAd != null && mFaceBookInterstitialAd!!.isAdLoaded) {
//            mFaceBookInterstitialAd!!.show()
//        } else {
//            if (mShowAds == "facebook") {
//                showAdmob()
//            } else {
//                setResult(Activity.RESULT_OK)
//                finish()
//            }
//        }
//
//    }

    fun showAdmob() {
        if (mAdmobInterstitialAd != null) {
            mAdmobInterstitialAd!!.show(this)

            setResult(Activity.RESULT_OK)
            finish()
        } else {
            setResult(Activity.RESULT_OK)
            finish()

        }

    }

    override fun onBackPressed() {
    }
}
