package com.pplus.prnumberuser.apps.setting.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import com.pplus.networks.common.PplusCallback
import com.pplus.prnumberuser.Const
import com.pplus.prnumberuser.R
import com.pplus.prnumberuser.apps.common.mgmt.LoginInfoManager
import com.pplus.prnumberuser.apps.common.ui.base.BaseFragment
import com.pplus.prnumberuser.apps.signup.ui.VerificationMeActivity
import com.pplus.prnumberuser.core.network.ApiBuilder
import com.pplus.prnumberuser.core.network.model.dto.Verification
import com.pplus.prnumberuser.core.network.model.response.NewResultResponse
import com.pplus.prnumberuser.core.util.PplusCommonUtil
import com.pplus.prnumberuser.core.util.PplusCommonUtil.Companion.getCountryCode
import com.pplus.prnumberuser.databinding.FragmentSecessionPreCautionBinding
import com.pplus.utils.part.format.FormatUtil
import retrofit2.Call
import java.util.*

/**
 * 회원 탈퇴 First - 진행방법 및 대기기간
 */
class SecessionPreCautionFragment : BaseFragment<SecessionActivity>() {
    override fun getPID(): String? {
        return ""
    }

    private var _binding: FragmentSecessionPreCautionBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        _binding = FragmentSecessionPreCautionBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun init() {

        binding.tvSecessionCaution.text = PplusCommonUtil.fromHtml(getString(R.string.html_msg_leave_caution))

        //        ((TextView) container.findViewById(R.id.text_secession_retention_cash)).setText(getString(R.string.format_money_unit, FormatUtil.getMoneyType("" + LoginInfoManager.getInstance().getUser().getTotalCash())));
        binding.textSecessionRetentionPoint.text = getString(R.string.format_cash_unit, FormatUtil.getMoneyType("" + LoginInfoManager.getInstance().user.totalBol))


        if (LoginInfoManager.getInstance().user.page != null) {
            binding.textSecessionStep1.setText(R.string.msg_user_withdrawal_step1_biz)
            binding.textSecessionStep2.setText(R.string.msg_user_withdrawal_step2_biz)
        } else {
            binding.textSecessionStep1.setText(R.string.msg_user_withdrawal_step1_user)
            binding.textSecessionStep2.setText(R.string.msg_user_withdrawal_step2_user)
        }
        binding.textSecession.setOnClickListener {
            if (LoginInfoManager.getInstance().user.verification!!.media == "external") {
                val intent = Intent(activity, VerificationMeActivity::class.java)
                intent.putExtra(Const.KEY, Const.VERIFICATION)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                leaveLauncher.launch(intent)
            } else {
                val intent = Intent(activity, VerificationActivity::class.java)
                intent.putExtra(Const.KEY, Const.LEAVE)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                leaveLauncher.launch(intent)
            }
        }
    }

    val leaveLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if(result.resultCode == Activity.RESULT_OK){
            if (result.data != null) {
                val data = result.data!!
                val verification = data.getParcelableExtra<Verification>(Const.VERIFICATION)
                val params = HashMap<String, String>()
                params["number"] = verification!!.number
                params["token"] = verification.token
                params["mobile"] = data.getStringExtra(Const.MOBILE_NUMBER)!!
                params["appType"] = Const.APP_TYPE
                showProgress("")
                ApiBuilder.create().leave(params).setCallback(object : PplusCallback<NewResultResponse<Any>> {
                    override fun onResponse(call: Call<NewResultResponse<Any>>?, response: NewResultResponse<Any>?) {
                        hideProgress()
                        getParentActivity().secessionResult()
                    }

                    override fun onFailure(call: Call<NewResultResponse<Any>>?, t: Throwable?, response: NewResultResponse<Any>?) {
                        hideProgress()
                    }
                }).build().call()
            }
        }
    }

    companion object {

        @JvmStatic
        fun newInstance() = SecessionPreCautionFragment().apply {
            arguments = Bundle().apply {
            }
        }
    }
}