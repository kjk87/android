//package com.pplus.prnumberuser.apps.main.ui
//
//import android.content.Intent
//import android.net.Uri
//import android.os.Bundle
//import android.os.Handler
//import android.support.customtabs.CustomTabsClient
//import android.view.View
//import com.igaworks.IgawCommon
//import com.pplus.utils.part.logs.LogUtil
//import com.pplus.utils.part.pref.PreferenceUtil
//import com.pplus.utils.part.utils.StringUtils
//import com.pplus.prnumberuser.Const
//import com.pplus.prnumberuser.PRNumberApplication
//import com.pplus.prnumberuser.R
//import com.pplus.prnumberuser.apps.common.Foreground
//import com.pplus.prnumberuser.apps.common.mgmt.LoginInfoManager
//import com.pplus.prnumberuser.apps.common.mgmt.SchemaManager
//import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
//import com.pplus.prnumberuser.apps.goods.ui.BuyHistoryActivity
//import com.pplus.prnumberuser.apps.setting.ui.ProfileConfigActivity
//import com.pplus.prnumberuser.core.chrome.customtab.CustomTabUtil
//import com.pplus.prnumberuser.core.chrome.shared.ServiceConnectionCallback
//import com.pplus.prnumberuser.core.code.common.EnumData
//import com.pplus.prnumberuser.core.code.common.SnsTypeCode
//import com.pplus.prnumberuser.core.network.ApiBuilder
//import com.pplus.prnumberuser.core.util.ToastUtil
//import com.pplus.prnumberuser.push.PushReceiveData
//import kotlinx.android.synthetic.main.activity_app_main2.*
//import java.util.*
//
//class AppMainActivity2 : BaseActivity() {
//    override fun getPID(): String {
//        return ""
//    }
//
//    override fun getLayoutRes(): Int {
//        return R.layout.activity_app_main2
//    }
//
//    private var mPushData: PushReceiveData? = null
//    private var mIsCalledLocation = false
//
//    override fun initializeView(savedInstanceState: Bundle?) {
//
//        if (PreferenceUtil.getDefaultPreference(this).get(Const.SIGN_UP, false)) {
//            PreferenceUtil.getDefaultPreference(this).put(Const.SIGN_UP, false)
//            val intent = Intent(this, ProfileConfigActivity::class.java)
//            intent.putExtra(Const.SIGN_UP, true)
//            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//            startActivityForResult(intent, Const.REQ_SIGN_UP_PROFILE)
//        } else {
//            setAdbrixData()
//        }
//
//        if (!LoginInfoManager.getInstance().user.accountType.equals(SnsTypeCode.pplus.name)) {
//            val intent = Intent(this, SnsAccountAlertActivity::class.java)
//            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//            startActivity(intent)
//        }
//
//        Foreground.get(this).addListener(object : Foreground.Listener {
//            override fun onBecameForeground() {
//                ApiBuilder.create().session.build().call()
//                CustomTabUtil.unbindCustomTabsService(this@AppMainActivity2)
//                bindCustomTab()
//            }
//
//            override fun onBecameBackground() {
//
//            }
//        })
//
//        mPushData = intent.getParcelableExtra<PushReceiveData>(Const.PUSH_DATA)
//
//        layout_main_pr_tab.setOnClickListener {
//            if (!layout_main_pr_tab.isSelected) {
//                setPREventFragment()
//            }
//        }
//
//        layout_main_get_bol_tab.setOnClickListener {
//            if (!layout_main_get_bol_tab.isSelected) {
//                setGetBolFragment()
//            }
//        }
//
//        layout_main_number_event_tab.setOnClickListener {
//            if (!layout_main_number_event_tab.isSelected) {
//                setNumberEventFragment()
//            }
//        }
//
//        layout_main_lotto_event_tab.setOnClickListener {
//            if (!layout_main_lotto_event_tab.isSelected) {
//                setLottoEventFragment()
//            }
//        }
//
////        layout_main_friend_tab.setOnClickListener {
////            if (!layout_main_friend_tab.isSelected) {
////                setFriendFragment()
////            }
////        }
//
//        layout_main_my_tab.setOnClickListener {
//            if (!layout_main_my_tab.isSelected) {
//                setMyInfoFragment()
//            }
//        }
//
//
//        onNewIntent(intent)
//
//        bindCustomTab()
//    }
//
//    fun bindCustomTab() {
//        CustomTabUtil.bindCustomTabsService(this@AppMainActivity2, object : ServiceConnectionCallback {
//            override fun onServiceConnected(client: CustomTabsClient?) {
//                LogUtil.e(LOG_TAG, "onServiceConnected")
//                CustomTabUtil.mClient = client
//                CustomTabUtil.mClient!!.warmup(0)
//            }
//
//            override fun onServiceDisconnected() {
//                LogUtil.e(LOG_TAG, "onServiceDisconnected")
//            }
//        })
//    }
//
//    private fun runOtherApp(packageName: String) {
//
//        var intent = packageManager.getLaunchIntentForPackage(packageName)
//        if (intent != null) {
//            // We found the activity now start the activity
//            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
//            startActivity(intent)
//        } else {
//            // Bring user to the market or let them choose an app?
//            intent = Intent(Intent.ACTION_VIEW)
//            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
//            intent.data = Uri.parse("market://details?id=$packageName")
//            startActivity(intent)
//        }
//    }
//
//    fun setAdbrixData() {
//        IgawCommon.setUserId(this, LoginInfoManager.getInstance().user.no.toString())
//        if (StringUtils.isNotEmpty(LoginInfoManager.getInstance().user.gender)) {
//
//            if (LoginInfoManager.getInstance().user.gender.equals(EnumData.GenderType.male.name)) {
//                IgawCommon.setGender(IgawCommon.Gender.MALE)
//            } else {
//                IgawCommon.setGender(IgawCommon.Gender.FEMALE)
//            }
//        }
//
//        if (StringUtils.isNotEmpty(LoginInfoManager.getInstance().user.birthday)) {
//            val birth = LoginInfoManager.getInstance().user.birthday!!.substring(0, 4)
//            val year = Calendar.getInstance().get(Calendar.YEAR)
//            val age = year - birth.toInt()
//            IgawCommon.setAge(age)
//        }
//    }
//
//    fun setSelect(view1: View, view2: View, view3: View, view4: View, view5: View) {
//        view1.isSelected = true
//        view2.isSelected = false
//        view3.isSelected = false
//        view4.isSelected = false
//        view5.isSelected = false
//    }
//
//    fun setGetBolFragment() {
//        setSelect(layout_main_get_bol_tab, layout_main_lotto_event_tab, layout_main_pr_tab, layout_main_my_tab, layout_main_number_event_tab)
//        val ft = supportFragmentManager.beginTransaction()
//        ft.replace(R.id.main_container2, MainHomeFragment2.newInstance(), MainHomeFragment2::class.java.simpleName)
////        ft.replace(R.id.main_container2, MainHomeFragment.newInstance(), MainHomeFragment::class.java.simpleName)
//        ft.commitNow()
//    }
//
//    fun setPREventFragment() {
//        setSelect(layout_main_pr_tab, layout_main_get_bol_tab, layout_main_lotto_event_tab, layout_main_my_tab, layout_main_number_event_tab)
//        val ft = supportFragmentManager.beginTransaction()
//        ft.replace(R.id.main_container2, PREventFragment.newInstance(), PREventFragment::class.java.simpleName)
//        ft.commitNow()
//    }
//
////    fun setPlayFragment() {
////        setSelect(layout_main_play_tab, layout_main_event_tab, layout_main_home_tab, layout_main_my_tab, layout_main_number_event_tab)
////        val ft = supportFragmentManager.beginTransaction()
////        ft.replace(R.id.main_container2, PlayFragment.newInstance(), PlayFragment::class.java.simpleName)
////        ft.commitNow()
////    }
//
//    fun setNumberEventFragment() {
//        setSelect(layout_main_number_event_tab, layout_main_lotto_event_tab, layout_main_get_bol_tab, layout_main_pr_tab, layout_main_my_tab)
//        val ft = supportFragmentManager.beginTransaction()
//        ft.replace(R.id.main_container2, NumberEventFragment.newInstance(), NumberEventFragment::class.java.simpleName)
//        ft.commitNow()
//    }
//
//    fun setLottoEventFragment() {
//        setSelect(layout_main_lotto_event_tab, layout_main_get_bol_tab, layout_main_pr_tab, layout_main_my_tab, layout_main_number_event_tab)
//        val ft = supportFragmentManager.beginTransaction()
//        ft.replace(R.id.main_container2, LottoEventFragment.newInstance(), LottoEventFragment::class.java.simpleName)
//        ft.commitNow()
//    }
//
////    fun setFriendFragment() {
////        setSelect(layout_main_friend_tab, layout_main_lotto_event_tab, layout_main_get_bol_tab, layout_main_my_tab, layout_main_number_event_tab)
////        val ft = supportFragmentManager.beginTransaction()
////        ft.replace(R.id.main_container2, MainFriendFragment.newInstance(), MainFriendFragment::class.java.simpleName)
////        ft.commitNow()
////    }
//
//    private fun setMyInfoFragment() {
//        setSelect(layout_main_my_tab, layout_main_lotto_event_tab, layout_main_get_bol_tab, layout_main_pr_tab, layout_main_number_event_tab)
//        val ft = supportFragmentManager.beginTransaction()
//        ft.replace(R.id.main_container2, MyInfoFragment.newInstance(), MyInfoFragment::class.java.simpleName)
//        ft.commitNow()
//    }
//
//    override fun onNewIntent(intent: Intent) {
//
//        super.onNewIntent(intent)
//        mPushData = intent.getParcelableExtra(Const.PUSH_DATA)
//        setPushData()
//        setNumberData(intent)
//        setGoodsHistory(intent)
//        setSchemeData()
//        val key = intent.getStringExtra(Const.KEY)
//        if(key == Const.LOTTO){
//            setLottoEventFragment()
//        }else{
//            setPREventFragment()
//        }
//    }
//
//    fun setNumberData(data: Intent) {
//        val key = data.getStringExtra(Const.KEY)
//        val number = data.getStringExtra(Const.NUMBER)
//
//        if (StringUtils.isNotEmpty(key)) {
//            if (key == Const.OUT_GOING || key == Const.PAD) {
//            }
//        }
//    }
//
//    private fun setGoodsHistory(data: Intent) {
//        val key = data.getStringExtra(Const.KEY)
//        if (StringUtils.isNotEmpty(key) && key == Const.GOODS_HISTORY) {
//            setMyInfoFragment()
//
//            val intent = Intent(this, BuyHistoryActivity::class.java)
//            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//            startActivity(intent)
//        }
//    }
//
//    private fun setPushData() {
//
//        if (mPushData != null) {
//            PreferenceUtil.getDefaultPreference(this).put(Const.PUSH_ID, 0)
//            val schema = SchemaManager.getInstance(this).setSchemaData(mPushData)
//            SchemaManager.getInstance(this).moveToSchema(schema, true)
//        }
//    }
//
//    private fun setSchemeData() {
//
//        val scheme = PRNumberApplication.getSchemaData()
//
//        if (StringUtils.isNotEmpty(scheme)) {
//            if (scheme.contains(SchemaManager.SCHEMA_PRNUMBER)) {
//                SchemaManager.getInstance(this).moveToSchema(scheme, false)
//                PRNumberApplication.setSchemaData(null)
//            }
//        }
//    }
//
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        when (requestCode) {
//            Const.REQ_LOCATION_CODE -> {
////                PplusCommonUtil.alertLocation(this, true, object : PplusCommonUtil.Companion.SuccessLocationListener {
////                    override fun onSuccess() {
////                        PplusCommonUtil.callAddress(LocationUtil.getSpecifyLocationData(), null)
////                        setSelect(layout_main_pr_tab, layout_main_plus_goods_tab, layout_main_event_tab, layout_main_my_store_tab, layout_main_my_tab)
////                        setPRFragment(MainPRFragment.Companion.Tab.store, false)
////                    }
////                })
//            }
//            Const.REQ_SIGN_UP_PROFILE -> {
////                val intent = Intent(this, GuideWebActivity::class.java)
////                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
////                startActivity(intent)
//                setAdbrixData()
////                PplusCommonUtil.getSession(object : PplusCommonUtil.Companion.ReloadListener {
////                    override fun reload() {
////                        checkAlaramCount()
////                    }
////                })
//            }
//        }
//    }
//
//    internal var isExitBackPressed = false
//    private val mHandler = Handler()
//
//    override fun onBackPressed() {
//
//        back()
//    }
//
//    private fun back() {
//        if (isExitBackPressed) {
//
//            for (activity in PRNumberApplication.getActivityList()) {
//                activity.finish()
//            }
//
//            if (!isFinishing) {
//                finish()
//            }
//
//        } else {
//            isExitBackPressed = true
//
//            ToastUtil.show(this, getString(R.string.msg_quit))
//
//            mHandler.postDelayed({
//                // TODO Auto-generated method stub
//                isExitBackPressed = false
//            }, 3000)
//        }
//    }
//
//    override fun onDestroy() {
//        super.onDestroy()
//        CustomTabUtil.unbindCustomTabsService(this)
//    }
//}
