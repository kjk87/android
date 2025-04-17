package com.lejel.wowbox.apps.my.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.lejel.wowbox.R
import com.lejel.wowbox.apps.common.mgmt.LoginInfoManager
import com.lejel.wowbox.apps.common.ui.base.BaseFragment
import com.lejel.wowbox.core.network.ApiBuilder
import com.lejel.wowbox.core.network.model.response.NewResultResponse
import com.lejel.wowbox.databinding.FragmentEmailAuthBinding
import com.pplus.networks.common.PplusCallback
import com.pplus.utils.part.format.FormatUtil
import com.pplus.utils.part.utils.StringUtils
import retrofit2.Call

class EmailAuthFragment : BaseFragment<EmailAuthActivity>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

            //            param1 = it.getString(ARG_PARAM1)
            //            param2 = it.getString(ARG_PARAM2)
        }
    }

    private var _binding: FragmentEmailAuthBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        _binding = FragmentEmailAuthBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun getPID(): String? {
        return ""
    }

    override fun init() {

        binding.editEmailAuthEmail.setSingleLine()

        binding.editEmailAuthEmail.setText(LoginInfoManager.getInstance().member!!.email)

        binding.textEmailAuth.setOnClickListener {
            val email = binding.editEmailAuthEmail.text.toString()

            if (StringUtils.isEmpty(email)) {
                showAlert(R.string.msg_input_email)
                return@setOnClickListener
            }

            if (!FormatUtil.isEmailAddress(email)) {
                showAlert(R.string.msg_valid_email)
                return@setOnClickListener
            }

            send(email)
        }
    }

    private fun send(email: String) {
        val params = HashMap<String, String>()
        params["type"] = "emailAuth"
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
        fun newInstance() = EmailAuthFragment().apply {
            arguments = Bundle().apply {

                //                putString(ARG_PARAM1, param1)
                //                putString(ARG_PARAM2, param2)
            }
        }
    }
}