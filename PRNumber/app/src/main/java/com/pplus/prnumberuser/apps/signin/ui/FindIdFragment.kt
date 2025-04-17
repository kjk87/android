package com.pplus.prnumberuser.apps.signin.ui


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.pplus.networks.common.PplusCallback
import com.pplus.prnumberuser.Const
import com.pplus.prnumberuser.R
import com.pplus.prnumberuser.apps.common.ui.base.BaseFragment
import com.pplus.prnumberuser.core.network.ApiBuilder
import com.pplus.prnumberuser.core.network.model.dto.User
import com.pplus.prnumberuser.core.network.model.dto.Verification
import com.pplus.prnumberuser.core.network.model.response.NewResultResponse
import com.pplus.prnumberuser.databinding.FragmentFindIdBinding
import com.pplus.utils.part.utils.StringUtils
import retrofit2.Call
import java.util.*


class FindIdFragment : BaseFragment<FindIdActivity>() {

    private var verification: Verification? = null
    private var mobileNumber: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
    }

    override fun getPID(): String {
        return "Login_id find"
    }

    private var _binding: FragmentFindIdBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        _binding = FragmentFindIdBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun init() {

        binding.textFindIdAuth.setOnClickListener {

            mobileNumber = binding.editFindIdMobileNumber.text.toString().trim()
            if (StringUtils.isEmpty(mobileNumber)) {
                showAlert(R.string.msg_input_mobile_number)
                return@setOnClickListener
            }
            request(mobileNumber!!)
        }

        binding.textFindIdConfirm.setOnClickListener {
            confirm()
        }
    }

    private fun request(mobile: String) {

        val params = HashMap<String, String>()
        params["media"] = "sms"
        params["mobile"] = mobile
        params["type"] = "findId"
        params["appType"] = Const.APP_TYPE
        showProgress("")
        ApiBuilder.create().verification(params).setCallback(object : PplusCallback<NewResultResponse<Verification>> {

            override fun onResponse(call: Call<NewResultResponse<Verification>>, response: NewResultResponse<Verification>) {

                if(!isAdded){
                    return
                }
                hideProgress()
                verification = response.data
                binding.textFindIdAuth.setText(R.string.word_reAuth)
                binding.editFindIdNumber.requestFocus()
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

        val number = binding.editFindIdNumber.text.toString().trim()
        if (StringUtils.isEmpty(number)) {
            showAlert(R.string.hint_auth_number)
            return
        }

        val params = HashMap<String, String>()
        params["number"] = number
        params["token"] = verification!!.token
        params["mobile"] = mobileNumber!!
        params["media"] = "sms"
        params["appType"] = Const.APP_TYPE
        showProgress("")
        ApiBuilder.create().getUserByVerification(params).setCallback(object : PplusCallback<NewResultResponse<User>> {

            override fun onResponse(call: Call<NewResultResponse<User>>, response: NewResultResponse<User>) {

                hideProgress()
                getParentActivity().findIdResult(response.data)
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
