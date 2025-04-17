package com.lejel.wowbox.core.util

import android.content.Context
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import com.lejel.wowbox.Const
import com.lejel.wowbox.apps.common.ui.base.BaseActivity
import com.pplus.utils.part.logs.LogUtil

class AdmobUtil {
    private val LOG_TAG = this.javaClass.simpleName

    companion object {
        private var mAdmobUtil:AdmobUtil? = null

        fun getInstance(context: Context): AdmobUtil {
            if (mAdmobUtil == null) {
                mAdmobUtil = AdmobUtil()
                mAdmobUtil!!.mContext = context
                MobileAds.initialize(context)
            }
            return mAdmobUtil!!
        }
    }

    interface OnLoadListener {
        fun onLoaded()
    }

    var mAdmobInterstitialAd: InterstitialAd? = null
    var mRewardedAd: RewardedAd? = null
    var mIsLoaded = false
    var mIsRewardedLoaded = false
    var mContext:Context? = null
    var listener: OnLoadListener? = null
    var rewardListener: OnLoadListener? = null

    var mAdLoadCallback = object : InterstitialAdLoadCallback() {
        override fun onAdFailedToLoad(adError: LoadAdError) {
            (mContext!! as BaseActivity).hideProgress()
            LogUtil.e(LOG_TAG, "onAdFailedToLoad : {}", adError.message)
            mAdmobInterstitialAd = null
            mIsLoaded = true
            listener?.onLoaded()
        }

        override fun onAdLoaded(interstitialAd: InterstitialAd) {
            LogUtil.e(LOG_TAG, "onAdLoaded")
            mAdmobInterstitialAd = interstitialAd
            mIsLoaded = true
            listener?.onLoaded()
        }
    }

    var mRewardedAdLoadCallback = object : RewardedAdLoadCallback() {
        override fun onAdFailedToLoad(adError: LoadAdError) {
            LogUtil.e(LOG_TAG, "onRewardAdFailedToLoad : {}", adError.toString())
            mRewardedAd = null
            mIsRewardedLoaded = true
            rewardListener?.onLoaded()
        }

        override fun onAdLoaded(ad: RewardedAd) {
            LogUtil.e(LOG_TAG, "RewardedAd was loaded.")
            mRewardedAd = ad
            mIsRewardedLoaded = true
            rewardListener?.onLoaded()
        }
    }



    fun initAdMob() {
        if(mIsLoaded){
            listener?.onLoaded()
        }else{
            val adRequest = AdRequest.Builder().build()
            if(Const.API_URL.startsWith("https://api")){
                InterstitialAd.load(mContext!!, Const.ADMOB_INTERSTITIAL_ID, adRequest, mAdLoadCallback)
            }else{
                InterstitialAd.load(mContext!!, "ca-app-pub-3940256099942544/1033173712", adRequest, mAdLoadCallback)
            }


        }
    }

    fun initRewardAd(){
        if(mIsRewardedLoaded){
            rewardListener?.onLoaded()
        }else{
            val adRequest = AdRequest.Builder().build()
            if(Const.API_URL.startsWith("https://api")){
                RewardedAd.load(mContext!!,Const.ADMOB_REWARDED_ID, adRequest, mRewardedAdLoadCallback)
            }else{
                RewardedAd.load(mContext!!,"ca-app-pub-3940256099942544/5224354917", adRequest, mRewardedAdLoadCallback)
            }
        }

    }

}