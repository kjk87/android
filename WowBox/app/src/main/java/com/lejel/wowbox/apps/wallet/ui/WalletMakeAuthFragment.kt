package com.lejel.wowbox.apps.wallet.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.pplus.networks.common.PplusCallback
import com.pplus.utils.part.format.FormatUtil
import com.pplus.utils.part.utils.StringUtils
import com.lejel.wowbox.R
import com.lejel.wowbox.apps.common.mgmt.LoginInfoManager
import com.lejel.wowbox.apps.common.ui.base.BaseFragment
import com.lejel.wowbox.core.network.ApiBuilder
import com.lejel.wowbox.core.network.model.dto.WalletRes
import com.lejel.wowbox.core.network.model.response.NewResultResponse
import com.lejel.wowbox.databinding.FragmentWalletMakeAuthBinding
import retrofit2.Call

class WalletMakeAuthFragment : BaseFragment<WalletMakeActivity>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

            //            param1 = it.getString(ARG_PARAM1)
            //            param2 = it.getString(ARG_PARAM2)
        }
    }

    private var _binding: FragmentWalletMakeAuthBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        _binding = FragmentWalletMakeAuthBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun getPID(): String? {
        return ""
    }

    override fun init() {

        binding.editWalletMakeAuthEmail.setSingleLine()

        binding.editWalletMakeAuthEmail.setText(LoginInfoManager.getInstance().member!!.email)

        binding.textWalletMakeAuth.setOnClickListener {
            val email = binding.editWalletMakeAuthEmail.text.toString()

            if (StringUtils.isEmpty(email)) {
                showAlert(R.string.msg_input_email)
                return@setOnClickListener
            }

            if (!FormatUtil.isEmailAddress(email)) {
                showAlert(R.string.msg_valid_email)
                return@setOnClickListener
            }

            duplicateUser(email)
        }
    }

    private fun duplicateUser(email:String){
        val params = HashMap<String, String>()
        params["email"] = email
        showProgress("")
        ApiBuilder.create().walletDuplicateUser(params).setCallback(object : PplusCallback<NewResultResponse<WalletRes>>{
            override fun onResponse(call: Call<NewResultResponse<WalletRes>>?, response: NewResultResponse<WalletRes>?) {
                hideProgress()
                if(response?.result != null){
                    if(response.result!!.result == "SUCCESS"){//미가입
                        send(email)
                    }else{
                        showAlert(R.string.msg_alread_joined_email)
                    }
                }
            }

            override fun onFailure(call: Call<NewResultResponse<WalletRes>>?, t: Throwable?, response: NewResultResponse<WalletRes>?) {
                hideProgress()
            }
        }).build().call()
    }

    private fun send(email: String){
        val params = HashMap<String, String>()
        params["type"] = "wallet"
        params["email"] = email
        params["language"] = LoginInfoManager.getInstance().member!!.language!!
        showProgress("")
        ApiBuilder.create().sendEmailForAuth(params).setCallback(object : PplusCallback<NewResultResponse<String>> {
            override fun onResponse(call: Call<NewResultResponse<String>>?, response: NewResultResponse<String>?) {
                hideProgress()
                if (response?.result != null) {
                    val authNumber = response.result!!
                    getParentActivity().confirm(email, authNumber)
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

    companion object {

        @JvmStatic
        fun newInstance() = WalletMakeAuthFragment().apply {
            arguments = Bundle().apply {

                //                putString(ARG_PARAM1, param1)
                //                putString(ARG_PARAM2, param2)
            }
        }
    }
}