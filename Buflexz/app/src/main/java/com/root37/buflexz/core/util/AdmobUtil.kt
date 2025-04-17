package com.root37.buflexz.core.util

import android.content.Context
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.root37.buflexz.Const
import com.root37.buflexz.apps.common.ui.base.BaseActivity
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
    var mIsLoaded = false
    var mContext:Context? = null
    var listener: OnLoadListener? = null

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



    fun initAdMob() {
        if(mIsLoaded){
            listener?.onLoaded()
        }else{
            val adRequest = AdRequest.Builder().build()
            InterstitialAd.load(mContext!!, Const.ADMOB_INTERSTITIAL_ID, adRequest, mAdLoadCallback)
//            InterstitialAd.load(mContext!!, "ca-app-pub-3940256099942544/1033173712", adRequest, mAdLoadCallback)
        }
    }

}