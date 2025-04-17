//package com.pplus.prnumberbiz.apps.main.ui
//
//
//import android.content.Intent
//import android.net.Uri
//import android.os.Bundle
//import android.view.View
//import android.widget.CompoundButton
//import com.bumptech.glide.Glide
//import com.bumptech.glide.request.RequestOptions
//import com.pple.pplus.utils.part.format.FormatUtil
//import com.pplus.prnumberbiz.Const
//import com.pplus.prnumberbiz.R
//import com.pplus.prnumberbiz.apps.UseInfoActivity
//import com.pplus.prnumberbiz.apps.common.mgmt.LoginInfoManager
//import com.pplus.prnumberbiz.apps.common.ui.base.BaseActivity
//import com.pplus.prnumberbiz.apps.common.ui.base.BaseFragment
//import com.pplus.prnumberbiz.apps.common.ui.custom.SafeSwitchCompat
//import com.pplus.prnumberbiz.apps.customer.ui.ContactConfigActivity
//import com.pplus.prnumberbiz.apps.goods.ui.GoodsSaleTotalHistoryActivity
//import com.pplus.prnumberbiz.apps.pages.ui.PageActivity2
//import com.pplus.prnumberbiz.apps.pages.ui.PageLinkActivity
//import com.pplus.prnumberbiz.apps.pages.ui.PageSetActivity
//import com.pplus.prnumberbiz.apps.setting.ui.AlarmContainerActivity
//import com.pplus.prnumberbiz.apps.setting.ui.SettingActivity
//import com.pplus.prnumberbiz.core.code.common.PageOpenBoundsCode
//import com.pplus.prnumberbiz.core.network.ApiBuilder
//import com.pplus.prnumberbiz.core.network.model.dto.Group
//import com.pplus.prnumberbiz.core.network.model.dto.Page
//import com.pplus.prnumberbiz.core.network.model.response.NewResultResponse
//import com.pplus.prnumberbiz.core.util.PplusCommonUtil
//import com.pplus.prnumberbiz.core.util.PplusNumberUtil
//import kotlinx.android.synthetic.main.fragment_home.*
//import network.common.PplusCallback
//import retrofit2.Call
//import java.util.*
//
//class HomeFragment : BaseFragment<BaseActivity>() {
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        arguments?.let {
//            //            param1 = it.getString(ARG_PARAM1)
////            param2 = it.getString(ARG_PARAM2)
//        }
//    }
//
//    override fun getLayoutResourceId(): Int {
//        return R.layout.fragment_home
//    }
//
//    override fun initializeView(container: View?) {
//
//    }
//
//    override fun init() {
//
//        image_home_info.setOnClickListener {
//            val intent = Intent(activity, UseInfoActivity::class.java)
//            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//            startActivity(intent)
//        }
//
//        layout_home_alarm.setOnClickListener {
//            val intent = Intent(activity, AlarmContainerActivity::class.java)
//            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//            activity?.startActivityForResult(intent, Const.REQ_ALARM)
//        }
//
//        image_home_user_mode.setOnClickListener {
//            var intent = activity?.packageManager?.getLaunchIntentForPackage("com.pplus.prnumberuser")
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
//
//        checkAlarmCount()
//        getPage()
//    }
//
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
//                    setData()
//                }
//            }
//
//            override fun onFailure(call: Call<NewResultResponse<Page?>>?, t: Throwable?, response: NewResultResponse<Page?>?) {
//                hideProgress()
//            }
//        }).build().call()
//    }
//
//    fun setData() {
//
//        text_my_page_header_name.setOnClickListener {
//            val page = LoginInfoManager.getInstance().user.page!!
//            if (page.category == null) {
//                val intent = Intent(activity, PageSetActivity::class.java)
//                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//                activity?.startActivityForResult(intent, Const.REQ_SET_PAGE)
//                return@setOnClickListener
//            }
//
//            val intent = Intent(activity, PageActivity2::class.java)
//            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//            activity?.startActivityForResult(intent, Const.REQ_SET_PAGE)
//        }
//
//        layout_home_customer_config.setOnClickListener {
//            val page = LoginInfoManager.getInstance().user.page!!
//            if (page.category == null) {
//                val intent = Intent(activity, PageSetActivity::class.java)
//                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//                activity?.startActivityForResult(intent, Const.REQ_SET_PAGE)
//                return@setOnClickListener
//            }
//
////            val intent = Intent(activity, MarketingActivity::class.java)
//            val intent = Intent(activity, ContactConfigActivity::class.java)
//            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//            activity?.startActivityForResult(intent, Const.REQ_CASH_CHANGE)
//        }
//
////        layout_home_sale_goods.setOnClickListener {
////            val page = LoginInfoManager.getInstance().user.page!!
////            if (page.category == null) {
////                val intent = Intent(activity, PageSetActivity::class.java)
////                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
////                activity?.startActivityForResult(intent, Const.REQ_SET_PAGE)
////                return@setOnClickListener
////            }
////
////            if (page.sellerStatus != null && page.sellerStatus!!) {
////                val intent = Intent(activity, GoodsListActivity::class.java)
////                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
////                startActivity(intent)
////            } else {
////                val intent = Intent(activity, GoodsSaleApplyPreActivity::class.java)
////                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
////                activity?.startActivityForResult(intent, Const.REQ_APPLY)
////            }
////
//////            val intent = Intent(activity, GoodsListActivity::class.java)
//////            val intent = Intent(activity, GoodsSaleApplyPreActivity::class.java)
//////            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//////            startActivityForResult(intent, Const.REQ_APPLY)
////        }
//
//        layout_home_sale_history.setOnClickListener {
//            //            val intent = Intent(activity, SaleOrderProcessyActivity::class.java)
//            val intent = Intent(activity, GoodsSaleTotalHistoryActivity::class.java)
//            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//            startActivity(intent)
//        }
//
//        image_home_setting.setOnClickListener {
//            val intent = Intent(activity, SettingActivity::class.java)
//            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//            startActivity(intent)
//        }
//
//        text_home_setting_link.setOnClickListener {
//
//            val page = LoginInfoManager.getInstance().user.page!!
//            if (page.category == null) {
//                val intent = Intent(activity, PageSetActivity::class.java)
//                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//                activity?.startActivityForResult(intent, Const.REQ_SET_PAGE)
//                return@setOnClickListener
//            }
//
//            val intent = Intent(activity, PageLinkActivity::class.java)
//            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//            startActivityForResult(intent, Const.REQ_SET_PAGE)
//        }
//
////        layout_home_plus.setOnClickListener {
////            val page = LoginInfoManager.getInstance().user.page!!
////            if (page.category == null) {
////                val intent = Intent(activity, PageSetActivity::class.java)
////                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
////                activity?.startActivityForResult(intent, Const.REQ_SET_PAGE)
////                return@setOnClickListener
////            }
////
////            if (page.sellerStatus == null || !page.sellerStatus!!) {
////                val intent = Intent(activity, AlertPromotionActivity::class.java)
////                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
////                activity?.startActivityForResult(intent, Const.REQ_SET_PAGE)
////            }else{
////                val intent = Intent(activity, GoodsRegActivity::class.java)
////                intent.putExtra(Const.MODE, EnumData.MODE.WRITE)
////                intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
////                activity?.startActivityForResult(intent, Const.REQ_REG)
////            }
////
////
////        }
//
//        switch_home_secret_mode.onSafeCheckedListener = object : SafeSwitchCompat.OnSafeCheckedListener {
//            override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
//
//                val page = LoginInfoManager.getInstance().user.page!!
//                if (page.category == null) {
//                    val intent = Intent(activity, PageSetActivity::class.java)
//                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//                    activity?.startActivityForResult(intent, Const.REQ_SET_PAGE)
//                    return
//                }
//
//                val intent = Intent(activity, AlertSecretModeActivity::class.java)
//                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//                startActivityForResult(intent, Const.REQ_SECRET_MODE)
//            }
//
//            override fun onAlwaysCalledListener(buttonView: CompoundButton?, isChecked: Boolean) {
//
//            }
//        }
//
//        val page = LoginInfoManager.getInstance().user.page!!
//        text_my_page_header_name.setSingleLine()
//        text_my_page_header_name.text = page.name
//
//        Glide.with(this).load(page.profileImage?.url).apply(RequestOptions().centerCrop().placeholder(R.drawable.img_main_profile_default).error(R.drawable.ic_message_profile_default)).into(image_home_page_profile)
//        if (page.numberList != null && page.numberList!!.isNotEmpty()) {
//            text_home_prnumber.text = PplusNumberUtil.getPrNumberFormat(page.numberList!![0].number)
//        }
//
//        checkOpenBound()
//
//        getPlusCount()
//        getCustomerCount()
//    }
//
//    private fun getPlusCount() {
//        val params = HashMap<String, String>()
//        params["no"] = "" + LoginInfoManager.getInstance().user.page!!.no!!
//
//        ApiBuilder.create().getFanGroupAll(params).setCallback(object : PplusCallback<NewResultResponse<Group>> {
//
//            override fun onResponse(call: Call<NewResultResponse<Group>>, response: NewResultResponse<Group>) {
//                if (!isAdded) {
//                    return
//                }
//                for (group in response.datas) {
//                    if (group.isDefaultGroup) {
//                        text_home_plus_count.text = getString(R.string.format_person_count, FormatUtil.getMoneyType(group.count.toString()))
//                        break
//                    }
//                }
//
//            }
//
//            override fun onFailure(call: Call<NewResultResponse<Group>>, t: Throwable, response: NewResultResponse<Group>) {
//
//            }
//        }).build().call()
//    }
//
//    private fun getCustomerCount() {
//        val params = HashMap<String, String>()
//        params["no"] = "" + LoginInfoManager.getInstance().user.page!!.no!!
//
//        ApiBuilder.create().getGroupAll(params).setCallback(object : PplusCallback<NewResultResponse<Group>> {
//
//            override fun onResponse(call: Call<NewResultResponse<Group>>, response: NewResultResponse<Group>) {
//                if (!isAdded) {
//                    return
//                }
//                for (group in response.datas) {
//                    if (group.isDefaultGroup) {
//                        text_home_customer_count.text = getString(R.string.format_person_count, FormatUtil.getMoneyType(group.count.toString()))
//                        break
//                    }
//                }
//
//            }
//
//            override fun onFailure(call: Call<NewResultResponse<Group>>, t: Throwable, response: NewResultResponse<Group>) {
//
//            }
//        }).build().call()
//    }
//
//    fun checkAlarmCount() {
//        if (LoginInfoManager.getInstance().user.properties == null || LoginInfoManager.getInstance().user.properties!!.newMsgCount == 0) {
//            text_home_alarm_count!!.visibility = View.INVISIBLE
//        } else {
//            text_home_alarm_count!!.visibility = View.VISIBLE
//            if (LoginInfoManager.getInstance().user.properties!!.newMsgCount > 99) {
//                text_home_alarm_count!!.text = "99+"
//            } else {
//                text_home_alarm_count!!.text = LoginInfoManager.getInstance().user.properties!!.newMsgCount.toString()
//            }
//        }
//    }
//
//    private fun checkOpenBound(){
//        val page = LoginInfoManager.getInstance().user.page!!
//        when (page.openBound) {
//            PageOpenBoundsCode.everybody.name -> {
//                switch_home_secret_mode.setSafeCheck(true, SafeSwitchCompat.IGNORE)
//            }
//            else -> {
//                switch_home_secret_mode.setSafeCheck(false, SafeSwitchCompat.IGNORE)
//            }
//        }
//    }
//
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        when (requestCode) {
//            Const.REQ_ALARM, Const.REQ_CASH_CHANGE -> {
//                PplusCommonUtil.reloadSession(object : PplusCommonUtil.Companion.ReloadListener {
//                    override fun reload() {
//                        checkAlarmCount()
//                    }
//                })
//            }
//            Const.REQ_SET_PAGE -> {
//                getPage()
//            }
//            Const.REQ_APPLY -> {
//                getPage()
//            }
//            Const.REQ_SECRET_MODE -> {
//                checkOpenBound()
//            }
//        }
//    }
//
//    override fun getPID(): String {
//        return ""
//    }
//
//    companion object {
//
//        @JvmStatic
//        fun newInstance() =
//                HomeFragment().apply {
//                    arguments = Bundle().apply {
//                        //                        putString(ARG_PARAM1, param1)
////                        putString(ARG_PARAM2, param2)
//                    }
//                }
//    }
//}
