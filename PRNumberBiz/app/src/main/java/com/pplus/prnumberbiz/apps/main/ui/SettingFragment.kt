package com.pplus.prnumberbiz.apps.main.ui


import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import androidx.fragment.app.Fragment
import android.view.View
import com.pplus.prnumberbiz.Const
import com.pplus.prnumberbiz.R
import com.pplus.prnumberbiz.apps.common.builder.AlertBuilder
import com.pplus.prnumberbiz.apps.common.builder.OnAlertResultListener
import com.pplus.prnumberbiz.apps.common.builder.data.AlertData
import com.pplus.prnumberbiz.apps.common.mgmt.LoginInfoManager
import com.pplus.prnumberbiz.apps.common.ui.base.BaseActivity
import com.pplus.prnumberbiz.apps.common.ui.base.BaseFragment
import com.pplus.prnumberbiz.apps.common.ui.common.WebViewActivity
import com.pplus.prnumberbiz.apps.setting.ui.*
import com.pplus.prnumberbiz.core.network.ApiBuilder
import com.pplus.prnumberbiz.core.network.model.dto.Terms
import com.pplus.prnumberbiz.core.network.model.response.NewResultResponse
import com.pplus.prnumberbiz.core.util.PplusCommonUtil
import kotlinx.android.synthetic.main.fragment_setting.*
import network.common.PplusCallback
import retrofit2.Call
import java.util.*

/**
 * A simple [Fragment] subclass.
 * Use the [SettingFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class SettingFragment : BaseFragment<BaseActivity>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            //            param1 = it.getString(ARG_PARAM1)
//            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun getLayoutResourceId(): Int {
        return R.layout.fragment_setting
    }

    override fun initializeView(container: View?) {

    }

    override fun init() {
        val page = LoginInfoManager.getInstance().user.page!!

//        text_setting_page_name.text = page.name
//
//        if (page.numberList != null && page.numberList!!.isNotEmpty()) {
//            text_setting_page_number.text = PplusNumberUtil.getPrNumberFormat(page.numberList!![0].number)
//        }

        layout_setting_account_config.setOnClickListener {
            val intent = Intent(activity, AccountConfigActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
        }

        layout_setting_terms.setOnClickListener {
            if (mTermsList != null && mTermsList!!.isNotEmpty()) {
                val intent = Intent(activity, WebViewActivity::class.java)
                intent.putExtra(Const.TOOLBAR_RIGHT_ARROW, true)
                intent.putExtra(Const.TITLE, mTermsList!![0].subject)
                intent.putExtra(Const.WEBVIEW_URL, mTermsList!![0].url)
                intent.putParcelableArrayListExtra(Const.TERMS_LIST, mTermsList as ArrayList<out Parcelable>)
                startActivity(intent)
            }
        }

        val pinfo = activity?.packageManager!!.getPackageInfo(activity?.packageName, 0)
        text_setting_current_version.text = pinfo.versionName

        layout_setting_version.setOnClickListener {
            val intent = Intent(activity, ServiceVersionActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
        }

        layout_setting_open_license.setOnClickListener {
            val intent = Intent(activity, OpenLicenseActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
        }

        layout_setting_inquiry.setOnClickListener {
            val intent = Intent(activity, InquiryActivity::class.java)
            intent.putExtra(Const.KEY, Const.OTHER)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
        }

        layout_setting_notice.setOnClickListener {
            val intent = Intent(activity, NoticeActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
        }

        layout_setting_faq.setOnClickListener {
            val intent = Intent(activity, FAQActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
        }

        layout_setting_alarm_setting.setOnClickListener {
            val intent = Intent(activity, AlimActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
        }


        layout_setting_alarm_container.setOnClickListener {
            val intent = Intent(activity, AlarmContainerActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
        }

        layout_setting_logout.setOnClickListener {
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
            }).builder().show(activity)
        }

        getTermsList()
    }

    var mTermsList: List<Terms>? = null

    private fun getTermsList() {
        ApiBuilder.create().getActiveTermsAll(activity?.packageName).setCallback(object : PplusCallback<NewResultResponse<Terms>> {

            override fun onResponse(call: Call<NewResultResponse<Terms>>, response: NewResultResponse<Terms>) {

                mTermsList = response.datas

            }

            override fun onFailure(call: Call<NewResultResponse<Terms>>, t: Throwable, response: NewResultResponse<Terms>) {

            }
        }).build().call()
    }

    override fun getPID(): String {
        return ""
    }


    companion object {

        @JvmStatic
        fun newInstance() =
                SettingFragment().apply {
                    arguments = Bundle().apply {
                        //                        putString(ARG_PARAM1, param1)
//                        putString(ARG_PARAM2, param2)
                    }
                }
    }
}
