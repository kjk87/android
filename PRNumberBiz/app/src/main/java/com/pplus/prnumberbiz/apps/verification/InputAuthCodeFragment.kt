package com.pplus.prnumberbiz.apps.verification


import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.view.View
import com.pple.pplus.utils.part.utils.StringUtils
import com.pplus.prnumberbiz.Const
import com.pplus.prnumberbiz.R
import com.pplus.prnumberbiz.apps.common.mgmt.LoginInfoManager
import com.pplus.prnumberbiz.apps.common.ui.base.BaseFragment
import com.pplus.prnumberbiz.core.network.ApiBuilder
import com.pplus.prnumberbiz.core.network.model.dto.Verification
import com.pplus.prnumberbiz.core.network.model.response.NewResultResponse
import kotlinx.android.synthetic.main.fragment_input_auth_code.*
import network.common.PplusCallback
import retrofit2.Call


/**
 * A simple [Fragment] subclass.
 * Use the [InputAuthCodeFragment.newInstance] factory method to
 * create an instance of this mapFragment.
 */
class InputAuthCodeFragment : BaseFragment<AuthCodeConfigActivity>() {

    private var mCode = ""
    private var mConfirmCode = ""
    private var mIsConfirm = false;
    override fun getPID(): String {
        return ""
    }

    override fun getLayoutResourceId(): Int {
        return R.layout.fragment_input_auth_code
    }

    override fun initializeView(container: View?) {

    }

    override fun init() {

        text_verification_number_description.text = getString(R.string.msg_input_change_verification_number)

        edit_input_auth_code1.setSingleLine()
        edit_input_auth_code1.inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_VARIATION_PASSWORD
        edit_input_auth_code2.setSingleLine()
        edit_input_auth_code2.inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_VARIATION_PASSWORD
        edit_input_auth_code3.setSingleLine()
        edit_input_auth_code3.inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_VARIATION_PASSWORD
        edit_input_auth_code4.setSingleLine()
        edit_input_auth_code4.inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_VARIATION_PASSWORD

        edit_input_auth_code1.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if(s!!.isNotEmpty()){
                    edit_input_auth_code2.requestFocus()
                    check()
                }

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        edit_input_auth_code2.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if(s!!.isNotEmpty()){
                    edit_input_auth_code3.requestFocus()
                    check()
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        edit_input_auth_code3.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if(s!!.isNotEmpty()){
                    edit_input_auth_code4.requestFocus()
                    check()
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        edit_input_auth_code4.addTextChangedListener(object : TextWatcher {
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
        val code1 = edit_input_auth_code1.text.toString().trim()
        val code2 = edit_input_auth_code2.text.toString().trim()
        val code3 = edit_input_auth_code3.text.toString().trim()
        val code4 = edit_input_auth_code4.text.toString().trim()

        val code = code1+code2+code3+code4
        if(code.length == 4){
            if(!mIsConfirm){
                mIsConfirm = true
                text_verification_number_description.text = getString(R.string.msg_input_change_verification_number_confirm)
                mCode = code
                initEditText()
            }else{
                mConfirmCode = code
                if(mCode == mConfirmCode){
                    if(verification == null){
                        val params = HashMap<String, String>()
                        params["no"] = LoginInfoManager.getInstance().user.page!!.no.toString()
                        params["newAuthCode"] = mCode
                        if(StringUtils.isNotEmpty(authCode)){
                            params["authCode"] = authCode!!
                        }
                        showProgress("")
                        ApiBuilder.create().checkAndUpdateAuthCode(params).setCallback(object : PplusCallback<NewResultResponse<Int>> {
                            override fun onResponse(call: Call<NewResultResponse<Int>>?, response: NewResultResponse<Int>?) {
                                if(!isAdded){
                                    return
                                }
                                hideProgress()
                                showAlert(R.string.msg_complete_change_verification_number)
                                LoginInfoManager.getInstance().user.page!!.authCode = mCode
                                LoginInfoManager.getInstance().save()
                                parentActivity.configNumber()
                            }

                            override fun onFailure(call: Call<NewResultResponse<Int>>?, t: Throwable?, response: NewResultResponse<Int>?) {
                                hideProgress()
                            }

                        }).build().call()
                    }else{
                        val params = HashMap<String, String>()
                        params["authCode"] = mCode
                        params["number"] = verification!!.number
                        params["token"] = verification!!.token
                        showProgress("")
                        ApiBuilder.create().updateAuthCodeByVerification(params).setCallback(object : PplusCallback<NewResultResponse<Any>> {

                            override fun onResponse(call: Call<NewResultResponse<Any>>, response: NewResultResponse<Any>) {
                                if(!isAdded){
                                    return
                                }
                                hideProgress()
                                showAlert(R.string.msg_complete_change_verification_number)
                                LoginInfoManager.getInstance().user.page!!.authCode = mCode
                                LoginInfoManager.getInstance().save()
                                parentActivity.configNumber()
                            }

                            override fun onFailure(call: Call<NewResultResponse<Any>>, t: Throwable, response: NewResultResponse<Any>) {
                                hideProgress()
                            }
                        }).build().call()
                    }
                }else{
                    mIsConfirm = false
                    mCode = ""
                    mConfirmCode = ""
                    initEditText()
                    showAlert(R.string.msg_incorrect_verification_number)
                    text_verification_number_description.text = getString(R.string.msg_input_change_verification_number)
                }
            }
        }
    }

    private fun initEditText(){
        edit_input_auth_code1.setText("")
        edit_input_auth_code2.setText("")
        edit_input_auth_code3.setText("")
        edit_input_auth_code4.setText("")
        edit_input_auth_code1.requestFocus()
    }

    var authCode:String? = null
    var verification:Verification? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        if (arguments != null) {
            authCode = arguments!!.getString(Const.AUTH_CODE)
            verification = arguments!!.getParcelable(Const.VERIFICATION)
        }
    }


    companion object {

        // TODO: Rename and change types and number of parameters
        fun newInstance(authCode:String?, verification:Verification?): InputAuthCodeFragment {

            val fragment = InputAuthCodeFragment()
            val args = Bundle()
            args.putString(Const.AUTH_CODE, authCode)
            args.putParcelable(Const.VERIFICATION, verification)
            fragment.arguments = args
            return fragment
        }
    }

}// Required empty public constructor
