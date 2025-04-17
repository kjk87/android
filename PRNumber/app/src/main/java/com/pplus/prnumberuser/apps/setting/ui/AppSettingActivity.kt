package com.pplus.prnumberuser.apps.setting.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.pplus.networks.common.PplusCallback
import com.pplus.prnumberuser.Const
import com.pplus.prnumberuser.R
import com.pplus.prnumberuser.apps.common.builder.AlertBuilder
import com.pplus.prnumberuser.apps.common.builder.OnAlertResultListener
import com.pplus.prnumberuser.apps.common.builder.data.AlertData
import com.pplus.prnumberuser.apps.common.mgmt.LoginInfoManager
import com.pplus.prnumberuser.apps.common.toolbar.ImplToolbar
import com.pplus.prnumberuser.apps.common.toolbar.OnToolbarListener
import com.pplus.prnumberuser.apps.common.toolbar.ToolbarOption
import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
import com.pplus.prnumberuser.apps.common.ui.common.WebViewActivity
import com.pplus.prnumberuser.apps.signin.ui.SnsLoginActivity
import com.pplus.prnumberuser.core.network.ApiBuilder
import com.pplus.prnumberuser.core.network.model.dto.Terms
import com.pplus.prnumberuser.core.network.model.response.NewResultResponse
import com.pplus.prnumberuser.core.util.PplusCommonUtil
import com.pplus.prnumberuser.databinding.ActivityAppSettingBinding
import com.pplus.prnumberuser.databinding.ItemAppSettingCBinding
import com.pplus.prnumberuser.databinding.ItemAppSettingHBinding
import com.pplus.utils.part.logs.LogUtil
import retrofit2.Call
import java.util.*

/**
 * 앱 설정
 */
class AppSettingActivity : BaseActivity(), ImplToolbar {

    private var isClickTerms = false

    override fun getPID(): String {

        return "Main_mypage_setting"
    }

    private lateinit var binding: ActivityAppSettingBinding

    override fun getLayoutView(): View {
        binding = ActivityAppSettingBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun initializeView(savedInstanceState: Bundle?) {

        val headers = resources.getStringArray(R.array.setting)
        val childList = ArrayList<Array<String>>()

        childList.add(resources.getStringArray(R.array.setting_account))
//        childList.add(resources.getStringArray(R.array.setting_pay))
        childList.add(resources.getStringArray(R.array.setting_config_info))
        childList.add(resources.getStringArray(R.array.setting_cs))
        childList.add(resources.getStringArray(R.array.setting_alarm))

        for (i in headers.indices) {

            //header 추가
            val appSettingHBinding = ItemAppSettingHBinding.inflate(layoutInflater, LinearLayout(this), false)
            appSettingHBinding.textAppSettingHeaderTitle.text = headers[i]
            binding.layoutSettingMenu.addView(appSettingHBinding.root)

            for (j in 0 until childList[i].size) {

                if (j != 0) {
                    val lineView = View(this)
                    lineView.layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, resources.getDimensionPixelSize(R.dimen.height_2))
                    lineView.setBackgroundColor(ContextCompat.getColor(this, R.color.color_19000000))
                    binding.layoutSettingMenu.addView(lineView)
                }

                //child추가
                val appSettingCBinding = ItemAppSettingCBinding.inflate(layoutInflater, LinearLayout(this), false)

                if (i == 1 && j == 1) {
                    appSettingCBinding.textAppSettingConfigValue.setTextColor(ContextCompat.getColor(this, R.color.color_579ffb))
                    try {
                        val pinfo = packageManager.getPackageInfo(packageName, 0)

                        appSettingCBinding.textAppSettingConfigValue.text = pinfo.versionName

                    } catch (e: Exception) {

                    }

                }
                appSettingCBinding.textAppSettingChildTitle.text = childList[i][j]
                appSettingCBinding.root.id = Integer.parseInt(i.toString() + "" + j)
                appSettingCBinding.root.setOnClickListener(mSettingClickListener)
                binding.layoutSettingMenu.addView(appSettingCBinding.root)
            }
        }

        val lineView = View(this)
        lineView.layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, resources.getDimensionPixelSize(R.dimen.height_2))
        lineView.setBackgroundColor(ContextCompat.getColor(this, R.color.color_19000000))
        binding.layoutSettingMenu.addView(lineView)


        if(LoginInfoManager.getInstance().isMember){
            binding.textSignOut.setText(R.string.word_signOut)
        }else{
            binding.textSignOut.setText(R.string.word_signIn)
        }

