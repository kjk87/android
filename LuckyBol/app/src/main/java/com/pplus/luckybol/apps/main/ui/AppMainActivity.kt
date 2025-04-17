package com.pplus.luckybol.apps.main.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import com.igaworks.v2.core.AdBrixRm
import com.pplus.luckybol.Const
import com.pplus.luckybol.LuckyBolApplication
import com.pplus.luckybol.R
import com.pplus.luckybol.apps.common.Foreground
import com.pplus.luckybol.apps.common.mgmt.LoginInfoManager
import com.pplus.luckybol.apps.common.mgmt.SchemaManager
import com.pplus.luckybol.apps.common.ui.base.BaseActivity
import com.pplus.luckybol.apps.event.ui.LifeSaveFragment
import com.pplus.luckybol.apps.event.ui.RandomPlayFragment
import com.pplus.luckybol.core.code.common.EnumData
import com.pplus.luckybol.core.network.ApiBuilder
import com.pplus.luckybol.core.network.model.dto.PopupMange
import com.pplus.luckybol.core.network.model.dto.User
import com.pplus.luckybol.core.network.model.response.NewResultResponse
import com.pplus.luckybol.core.util.PplusCommonUtil
import com.pplus.luckybol.core.util.ToastUtil
import com.pplus.luckybol.databinding.ActivityAppMainBinding
import com.pplus.luckybol.push.PushReceiveData
import com.pplus.networks.common.PplusCallback
import com.pplus.utils.part.pref.PreferenceUtil
import com.pplus.utils.part.utils.StringUtils
import retrofit2.Call
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

        //            val manager = ReviewManagerFactory.create(this)
        //            val request = manager.requestReviewFlow()
        //            request.addOnCompleteListener { request ->
        //                if (request.isSuccessful) {
        //
        //                    // We got the ReviewInfo object
        //                    val reviewInfo = request.result
        //                    val flow = manager.launchReviewFlow(this, reviewInfo)
        //                    LogUtil.e(LOG_TAG, "addOnCompleteListener")
        //                    flow.addOnCompleteListener { _ ->
        //                        // The flow has finished. The API does not indicate whether the user
        //                        // reviewed or not, or even whether the review dialog was shown. Thus, no
        //                        // matter the result, we continue our app flow.
        //                    }
        //
        //
        //                } else {
        //                    // There was some problem, continue regardless of the result.
        //                    LogUtil.e(LOG_TAG, "requestReviewFlow: " + request.exception.toString())
        //                }
        //            }

        Foreground.get(this).addListener(object : Foreground.Listener {
            override fun onBecameForeground() {
                if (LoginInfoManager.getInstance().isMember) {
                    ApiBuilder.create().session.setCallback(object : PplusCallback<NewResultResponse<User>>{
                        override fun onResponse(call: Call<NewResultResponse<User>>?,
                                                response: NewResultResponse<User>?) {

                        }

                        override fun onFailure(call: Call<NewResultResponse<User>>?,
                                               t: Throwable?,
                                               response: NewResultResponse<User>?) {

                        }
                    }).build().call()
                }
            }

            override fun onBecameBackground() {

            }
        })

        binding.layoutMainEvent.setOnClickListener {
            setMainEventFragment()
        }

        binding.layoutMainHomeTab.setOnClickListener {
            setHomeFragment()
        }

        binding.layoutMainPlay.setOnClickListener {
            setPlayFragment()
        }

        binding.layoutMainBuffTab.setOnClickListener {
            if (!PplusCommonUtil.loginCheck(this, signInLauncher)) {
                return@setOnClickListener
            }
            setBuffFragment()
        }

        binding.layoutMainShoppingTab.setOnClickListener {
            setShoppingFragment()
        }

        binding.layoutMainGifishowTab.setOnClickListener {
            setGiftishowFragment()
        }

        //        layout_main_event_win_review_tab.setOnClickListener {
        //            setEventWinReviewFragment()
        //        }

        binding.layoutMainMyTab.setOnClickListener {
            setMyFragment()
        }

        mTabList = arrayListOf()
        mTabList?.add(binding.layoutMainHomeTab)
        mTabList?.add(binding.layoutMainPlay)
        mTabList?.add(binding.layoutMainEvent)
        mTabList?.add(binding.layoutMainGifishowTab) //        mTabList?.add(layout_main_event_win_review_tab)
        mTabList?.add(binding.layoutMainMyTab)
        mTabList?.add(binding.layoutMainBuffTab)
        mTabList?.add(binding.layoutMainShoppingTab)

        if (LoginInfoManager.getInstance().isMember) {
            setAdbrixData()
        }

        getMainPopup()

        setHomeFragment()

        onNewIntent(intent)
    }

    private fun getMainPopup() {
        val params = HashMap<String, String>()
        params["platform"] = "aos"
        ApiBuilder.create().getPopupList(params).setCallback(object : PplusCallback<NewResultResponse<PopupMange>> {

            override fun onResponse(call: Call<NewResultResponse<PopupMange>>?,
                                    response: NewResultResponse<PopupMange>?) {
                if (response?.datas != null) {
                    for (popupMange in response.datas!!) {
                        val isPopupShow = PreferenceUtil.getDefaultPreference(this@AppMainActivity).get(Const.POPUP + popupMange.seqNo, true)
                        if (isPopupShow) {
                            val intent = Intent(this@AppMainActivity, AlertMainPopupActivity::class.java)
                            intent.putExtra(Const.DATA, popupMange)
                            startActivity(intent)
                        }
                    }

                }
            }

            override fun onFailure(call: Call<NewResultResponse<PopupMange>>?,
                                   t: Throwable?,
                                   response: NewResultResponse<PopupMange>?) {

            }
        }).build().call()
    }


    fun setBuffFragment() {
        for (tab in mTabList!!) {
            tab.isSelected = false
        }
        binding.layoutMainBuffTab.isSelected = true
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.main_container, MainBuffFragment.newInstance(), MainBuffFragment::class.java.simpleName)
        ft.commit()
    }

    fun setShoppingFragment() {
        for (tab in mTabList!!) {
            tab.isSelected = false
        }
        binding.layoutMainShoppingTab.isSelected = true
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.main_container, MainShipTypeFragment.newInstance(), MainShipTypeFragment::class.java.simpleName)
        ft.commit()
    }

    fun setGiftishowFragment() {
        for (tab in mTabList!!) {
            tab.isSelected = false
        }
        binding.layoutMainGifishowTab.isSelected = true
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.main_container, MainGiftishowFragment.newInstance(), MainGiftishowFragment::class.java.simpleName)
        ft.commit()
    }

    //    fun setEventWinReviewFragment() {
    //        for (tab in mTabList!!) {
    //            tab.isSelected = false
    //        }
    //        layout_main_event_win_review_tab.isSelected = true
    //        val ft = supportFragmentManager.beginTransaction()
    //        ft.replace(R.id.main_container, MainEventReviewFragment.newInstance(), MainEventReviewFragment::class.java.simpleName)
    //        ft.commit()
    //    }

    fun setHomeFragment() {
        for (tab in mTabList!!) {
            tab.isSelected = false
        }
        binding.layoutMainHomeTab.isSelected = true

        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.main_container, HomeFragment.newInstance(), HomeFragment::class.java.simpleName)
        ft.commitNow()
    }

    fun setMainFragment() {
        for (tab in mTabList!!) {
            tab.isSelected = false
        }
        binding.layoutMainHomeTab.isSelected = true

        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.main_container, MainFragment.newInstance(), MainFragment::class.java.simpleName)
        ft.commitNow()
    }

    var mMainEventFragment: MainEventFragment? = null

    fun setMainEventFragment() {
        for (tab in mTabList!!) {
            tab.isSelected = false
        }
        binding.layoutMainEvent.isSelected = true

        val ft = supportFragmentManager.beginTransaction()
        mMainEventFragment = MainEventFragment.newInstance()
        ft.replace(R.id.main_container, mMainEventFragment!!, MainEventFragment::class.java.simpleName)
        ft.commitNow()
    }

    fun setRandomEventFragment() {
        for (tab in mTabList!!) {
            tab.isSelected = false
        } //        layout_main_random_tab.isSelected = true

        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.main_container, RandomPlayFragment.newInstance(), RandomPlayFragment::class.java.simpleName)
        ft.commitNow()
    }

    fun setLifeSaveFragment() {

        for (tab in mTabList!!) {
            tab.isSelected = false
        } //        layout_main_life_save_tab.isSelected = true

        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.main_container, LifeSaveFragment.newInstance(), LifeSaveFragment::class.java.simpleName)
        ft.commitNow()
    }

    fun setTargetEventFragment(tab: Int) {

        for (tab in mTabList!!) {
            tab.isSelected = false
        }
        when (tab) {
            0 -> {
                binding.layoutMainEvent.isSelected = true
            } //            1->{
            //                layout_main_target_event_tab2.isSelected = true
            //            }
        }


        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.main_container, MainTargetEventFragment.newInstance(tab), MainTargetEventFragment::class.java.simpleName)
        ft.commitNow()
    }

    fun setPlayFragment() {

        for (tab in mTabList!!) {
            tab.isSelected = false
        }
        binding.layoutMainPlay.isSelected = true

        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.main_container, MainLottoFragment.newInstance(), MainLottoFragment::class.java.simpleName)
        ft.commitNow()
    }


    fun setInviteFragment() {

        for (tab in mTabList!!) {
            tab.isSelected = false
        } //        layout_main_invite_tab.isSelected = true

        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.main_container, MainInviteFragment.newInstance(), MainInviteFragment::class.java.simpleName)
        ft.commitNow()
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
        ft.replace(R.id.main_container, MyInfoFragment.newInstance(), MyInfoFragment::class.java.simpleName)
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
            }
        }
    }

    private fun setSchemeData() {

        val scheme = LuckyBolApplication.getSchemaData()

        if (StringUtils.isNotEmpty(scheme)) {
        }
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
        if (LoginInfoManager.getInstance().isMember) {
            setAdbrixData()
        }

        if (binding.layoutMainEvent.isSelected) {
            mMainEventFragment?.signInLauncher
        }
    }

    val setUpProfileLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        setAdbrixData()
        PplusCommonUtil.setBuzvilProfileData()

    }

    internal var isExitBackPressed = false
    private val mHandler = Handler(Looper.myLooper()!!)

    override fun onBackPressed() {

        back()
    }

    private fun back() {
        if (isExitBackPressed) {

            for (activity in LuckyBolApplication.getActivityList()) {
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
