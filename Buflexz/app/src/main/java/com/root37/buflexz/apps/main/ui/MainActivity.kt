package com.root37.buflexz.apps.main.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.activity.OnBackPressedCallback
import com.google.android.ump.ConsentDebugSettings
import com.google.android.ump.ConsentForm
import com.google.android.ump.ConsentInformation
import com.google.android.ump.ConsentRequestParameters
import com.google.android.ump.UserMessagingPlatform
import com.pplus.networks.common.PplusCallback
import com.pplus.utils.part.logs.LogUtil
import com.pplus.utils.part.pref.PreferenceUtil
import com.root37.buflexz.BuflexzApplication
import com.root37.buflexz.Const
import com.root37.buflexz.R
import com.root37.buflexz.apps.common.Foreground
import com.root37.buflexz.apps.common.builder.AlertBuilder
import com.root37.buflexz.apps.common.builder.OnAlertResultListener
import com.root37.buflexz.apps.common.builder.data.AlertData
import com.root37.buflexz.apps.common.mgmt.LoginInfoManager
import com.root37.buflexz.apps.common.ui.base.BaseActivity
import com.root37.buflexz.core.network.ApiBuilder
import com.root37.buflexz.core.network.model.dto.Device
import com.root37.buflexz.core.network.model.dto.Member
import com.root37.buflexz.core.network.model.dto.Popup
import com.root37.buflexz.core.network.model.response.ListResultResponse
import com.root37.buflexz.core.network.model.response.NewResultResponse
import com.root37.buflexz.core.util.AdmobUtil
import com.root37.buflexz.core.util.PplusCommonUtil
import com.root37.buflexz.core.util.ToastUtil
import com.root37.buflexz.databinding.ActivityMainBinding
import com.root37.buflexz.push.PushReceiveData
import retrofit2.Call

class MainActivity : BaseActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun getLayoutView(): View {
        binding = ActivityMainBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun getPID(): String? {
        return ""
    }

    private var mPushData: PushReceiveData? = null
    private var mTabList: MutableList<View>? = null
    private val mHandler = Handler(Looper.myLooper()!!)
    private var isExitBackPressed = false

    private lateinit var consentInformation: ConsentInformation

