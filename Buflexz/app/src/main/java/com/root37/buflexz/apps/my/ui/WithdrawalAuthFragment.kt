package com.root37.buflexz.apps.my.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.pplus.networks.common.PplusCallback
import com.pplus.utils.part.format.FormatUtil
import com.pplus.utils.part.utils.StringUtils
import com.root37.buflexz.R
import com.root37.buflexz.apps.common.mgmt.LoginInfoManager
import com.root37.buflexz.apps.common.ui.base.BaseFragment
import com.root37.buflexz.core.network.ApiBuilder
import com.root37.buflexz.core.network.model.dto.WalletRes
import com.root37.buflexz.core.network.model.response.NewResultResponse
import com.root37.buflexz.databinding.FragmentWalletMakeAuthBinding
import com.root37.buflexz.databinding.FragmentWithdrawalAuthBinding
import retrofit2.Call

class WithdrawalAuthFragment : BaseFragment<WithdrawalAuthActivity>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

            //            param1 = it.getString(ARG_PARAM1)
            //            param2 = it.getString(ARG_PARAM2)
        }
    }

    private var _binding: FragmentWithdrawalAuthBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        _binding = FragmentWithdrawalAuthBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun getPID(): String? {
        return ""
    }

    var mReason = ""

    override fun init() {


        binding.textWithdrawalAuthReason1.setOnClickListener {
            binding.textWithdrawalAuthReason1.isSelected = true
            binding.textWithdrawalAuthReason2.isSelected = false
            binding.textWithdrawalAuthReason3.isSelected = false
            binding.textWithdrawalAuthReason4.isSelected = false
            binding.textWithdrawalAuthReasonEtc.isSelected = false
            binding.editWithdrawalAuthReasonEtc.visibility = View.GONE
            binding.textWithdrawalAuthSendEmail.setBackgroundResource(R.drawable.bg_48b778_radius_30)
            mReason = binding.textWithdrawalAuthReason1.text.toString()
        }

        binding.textWithdrawalAuthReason2.setOnClickListener {
            binding.textWithdrawalAuthReason1.isSelected = false
            binding.textWithdrawalAuthReason2.isSelected = true
            binding.textWithdrawalAuthReason3.isSelected = false
            binding.textWithdrawalAuthReason4.isSelected = false
            binding.textWithdrawalAuthReasonEtc.isSelected = false
            binding.editWithdrawalAuthReasonEtc.visibility = View.GONE
            binding.textWithdrawalAuthSendEmail.setBackgroundResource(R.drawable.bg_48b778_radius_30)
            mReason = binding.textWithdrawalAuthReason2.text.toString()
        }

        binding.textWithdrawalAuthReason3.setOnClickListener {
            binding.textWithdrawalAuthReason1.isSelected = false
            binding.textWithdrawalAuthReason2.isSelected = false
            binding.textWithdrawalAuthReason3.isSelected = true
            binding.textWithdrawalAuthReason4.isSelected = false
            binding.textWithdrawalAuthReasonEtc.isSelected = false
            binding.editWithdrawalAuthReasonEtc.visibility = View.GONE
            binding.textWithdrawalAuthSendEmail.setBackgroundResource(R.drawable.bg_48b778_radius_30)
            mReason = binding.textWithdrawalAuthReason3.text.toString()
        }

        binding.textWithdrawalAuthReason4.setOnClickListener {
            binding.textWithdrawalAuthReason1.isSelected = false
            binding.textWithdrawalAuthReason2.isSelected = false
            binding.textWithdrawalAuthReason3.isSelected = false
            binding.textWithdrawalAuthReason4.isSelected = true
            binding.textWithdrawalAuthReasonEtc.isSelected = false
            binding.editWithdrawalAuthReasonEtc.visibility = View.GONE
            binding.textWithdrawalAuthSendEmail.setBackgroundResource(R.drawable.bg_48b778_radius_30)
            mReason = binding.textWithdrawalAuthReason4.text.toString()
        }

        binding.textWithdrawalAuthReasonEtc.setOnClickListener {
            binding.textWithdrawalAuthReason1.isSelected = false
            binding.textWithdrawalAuthReason2.isSelected = false
            binding.textWithdrawalAuthReason3.isSelected = false
            binding.textWithdrawalAuthReason4.isSelected = false
            binding.textWithdrawalAuthReasonEtc.isSelected = true
            binding.editWithdrawalAuthReasonEtc.visibility = View.VISIBLE
            binding.textWithdrawalAuthSendEmail.setBackgroundResource(R.drawable.bg_48b778_radius_30)
        }

        binding.textWithdrawalAuthSendEmail.setOnClickListener {
            if (StringUtils.isEmpty(mReason) && !binding.textWithdrawalAuthReasonEtc.isSelected) {
                showAlert(R.string.msg_select_withdrawal_reason)
                return@setOnClickListener
            }

            if (binding.textWithdrawalAuthReasonEtc.isSelected) {
                mReason = binding.editWithdrawalAuthReasonEtc.text.toString().trim()
                if (StringUtils.isEmpty(mReason)) {
                    showAlert(R.string.msg_input_withdrawal_reason)
                    return@setOnClickListener
                }
            }

            val params = HashMap<String, String>()
            params["type"] = "wallet"
            params["email"] = LoginInfoManager.getInstance().member!!.email!!
            params["language"] = LoginInfoManager.getInstance().member!!.language!!
            showProgress("")
            ApiBuilder.create().sendEmailForAuth(params).setCallback(object : PplusCallback<NewResultResponse<String>> {
                override fun onResponse(call: Call<NewResultResponse<String>>?, response: NewResultResponse<String>?) {
                    hideProgress()
                    if (response?.result != null) {
                        val authNumber = response.result!!
                        getParentActivity().confirm(mReason, authNumber)
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
        fun newInstance() = WithdrawalAuthFragment().apply {
            arguments = Bundle().apply {

                //                putString(ARG_PARAM1, param1)
                //                putString(ARG_PARAM2, param2)
            }
        }
    }
}