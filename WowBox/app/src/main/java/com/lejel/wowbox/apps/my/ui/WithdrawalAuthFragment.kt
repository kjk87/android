package com.lejel.wowbox.apps.my.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import com.lejel.wowbox.Const
import com.lejel.wowbox.R
import com.lejel.wowbox.apps.common.mgmt.LoginInfoManager
import com.lejel.wowbox.apps.common.ui.base.BaseFragment
import com.lejel.wowbox.apps.verify.ui.SelectVerifyTypeActivity
import com.lejel.wowbox.apps.verify.ui.VerifySmsActivity
import com.lejel.wowbox.apps.verify.ui.VerifyWhatsAppActivity
import com.lejel.wowbox.core.network.ApiBuilder
import com.lejel.wowbox.core.network.model.response.NewResultResponse
import com.lejel.wowbox.databinding.FragmentWithdrawalAuthBinding
import com.pplus.networks.common.PplusCallback
import com.pplus.utils.part.utils.StringUtils
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
            binding.textWithdrawalAuthSend.setBackgroundResource(R.drawable.bg_ea5506_radius_27)
            mReason = binding.textWithdrawalAuthReason1.text.toString()
        }

        binding.textWithdrawalAuthReason2.setOnClickListener {
            binding.textWithdrawalAuthReason1.isSelected = false
            binding.textWithdrawalAuthReason2.isSelected = true
            binding.textWithdrawalAuthReason3.isSelected = false
            binding.textWithdrawalAuthReason4.isSelected = false
            binding.textWithdrawalAuthReasonEtc.isSelected = false
            binding.editWithdrawalAuthReasonEtc.visibility = View.GONE
            binding.textWithdrawalAuthSend.setBackgroundResource(R.drawable.bg_ea5506_radius_27)
            mReason = binding.textWithdrawalAuthReason2.text.toString()
        }

        binding.textWithdrawalAuthReason3.setOnClickListener {
            binding.textWithdrawalAuthReason1.isSelected = false
            binding.textWithdrawalAuthReason2.isSelected = false
            binding.textWithdrawalAuthReason3.isSelected = true
            binding.textWithdrawalAuthReason4.isSelected = false
            binding.textWithdrawalAuthReasonEtc.isSelected = false
            binding.editWithdrawalAuthReasonEtc.visibility = View.GONE
            binding.textWithdrawalAuthSend.setBackgroundResource(R.drawable.bg_ea5506_radius_27)
            mReason = binding.textWithdrawalAuthReason3.text.toString()
        }

        binding.textWithdrawalAuthReason4.setOnClickListener {
            binding.textWithdrawalAuthReason1.isSelected = false
            binding.textWithdrawalAuthReason2.isSelected = false
            binding.textWithdrawalAuthReason3.isSelected = false
            binding.textWithdrawalAuthReason4.isSelected = true
            binding.textWithdrawalAuthReasonEtc.isSelected = false
            binding.editWithdrawalAuthReasonEtc.visibility = View.GONE
            binding.textWithdrawalAuthSend.setBackgroundResource(R.drawable.bg_ea5506_radius_27)
            mReason = binding.textWithdrawalAuthReason4.text.toString()
        }

        binding.textWithdrawalAuthReasonEtc.setOnClickListener {
            binding.textWithdrawalAuthReason1.isSelected = false
            binding.textWithdrawalAuthReason2.isSelected = false
            binding.textWithdrawalAuthReason3.isSelected = false
            binding.textWithdrawalAuthReason4.isSelected = false
            binding.textWithdrawalAuthReasonEtc.isSelected = true
            binding.editWithdrawalAuthReasonEtc.visibility = View.VISIBLE
            binding.textWithdrawalAuthSend.setBackgroundResource(R.drawable.bg_ea5506_radius_27)
        }

        binding.textWithdrawalAuthSend.setOnClickListener {
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

            val intent = Intent(requireActivity(), SelectVerifyTypeActivity::class.java)
            intent.putExtra(Const.MOOBILE_NUMBER, LoginInfoManager.getInstance().member!!.mobileNumber)
            selectVerifyTypeLauncher.launch(intent)

//            val params = HashMap<String, String>()
//            params["type"] = "wallet"
//            params["email"] = LoginInfoManager.getInstance().member!!.email!!
//            params["language"] = LoginInfoManager.getInstance().member!!.language!!
//            showProgress("")
//            ApiBuilder.create().sendEmailForAuth(params).setCallback(object : PplusCallback<NewResultResponse<String>> {
//                override fun onResponse(call: Call<NewResultResponse<String>>?, response: NewResultResponse<String>?) {
//                    hideProgress()
//                    if (response?.result != null) {
//                        val authNumber = response.result!!
//                        getParentActivity().confirm(mReason, authNumber)
//                    }
//                }
//
//                override fun onFailure(call: Call<NewResultResponse<String>>?, t: Throwable?, response: NewResultResponse<String>?) {
//                    hideProgress()
//                }
//            }).build().call()
        }
    }

    val selectVerifyTypeLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val mobileNumber = result.data!!.getStringExtra(Const.MOOBILE_NUMBER)!!
            val verifyType = result.data!!.getStringExtra(Const.VERIFY_TYPE)!!
            when (verifyType) {
                "whatsapp" -> {
                    sendWhatsApp(mobileNumber)
                }
                else -> {
                    sendSms(mobileNumber)
                }
            }
        }
    }

    private fun sendWhatsApp(mobileNumber: String) {
        val params = HashMap<String, String>()
        params["mobileNumber"] = mobileNumber
        params["type"] = "withdrawal"
        showProgress("")
        ApiBuilder.create().sendWhatsapp(params).setCallback(object : PplusCallback<NewResultResponse<Any>> {
            override fun onResponse(call: Call<NewResultResponse<Any>>?, response: NewResultResponse<Any>?) {
                hideProgress()
                val intent = Intent(requireActivity(), VerifyWhatsAppActivity::class.java)
                intent.putExtra(Const.MOOBILE_NUMBER, mobileNumber)
                intent.putExtra(Const.TYPE, "withdrawal")
                intent.putExtra(Const.DATA, mReason)
                defaultLauncher.launch(intent)
            }

            override fun onFailure(call: Call<NewResultResponse<Any>>?, t: Throwable?, response: NewResultResponse<Any>?) {
                hideProgress()
            }
        }).build().call()
    }

    private fun sendSms(mobileNumber: String) {
        val params = HashMap<String, String>()
        params["mobileNumber"] = mobileNumber
        params["type"] = "withdrawal"
        showProgress("")
        ApiBuilder.create().sendSms(params).setCallback(object : PplusCallback<NewResultResponse<Any>> {
            override fun onResponse(call: Call<NewResultResponse<Any>>?, response: NewResultResponse<Any>?) {
                hideProgress()
                val intent = Intent(requireActivity(), VerifySmsActivity::class.java)
                intent.putExtra(Const.MOOBILE_NUMBER, mobileNumber)
                intent.putExtra(Const.TYPE, "withdrawal")
                intent.putExtra(Const.DATA, mReason)
                defaultLauncher.launch(intent)
            }

            override fun onFailure(call: Call<NewResultResponse<Any>>?, t: Throwable?, response: NewResultResponse<Any>?) {
                hideProgress()
            }
        }).build().call()
    }

    val defaultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
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