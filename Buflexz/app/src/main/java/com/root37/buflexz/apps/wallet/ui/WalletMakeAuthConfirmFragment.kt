package com.root37.buflexz.apps.wallet.ui

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.pplus.networks.common.PplusCallback
import com.pplus.utils.part.utils.StringUtils
import com.root37.buflexz.Const
import com.root37.buflexz.R
import com.root37.buflexz.apps.common.builder.AlertBuilder
import com.root37.buflexz.apps.common.builder.OnAlertResultListener
import com.root37.buflexz.apps.common.builder.data.AlertData
import com.root37.buflexz.apps.common.mgmt.LoginInfoManager
import com.root37.buflexz.apps.common.ui.base.BaseFragment
import com.root37.buflexz.core.network.ApiBuilder
import com.root37.buflexz.core.network.model.response.NewResultResponse
import com.root37.buflexz.databinding.FragmentWithdrawalAuthConfirmBinding
import retrofit2.Call

/**
 * A simple [Fragment] subclass.
 * Use the [WalletMakeAuthConfirmFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class WalletMakeAuthConfirmFragment : BaseFragment<WalletMakeActivity>() {

    private var mAuthEmail: String? = null
    private var mAuthNumber: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            mAuthEmail = it.getString(Const.AUTH_EMAIL)
            mAuthNumber = it.getString(Const.NUMBER)
        }
    }

    private var _binding: FragmentWithdrawalAuthConfirmBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        _binding = FragmentWithdrawalAuthConfirmBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun getPID(): String? {
        return ""
    }

    override fun init() {
        binding.textWithdrawalAuthConfirmEmail.text = mAuthEmail
        binding.editWithdrawalAuthConfirmNumber.setSingleLine()

        binding.editWithdrawalAuthConfirmNumber.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {
                if (p0?.length == 6) {
                    binding.textWithdrawalAuthConirm.setBackgroundResource(R.drawable.bg_48b778_radius_30)
                } else {
                    binding.textWithdrawalAuthConirm.setBackgroundResource(R.drawable.bg_595959_radius_30)
                }
            }
        })

        binding.textWithdrawalAuthConirm.setOnClickListener {
            val authNumber = binding.editWithdrawalAuthConfirmNumber.text.toString().trim()
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

        binding.textWithdrawalAuthConfirmResend.setOnClickListener {
            val params = HashMap<String, String>()
            params["type"] = "wallet"
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
                }
            }).build().call()
        }
    }

    companion object {

        @JvmStatic
        fun newInstance(authEmail: String, authNumber: String) = WalletMakeAuthConfirmFragment().apply {
            arguments = Bundle().apply {
                putString(Const.AUTH_EMAIL, authEmail)
                putString(Const.NUMBER, authNumber)
            }
        }
    }
}