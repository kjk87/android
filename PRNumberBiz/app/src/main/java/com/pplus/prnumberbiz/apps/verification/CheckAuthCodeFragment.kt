package com.pplus.prnumberbiz.apps.verification


import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.view.View
import com.pplus.prnumberbiz.R
import com.pplus.prnumberbiz.apps.common.mgmt.LoginInfoManager
import com.pplus.prnumberbiz.apps.common.ui.base.BaseFragment
import com.pplus.prnumberbiz.core.network.ApiBuilder
import com.pplus.prnumberbiz.core.network.model.response.NewResultResponse
import kotlinx.android.synthetic.main.fragment_check_auth_code.*
import network.common.PplusCallback
import retrofit2.Call


/**
 * A simple [Fragment] subclass.
 * Use the [CheckAuthCodeFragment.newInstance] factory method to
 * create an instance of this mapFragment.
 */
class CheckAuthCodeFragment : BaseFragment<AuthCodeConfigActivity>() {

    override fun getPID(): String {
        return ""
    }

    override fun getLayoutResourceId(): Int {
        return R.layout.fragment_check_auth_code;
    }

    override fun initializeView(container: View?) {

    }

    override fun init() {
        edit_check_auth_code1.setSingleLine()
        edit_check_auth_code1.inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_VARIATION_PASSWORD
        edit_check_auth_code2.setSingleLine()
        edit_check_auth_code2.inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_VARIATION_PASSWORD
        edit_check_auth_code3.setSingleLine()
        edit_check_auth_code3.inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_VARIATION_PASSWORD
        edit_check_auth_code4.setSingleLine()
        edit_check_auth_code4.inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_VARIATION_PASSWORD

        edit_check_auth_code1.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if(s!!.isNotEmpty()){
                    edit_check_auth_code2.requestFocus()
                    check()
                }

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        edit_check_auth_code2.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if(s!!.isNotEmpty()){
                    edit_check_auth_code3.requestFocus()
                    check()
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        edit_check_auth_code3.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if(s!!.isNotEmpty()){
                    edit_check_auth_code4.requestFocus()
                    check()
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        edit_check_auth_code4.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if(s!!.isNotEmpty()){
                    check()
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    private fun check(){
        val code1 = edit_check_auth_code1.text.toString().trim()
        val code2 = edit_check_auth_code2.text.toString().trim()
        val code3 = edit_check_auth_code3.text.toString().trim()
        val code4 = edit_check_auth_code4.text.toString().trim()

        val code = code1+code2+code3+code4
        if(code.length == 4){
            val params = HashMap<String, String>()
            params["no"] = LoginInfoManager.getInstance().user.page!!.no.toString()
            params["authCode"] = code
            showProgress("")
            ApiBuilder.create().checkAuthCode(params).setCallback(object : PplusCallback<NewResultResponse<Int>>{
                override fun onResponse(call: Call<NewResultResponse<Int>>?, response: NewResultResponse<Int>?) {
                    if(!isAdded){
                        return
                    }
                    hideProgress()
                    parentActivity.inputNumber(code)
                }

                override fun onFailure(call: Call<NewResultResponse<Int>>?, t: Throwable?, response: NewResultResponse<Int>?) {
                    hideProgress()
                    showAlert(R.string.msg_invalid_verification_number)
                    edit_check_auth_code1.setText("")
                    edit_check_auth_code2.setText("")
                    edit_check_auth_code3.setText("")
                    edit_check_auth_code4.setText("")
                }

            }).build().call()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
//        if (arguments != null) {
//            mParam1 = arguments.getString(ARG_PARAM1)
//            mParam2 = arguments.getString(ARG_PARAM2)
//        }
    }


    companion object {

        // TODO: Rename and change types and number of parameters
        fun newInstance(): CheckAuthCodeFragment {

            val fragment = CheckAuthCodeFragment()
            val args = Bundle()
//            args.putString(ARG_PARAM1, param1)
//            args.putString(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }
    }

}// Required empty public constructor
