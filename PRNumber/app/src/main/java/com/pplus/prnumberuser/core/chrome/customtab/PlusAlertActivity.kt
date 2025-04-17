package com.pplus.prnumberuser.core.chrome.customtab

import android.os.Bundle
import android.view.View
import com.pplus.networks.common.PplusCallback
import com.pplus.prnumberuser.BusProviderData
import com.pplus.prnumberuser.Const
import com.pplus.prnumberuser.R
import com.pplus.prnumberuser.apps.common.builder.AlertBuilder
import com.pplus.prnumberuser.apps.common.builder.OnAlertResultListener
import com.pplus.prnumberuser.apps.common.builder.data.AlertData
import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
import com.pplus.prnumberuser.core.network.ApiBuilder
import com.pplus.prnumberuser.core.network.model.dto.Page
import com.pplus.prnumberuser.core.network.model.dto.Plus
import com.pplus.prnumberuser.core.network.model.response.NewResultResponse
import com.pplus.prnumberuser.core.util.ToastUtil
import com.pplus.prnumberuser.databinding.ActivityPlusAlertBinding
import com.pplus.utils.BusProvider
import retrofit2.Call
import java.util.*

class PlusAlertActivity : BaseActivity() {
    override fun getPID(): String {
        return ""
    }

    private lateinit var binding: ActivityPlusAlertBinding

    override fun getLayoutView(): View {
        binding = ActivityPlusAlertBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun initializeView(savedInstanceState: Bundle?) {

        var session = SessionHelper.getCurrentSession()

        if (session == null) {
            session = CustomTabUtil.getSession()
        }

        val mPage = intent.getParcelableExtra<Page>(Const.DATA)

        if (!mPage!!.plus!!) {

            val builder = AlertBuilder.Builder()
            builder.setTitle(getString(R.string.word_notice_alert))
            builder.addContents(AlertData.MessageData(getString(R.string.format_msg_question_plus, mPage.name), AlertBuilder.MESSAGE_TYPE.TEXT, 4))
            builder.setLeftText(getString(R.string.word_cancel)).setRightText(getString(R.string.word_confirm))
            builder.setOnAlertResultListener(object : OnAlertResultListener {
                override fun onCancel() {
                    finish()
                }

                override fun onResult(event_alert: AlertBuilder.EVENT_ALERT?) {
                    when (event_alert) {
                        AlertBuilder.EVENT_ALERT.RIGHT -> {
                            val params = Plus()
                            params.no = mPage.no
                            showProgress("")
                            ApiBuilder.create().insertPlus(params).setCallback(object : PplusCallback<NewResultResponse<Plus>> {

                                override fun onResponse(call: Call<NewResultResponse<Plus>>, response: NewResultResponse<Plus>) {
                                    hideProgress()
                                    mPage.plus = true
                                    ToastUtil.show(this@PlusAlertActivity, R.string.msg_plus_ing)
                                    session!!.setSecondaryToolbarViews(BottomBarManager2.createRemoteViews(this@PlusAlertActivity, mPage), BottomBarManager2.clickableIDs,
                                            BottomBarManager2.getOnClickPendingIntent(this@PlusAlertActivity))
                                    val bus = BusProviderData()
                                    bus.subData = mPage
                                    bus.type = BusProviderData.BUS_MAIN
                                    BusProvider.getInstance().post(bus)
                                    finish()
                                }

                                override fun onFailure(call: Call<NewResultResponse<Plus>>, t: Throwable, response: NewResultResponse<Plus>) {
                                    hideProgress()
                                    finish()
                                }
                            }).build().call()
                        }
                        else -> {
                            finish()
                        }
                    }
                }
            }).builder().show(this)

        } else {
            val builder = AlertBuilder.Builder()
            builder.setTitle(getString(R.string.word_notice_alert))
            builder.addContents(AlertData.MessageData(getString(R.string.format_msg_question_cancel_plus, mPage.name), AlertBuilder.MESSAGE_TYPE.TEXT, 3))
            builder.setLeftText(getString(R.string.word_cancel)).setRightText(getString(R.string.word_confirm))
            builder.setOnAlertResultListener(object : OnAlertResultListener {

                override fun onCancel() {
                    finish()
                }

                override fun onResult(event_alert: AlertBuilder.EVENT_ALERT) {

                    when (event_alert) {
                        AlertBuilder.EVENT_ALERT.RIGHT -> {
                            val params = HashMap<String, String>()
                            params["no"] = "" + mPage.no!!
                            showProgress("")
                            ApiBuilder.create().deletePlusByPage(params).setCallback(object : PplusCallback<NewResultResponse<Any>> {

                                override fun onResponse(call: Call<NewResultResponse<Any>>, response: NewResultResponse<Any>) {
                                    hideProgress()
                                    mPage.plus = false
                                    ToastUtil.show(this@PlusAlertActivity, R.string.msg_plus_released)
                                    session!!.setSecondaryToolbarViews(BottomBarManager2.createRemoteViews(this@PlusAlertActivity, mPage), BottomBarManager2.clickableIDs,
                                            BottomBarManager2.getOnClickPendingIntent(this@PlusAlertActivity))
                                    val bus = BusProviderData()
                                    bus.subData = mPage
                                    bus.type = BusProviderData.BUS_MAIN
                                    BusProvider.getInstance().post(bus)
                                    finish()
                                }

                                override fun onFailure(call: Call<NewResultResponse<Any>>, t: Throwable, response: NewResultResponse<Any>) {
                                    hideProgress()
                                    finish()
                                }
                            }).build().call()
                        }
                        else -> {
                            finish()
                        }
                    }
                }
            }).builder().show(this)
        }
    }

}
