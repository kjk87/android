package com.pplus.luckybol.apps.signin.ui


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.pplus.luckybol.Const
import com.pplus.utils.part.utils.StringUtils
import com.pplus.luckybol.R
import com.pplus.luckybol.apps.common.builder.AlertBuilder
import com.pplus.luckybol.apps.common.builder.OnAlertResultListener
import com.pplus.luckybol.apps.common.builder.data.AlertData
import com.pplus.luckybol.apps.common.mgmt.LoginInfoManager
import com.pplus.luckybol.apps.common.ui.base.BaseFragment
import com.pplus.luckybol.core.network.ApiBuilder
import com.pplus.luckybol.core.network.model.dto.User
import com.pplus.luckybol.core.network.model.dto.Verification
import com.pplus.luckybol.core.network.model.response.NewResultResponse
import com.pplus.luckybol.databinding.FragmentFindPwStep1Binding
import com.pplus.networks.common.PplusCallback
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

        return "Login_password find"
    }

    private var _binding: FragmentFindPwStep1Binding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        _binding = FragmentFindPwStep1Binding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun init() {
        if (LoginInfoManager.getInstance().isMember) {
            val user = LoginInfoManager.getInstance().user

            binding.editFindPwMobileNumber.setText(user.mobile?.replace(Const.APP_TYPE+"##", ""))
            getParentActivity().setLoginId(user.loginId!!.replace(Const.APP_TYPE+"##", ""))
//            request(user.mobile, user.loginId)
        }
        binding.editFindPwId.setSingleLine()

        binding.textFindPwRequest.setOnClickListener {

            val id = binding.editFindPwId.text.toString().trim()

            if (StringUtils.isEmpty(id)) {
                showAlert(R.string.msg_input_id)
                return@setOnClickListener
            }

            val mobileNumber = binding.editFindPwMobileNumber.text.toString().trim()
            if (StringUtils.isEmpty(mobileNumber)) {
                showAlert(R.string.msg_input_mobile_number)
                return@setOnClickListener
            }

            confirm(id, mobileNumber)
        }

        binding.textFindPwStep1Confirm.setOnClickListener {
            confirm()
        }
    }

    private fun confirm(id: String, mobileNumber: String) {

        val params = HashMap<String, String>()
        params["loginId"] = id
        params["mobile"] = mobileNumber
        params["appType"] = Const.APP_TYPE
        showProgress("")
        ApiBuilder.create().getUserByLoginIdAndMobile(params).setCallback(object : PplusCallback<NewResultResponse<User>> {

            override fun onResponse(call: Call<NewResultResponse<User>>, response: NewResultResponse<User>) {

                hideProgress()

                if (response.data == null) {
                    showAlert(R.string.msg_not_user)
                } else {
                    if (response.data!!.verification!!.media != "external") {
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
                                    else -> {}
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
                                    AlertBuilder.EVENT_ALERT.RIGHT -> getParentActivity().verification(id, mobileNumber)
                                    else -> {}
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
        params["appType"] = Const.APP_TYPE
        showProgress("")
        ApiBuilder.create().verification(params).setCallback(object : PplusCallback<NewResultResponse<Verification>> {

            override fun onResponse(call: Call<NewResultResponse<Verification>>, response: NewResultResponse<Verification>) {
                if (!isAdded) {
                    return
                }

                hideProgress()
                getParentActivity().setLoginId(id)
                verification = response.data
                binding.editFindPwNumber.requestFocus()
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

        val number = binding.editFindPwNumber.text.toString().trim()
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
        params["appType"] = Const.APP_TYPE
        showProgress("")
        ApiBuilder.create().confirmVerification(params).setCallback(object : PplusCallback<NewResultResponse<Any>> {

            override fun onResponse(call: Call<NewResultResponse<Any>>, response: NewResultResponse<Any>) {
                if (!isAdded) {
                    return
                }
                hideProgress()
                verification!!.number = number
                getParentActivity().changePassword(verification!!)
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
