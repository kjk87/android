package com.pplus.prnumberbiz.apps.main.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.FragmentTransaction
import androidx.core.view.GravityCompat
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.pple.pplus.utils.part.info.DeviceUtil
import com.pple.pplus.utils.part.pref.PreferenceUtil
import com.pple.pplus.utils.part.utils.StringUtils
import com.pplus.prnumberbiz.Const
import com.pplus.prnumberbiz.PRNumberBizApplication
import com.pplus.prnumberbiz.R
import com.pplus.prnumberbiz.apps.common.Foreground
import com.pplus.prnumberbiz.apps.common.mgmt.LoginInfoManager
import com.pplus.prnumberbiz.apps.common.mgmt.SchemaManager
import com.pplus.prnumberbiz.apps.common.ui.base.BaseActivity
import com.pplus.prnumberbiz.apps.customer.ui.ContactCustomerActivity
import com.pplus.prnumberbiz.apps.goods.ui.GoodsReviewActivity
import com.pplus.prnumberbiz.apps.goods.ui.PlusGoodsActivity
import com.pplus.prnumberbiz.apps.goods.ui.SellerApplyActivity
import com.pplus.prnumberbiz.apps.keyword.ui.KeywordConfigActivity
import com.pplus.prnumberbiz.apps.marketing.ui.SnsSyncActivity
import com.pplus.prnumberbiz.apps.menu.ui.MenuConfigActivity
import com.pplus.prnumberbiz.apps.pages.ui.AlertPageLinkActivity
import com.pplus.prnumberbiz.apps.pages.ui.OperationInfoActivity
import com.pplus.prnumberbiz.apps.pages.ui.PageConfigActivity
import com.pplus.prnumberbiz.apps.pages.ui.PageLinkActivity
import com.pplus.prnumberbiz.apps.push.ui.PushSendActivity
import com.pplus.prnumberbiz.apps.setting.ui.AlarmContainerActivity
import com.pplus.prnumberbiz.apps.setting.ui.NoticeActivity
import com.pplus.prnumberbiz.apps.setting.ui.SettingActivity
import com.pplus.prnumberbiz.apps.sms.SmsSendActivity
import com.pplus.prnumberbiz.apps.verification.AuthCodeConfigActivity
import com.pplus.prnumberbiz.core.code.common.EnumData
import com.pplus.prnumberbiz.core.network.ApiBuilder
import com.pplus.prnumberbiz.core.util.PplusCommonUtil
import com.pplus.prnumberbiz.core.util.ToastUtil
import com.pplus.prnumberbiz.push.PushReceiveData
import kotlinx.android.synthetic.main.activity_biz_main.*
import kotlinx.android.synthetic.main.layout_main_menu.*

class BizMainActivity : BaseActivity() {
    override fun getPID(): String {
        return ""
    }

    override fun getLayoutRes(): Int {
        return R.layout.activity_biz_main
    }

