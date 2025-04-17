package com.pplus.prnumberbiz.apps.signup.ui


import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.core.content.ContextCompat
import android.telephony.TelephonyManager
import android.view.View
import com.pple.pplus.utils.part.apps.permission.Permission
import com.pple.pplus.utils.part.apps.permission.PermissionListener
import com.pple.pplus.utils.part.logs.LogUtil
import com.pple.pplus.utils.part.utils.StringUtils
import com.pplus.prnumberbiz.R
import com.pplus.prnumberbiz.apps.common.builder.PPlusPermission
import com.pplus.prnumberbiz.apps.common.ui.base.BaseFragment
import com.pplus.prnumberbiz.core.network.ApiBuilder
import com.pplus.prnumberbiz.core.network.model.dto.User
import com.pplus.prnumberbiz.core.network.model.dto.Verification
import com.pplus.prnumberbiz.core.network.model.response.NewResultResponse
import kotlinx.android.synthetic.main.fragment_join_verification.*
import network.common.PplusCallback
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

    override fun getLayoutResourceId(): Int {
        return R.layout.fragment_join_verification
    }

    override fun initializeView(container: View?) {

    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        if (arguments != null) {
            //            paramsJoin = getArguments().getParcelable(Const.JOIN);
        }

    }

    override fun init() {

        val pPlusPermission = PPlusPermission(activity)
        pPlusPermission.addPermission(Permission.PERMISSION_KEY.PPLUS_PHONE)
        pPlusPermission.setPermissionListener(object : PermissionListener{
            override fun onPermissionGranted() {
                val phoneStatePermission = ContextCompat.checkSelfPermission(activity!!, Manifest.permission.READ_PHONE_STATE)
                if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M || (phoneStatePermission == PackageManager.PERMISSION_GRANTED)) {

                    try{
                        val tMgr = activity!!.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
                        var phone_number = tMgr.line1Number.toString()
                        if (phone_number.startsWith("+82")) {
                            phone_number = phone_number.replace("+82", "0")
                            phone_number = phone_number.replace("-", "")
                        }
                        edit_join_verification_mobile_number.setText(phone_number)
                    }catch (e:Exception){

                    }

                }else{
                    LogUtil.e(LOG_TAG, "permission denied")
                }
            }

            override fun onPermissionDenied(deniedPermissions: ArrayList<String>?) {

            }
        })
        pPlusPermission.checkPermission()

        text_join_verification_reqAuth.setOnClickListener {
            mobileNumber = edit_join_verification_mobile_number.text.toString().trim()
            if (StringUtils.isEmpty(mobileNumber)) {
                showAlert(R.string.msg_input_mobile_number)
                return@setOnClickListener
            }
            request(mobileNumber!!)
        }

        text_join_verification_confirm.setOnClickListener {
            confirm()
        }
    }

    private fun request(mobile: String) {

        val params = HashMap<String, String>()
        params["media"] = "sms"
        params["mobile"] = mobile
        params["type"] = "join"
        showProgress("")
        ApiBuilder.create().verification(params).setCallback(object : PplusCallback<NewResultResponse<Verification>> {

            override fun onResponse(call: Call<NewResultResponse<Verification>>, response: NewResultResponse<Verification>) {

                hideProgress()
                verification = response.data
                text_join_verification_reqAuth.setText(R.string.word_reAuth)
                edit_join_verification_authNumber.requestFocus()
                showAlert(R.string.msg_request_sms)
            }

            override fun onFailure(call: Call<NewResultResponse<Verification>>, t: Throwable, response: NewResultResponse<Verification>) {

                hideProgress()
                if (response.resultCode == 504) {
                    showAlert(R.string.msg_exist_mobile)
                } else {
//                    showAlert(R.string.msg_not_signed_mobile_number)
                }
            }
        }).build().call()
    }

    private fun confirm() {

        if (verification == null) {
            showAlert(R.string.msg_request_verification)
            return
        }

        val number = edit_join_verification_authNumber.text.toString().trim()
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
        showProgress("")
        ApiBuilder.create().confirmVerification(params).setCallback(object : PplusCallback<NewResultResponse<Any>> {

            override fun onResponse(call: Call<NewResultResponse<Any>>, response: NewResultResponse<Any>) {

                hideProgress()
                val params = User()
                params.mobile = mobileNumber
                params.verification = verification
                parentActivity.join(params)
            }

            override fun onFailure(call: Call<NewResultResponse<Any>>, t: Throwable, response: NewResultResponse<Any>) {

                hideProgress()
                showAlert(R.string.msg_failed_verification)
            }
        }).build().call()
    }

    companion object {

        fun newInstance(): JoinVerificationFragment {

            val fragment = JoinVerificationFragment()
            val args = Bundle()
            //        args.putParcelable(Const.JOIN, params);
            fragment.arguments = args
            return fragment
        }
    }
}// Required empty public constructor
