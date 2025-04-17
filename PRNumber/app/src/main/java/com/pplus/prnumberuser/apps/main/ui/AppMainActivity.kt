package com.pplus.prnumberuser.apps.main.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.browser.customtabs.CustomTabsClient
import com.igaworks.v2.core.AdBrixRm
import com.pplus.prnumberuser.Const
import com.pplus.prnumberuser.PRNumberApplication
import com.pplus.prnumberuser.R
import com.pplus.prnumberuser.apps.common.Foreground
import com.pplus.prnumberuser.apps.common.mgmt.LoginInfoManager
import com.pplus.prnumberuser.apps.common.mgmt.SchemaManager
import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
import com.pplus.prnumberuser.apps.event.ui.TuneEventGroupAFragment
import com.pplus.prnumberuser.apps.event.ui.TuneEventGroupBFragment
import com.pplus.prnumberuser.apps.mobilegift.ui.MainGiftishowFragment
import com.pplus.prnumberuser.apps.product.ui.PurchaseHistoryActivity
import com.pplus.prnumberuser.core.chrome.customtab.CustomTabUtil
import com.pplus.prnumberuser.core.chrome.shared.ServiceConnectionCallback
import com.pplus.prnumberuser.core.code.common.EnumData
import com.pplus.prnumberuser.core.network.ApiBuilder
import com.pplus.prnumberuser.core.util.PplusCommonUtil
import com.pplus.prnumberuser.core.util.ToastUtil
import com.pplus.prnumberuser.databinding.ActivityAppMainBinding
import com.pplus.prnumberuser.push.PushReceiveData
import com.pplus.utils.part.logs.LogUtil
import com.pplus.utils.part.pref.PreferenceUtil
import com.pplus.utils.part.utils.StringUtils
import java.util.*

class AppMainActivity : BaseActivity() {
    override fun getPID(): String {
        return ""
    }

    private lateinit var binding: ActivityAppMainBinding

    override fun getLayoutView(): View {
        binding = ActivityAppMainBinding.inflate(layoutInflater)
        return binding.root
    }

    private var mPushData: PushReceiveData? = null
    private var mTabList: MutableList<View>? = null

    override fun initializeView(savedInstanceState: Bundle?) {

        //        if (PreferenceUtil.getDefaultPreference(this).get(Const.SIGN_UP, false)) {
        //            PreferenceUtil.getDefaultPreference(this).put(Const.SIGN_UP, false)
        //            val intent = Intent(this, ProfileConfigActivity::class.java)
        //            intent.putExtra(Const.SIGN_UP, true)
        //            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        //            startActivityForResult(intent, Const.REQ_SIGN_UP_PROFILE)
        //        } else {
        //            setAdbrixData()
        //        }
//        val intent = Intent(this, StampActivity::class.java)
//        intent.putExtra(Const.SIGN_UP, true)
//        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//        startActivity(intent)

        Foreground.get(this).addListener(object : Foreground.Listener {
            override fun onBecameForeground() {
                if (LoginInfoManager.getInstance().isMember) {
                    ApiBuilder.create().session.build().call()
                }
                try {
                    CustomTabUtil.unbindCustomTabsService(this@AppMainActivity)
                } catch (e: Exception) {
                }

                bindCustomTab()
            }

            override fun onBecameBackground() {

            }
        })

        //        layout_main_number_tab.setOnClickListener {
        //            setNumberFragment()
        //        }
        //        layout_main_store_tab.setOnClickListener{
        //            setMainPlusFragment()
        //        }

        binding.layoutMainEventTab.setOnClickListener {
            if (!PplusCommonUtil.loginCheck(this, signInLauncher)) {
                return@setOnClickListener
            }
            setMainGiftishowFragment()
        }

        binding.layoutMainDeliveryPageTab.setOnClickListener {
            setMainDeliveryFragment()
        }

        binding.layoutMainVisitPageTab.setOnClickListener {
            setMainPageFragment()
        }

        binding.layoutMainFirstComeTab.setOnClickListener {
            setMainPageWithProductFragment()
        }

        binding.layoutMainShoppingTab.setOnClickListener {
            setShipTypeFragment()
        }

        binding.layoutMainMyTab.setOnClickListener {
            setMyFragment()
        }

//        binding.layoutMainNumberApplyTab.setOnClickListener {
//            setNumberApplyFragment()
//        }

        binding.layoutMainPrepaymentTab.setOnClickListener {
//            if (!PplusCommonUtil.loginCheck(this, signInLauncher)) {
//                return@setOnClickListener
//            }
            setMainPrepaymentFragment()
        }

        mTabList = arrayListOf()
        mTabList?.add(binding.layoutMainEventTab)
        mTabList?.add(binding.layoutMainDeliveryPageTab)
        mTabList?.add(binding.layoutMainVisitPageTab)
        mTabList?.add(binding.layoutMainFirstComeTab)
        mTabList?.add(binding.layoutMainPrepaymentTab)
        mTabList?.add(binding.layoutMainShoppingTab)
        mTabList?.add(binding.layoutMainMyTab)
//        mTabList?.add(binding.layoutMainNumberApplyTab)

        bindCustomTab()

        if (LoginInfoManager.getInstance().isMember) {
            setAdbrixData()
        }

//        showProgress(getString(R.string.msg_getting_current_location))
//        PplusCommonUtil.alertLocation(this, false, object : PplusCommonUtil.Companion.SuccessLocationListener {
//            override fun onSuccess() {
//                hideProgress() //                setNumberFragment()
//
//            }
//        })

        onNewIntent(intent)
        setMainPageFragment()
    }

