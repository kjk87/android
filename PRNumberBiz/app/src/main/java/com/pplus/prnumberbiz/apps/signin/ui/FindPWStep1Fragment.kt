package com.pplus.prnumberbiz.apps.signin.ui


import android.os.Bundle
import android.view.View
import com.pple.pplus.utils.part.utils.StringUtils
import com.pplus.prnumberbiz.R
import com.pplus.prnumberbiz.apps.common.builder.AlertBuilder
import com.pplus.prnumberbiz.apps.common.builder.OnAlertResultListener
import com.pplus.prnumberbiz.apps.common.builder.data.AlertData
import com.pplus.prnumberbiz.apps.common.mgmt.LoginInfoManager
import com.pplus.prnumberbiz.apps.common.ui.base.BaseFragment
import com.pplus.prnumberbiz.core.network.ApiBuilder
import com.pplus.prnumberbiz.core.network.model.dto.User
import com.pplus.prnumberbiz.core.network.model.dto.Verification
import com.pplus.prnumberbiz.core.network.model.response.NewResultResponse
import kotlinx.android.synthetic.main.fragment_find_pw_step1.*
import network.common.PplusCallback
import retrofit2.Call
import java.util.*


class FindPWStep1Fragment : BaseFragment<FindPWActivity>() {

    private var verification: Verification? = null

    private val mToken: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        if (arguments != null) {
        }
    }

    override fun onDestroy() {

        super.onDestroy()
    }

    override fun getPID(): String {

        return "Login_password_find"
    }

    override fun getLayoutResourceId(): Int {

        return R.layout.fragment_find_pw_step1
    }

    override fun init() {

        edit_find_pw_id.setSingleLine()
        edit_find_pw_mobileNumber.setSingleLine()
        if (LoginInfoManager.getInstance().isMember) {
            val user = LoginInfoManager.getInstance().user

            edit_find_pw_mobileNumber.setText(user.mobile)
            parentActivity.setLoginId(user.loginId!!)
//            request(user.mobile, user.loginId)
        }

        text_find_pw_request.setOnClickListener {

            val id = edit_find_pw_id.text.toString().trim()

            if (StringUtils.isEmpty(id)) {
                showAlert(R.string.msg_input_id)
                return@setOnClickListener
            }

            val mobileNumber = edit_find_pw_mobileNumber.text.toString().trim()
            if (StringUtils.isEmpty(mobileNumber)) {
                showAlert(R.string.msg_input_mobile_number)
                return@setOnClickListener
            }

            confirm(id, mobileNumber)
        }

        text_find_pw_step1_confirm.setOnClickListener {
            confirm()
        }
    }

    override fun initializeView(container: View?) {

    }

    private fun confirm(id: String, mobileNumber: String) {

        val params = HashMap<String, String>()
        params["loginId"] = id
        params["mobile"] = mobileNumber
        showProgress("")
        ApiBuilder.create().getUserByLoginIdAndMobile(params).setCallback(object : PplusCallback<NewResultResponse<User>> {

            override fun onResponse(call: Call<NewResultResponse<User>>, response: NewResultResponse<User>) {

                hideProgress()

                if (response.data == null) {
                    showAlert(R.string.msg_not_user)
                } else {
                    if (response.data.page == null) {
                        val builder = AlertBuilder.Builder()
                        builder.setTitle(getString(R.string.word_notice_alert))
                        builder.addContents(AlertData.MessageData(getString(R.string.msg_find_pw_alert1), AlertBuilder.MESSAGE_TYPE.TEXT, 2))
                        builder.addContents(AlertData.MessageData(getString(R.string.msg_find_pw_alert2), AlertBuilder.MESSAGE_TYPE.TEXT, 2))
                        builder.setLeftText(getString(R.string.word_cancel)).setRightText(getString(R.string.word_confirm))
                        builder.setOnAlertResultListener(object : OnAlertResultListener {

                            override fun onCancel() {

                            }

                            override fun onResult(event_alert: AlertBuilder.EVENT_ALERT) {

                                when (event_alert) {
                                    AlertBuilder.EVENT_ALERT.RIGHT -> {
                                        request(mobileNumber, id)
                                    }
                                }
                            }
                        }).builder().show(activity)

                    } else {

                        val builder = AlertBuilder.Builder()
                        builder.setTitle(getString(R.string.word_notice_alert))
                        builder.addContents(AlertData.MessageData(getString(R.string.msg_find_pw_verification_alert1), AlertBuilder.MESSAGE_TYPE.TEXT, 2))
                        builder.addContents(AlertData.MessageData(getString(R.string.msg_find_pw_verification_alert2), AlertBuilder.MESSAGE_TYPE.TEXT, 2))
                        builder.setLeftText(getString(R.string.word_cancel)).setRightText(getString(R.string.word_confirm))
                        builder.setOnAlertResultListener(object : OnAlertResultListener {

                            override fun onCancel() {

                            }

                            override fun onResult(event_alert: AlertBuilder.EVENT_ALERT) {

                                when (event_alert) {
                                    AlertBuilder.EVENT_ALERT.RIGHT -> parentActivity.verification(id, mobileNumber)
                                }
                            }
                        }).builder().show(activity)

                    }
                }

            }

            override fun onFailure(call: Call<NewResultResponse<User>>, t: Throwable, response: NewResultResponse<User>) {

                hideProgress()
                showAlert(R.string.msg_not_user)
            }
        }).build().call()
    }

    private fun request(mobile: String?, id: String?) {

        val params = HashMap<String, String>()
        params["media"] = "sms"
        params["mobile"] = mobile!!
        params["loginId"] = id!!
        params["type"] = "findPassword"
        showProgress("")
        ApiBuilder.create().verification(params).setCallback(object : PplusCallback<NewResultResponse<Verification>> {

            override fun onResponse(call: Call<NewResultResponse<Verification>>, response: NewResultResponse<Verification>) {
                if (!isAdded) {
                    return
                }
                hideProgress()
                parentActivity.setLoginId(id)
                verification = response.data
                edit_find_pw_number.requestFocus()
                showAlert(R.string.msg_request_sms)
            }

            override fun onFailure(call: Call<NewResultResponse<Verification>>, t: Throwable, response: NewResultResponse<Verification>) {
                if (!isAdded) {
                    return
                }
                hideProgress()
                showAlert(R.string.msg_not_signed_mobile_number)
            }
        }).build().call()
    }

    private fun confirm() {

        val number = edit_find_pw_number.text.toString().trim()
        if (StringUtils.isEmpty(number)) {
            showAlert(R.string.hint_auth_number)
            return
        }
        if (verification == null) {
            showAlert(R.string.msg_request_verification)
            return
        }

        val params = HashMap<String, String>()
        params["number"] = number
        params["token"] = verification!!.token
        params["media"] = "sms"
        showProgress("")
        ApiBuilder.create().confirmVerification(params).setCallback(object : PplusCallback<NewResultResponse<Any>> {

            override fun onResponse(call: Call<NewResultResponse<Any>>, response: NewResultResponse<Any>) {

                hideProgress()
                verification!!.number = number
                parentActivity.changePassword(verification!!)
            }

            override fun onFailure(call: Call<NewResultResponse<Any>>, t: Throwable, response: NewResultResponse<Any>) {

                if (!isAdded) {
                    return
                }
                hideProgress()
                showAlert(R.string.msg_not_user)
            }
        }).build().call()
    }

    companion object {

        fun newInstance(): FindPWStep1Fragment {

            val fragment = FindPWStep1Fragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }
}// Required empty public constructor
