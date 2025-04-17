package com.pplus.prnumberbiz.apps.signin.ui


import android.os.Bundle
import android.view.View
import com.pple.pplus.utils.part.utils.StringUtils
import com.pplus.prnumberbiz.R
import com.pplus.prnumberbiz.apps.common.ui.base.BaseFragment
import com.pplus.prnumberbiz.core.network.ApiBuilder
import com.pplus.prnumberbiz.core.network.model.dto.User
import com.pplus.prnumberbiz.core.network.model.dto.Verification
import com.pplus.prnumberbiz.core.network.model.response.NewResultResponse
import kotlinx.android.synthetic.main.fragment_find_id.*
import network.common.PplusCallback
import retrofit2.Call
import java.util.*


class FindIdFragment : BaseFragment<FindIdActivity>() {

    private var verification: Verification? = null
    private var mobileNumber: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        if (arguments != null) {
        }
    }

    override fun getPID(): String {
        return "Login_id_find"
    }

    override fun getLayoutResourceId(): Int {
        return R.layout.fragment_find_id
    }

    override fun initializeView(container: View?) {

    }

    override fun init() {

        text_find_id_auth.setOnClickListener {

            mobileNumber = edit_find_id_mobileNumber.text.toString().trim()
            if (StringUtils.isEmpty(mobileNumber)) {
                showAlert(R.string.msg_input_mobile_number)
                return@setOnClickListener
            }
            request(mobileNumber!!)
        }

        text_find_id_confirm.setOnClickListener {
            confirm()
        }
    }

    private fun request(mobile: String) {

        val params = HashMap<String, String>()
        params["media"] = "sms"
        params["mobile"] = mobile
        params["type"] = "findId"
        showProgress("")
        ApiBuilder.create().verification(params).setCallback(object : PplusCallback<NewResultResponse<Verification>> {

            override fun onResponse(call: Call<NewResultResponse<Verification>>, response: NewResultResponse<Verification>) {
                if (!isAdded) {
                    return
                }
                hideProgress()
                verification = response.data
                text_find_id_auth.setText(R.string.word_reAuth)
                edit_find_id_number.requestFocus()
                showAlert(R.string.msg_request_sms)
            }

            override fun onFailure(call: Call<NewResultResponse<Verification>>, t: Throwable, response: NewResultResponse<Verification>) {

                hideProgress()
                showAlert(R.string.msg_not_signed_mobile_number)
            }
        }).build().call()
    }

    private fun confirm() {

        if (verification == null) {
            showAlert(R.string.msg_request_verification)
            return
        }

        val number = edit_find_id_number.text.toString().trim()
        if (StringUtils.isEmpty(number)) {
            showAlert(R.string.hint_auth_number)
            return
        }

        val params = HashMap<String, String>()
        params["number"] = number
        params["token"] = verification!!.token
        params["mobile"] = mobileNumber!!
        params["media"] = "sms"
        showProgress("")
        ApiBuilder.create().getUserByVerification(params).setCallback(object : PplusCallback<NewResultResponse<User>> {

            override fun onResponse(call: Call<NewResultResponse<User>>, response: NewResultResponse<User>) {

                hideProgress()
                parentActivity.findIdResult(response.data)
            }

            override fun onFailure(call: Call<NewResultResponse<User>>, t: Throwable, response: NewResultResponse<User>) {

                hideProgress()
                showAlert(R.string.msg_failed_verification)
            }
        }).build().call()
    }

    companion object {

        fun newInstance(): FindIdFragment {

            val fragment = FindIdFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }
}// Required empty public constructor