    fun setMainGiftishowFragment() {
        for (tab in mTabList!!) {
            tab.isSelected = false
        }
        binding.layoutMainEventTab.isSelected = true
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.main_container, MainGiftishowFragment.newInstance(), MainGiftishowFragment::class.java.simpleName)
        ft.commit()
    }

    fun setTuneAFragment() {
        for (tab in mTabList!!) {
            tab.isSelected = false
        }
        binding.layoutMainEventTab.isSelected = true
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.main_container, TuneEventGroupAFragment.newInstance(), TuneEventGroupAFragment::class.java.simpleName)
        ft.commit()
    }

    fun setTuneBFragment() {
        for (tab in mTabList!!) {
            tab.isSelected = false
        } //        layout_main_feed_tab.isSelected = true
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.main_container, TuneEventGroupBFragment.newInstance(), TuneEventGroupBFragment::class.java.simpleName)
        ft.commit()
    }

    fun setShipTypeFragment() {
        for (tab in mTabList!!) {
            tab.isSelected = false
        }
        binding.layoutMainShoppingTab.isSelected = true
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.main_container, MainShipTypeFragment.newInstance(), MainShipTypeFragment::class.java.simpleName)
        ft.commit()
    }

    fun setMainDeliveryFragment() {
        for (tab in mTabList!!) {
            tab.isSelected = false
        }
        binding.layoutMainDeliveryPageTab.isSelected = true
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.main_container, MainDeliveryFragment.newInstance(), MainDeliveryFragment::class.java.simpleName)
        ft.commit()
    }

    fun setMainPageFragment() {
        for (tab in mTabList!!) {
            tab.isSelected = false
        }
        binding.layoutMainVisitPageTab.isSelected = true
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.main_container, MainLocationFragment.newInstance(), MainLocationFragment::class.java.simpleName)
        ft.commit()
    }

    fun setMainPageWithProductFragment() {
        for (tab in mTabList!!) {
            tab.isSelected = false
        }
        binding.layoutMainFirstComeTab.isSelected = true
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.main_container, MainPageWithProductFragment.newInstance(), MainPageWithProductFragment::class.java.simpleName)
        ft.commit()
    }

    fun setMainPrepaymentFragment() {
        for (tab in mTabList!!) {
            tab.isSelected = false
        }
        binding.layoutMainPrepaymentTab.isSelected = true
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.main_container, MainPageWithPrepaymentFragment.newInstance(), MainPageWithPrepaymentFragment::class.java.simpleName)
        ft.commit()
    }