    override fun initializeView(savedInstanceState: Bundle?) {

        layout_biz_main_menu.layoutParams.width = (DeviceUtil.DISPLAY.SCREEN_WIDTH_PIXELS * 0.9).toInt()

        Foreground.get(this).addListener(object : Foreground.Listener {
            override fun onBecameForeground() {
                ApiBuilder.create().session.build().call()
            }

            override fun onBecameBackground() {
            }
        })


//        val intent = Intent(this, SellerApplyActivity::class.java)
//        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//        startActivityForResult(intent, Const.REQ_SET_PAGE)

//        layout_main_menu_page_config.setOnClickListener {
//            val intent = Intent(this, PageConfigActivity::class.java)
//            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//            startActivityForResult(intent, Const.REQ_SET_PAGE)
//        }
//
//        layout_main_menu_link.setOnClickListener {
//            val intent = Intent(this, PageLinkActivity::class.java)
//            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//            startActivityForResult(intent, Const.REQ_SET_PAGE)
//        }

        layout_main_menu_keyword_config.setOnClickListener {
            val intent = Intent(this, KeywordConfigActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
        }

//        layout_main_menu_sns.setOnClickListener {
//            val intent = Intent(this, SnsSyncActivity::class.java)
//            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//            startActivityForResult(intent, Const.REQ_SYNC_SNS)
//        }
//
//        layout_main_menu_customer_config.setOnClickListener {
//            val intent = Intent(this, ContactCustomerActivity::class.java)
////            val intent = Intent(this, PlusActivity::class.java)
//            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//            startActivity(intent)
//        }
//
//        layout_main_menu_send_sms.setOnClickListener {
//            val intent = Intent(this, SmsSendActivity::class.java)
//            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//            startActivityForResult(intent, Const.REQ_CASH_CHANGE)
//        }
//
//        layout_main_menu_send_push.setOnClickListener {
//            val intent = Intent(this, PushSendActivity::class.java)
//            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//            startActivityForResult(intent, Const.REQ_CASH_CHANGE)
//        }

//        layout_main_menu_post.setOnClickListener {
//            val intent = Intent(this, PostActivity::class.java)
//            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//            startActivity(intent)
//        }

//        layout_main_use_method.setOnClickListener {
//            val intent = Intent(this, UseInfoActivity::class.java)
//            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//            startActivity(intent)
//        }

        layout_main_menu_notice.setOnClickListener {
            val intent = Intent(this, NoticeActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
        }

        layout_main_menu_setting.setOnClickListener {
            val intent = Intent(this, SettingActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
        }

//        layout_main_menu_user_mode.setOnClickListener {
//            var intent = packageManager.getLaunchIntentForPackage("com.pplus.prnumberuser")
//            if (intent != null) {
//                // We found the activity now start the activity
//                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
//                startActivity(intent)
//            } else {
//                // Bring user to the market or let them choose an app?
//                intent = Intent(Intent.ACTION_VIEW)
//                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
//                intent.data = Uri.parse("market://details?id=com.pplus.prnumberuser")
//                startActivity(intent)
//            }
//        }

        layout_main_menu_alarm.setOnClickListener {
            val intent = Intent(this, AlarmContainerActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivityForResult(intent, Const.REQ_ALARM)
        }

//        layout_main_menu_goods_config.setOnClickListener {
//            val page = LoginInfoManager.getInstance().user.page!!
//
//            val intent = Intent(this, MenuConfigActivity::class.java)
//            intent.putExtra(Const.KEY, Const.REG)
//            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//            startActivity(intent)
//        }
//
//        layout_main_menu_operation_info.setOnClickListener {
//            val intent = Intent(this, OperationInfoActivity::class.java)
//            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//            startActivityForResult(intent, Const.REQ_SET_PAGE)
//        }
//
//        layout_main_menu_prlink.setOnClickListener {
//            val intent = Intent(this, PRLinkActivity::class.java)
//            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//            startActivity(intent)
//        }

//        layout_main_menu_plus_goods_config.setOnClickListener {
//            val page = LoginInfoManager.getInstance().user.page!!
//            if (page.type == EnumData.PageTypeCode.store.name) {
//                if (page.isShopOrderable!! || page.isDeliveryOrderable!! || page.isPackingOrderable!!) {
//                    val intent = Intent(this, PlusGoodsActivity::class.java)
//                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//                    startActivity(intent)
//                } else {
//                    val intent = Intent(this, OperationInfoActivity::class.java)
//                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//                    startActivityForResult(intent, Const.REQ_SET_PAGE)
//                }
//            } else {
//                val intent = Intent(this, PlusGoodsActivity::class.java)
//                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//                startActivity(intent)
//            }
//
//
//        }

//        layout_main_menu_review.setOnClickListener {
//            val intent = Intent(this, GoodsReviewActivity::class.java)
//            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//            startActivity(intent)
//        }

        layout_main_menu_user_mode.setOnClickListener {
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

        layout_main_menu_keyword_config.setOnClickListener {
            val intent = Intent(this, KeywordConfigActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
        }

        layout_main_menu_auth_code_config.setOnClickListener {
            val intent = Intent(this, AuthCodeConfigActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
        }

        onNewIntent(intent)

    }

    override fun onNewIntent(intent: Intent) {

        super.onNewIntent(intent)
        mPushData = intent.getParcelableExtra<PushReceiveData>(Const.PUSH_DATA)
        setPushData()
        setSchemeData()
        setBizMainFragment()
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


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
//            Const.REQ_SET_PAGE, Const.REQ_APPLY -> {
//                if (checkPageSet()) {
//                    getPage()
//                }
//            }
            Const.REQ_ALARM, Const.REQ_CASH_CHANGE -> {
                PplusCommonUtil.reloadSession(object : PplusCommonUtil.Companion.ReloadListener {
                    override fun reload() {
                        checkAlarmCount()
                    }
                })
            }
        }
    }

//    private fun getPage() {
//        val params = HashMap<String, String>()
//        params["no"] = LoginInfoManager.getInstance().user.page!!.no.toString()
//        showProgress("")
//        ApiBuilder.create().getPage(params).setCallback(object : PplusCallback<NewResultResponse<Page?>> {
//
//            override fun onResponse(call: Call<NewResultResponse<Page?>>?, response: NewResultResponse<Page?>?) {
//                hideProgress()
//                if (response!!.data != null) {
//
//                    LoginInfoManager.getInstance().user.page = response.data!!
//                    LoginInfoManager.getInstance().save()
//
////                    if(SchemaManager.mIsGoodsReg){
////                        SchemaManager.mIsGoodsReg = false
////
////                        if (LoginInfoManager.getInstance().user.page!!.sellerStatus == EnumData.SellerStatus.approval.status) {
////                            intent = Intent(this@BizMainActivity, GoodsRegActivity2::class.java)
////                            intent.putExtra(Const.MODE, EnumData.MODE.WRITE)
////                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
////                            startActivityForResult(intent, Const.REQ_REG)
////                        }
////                    }
//                    if (LoginInfoManager.getInstance().user.page!!.isSeller!!) {
//                        when (LoginInfoManager.getInstance().user.page!!.sellerStatus) {
//                            EnumData.SellerStatus.approvalWait.status, EnumData.SellerStatus.secondRequest.status -> {//승인대기
//                            }
//                            EnumData.SellerStatus.reject.status -> {
//                            }
//                        }
//                    } else {
//                        val intent = Intent(this@BizMainActivity, SaleGoodsApplyActivity::class.java)
//                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//                        startActivityForResult(intent, Const.REQ_APPLY)
//                    }
//
//                    if (bizMainFragment != null) {
//                        bizMainFragment!!.setData()
//                    } else {
//                        setBizMainFragment()
//                    }
//                    setPageData()
//                }
//            }
//
//            override fun onFailure(call: Call<NewResultResponse<Page?>>?, t: Throwable?, response: NewResultResponse<Page?>?) {
//                hideProgress()
//            }
//        }).build().call()
//    }

    //    var bizMainFragment: BizMainFragment? = null
    private fun setBizMainFragment() {
        val ft = supportFragmentManager.beginTransaction()
        ft.setTransition(androidx.fragment.app.FragmentTransaction.TRANSIT_FRAGMENT_FADE)
//        bizMainFragment = BizMainFragment.newInstance()
        ft.replace(R.id.main_container2, BizMainFragment2.newInstance(), BizMainFragment2::class.java.simpleName)
        ft.commit()
    }

    private fun share() {

        val shareText = LoginInfoManager.getInstance().user.page!!.catchphrase + "\n" + getString(R.string.format_msg_page_url, "index.php?no=" + LoginInfoManager.getInstance().user.page!!.no!!)

        val intent = Intent(Intent.ACTION_SEND)
        intent.action = Intent.ACTION_SEND
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_TEXT, shareText)
        val chooserIntent = Intent.createChooser(intent, getString(R.string.word_share_page))
        startActivity(chooserIntent)
    }

    fun setPageData() {
        val page = LoginInfoManager.getInstance().user.page!!
        text_main_menu_page_name.setSingleLine()
        text_main_menu_page_name.text = page.name

        Glide.with(this@BizMainActivity).load(page.profileImage?.url).apply(RequestOptions().centerCrop().placeholder(R.drawable.img_biz_profile_default).error(R.drawable.img_biz_profile_default)).into(image_main_menu_profile)

//        if (page.type == EnumData.PageTypeCode.store.name) {
//            text_main_menu_goods_config.setText(R.string.word_menu_config)
//        } else {
//            text_main_menu_goods_config.setText(R.string.word_goods_config)
//        }

        if (page.isLink == null) {
            page.isLink = false
        }

//        val isLink = page.isLink!!
//        layout_main_menu_url.visibility = View.GONE
//        if (isLink) {
//            text_main_menu_link_page.isSelected = false
//            text_main_menu_link_url.isSelected = true
//            if (StringUtils.isNotEmpty(page.homepageLink)) {
//                layout_main_menu_url.visibility = View.VISIBLE
//                text_main_menu_url.text = page.homepageLink
//            }
//        } else {
//            text_main_menu_link_page.isSelected = true
//            text_main_menu_link_url.isSelected = false
//            layout_main_menu_url.visibility = View.GONE
//        }

//        text_main_menu_link_page.setOnClickListener {
//            if(isLink){
//                val intent = Intent(this, AlertPageLinkActivity::class.java)
//                intent.putExtra(Const.IS_LINK, false)
//                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//                startActivityForResult(intent, Const.REQ_SET_PAGE)
//            }
//        }
//
//        text_main_menu_link_url.setOnClickListener {
//            if(!isLink){
//                val intent = Intent(this, AlertPageLinkActivity::class.java)
//                intent.putExtra(Const.IS_LINK, true)
//                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//                startActivityForResult(intent, Const.REQ_SET_PAGE)
//            }
//        }
//
//        text_main_menu_url_modify.setOnClickListener {
//            val intent = Intent(this, AlertPageLinkActivity::class.java)
//            intent.putExtra(Const.IS_LINK, true)
//            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//            startActivityForResult(intent, Const.REQ_SET_PAGE)
//        }
    }

//    private fun getCustomerCount() {
//        val params = HashMap<String, String>()
//        params["no"] = "" + LoginInfoManager.getInstance().user.page!!.no!!
//
//        ApiBuilder.create().getFanGroupAll(params).setCallback(object : PplusCallback<NewResultResponse<Group>> {
//
//            override fun onResponse(call: Call<NewResultResponse<Group>>, response: NewResultResponse<Group>) {
//                for (group in response.datas) {
//                    if (group.isDefaultGroup) {
//                        text_main_menu_customer_count.text = getString(R.string.format_person_count, FormatUtil.getMoneyType(group.count.toString()))
//                        break
//                    }
//                }
//            }
//
//            override fun onFailure(call: Call<NewResultResponse<Group>>, t: Throwable, response: NewResultResponse<Group>) {
//
//            }
//        }).build().call()
//    }

    fun checkAlarmCount() {
        if (LoginInfoManager.getInstance().user.properties == null || LoginInfoManager.getInstance().user.properties!!.newMsgCount == 0) {
            text_main_menu_alarm_count!!.visibility = View.INVISIBLE
        } else {
            text_main_menu_alarm_count!!.visibility = View.VISIBLE
            if (LoginInfoManager.getInstance().user.properties != null && LoginInfoManager.getInstance().user.properties!!.newMsgCount > 99) {
                text_main_menu_alarm_count!!.text = "99+"
            } else {
                text_main_menu_alarm_count!!.text = LoginInfoManager.getInstance().user.properties!!.newMsgCount.toString()
            }
        }
    }

//    private fun setHomeFragment() {
//        val ft = supportFragmentManager.beginTransaction()
//        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
//        ft.replace(R.id.main_container2, HomeFragment.newInstance(), HomeFragment::class.java.simpleName)
//        ft.commitNow()
//    }

    internal var isExitBackPressed = false
    private val mHandler = Handler()

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            if (isExitBackPressed) {

                for (activity in PRNumberBizApplication.getActivityList()) {
                    activity.finish()
                }

                if (!isFinishing) {
                    finish()
                }

            } else {
                isExitBackPressed = true

                ToastUtil.show(this, getString(R.string.msg_quit))

                mHandler.postDelayed({
                    // TODO Auto-generated method stub
                    isExitBackPressed = false
                }, 3000)
            }
        }
    }

}
