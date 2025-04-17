package com.lejel.wowbox.apps.my.ui

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
import com.lejel.wowbox.Const
import com.lejel.wowbox.R
import com.lejel.wowbox.apps.common.builder.AlertBuilder
import com.lejel.wowbox.apps.common.builder.OnAlertResultListener
import com.lejel.wowbox.apps.common.builder.data.AlertData
import com.lejel.wowbox.apps.common.mgmt.LoginInfoManager
import com.lejel.wowbox.apps.common.ui.base.BaseFragment
import com.lejel.wowbox.core.network.ApiBuilder
import com.lejel.wowbox.core.network.model.response.NewResultResponse
import com.lejel.wowbox.databinding.FragmentWithdrawalAuthConfirmBinding
import retrofit2.Call

/**
 * A simple [Fragment] subclass.
 * Use the [WithdrawalAuthConfirmFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class WithdrawalAuthConfirmFragment : BaseFragment<WithdrawalAuthActivity>() {

    private var mReason: String? = null
    private var mAuthNumber: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            mReason = it.getString(Const.REASON)
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
        binding.textWithdrawalAuthConfirmEmail.text = LoginInfoManager.getInstance().member!!.email
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

            val builder = AlertBuilder.Builder()
            builder.addContents(AlertData.MessageData(getString(R.string.msg_alert_withdrawal), AlertBuilder.MESSAGE_TYPE.TEXT, 2))
            builder.setLeftText(getString(R.string.word_cancel)).setRightText(getString(R.string.word_confirm))
            builder.setOnAlertResultListener(object : OnAlertResultListener {

                override fun onCancel() {

                }

                override fun onResult(event_alert: AlertBuilder.EVENT_ALERT) {
                    when (event_alert) {
                        AlertBuilder.EVENT_ALERT.RIGHT -> {
                            val params = HashMap<String, String>()
                            params["reason"] = mReason!!
                            params["authNumber"] = authNumber
                            showProgress("")
                            ApiBuilder.create().withdrawal(params).setCallback(object : PplusCallback<NewResultResponse<Any>> {
                                override fun onResponse(call: Call<NewResultResponse<Any>>?, response: NewResultResponse<Any>?) {
                                    hideProgress()
                                    setEvent(requireActivity(), "withdrawal")
                                    val intent = Intent(requireActivity(), WithdrawalCompleteActivity::class.java)
                                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                                    startActivity(intent)
                                }

                                override fun onFailure(call: Call<NewResultResponse<Any>>?, t: Throwable?, response: NewResultResponse<Any>?) {
                                    hideProgress()
                                    if(response?.code == 664){
                                        showAlert(R.string.msg_not_matched_auth_number)
                                    }
                                }
                            }).build().call()
                        }

                        else -> {

                        }
                    }
                }
            }).builder().show(requireActivity())
        }

        binding.textWithdrawalAuthConfirmResend.setOnClickListener {
            val params = HashMap<String, String>()
            params["type"] = "withdrawal"
            params["email"] = LoginInfoManager.getInstance().member!!.email!!
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
        fun newInstance(reason: String, authNumber: String) = WithdrawalAuthConfirmFragment().apply {
            arguments = Bundle().apply {
                putString(Const.REASON, reason)
                putString(Const.NUMBER, authNumber)
            }
        }
    }
}