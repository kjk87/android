package com.lejel.wowbox.apps.main.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.google.android.ump.ConsentInformation
import com.lejel.wowbox.Const
import com.lejel.wowbox.R
import com.lejel.wowbox.WowBoxApplication
import com.lejel.wowbox.apps.common.Foreground
import com.lejel.wowbox.apps.common.builder.AlertBuilder
import com.lejel.wowbox.apps.common.builder.OnAlertResultListener
import com.lejel.wowbox.apps.common.builder.data.AlertData
import com.lejel.wowbox.apps.common.mgmt.LoginInfoManager
import com.lejel.wowbox.apps.common.mgmt.SchemaManager
import com.lejel.wowbox.apps.common.ui.base.BaseActivity
import com.lejel.wowbox.apps.event.ui.EventFragment
import com.lejel.wowbox.apps.event.ui.EventWinFragment
import com.lejel.wowbox.apps.event.ui.PlayFragment
import com.lejel.wowbox.apps.luckybox.ui.LuckyBoxFragment
import com.lejel.wowbox.apps.luckybox.ui.LuckyBoxWinFragment
import com.lejel.wowbox.core.network.ApiBuilder
import com.lejel.wowbox.core.network.model.dto.Config
import com.lejel.wowbox.core.network.model.dto.Device
import com.lejel.wowbox.core.network.model.dto.Member
import com.lejel.wowbox.core.network.model.dto.Popup
import com.lejel.wowbox.core.network.model.response.ListResultResponse
import com.lejel.wowbox.core.network.model.response.NewResultResponse
import com.lejel.wowbox.core.util.AdmobUtil
import com.lejel.wowbox.core.util.PplusCommonUtil
import com.lejel.wowbox.core.util.ToastUtil
import com.lejel.wowbox.databinding.ActivityMainBinding
import com.lejel.wowbox.push.PushReceiveData
import com.pplus.networks.common.PplusCallback
import com.pplus.utils.part.pref.PreferenceUtil
import com.pplus.utils.part.utils.StringUtils
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

        checkPermission()

        //        AdmobUtil.getInstance(this).initRewardAd()
        AdmobUtil.getInstance(this).initAdMob()

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

        binding.layoutMainPlayTab.setOnClickListener {
            setPlayFragment()
        }

        binding.layoutMainLuckyDrawTab.setOnClickListener {
            setLuckyDrawFragment()
        }

        binding.layoutMainEventTab.setOnClickListener {
            setEventFragment()
        }

        binding.layoutMainRewardTab.setOnClickListener {
            setRewardFragment()
        }
        binding.layoutMainPointMallTab.setOnClickListener {
            setPointMallFragment()
        }

        binding.layoutMainMyTab.setOnClickListener {
            setMyFragment()
        }

        mTabList = arrayListOf()
        mTabList?.add(binding.layoutMainPlayTab)
        mTabList?.add(binding.layoutMainLuckyDrawTab)
        mTabList?.add(binding.layoutMainEventTab)
        mTabList?.add(binding.layoutMainRewardTab)
        mTabList?.add(binding.layoutMainPointMallTab)
        mTabList?.add(binding.layoutMainMyTab)

        getDevice()

        setPlayFragment()
        getPopupList()
        onNewIntent(intent)
    }

    private fun checkPermission() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val checkPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_DENIED
            val isFirst = PreferenceUtil.getDefaultPreference(this).get(Const.IS_FIRST_PERMISSION, true)
            if (checkPermission && isFirst) {
                PreferenceUtil.getDefaultPreference(this).put(Const.IS_FIRST_PERMISSION, false)
                permissionLauncher.launch(arrayOf(Manifest.permission.POST_NOTIFICATIONS))
            }
        }
    }

    //    private fun getWowBallConfig(){
    //        ApiBuilder.create().getConfig("wowBall").setCallback(object : PplusCallback<NewResultResponse<Config>>{
    //            override fun onResponse(call: Call<NewResultResponse<Config>>?, response: NewResultResponse<Config>?) {
    //                if(response?.result != null){
    //                    val config = response.result!!.config!!
    //                    if(config == "on"){
    //                        binding.layoutMainLuckyDrawTab.visibility = View.VISIBLE
    //                    }else{
    //                        binding.layoutMainLuckyDrawTab.visibility = View.GONE
    //                    }
    //                }else{
    //                    binding.layoutMainLuckyDrawTab.visibility = View.GONE
    //                }
    //            }
    //
    //            override fun onFailure(call: Call<NewResultResponse<Config>>?, t: Throwable?, response: NewResultResponse<Config>?) {
    //                binding.layoutMainLuckyDrawTab.visibility = View.GONE
    //            }
    //        }).build().call()
    //    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)

        mPushData = PplusCommonUtil.getParcelableExtra(intent, Const.PUSH_DATA, PushReceiveData::class.java)
        setPushData()
        setKeyMove(intent)
    }

    private fun setPushData() {

        if (mPushData != null) {
            PreferenceUtil.getDefaultPreference(this).put(Const.PUSH_ID, 0)
            val schema = SchemaManager.getInstance(this).setSchemaData(mPushData)
            SchemaManager.getInstance(this).moveToSchema(schema, true)
        }
    }

    private fun setKeyMove(data: Intent) {
        val key = data.getStringExtra(Const.KEY)
        if (StringUtils.isNotEmpty(key)) {
            when (key) {
                Const.POINT_MALL -> {
                    setPointMallFragment()
                }
                Const.EVENT -> {
                    setEventFragment()
                }
            }
        }
    }


    private fun getDevice() {
        val params = HashMap<String, String>()
        params["deviceId"] = PplusCommonUtil.getDeviceID()
        ApiBuilder.create().getDevice(params).setCallback(object : PplusCallback<NewResultResponse<Device>> {
            override fun onResponse(call: Call<NewResultResponse<Device>>?, response: NewResultResponse<Device>?) {
                val device: Device
                if (response?.result != null) {
                    device = response.result!!
                    device.pushId = PreferenceUtil.getDefaultPreference(this@MainActivity).getString(Const.PUSH_TOKEN)
                } else {
                    device = Device()
                    device.deviceId = PplusCommonUtil.getDeviceID()
                    device.pushId = PreferenceUtil.getDefaultPreference(this@MainActivity).getString(Const.PUSH_TOKEN)
                    device.pushActivate = true
                }

                postDevice(device)
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

    fun setLuckyBoxFragment() {
        for (tab in mTabList!!) {
            tab.isSelected = false
        }
//        binding.layoutMainWowBoxTab.isSelected = true
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.main_container, LuckyBoxFragment.newInstance(), LuckyBoxFragment::class.java.simpleName)
        ft.commitNow()
    }

    fun setLLuckyBoxWinFragment() {
        for (tab in mTabList!!) {
            tab.isSelected = false
        } //        binding.layoutMainPrizeTab.isSelected = true

        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.main_container, LuckyBoxWinFragment.newInstance(), LuckyBoxWinFragment::class.java.simpleName)
        ft.commitNow()
    }

    fun setRewardFragment() {
        for (tab in mTabList!!) {
            tab.isSelected = false
        }
        binding.layoutMainRewardTab.isSelected = true

        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.main_container, MainRewardFragment.newInstance(), MainRewardFragment::class.java.simpleName)
        ft.commitNow()
    }

    fun setPointMallFragment() {
        for (tab in mTabList!!) {
            tab.isSelected = false
        }
        binding.layoutMainPointMallTab.isSelected = true

        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.main_container, MainPointMallFragment.newInstance(), MainPointMallFragment::class.java.simpleName)
        ft.commitNow()
    }

    fun setPlayFragment() {
        for (tab in mTabList!!) {
            tab.isSelected = false
        }
        binding.layoutMainPlayTab.isSelected = true

        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.main_container, PlayFragment.newInstance(), PlayFragment::class.java.simpleName)
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

    fun setEventFragment() {
        for (tab in mTabList!!) {
            tab.isSelected = false
        }
        binding.layoutMainEventTab.isSelected = true

        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.main_container, EventFragment.newInstance(), EventFragment::class.java.simpleName)
        ft.commitNow()
    }

    fun setEventWinFragment() {
        for (tab in mTabList!!) {
            tab.isSelected = false
        }
//        binding.layoutMainEventWinTab.isSelected = true

        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.main_container, EventWinFragment.newInstance(), EventWinFragment::class.java.simpleName)
        ft.commitNow()
    }

    fun setMiningFragment() {
        for (tab in mTabList!!) {
            tab.isSelected = false
        } //        binding.layoutMainMiningTab.isSelected = true

        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.main_container, MainMiningFragment.newInstance(), MainMiningFragment::class.java.simpleName)
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

            for (activity in WowBoxApplication.getActivityList()) {
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

    private val permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { results ->
        var denied = false
        results.forEach {
            if (!it.value) {
                denied = true
            }
        }

        if (denied) {
            val builder = AlertBuilder.Builder()
            builder.setStyle_alert(AlertBuilder.STYLE_ALERT.MESSAGE)
            builder.addContents(AlertData.MessageData(getString(R.string.msg_alert_notification_permission_denied), AlertBuilder.MESSAGE_TYPE.TEXT, 4))
            builder.setRightText(getString(R.string.word_confirm))
            builder.setOnAlertResultListener(object : OnAlertResultListener {

                override fun onCancel() {

                }

                override fun onResult(event_alert: AlertBuilder.EVENT_ALERT) {

                }
            })
            builder.builder().show(this)
        }

    }

}