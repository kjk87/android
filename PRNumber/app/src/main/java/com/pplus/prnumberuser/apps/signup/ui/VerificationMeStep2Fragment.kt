package com.pplus.prnumberuser.apps.signup.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.JavascriptInterface
import com.pplus.prnumberuser.Const
import com.pplus.prnumberuser.R
import com.pplus.prnumberuser.apps.common.ui.base.BaseFragment
import com.pplus.prnumberuser.core.code.common.EnumData
import com.pplus.prnumberuser.core.network.model.dto.User
import com.pplus.prnumberuser.core.network.model.dto.Verification
import com.pplus.prnumberuser.core.network.model.dto.VerificationMe
import com.pplus.prnumberuser.databinding.FragmentVerificationMeStep2Binding
import com.pplus.utils.part.logs.LogUtil
import org.json.JSONObject

class VerificationMeStep2Fragment : BaseFragment<VerificationMeActivity>() {
    // TODO: Rename and change types of parameters
    private var mParam1: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            mParam1 = requireArguments().getString(ARG_PARAM1)
        }
    }

    private var _binding: FragmentVerificationMeStep2Binding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        _binding = FragmentVerificationMeStep2Binding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun init() {
        binding.webview.addJavascriptInterface(AndroidBridge(), "Auth")
        binding.webview.loadUrl(Const.API_URL + "jsp/auth")
    }

    private val handler = Handler()

    private inner class AndroidBridge {
        @JavascriptInterface
        fun close() {
            LogUtil.e(LOG_TAG, "AndroidBridge close")
            activity!!.setResult(Activity.RESULT_CANCELED)
            activity!!.finish()
        }

        @JavascriptInterface
        fun sendMessage(msg: String?) {
            handler.post {
                LogUtil.e(LOG_TAG, "msg : {}", msg)
                try {
                    val jsonObject = JSONObject(msg)
                    val verificationMe = VerificationMe()
                    verificationMe.order_id = jsonObject.optString("order_id")
                    verificationMe.username = jsonObject.optString("username")
                    verificationMe.phone = jsonObject.optString("phone")
                    verificationMe.birth = jsonObject.optString("birth")
                    verificationMe.gender = jsonObject.optString("gender")
                    verificationMe.unique = jsonObject.optString("unique")
                    verificationMe.di = jsonObject.optString("di")
                    verificationMe.token = jsonObject.optString("token")
                    val verification = Verification()
                    verification.media = "external"
                    verification.number = verificationMe.order_id
                    verification.token = verificationMe.token
                    if (getParentActivity().key == null || getParentActivity().key == Const.JOIN) {
                        val params = User()
                        params.mobile = verificationMe.phone
                        params.name = verificationMe.username
                        params.birthday = verificationMe.birth
                        if (verificationMe.gender == "0") {
                            params.gender = EnumData.GenderType.female.name
                        } else {
                            params.gender = EnumData.GenderType.male.name
                        }
                        params.verification = verification
                        val data = Intent()
                        data.putExtra(Const.USER, params)
                        activity!!.setResult(Activity.RESULT_OK, data)
                        activity!!.finish()
                    } else if (getParentActivity().key == Const.VERIFICATION_ME) {
                        val params = User()
                        params.mobile = verificationMe.phone
                        params.name = verificationMe.username
                        params.birthday = verificationMe.birth
                        if (verificationMe.gender == "0") {
                            params.gender = EnumData.GenderType.female.name
                        } else {
                            params.gender = EnumData.GenderType.male.name
                        }
                        params.verification = verification
                        val data = Intent()
                        data.putExtra(Const.DATA, params)
                        activity!!.setResult(Activity.RESULT_OK, data)
                        activity!!.finish()
                    } else {
                        val data = Intent()
                        data.putExtra(Const.VERIFICATION, verification)
                        data.putExtra(Const.MOBILE_NUMBER, verificationMe.phone)
                        activity!!.setResult(Activity.RESULT_OK, data)
                        activity!!.finish()
                    }
                } catch (e: Exception) {
                }
            }
        }
    }

    override fun getPID(): String? {
        return ""
    }

    companion object {
        private const val ARG_PARAM1 = "param1"
        @JvmStatic
        fun newInstance(param1: String?): VerificationMeStep2Fragment {
            val fragment = VerificationMeStep2Fragment()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            fragment.arguments = args
            return fragment
        }
    }
}