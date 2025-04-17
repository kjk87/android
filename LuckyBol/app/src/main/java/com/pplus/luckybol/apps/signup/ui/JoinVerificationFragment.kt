package com.pplus.luckybol.apps.signup.ui


import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.telephony.TelephonyManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.pplus.luckybol.Const
import com.pplus.luckybol.R
import com.pplus.luckybol.apps.common.builder.AlertBuilder
import com.pplus.luckybol.apps.common.builder.OnAlertResultListener
import com.pplus.luckybol.apps.common.builder.PPlusPermission
import com.pplus.luckybol.apps.common.builder.data.AlertData
import com.pplus.luckybol.apps.common.ui.base.BaseFragment
import com.pplus.luckybol.apps.signin.ui.FindIdActivity
import com.pplus.luckybol.core.network.ApiBuilder
import com.pplus.luckybol.core.network.model.dto.User
import com.pplus.luckybol.core.network.model.dto.Verification
import com.pplus.luckybol.core.network.model.response.NewResultResponse
import com.pplus.luckybol.databinding.FragmentJoinVerificationBinding
import com.pplus.networks.common.PplusCallback
import com.pplus.utils.part.apps.permission.Permission
import com.pplus.utils.part.apps.permission.PermissionListener
import com.pplus.utils.part.logs.LogUtil
import com.pplus.utils.part.utils.StringUtils
import retrofit2.Call
import java.util.*

/**
 * 회원가입 입력화면
 */
class JoinVerificationFragment : BaseFragment<JoinActivity>() {

    private var verification: Verification? = null
    private var mobileNumber: String? = null

    override fun getPID(): String {

        return "Sign_Phone Number certify"
    }

    private var _binding: FragmentJoinVerificationBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        _binding = FragmentJoinVerificationBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        if (arguments != null) { //            paramsJoin = getArguments().getParcelable(Const.JOIN);
        }

    }

    override fun init() {


        if (requestPermissions()) {
            val tMgr = requireActivity().getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            var phoneNumber = tMgr.line1Number.toString()
            if (phoneNumber.startsWith("+82")) {
                phoneNumber = phoneNumber.replace("+82", "0")
                phoneNumber = phoneNumber.replace("-", "")
            }
            binding.editJoinVerificationMobileNumber.setText(phoneNumber)
        }

        binding.textJoinVerificationReqAuth.setOnClickListener {
            mobileNumber = binding.editJoinVerificationMobileNumber.text.toString().trim()
            if (StringUtils.isEmpty(mobileNumber)) {
                showAlert(R.string.msg_input_mobile_number)
                return@setOnClickListener
            }
            request(mobileNumber!!)
        }

        binding.textJoinVerificationConfirm.setOnClickListener {
            confirm()
        }
    }

    private fun requestPermissions(): Boolean {
        if (ContextCompat.checkSelfPermission(requireActivity(), if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) Manifest.permission.READ_PHONE_NUMBERS else Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) Manifest.permission.READ_PHONE_NUMBERS else Manifest.permission.READ_PHONE_STATE
            permissionLauncher.launch(permission)
            LogUtil.e(LOG_TAG, "requestPermissions launch")
            return false
        }
        return true
    }

    val permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        if (isGranted) {
            val tMgr = requireActivity().getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            var phoneNumber = tMgr.line1Number.toString()
            if (phoneNumber.startsWith("+82")) {
                phoneNumber = phoneNumber.replace("+82", "0")
                phoneNumber = phoneNumber.replace("-", "")
            }
            binding.editJoinVerificationMobileNumber.setText(phoneNumber)
        }
    }

    private fun request(mobile: String) {

        val params = HashMap<String, String>()
        params["media"] = "sms"
        params["mobile"] = mobile
        params["type"] = "join"
        params["appType"] = Const.APP_TYPE
        showProgress("")
        ApiBuilder.create().verification(params).setCallback(object : PplusCallback<NewResultResponse<Verification>> {

            override fun onResponse(call: Call<NewResultResponse<Verification>>,
                                    response: NewResultResponse<Verification>) {

                if (!isAdded) {
                    return
                }

                hideProgress()
                verification = response.data
                binding.textJoinVerificationReqAuth.setText(R.string.word_reAuth)
                binding.editJoinVerificationAuthNumber.requestFocus()
                showAlert(R.string.msg_request_sms)
            }

            override fun onFailure(call: Call<NewResultResponse<Verification>>,
                                   t: Throwable,
                                   response: NewResultResponse<Verification>) {

                hideProgress()
                if (response.resultCode == 504) {
                    val builder = AlertBuilder.Builder()
                    builder.setTitle(getString(R.string.word_notice_alert))
                    builder.setStyle_alert(AlertBuilder.STYLE_ALERT.MESSAGE)
                    builder.addContents(AlertData.MessageData(getString(R.string.msg_exist_mobile), AlertBuilder.MESSAGE_TYPE.TEXT, 2))
                    builder.setLeftText(getString(R.string.word_close)).setRightText(getString(R.string.msg_find_id))
                    builder.setOnAlertResultListener(object : OnAlertResultListener {
                        override fun onCancel() {

                        }

                        override fun onResult(event_alert: AlertBuilder.EVENT_ALERT?) {
                            val intent = Intent(activity, FindIdActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                            startActivity(intent)
                        }
                    }).builder().show(activity)
                } else { //                    showAlert(R.string.msg_not_signed_mobile_number)
                }
            }
        }).build().call()
    }

    private fun confirm() {

        if (verification == null) {
            showAlert(R.string.msg_request_verification)
            return
        }

        val number = binding.editJoinVerificationAuthNumber.text.toString().trim()
        if (StringUtils.isEmpty(number)) {
            showAlert(R.string.hint_auth_number)
            return
        }

        verification!!.number = number
        val params = HashMap<String, String>()
        params["number"] = number
        params["token"] = verification!!.token
        params["mobile"] = mobileNumber!!
        params["media"] = "sms"
        params["appType"] = Const.APP_TYPE
        showProgress("")
        ApiBuilder.create().confirmVerification(params).setCallback(object : PplusCallback<NewResultResponse<Any>> {

            override fun onResponse(call: Call<NewResultResponse<Any>>,
                                    response: NewResultResponse<Any>) {

                if (!isAdded) {
                    return
                }

                hideProgress()
                val params = User()
                params.mobile = mobileNumber
                params.verification = verification
                getParentActivity().join(params)
            }

            override fun onFailure(call: Call<NewResultResponse<Any>>,
                                   t: Throwable,
                                   response: NewResultResponse<Any>) {

                hideProgress()
                showAlert(R.string.msg_failed_verification)
            }
        }).build().call()
    }

    companion object {

        fun newInstance(): JoinVerificationFragment {

            val fragment = JoinVerificationFragment()
            val args = Bundle() //        args.putParcelable(Const.JOIN, params);
            fragment.arguments = args
            return fragment
        }
    }
} // Required empty public constructor
