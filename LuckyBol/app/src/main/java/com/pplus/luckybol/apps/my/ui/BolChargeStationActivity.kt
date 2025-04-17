package com.pplus.luckybol.apps.my.ui

import android.graphics.Color
import android.os.Bundle
import android.view.View
import com.buzzvil.buzzad.benefit.BuzzAdBenefit
import com.buzzvil.buzzad.benefit.presentation.feed.BuzzAdFeed
import com.buzzvil.buzzad.benefit.privacy.PrivacyPolicyEventListener
import com.fpang.lib.FpangSession
import com.igaworks.adpopcorn.IgawAdpopcorn
import com.igaworks.adpopcorn.style.ApStyleManager
import com.pincrux.offerwall.PincruxOfferwall
import com.pplus.luckybol.Const
import com.pplus.luckybol.R
import com.pplus.luckybol.apps.common.mgmt.LoginInfoManager
import com.pplus.luckybol.apps.common.toolbar.ImplToolbar
import com.pplus.luckybol.apps.common.toolbar.OnToolbarListener
import com.pplus.luckybol.apps.common.toolbar.ToolbarOption
import com.pplus.luckybol.apps.common.ui.base.BaseActivity
import com.pplus.luckybol.core.code.common.EnumData
import com.pplus.luckybol.core.util.PplusCommonUtil
import com.pplus.luckybol.databinding.ActivityBolChargeStationBinding
import com.pplus.utils.part.utils.StringUtils
import com.tnkfactory.ad.TnkSession
import net.flexplatform.sdk.FlexTools
import java.util.*


class BolChargeStationActivity : BaseActivity(), ImplToolbar {

    private lateinit var binding: ActivityBolChargeStationBinding

    override fun getLayoutView(): View {
        binding = ActivityBolChargeStationBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun getPID(): String? {
        return "Home_Charging station"
    }

    override fun initializeView(savedInstanceState: Bundle?) {

        binding.layoutBolChargeStation1.setOnClickListener {
            openBuzvil()
        }

        binding.textBolChargeStation1Title.text = PplusCommonUtil.fromHtml(getString(R.string.html_charge_station_title))

        binding.layoutBolChargeAdpopcorn.setOnClickListener {
            openAdpopcorn()
        }

        binding.layoutBolChargeAdSync.setOnClickListener {
            openAdSync()
        }

        binding.layoutBolChargeFlex.setOnClickListener {
            openFlex()
        }
        binding.layoutBolChargeTnk.setOnClickListener {
            openTNK()
        }
        binding.layoutBolChargePincrux.setOnClickListener {
            openPincrux()
        }
    }

    private fun openBuzvil(){
        if(BuzzAdBenefit.getPrivacyPolicyManager()!!.isConsentGranted()){
            setAnalytics("Home_Buzzvil feed")
            BuzzAdFeed.Builder().build().show(this)
        }else{
            BuzzAdBenefit.getPrivacyPolicyManager()?.showConsentUI(this, object : PrivacyPolicyEventListener {
                override fun onUpdated(accepted: Boolean) {
                    if(accepted){
                        PplusCommonUtil.setBuzvilProfileData()
                        setAnalytics("Home_Buzzvil feed")
                        BuzzAdFeed.Builder().build().show(this@BolChargeStationActivity)
                    }

                }
            })
        }
    }

    private fun openAdpopcorn(){
        IgawAdpopcorn.setUserId(this, LoginInfoManager.getInstance().user.no.toString())
        val optionMap = HashMap<String, Any>()
        optionMap.put(ApStyleManager.CustomStyle.OFFERWALL_TITLE_TEXT, getString(R.string.word_bol_charge_station))
        optionMap.put(ApStyleManager.CustomStyle.OFFERWALL_THEME_COLOR, Color.parseColor("#fc5c57"))
        optionMap.put(ApStyleManager.CustomStyle.TOP_BAR_BG_COLOR, Color.parseColor("#fc5c57"))
        optionMap.put(ApStyleManager.CustomStyle.BOTTOM_BAR_BG_COLOR, Color.parseColor("#fc5c57"))
        ApStyleManager.setCustomOfferwallStyle(optionMap)
        IgawAdpopcorn.openOfferWall(this)
    }

    private fun openTNK(){
        TnkSession.setCOPPA(this, true)
        TnkSession.setUserName(this, LoginInfoManager.getInstance().user.no.toString())
        TnkSession.showAdList(this, getString(R.string.word_bol_charge_station))
    }

    private fun openFlex(){
        FlexTools.FlexAdList(this, null, LoginInfoManager.getInstance().user.no.toString(), "PORTRAIT") // 호출되는 class 명
    }

    private fun openAdSync(){
        FpangSession.init(this)
        FpangSession.setDebug(Const.API_URL.startsWith("https://stg")) // 배포시 false 로 설정

        FpangSession.setUserId(LoginInfoManager.getInstance().user.no.toString()) // 사용자 ID 설정

        //추가 정보 설정(options)
        if (StringUtils.isNotEmpty(LoginInfoManager.getInstance().user.birthday)) {
            val birth = LoginInfoManager.getInstance().user.birthday!!.substring(0, 4)
            val year = Calendar.getInstance().get(Calendar.YEAR)
            val age = year - birth.toInt()
            FpangSession.setAge(age) // 0 이면 값없음
        }else{
            FpangSession.setAge(0) // 0 이면 값없음
        }
        if (StringUtils.isNotEmpty(LoginInfoManager.getInstance().user.gender)) {

            if (LoginInfoManager.getInstance().user.gender.equals(EnumData.GenderType.male.name)) {
                FpangSession.setGender("M") // M:남자, F:여자, A:값없음
            } else {
                FpangSession.setGender("F") // M:남자, F:여자, A:값없음
            }
        }else{
            FpangSession.setGender("A") // M:남자, F:여자, A:값없음
        }

        // Activity 일 경우.
        FpangSession.showAdsyncList(this, getString(R.string.word_bol_charge_station))
    }

    private fun openPincrux(){
        val offerwall = PincruxOfferwall.getInstance()
        offerwall.init(this, "911129", LoginInfoManager.getInstance().user.no.toString())
        offerwall.startPincruxOfferwallActivity(this)
    }

    override fun getToolbarOption(): ToolbarOption {

        val toolbarOption = ToolbarOption(this)
        toolbarOption.initializeDefaultToolbar(getString(R.string.word_charge_station), ToolbarOption.ToolbarMenu.LEFT)
        return toolbarOption
    }

    override fun getOnToolbarClickListener(): OnToolbarListener {

        return OnToolbarListener { v, toolbarMenu, tag ->
            when (toolbarMenu) {
                ToolbarOption.ToolbarMenu.LEFT -> if (tag == 1) {
                    onBackPressed()
                }
                else -> {}
            }
        }
    }
}