//    fun setMainPlusNewsFragment() {
//
//        if (!PplusCommonUtil.loginCheck(this, signInLauncher)) {
//            return
//        }
//
//        for (tab in mTabList!!) {
//            tab.isSelected = false
//        } //        layout_main_news_tab.isSelected = true
//        val ft = supportFragmentManager.beginTransaction() //        ft.replace(R.id.main_container, MainPlusNewsFragment.newInstance(), MainPlusNewsFragment::class.java.simpleName)
//        ft.replace(R.id.main_container, MainPlusFragment.newInstance(), MainPlusFragment::class.java.simpleName)
//        ft.commit()
//    }

    fun setPadFragment(key: String?, number: String?) {

        for (tab in mTabList!!) {
            tab.isSelected = false
        } //        layout_main_number_tab.isSelected = true

        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.main_container, MainPadFragment.newInstance(key, number), MainPadFragment::class.java.simpleName)
        ft.commit()
    }

    fun setNumberFragment() {

        for (tab in mTabList!!) {
            tab.isSelected = false
        } //        layout_main_number_tab.isSelected = true

        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.main_container, MainNumberFragment.newInstance(), MainNumberFragment::class.java.simpleName)
        ft.commit()
    }

    fun setMyFragment() {

        //        if(!PplusCommonUtil.loginCheck(this)){
        //            return
        //        }

        for (tab in mTabList!!) {
            tab.isSelected = false
        }
        binding.layoutMainMyTab.isSelected = true
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.main_container, MyInfoFragment.newInstance(), MyInfoFragment::class.java.simpleName) //        ft.replace(R.id.main_container, MyInfoFragment2.newInstance(), MyInfoFragment2::class.java.simpleName)
        ft.commitNow()
    }

    fun setNumberApplyFragment() {

        for (tab in mTabList!!) {
            tab.isSelected = false
        }
//        binding.layoutMainNumberApplyTab.isSelected = true
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.main_container, MainNumberApplyFragment.newInstance(), MainNumberApplyFragment::class.java.simpleName)
        ft.commitNow()
    }

    override fun onNewIntent(intent: Intent) {

        super.onNewIntent(intent)
        mPushData = intent.getParcelableExtra(Const.PUSH_DATA)
        setPushData()
        setSchemeData()
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
                Const.HISTORY -> {
                    val intent = Intent(this, PurchaseHistoryActivity::class.java)
                    intent.putExtra(Const.KEY, Const.ORDER)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                    startActivity(intent)
                }
                Const.TICKET_HISTORY -> {
                    val intent = Intent(this, PurchaseHistoryActivity::class.java)
                    intent.putExtra(Const.KEY, Const.TICKET)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                    startActivity(intent)
                }
                Const.PAD -> {
                    val number = data.getStringExtra(Const.NUMBER)
                    setPadFragment(key, number)
                }
            }
        }
    }

    private fun setSchemeData() {

        val scheme = PRNumberApplication.getSchemaData()

        if (StringUtils.isNotEmpty(scheme)) {
            if (scheme.contains(SchemaManager.SCHEMA_PRNUMBER)) {
                SchemaManager.getInstance(this).moveToSchema(scheme, false)
                PRNumberApplication.setSchemaData("")
            }
        }
    }


    fun bindCustomTab() {
        LogUtil.e(LOG_TAG, "bindCustomTab()")
        CustomTabUtil.bindCustomTabsService(this@AppMainActivity, object : ServiceConnectionCallback {
            override fun onServiceConnected(client: CustomTabsClient?) {
                LogUtil.e(LOG_TAG, "onServiceConnected")
                CustomTabUtil.mClient = client
                CustomTabUtil.mClient!!.warmup(0)
            }

            override fun onServiceDisconnected() {
                LogUtil.e(LOG_TAG, "onServiceDisconnected")
            }
        })
    }

    fun setAdbrixData() {
        AdBrixRm.login(LoginInfoManager.getInstance().user.loginId)
        if (StringUtils.isNotEmpty(LoginInfoManager.getInstance().user.gender)) {

            if (LoginInfoManager.getInstance().user.gender.equals(EnumData.GenderType.male.name)) {
                AdBrixRm.setGender(AdBrixRm.AbxGender.MALE)
            } else {
                AdBrixRm.setGender(AdBrixRm.AbxGender.FEMALE)
            }
        }

        if (StringUtils.isNotEmpty(LoginInfoManager.getInstance().user.birthday)) {
            val birth = LoginInfoManager.getInstance().user.birthday!!.substring(0, 4)
            val year = Calendar.getInstance().get(Calendar.YEAR)
            val age = year - birth.toInt()
            AdBrixRm.setAge(age)
        }
    }

    val signInLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) { // There are no request codes
            val data = result.data
            checkSignIn()
        }
    }

    private fun checkSignIn() {
        if (LoginInfoManager.getInstance().isMember) {
            setAdbrixData()
            setSchemeData()
        }
        val intent = Intent(packageName + ".sigIn")
        sendBroadcast(intent)
    }

    internal var isExitBackPressed = false
    private val mHandler = Handler()

    override fun onBackPressed() {

        back()
    }

    private fun back() {
        if (isExitBackPressed) {

            for (activity in PRNumberApplication.getActivityList()) {
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

    override fun onDestroy() { //        CustomTabUtil.unbindCustomTabsService(this)
        super.onDestroy()
    }
}