        binding.textSignOut.setOnClickListener {
            if(LoginInfoManager.getInstance().isMember){
                val builder = AlertBuilder.Builder()
                builder.setTitle(getString(R.string.word_notice_alert))
                builder.addContents(AlertData.MessageData(getString(R.string.msg_question_logout), AlertBuilder.MESSAGE_TYPE.TEXT, 1))
                builder.setLeftText(getString(R.string.word_cancel)).setRightText(getString(R.string.word_confirm))
                builder.setOnAlertResultListener(object : OnAlertResultListener {

                    override fun onCancel() {

                    }

                    override fun onResult(event_alert: AlertBuilder.EVENT_ALERT) {

                        when (event_alert) {
                            AlertBuilder.EVENT_ALERT.RIGHT -> PplusCommonUtil.logOutAndRestart()
                        }
                    }
                }).builder().show(this)
            }else{
                val intent = Intent(this, SnsLoginActivity::class.java)
                signInLauncher.launch(intent)
            }

        }

    }

    private val mSettingClickListener = View.OnClickListener { view ->
        when (view.id) {
            0//계정관리
            -> {
                if(!PplusCommonUtil.loginCheck(this, signInLauncher)){
                    return@OnClickListener
                }
                val intent = Intent(view.context, ProfileConfigActivity::class.java)
                startActivity(intent)
            }
            1 -> {
                if(!PplusCommonUtil.loginCheck(this, signInLauncher)){
                    return@OnClickListener
                }
                val intent = Intent(view.context, AccountConfigActivity::class.java)
                startActivity(intent)
            }
//            10->{
//                if(!PplusCommonUtil.loginCheck(this)){
//                    return@OnClickListener
//                }
//                val intent = Intent(view.context, BuyPlusTermsSetActivity::class.java)
//                startActivity(intent)
//            }
            10//약관
            -> if (!isClickTerms) {
                isClickTerms = true
                ApiBuilder.create().getActiveTermsAll(Const.APP_TYPE).setCallback(object : PplusCallback<NewResultResponse<Terms>> {

                    override fun onResponse(call: Call<NewResultResponse<Terms>>, response: NewResultResponse<Terms>) {
                        isClickTerms = false
                        val mTermsList = response.datas

                        if (mTermsList != null && mTermsList.size > 0) {
                            LogUtil.e(LOG_TAG, "term url : {}", mTermsList[0].url)
                            val intent = Intent(this@AppSettingActivity, WebViewActivity::class.java)
                            intent.putExtra(Const.TOOLBAR_RIGHT_ARROW, true)
                            intent.putExtra(Const.TITLE, mTermsList[0].subject)
                            intent.putExtra(Const.WEBVIEW_URL, mTermsList[0].url)
                            intent.putParcelableArrayListExtra(Const.TERMS_LIST, mTermsList as ArrayList<out Parcelable>)
                            startActivity(intent)
                        }
                    }

                    override fun onFailure(call: Call<NewResultResponse<Terms>>, t: Throwable, response: NewResultResponse<Terms>) {
                        isClickTerms = false
                    }
                }).build().call()
            }
            11//버전정보
            -> {
                val intent = Intent(view.context, ServiceVersionActivity::class.java)
                startActivity(intent)
            }
            12->{//오픈소스
                val intent = Intent(view.context, OpenLicenseActivity::class.java)
                startActivity(intent)
            }
            20//문의하기
            -> {
                if(!PplusCommonUtil.loginCheck(this, signInLauncher)){
                    return@OnClickListener
                }
                val intent = Intent(view.context, InquiryActivity::class.java)
                startActivity(intent)
            }
            21//공지사항
            -> {
                val intent = Intent(view.context, NoticeActivity::class.java)
                startActivity(intent)
            }
            22//FAQ
            -> {
                val intent = Intent(view.context, FAQActivity::class.java)
                startActivity(intent)
            }
            30//알림보관함
            -> {
                if(!PplusCommonUtil.loginCheck(this, signInLauncher)){
                    return@OnClickListener
                }

                val intent = Intent(view.context, AlarmContainerActivity::class.java)
                startActivity(intent)
            }
        }
    }

    val signInLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            // There are no request codes
            val data = result.data
            checkSignIn()
        }
    }

    private fun checkSignIn(){
        if(LoginInfoManager.getInstance().isMember){
            binding.textSignOut.setText(R.string.word_signOut)
        }else{
            binding.textSignOut.setText(R.string.word_signIn)
        }
    }

    override fun getToolbarOption(): ToolbarOption {

        val toolbarOption = ToolbarOption(this)
        toolbarOption.initializeDefaultToolbar(getString(R.string.word_setting), ToolbarOption.ToolbarMenu.LEFT)
        return toolbarOption
    }

    override fun getOnToolbarClickListener(): OnToolbarListener {

        return object : OnToolbarListener {
            override fun onClick(v: View?, toolbarMenu: ToolbarOption.ToolbarMenu?, tag: Any?) {
                when (toolbarMenu) {
                    ToolbarOption.ToolbarMenu.LEFT -> if (tag == 1) {
                        onBackPressed()
                    }
                }
            }
        }
    }
}
