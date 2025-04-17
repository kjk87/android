package com.pplus.luckybol.apps.setting.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.google.gson.Gson
import com.google.gson.JsonParser
import com.pplus.luckybol.Const
import com.pplus.luckybol.R
import com.pplus.luckybol.apps.common.mgmt.LoginInfoManager
import com.pplus.luckybol.apps.common.ui.base.BaseFragment
import com.pplus.luckybol.apps.signup.ui.VerificationMeActivity
import com.pplus.luckybol.core.network.ApiBuilder
import com.pplus.luckybol.core.network.model.dto.Verification
import com.pplus.luckybol.core.network.model.response.NewResultResponse
import com.pplus.luckybol.core.util.PplusCommonUtil
import com.pplus.luckybol.core.util.PplusCommonUtil.Companion.getCountryCode
import com.pplus.luckybol.databinding.FragmentSecessionPreCautionBinding
import com.pplus.networks.common.PplusCallback
import com.pplus.utils.part.format.FormatUtil
import retrofit2.Call
import java.util.*


/**
 * A simple [Fragment] subclass.
 * Use the [SecessionPreCautionFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SecessionPreCautionFragment : BaseFragment<SecessionActivity>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            //            param1 = it.getString(ARG_PARAM1)
            //            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun getPID(): String {
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
        binding.textSecessionRetentionPoint.text = getString(R.string.format_bol_unit, FormatUtil.getMoneyTypeFloat("" + LoginInfoManager.getInstance().user.totalBol))

        if (LoginInfoManager.getInstance().user.page != null) {
            binding.textSecessionStep1.setText(R.string.msg_user_withdrawal_step1_biz)
            binding.textSecessionStep2.setText(R.string.msg_user_withdrawal_step2_biz)
        } else {
            binding.textSecessionStep1.setText(R.string.msg_user_withdrawal_step1_user)
            binding.textSecessionStep2.setText(R.string.msg_user_withdrawal_step2_user)
        }
        binding.textSecession.setOnClickListener {
            if (getCountryCode().equals("kr", ignoreCase = true)) {
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
            } else {
                getParentActivity().secessionAuth()
            }
        }

        checkUser()
    }

    private fun checkUser() {
        showProgress("")
        ApiBuilder.create().walletDuplicateUser().setCallback(object : PplusCallback<NewResultResponse<String>> {
            override fun onResponse(call: Call<NewResultResponse<String>>?,
                                    response: NewResultResponse<String>?) {
                hideProgress()

                if (response?.data != null && response.data == "SUCCESS") {
                    binding.layoutSecessionRetentionCoin.visibility = View.GONE
                } else {
                    binding.layoutSecessionRetentionCoin.visibility = View.VISIBLE
                    walletBalance()
                }
            }

            override fun onFailure(call: Call<NewResultResponse<String>>?,
                                   t: Throwable?,
                                   response: NewResultResponse<String>?) {
                hideProgress()
            }
        }).build().call()
    }

    private fun walletBalance() {
        showProgress("")
        ApiBuilder.create().walletBalance().setCallback(object : PplusCallback<NewResultResponse<Map<String, Any>>>{
            override fun onResponse(call: Call<NewResultResponse<Map<String, Any>>>?,
                                    response: NewResultResponse<Map<String, Any>>?) {
                hideProgress()
                if(response?.data != null){
                    val gson = Gson()
                    val jsonObject = JsonParser.parseString(gson.toJson(response.data)).asJsonObject
                    val buffCoin = jsonObject.get("balances").asJsonObject.get("BUFF").asJsonObject
                    if(buffCoin != null){
                        binding.textSecessionRetentionCoin.text = getString(R.string.format_count_unit, FormatUtil.getMoneyType(buffCoin.get("balance").asString))
                    }

                }
            }

            override fun onFailure(call: Call<NewResultResponse<Map<String, Any>>>?,
                                   t: Throwable?,
                                   response: NewResultResponse<Map<String, Any>>?) {
                hideProgress()
            }
        }).build().call()
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
                //                    putString(ARG_PARAM1, param1)
                //                    putString(ARG_PARAM2, param2)
            }
        }
    }
}