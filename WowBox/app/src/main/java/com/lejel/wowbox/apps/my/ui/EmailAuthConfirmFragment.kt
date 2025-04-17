package com.lejel.wowbox.apps.my.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.pplus.networks.common.PplusCallback
import com.pplus.utils.part.utils.StringUtils
import com.lejel.wowbox.Const
import com.lejel.wowbox.R
import com.lejel.wowbox.apps.common.mgmt.LoginInfoManager
import com.lejel.wowbox.apps.common.ui.base.BaseFragment
import com.lejel.wowbox.core.network.ApiBuilder
import com.lejel.wowbox.core.network.model.response.NewResultResponse
import com.lejel.wowbox.databinding.FragmentEmailAuthConfirmBinding
import com.lejel.wowbox.databinding.FragmentWithdrawalAuthConfirmBinding
import retrofit2.Call

/**
 * A simple [Fragment] subclass.
 * Use the [EmailAuthConfirmFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class EmailAuthConfirmFragment : BaseFragment<EmailAuthActivity>() {

    private var mAuthEmail: String? = null
    private var mAuthNumber: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            mAuthEmail = it.getString(Const.AUTH_EMAIL)
            mAuthNumber = it.getString(Const.NUMBER)
        }
    }

    private var _binding: FragmentEmailAuthConfirmBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        _binding = FragmentEmailAuthConfirmBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun getPID(): String? {
        return ""
    }

    override fun init() {
        binding.textEmailAuthConfirmEmail.text = mAuthEmail
        binding.editEmailAuthConfirmNumber.setSingleLine()

        binding.editEmailAuthConfirmNumber.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {
                if (p0?.length == 6) {
                    binding.textEmailAuthConfirm.setBackgroundResource(R.drawable.bg_ea5506_radius_27)
                } else {
                    binding.textEmailAuthConfirm.setBackgroundResource(R.drawable.bg_dcdcdc_radius_27)
                }
            }
        })

        binding.textEmailAuthConfirm.setOnClickListener {
            val authNumber = binding.editEmailAuthConfirmNumber.text.toString().trim()
            if (StringUtils.isEmpty(authNumber)) {
                showAlert(R.string.msg_input_auth_number)
                return@setOnClickListener
            }

            if (mAuthNumber != authNumber) {
                showAlert(R.string.msg_not_matched_auth_number)
                return@setOnClickListener
            }

            val params = HashMap<String, String>()
            params["authEmail"] = mAuthEmail!!
            params["authNumber"] = authNumber
            showProgress("")
            ApiBuilder.create().authWalletEmail(params).setCallback(object : PplusCallback<NewResultResponse<Boolean>> {
                override fun onResponse(call: Call<NewResultResponse<Boolean>>?, response: NewResultResponse<Boolean>?) {
                    hideProgress()
                    if(response?.result != null && response.result!!){
                        showAlert(R.string.msg_complete_make_buff_wallet)
                        getParentActivity().finish()
                    }else{
                        showAlert(R.string.msg_failed_make_buff_wallet)
                    }

                }

                override fun onFailure(call: Call<NewResultResponse<Boolean>>?, t: Throwable?, response: NewResultResponse<Boolean>?) {
                    hideProgress()
                    if(response?.code == 664){
                        showAlert(R.string.msg_not_matched_auth_number)
                    }
                }
            }).build().call()
        }

        binding.textEmailAuthConfirmResend.setOnClickListener {
            val params = HashMap<String, String>()
            params["type"] = "authEmail"
            params["email"] = mAuthEmail!!
            params["language"] = LoginInfoManager.getInstance().member!!.language!!
            showProgress("")
            ApiBuilder.create().sendEmailForAuth(params).setCallback(object : PplusCallback<NewResultResponse<String>> {
                override fun onResponse(call: Call<NewResultResponse<String>>?, response: NewResultResponse<String>?) {
                    hideProgress()
                    if (response?.result != null) {
                        mAuthNumber = response.result!!
                        showAlert(R.string.msg_resend_email_complete)
                    }
                }

                override fun onFailure(call: Call<NewResultResponse<String>>?, t: Throwable?, response: NewResultResponse<String>?) {
                    hideProgress()
                    if (response?.code == 504) {
                        showAlert(R.string.msg_alread_joined_email)
                    }
                }
            }).build().call()
        }
    }

    companion object {

        @JvmStatic
        fun newInstance(authEmail: String, authNumber: String) = EmailAuthConfirmFragment().apply {
            arguments = Bundle().apply {
                putString(Const.AUTH_EMAIL, authEmail)
                putString(Const.NUMBER, authNumber)
            }
        }
    }
}