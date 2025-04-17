package com.pplus.prnumberbiz.apps.main.ui

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.FragmentTransaction
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.pple.pplus.utils.part.info.DeviceUtil
import com.pple.pplus.utils.part.pref.PreferenceUtil
import com.pple.pplus.utils.part.utils.StringUtils
import com.pplus.prnumberbiz.Const
import com.pplus.prnumberbiz.PRNumberBizApplication
import com.pplus.prnumberbiz.R
import com.pplus.prnumberbiz.apps.LauncherScreenActivity
import com.pplus.prnumberbiz.apps.common.Foreground
import com.pplus.prnumberbiz.apps.common.mgmt.LoginInfoManager
import com.pplus.prnumberbiz.apps.common.mgmt.SchemaManager
import com.pplus.prnumberbiz.apps.common.ui.base.BaseActivity
import com.pplus.prnumberbiz.apps.goods.ui.SellerApplyActivity
import com.pplus.prnumberbiz.apps.keyword.ui.KeywordActivity
import com.pplus.prnumberbiz.apps.pages.ui.PageLinkActivity
import com.pplus.prnumberbiz.apps.setting.ui.AlarmContainerActivity
import com.pplus.prnumberbiz.apps.setting.ui.NoticeActivity
import com.pplus.prnumberbiz.apps.setting.ui.SettingActivity
import com.pplus.prnumberbiz.core.network.ApiBuilder
import com.pplus.prnumberbiz.push.PushReceiveData
import kotlinx.android.synthetic.main.activity_person_main.*
import kotlinx.android.synthetic.main.layout_person_main_menu.*

class PersonMainActivity : BaseActivity() {
    override fun getPID(): String {
        return ""
    }

    override fun getLayoutRes(): Int {
        return R.layout.activity_person_main
    }

    override fun initializeView(savedInstanceState: Bundle?) {

        layout_person_main_menu.layoutParams.width = (DeviceUtil.DISPLAY.SCREEN_WIDTH_PIXELS * 0.9).toInt()

        Foreground.get(this).addListener(object : Foreground.Listener {
            override fun onBecameForeground() {
                ApiBuilder.create().session.build().call()
            }

            override fun onBecameBackground() {
            }
        })

        layout_person_main_menu_alarm.setOnClickListener {
            val intent = Intent(this, AlarmContainerActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivityForResult(intent, Const.REQ_ALARM)
        }

        layout_person_main_menu_link.setOnClickListener {
            val intent = Intent(this, PageLinkActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivityForResult(intent, Const.REQ_SET_PAGE)
        }

        layout_person_main_menu_keyword.setOnClickListener {
            val intent = Intent(this, KeywordActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivityForResult(intent, Const.REQ_SET_PAGE)
        }

        layout_person_main_menu_apply_seller.setOnClickListener {
            val intent = Intent(this, SellerApplyActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivityForResult(intent, Const.REQ_APPLY)
        }

        layout_person_main_menu_notice.setOnClickListener {
            val intent = Intent(this, NoticeActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
        }

        layout_person_main_menu_setting.setOnClickListener {
            val intent = Intent(this, SettingActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
        }

        layout_person_main_menu_user_mode.setOnClickListener {
            var intent = packageManager.getLaunchIntentForPackage("com.pplus.prnumberuser")
            if (intent != null) {
                // We found the activity now start the activity
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
            } else {
                // Bring user to the market or let them choose an app?
                intent = Intent(Intent.ACTION_VIEW)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                intent.data = Uri.parse("market://details?id=com.pplus.prnumberuser")
                startActivity(intent)
            }
        }

        onNewIntent(intent)
    }

    override fun onNewIntent(intent: Intent) {

        super.onNewIntent(intent)
        mPushData = intent.getParcelableExtra<PushReceiveData>(Const.PUSH_DATA)
        setPushData()
        setSchemeData()
        setMainFragment()
    }

    private var mPushData: PushReceiveData? = null
    private fun setPushData() {

        if (mPushData != null) {
            PreferenceUtil.getDefaultPreference(this).put(Const.PUSH_ID, 0)
            SchemaManager.getInstance(this).moveToPushData(mPushData)
        }
    }

    private fun setSchemeData() {

        val scheme = PRNumberBizApplication.getSchemaData()

        if (StringUtils.isNotEmpty(scheme)) {
            if (scheme.contains(SchemaManager.SCHEMA_PRNUMBER)) {
                SchemaManager.getInstance(this).moveToSchema(scheme, false)
                PRNumberBizApplication.setSchemaData(null)
            }
        }
    }

    var fragment: PersonMainFragment? = null
    private fun setMainFragment() {
        val ft = supportFragmentManager.beginTransaction()
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
        fragment = PersonMainFragment.newInstance()
        ft.replace(R.id.person_main_container, fragment!!, PersonMainFragment::class.java.simpleName)
        ft.commit()
    }

    fun setPageData() {
        val page = LoginInfoManager.getInstance().user.page!!
        text_person_main_menu_page_name.setSingleLine()
        text_person_main_menu_page_name.text = page.name

        Glide.with(this).load(page.profileImage?.url).apply(RequestOptions().centerCrop().placeholder(R.drawable.img_biz_profile_default).error(R.drawable.img_biz_profile_default)).into(image_person_main_menu_profile)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when(requestCode){
            Const.REQ_APPLY->{
                if(resultCode == Activity.RESULT_OK){
                    val intent = Intent(this, LauncherScreenActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                    startActivity(intent)
                }
            }
        }
    }
}