    override fun initializeView(savedInstanceState: Bundle?) {

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                back()
            }
        })

        consentInformation = UserMessagingPlatform.getConsentInformation(this)
        if (consentInformation.canRequestAds()) {
            AdmobUtil.getInstance(this@MainActivity).initAdMob()
        }else{
            // Create a ConsentRequestParameters object.
            val params = ConsentRequestParameters.Builder()
                .build()

            consentInformation.requestConsentInfoUpdate(this, params, ConsentInformation.OnConsentInfoUpdateSuccessListener {
                UserMessagingPlatform.loadAndShowConsentFormIfRequired(this@MainActivity, ConsentForm.OnConsentFormDismissedListener { loadAndShowError -> // Consent gathering failed.
                    LogUtil.e(LOG_TAG, String.format("%s: %s", loadAndShowError?.errorCode, loadAndShowError?.message))

                    // Consent has been gathered.
                    if (consentInformation.canRequestAds()) {
                        AdmobUtil.getInstance(this@MainActivity).initAdMob()
                    }

                })
            }, ConsentInformation.OnConsentInfoUpdateFailureListener { requestConsentError -> // Consent gathering failed.
                LogUtil.e(LOG_TAG, String.format("%s: %s", requestConsentError.errorCode, requestConsentError.message))
            })
        }

        Foreground.get(this).addListener(object : Foreground.Listener {
            override fun onBecameForeground() {
                if (LoginInfoManager.getInstance().isMember()) {
                    ApiBuilder.create().getSession().setCallback(object : PplusCallback<NewResultResponse<Member>> {
                        override fun onResponse(call: Call<NewResultResponse<Member>>?, response: NewResultResponse<Member>?) {

                        }

                        override fun onFailure(call: Call<NewResultResponse<Member>>?, t: Throwable?, response: NewResultResponse<Member>?) {

                        }
                    }).build().call()
                }
            }

            override fun onBecameBackground() {

            }
        })

        binding.layoutMainLotteryTab.setOnClickListener {
            setLotteryFragment()
        }

        binding.layoutMainLuckyDrawTab.setOnClickListener {
            setLuckyDrawFragment()
        }

        binding.layoutMainGoodsShopTab.setOnClickListener {
            setProductFragment()
        }

        binding.layoutMainMyTab.setOnClickListener {
            setMyFragment()
        }

        mTabList = arrayListOf()
        mTabList?.add(binding.layoutMainLotteryTab)
        mTabList?.add(binding.layoutMainLuckyDrawTab)
        mTabList?.add(binding.layoutMainGoodsShopTab)
        mTabList?.add(binding.layoutMainMyTab)

        getDevice()

        setLotteryFragment()
        getPopupList()
    }

    private fun getDevice() {
        val params = HashMap<String, String>()
        params["deviceId"] = PplusCommonUtil.getDeviceID()
        ApiBuilder.create().getDevice(params).setCallback(object : PplusCallback<NewResultResponse<Device>> {
            override fun onResponse(call: Call<NewResultResponse<Device>>?, response: NewResultResponse<Device>?) {
                val device: Device

                var isFirst = PreferenceUtil.getDefaultPreference(this@MainActivity).get(Const.IS_FIRST, true)

                if (response?.result != null) {
                    device = response.result!!
                    device.pushId = PreferenceUtil.getDefaultPreference(this@MainActivity).getString(Const.PUSH_TOKEN)
                } else {
                    device = Device()
                    device.deviceId = PplusCommonUtil.getDeviceID()
                    device.pushId = PreferenceUtil.getDefaultPreference(this@MainActivity).getString(Const.PUSH_TOKEN)
                    device.pushActivate = false
                    isFirst = true
                }

                if (isFirst) {
                    PreferenceUtil.getDefaultPreference(this@MainActivity).put(Const.IS_FIRST, false)
                    val builder = AlertBuilder.Builder()
                    builder.setTitle(getString(R.string.msg_alarm_agree_title))
                    builder.addContents(AlertData.MessageData(getString(R.string.msg_alarm_agree_desc), AlertBuilder.MESSAGE_TYPE.TEXT, 2))
                    builder.setLeftText(getString(R.string.word_dis_agree)).setRightText(getString(R.string.word_agree))
                    builder.setOnAlertResultListener(object : OnAlertResultListener {
                        override fun onCancel() {

                        }

                        override fun onResult(event_alert: AlertBuilder.EVENT_ALERT?) {
                            when (event_alert) {
                                AlertBuilder.EVENT_ALERT.RIGHT -> {
                                    device.pushActivate = true
                                }

                                else -> {
                                    device.pushActivate = false
                                }
                            }
                            postDevice(device)
                        }
                    }).builder().show(this@MainActivity)
                } else {
                    postDevice(device)
                }
            }

            override fun onFailure(call: Call<NewResultResponse<Device>>?, t: Throwable?, response: NewResultResponse<Device>?) {

            }
        }).build().call()
    }

    private fun postDevice(device: Device) {
        ApiBuilder.create().postDevice(device).setCallback(object : PplusCallback<NewResultResponse<Device>> {
            override fun onResponse(call: Call<NewResultResponse<Device>>?, response: NewResultResponse<Device>?) {

            }

            override fun onFailure(call: Call<NewResultResponse<Device>>?, t: Throwable?, response: NewResultResponse<Device>?) {

            }
        }).build().call()
    }

    private fun getPopupList() {
        val params = HashMap<String, String>()
        params["aos"] = "1"
        ApiBuilder.create().getPopupList(params).setCallback(object : PplusCallback<NewResultResponse<ListResultResponse<Popup>>> {

            override fun onResponse(call: Call<NewResultResponse<ListResultResponse<Popup>>>?, response: NewResultResponse<ListResultResponse<Popup>>?) {
                if (response?.result != null && response.result!!.list != null) {
                    for (popup in response.result!!.list!!) {

                        when (popup.closeType) {
                            1 -> {
                                val intent = Intent(this@MainActivity, AlertMainPopupActivity::class.java)
                                intent.putExtra(Const.DATA, popup)
                                startActivity(intent)
                            }

                            2 -> {
                                val isPopupShow = PreferenceUtil.getDefaultPreference(this@MainActivity).get(Const.POPUP + popup.seqNo, true)
                                if (isPopupShow) {
                                    val intent = Intent(this@MainActivity, AlertMainPopupActivity::class.java)
                                    intent.putExtra(Const.DATA, popup)
                                    startActivity(intent)
                                }
                            }
                        }


                    }
                }
            }

            override fun onFailure(call: Call<NewResultResponse<ListResultResponse<Popup>>>?, t: Throwable?, response: NewResultResponse<ListResultResponse<Popup>>?) {

            }
        }).build().call()
    }

    fun setLotteryFragment() {
        for (tab in mTabList!!) {
            tab.isSelected = false
        }
        binding.layoutMainLotteryTab.isSelected = true

        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.main_container, MainLotteryFragment.newInstance(), MainLotteryFragment::class.java.simpleName)
        ft.commitNow()
    }

    fun setLuckyDrawFragment() {
        for (tab in mTabList!!) {
            tab.isSelected = false
        }
        binding.layoutMainLuckyDrawTab.isSelected = true

        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.main_container, MainLuckyDrawFragment.newInstance(), MainLuckyDrawFragment::class.java.simpleName)
        ft.commitNow()
    }

    fun setProductFragment() {
        for (tab in mTabList!!) {
            tab.isSelected = false
        }
        binding.layoutMainGoodsShopTab.isSelected = true

        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.main_container, MainProductFragment.newInstance(), MainProductFragment::class.java.simpleName)
        ft.commitNow()
    }

    fun setMyFragment() {
        for (tab in mTabList!!) {
            tab.isSelected = false
        }
        binding.layoutMainMyTab.isSelected = true

        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.main_container, MainMyFragment.newInstance(), MainMyFragment::class.java.simpleName)
        ft.commitNow()
    }

    private fun back() {
        if (isExitBackPressed) {

            for (activity in BuflexzApplication.getActivityList()) {
                if (!activity.isFinishing) {
                    activity.finish()
                }
            }

            if (!isFinishing) {
                finish()
            }

        } else {
            isExitBackPressed = true

            ToastUtil.show(this, getString(R.string.msg_quit))

            mHandler.postDelayed({
                isExitBackPressed = false
            }, 3000)
        }
    }
}