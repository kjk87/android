package com.pplus.luckybol.apps.card.ui

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.view.View
import android.view.ViewTreeObserver
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputConnection
import android.view.inputmethod.InputMethodManager
import com.pplus.luckybol.R
import com.pplus.luckybol.apps.common.toolbar.ImplToolbar
import com.pplus.luckybol.apps.common.toolbar.OnToolbarListener
import com.pplus.luckybol.apps.common.toolbar.ToolbarOption
import com.pplus.luckybol.apps.common.ui.base.BaseActivity
import com.pplus.luckybol.core.network.ApiBuilder
import com.pplus.luckybol.core.network.model.dto.Verification
import com.pplus.luckybol.core.network.model.response.NewResultResponse
import com.pplus.luckybol.databinding.ActivityPayPasswordSetBinding
import com.pplus.networks.common.PplusCallback
import retrofit2.Call

class PayPasswordSetActivity : BaseActivity(), ImplToolbar {
    override fun getPID(): String {
        return ""
    }

    private lateinit var binding: ActivityPayPasswordSetBinding

    override fun getLayoutView(): View {
        binding = ActivityPayPasswordSetBinding.inflate(layoutInflater)
        return binding.root
    }

    private var mPass = ""
    private var mConfirmPass = ""
    private var mIsConfirm = false;
    var verification: Verification? = null

    override fun initializeView(savedInstanceState: Bundle?) {

        binding.rootPayPasswordSet.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener{
            override fun onGlobalLayout() {

                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(binding.rootPayPasswordSet.windowToken, 0)
            }
        })

        binding.editPayPassword1.setTextIsSelectable(true)
        binding.editPayPassword1.inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_VARIATION_PASSWORD

        binding.editPayPassword2.setTextIsSelectable(true)
        binding.editPayPassword2.inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_VARIATION_PASSWORD

        binding.editPayPassword3.setTextIsSelectable(true)
        binding.editPayPassword3.inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_VARIATION_PASSWORD

        binding.editPayPassword4.setTextIsSelectable(true)
        binding.editPayPassword4.inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_VARIATION_PASSWORD

        binding.editPayPassword5.setTextIsSelectable(true)
        binding.editPayPassword5.inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_VARIATION_PASSWORD

        binding.editPayPassword6.setTextIsSelectable(true)
        binding.editPayPassword6.inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_VARIATION_PASSWORD

        val ic: InputConnection = binding.editPayPassword1.onCreateInputConnection(EditorInfo())
        binding.randomPadPayPassword.setInputConnection(ic)
        binding.editPayPassword1.requestFocus()

        binding.editPayPassword1.setOnFocusChangeListener { v, hasFocus ->
            if(hasFocus){

                v.post {
                    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(v.windowToken, 0)
                }

                val ic = binding.editPayPassword1.onCreateInputConnection(EditorInfo())
                binding.randomPadPayPassword.setInputConnection(ic)
            }
        }

        binding.editPayPassword2.setOnFocusChangeListener { v, hasFocus ->
            if(hasFocus){

                v.post {
                    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(v.windowToken, 0)
                }

                val ic = binding.editPayPassword2.onCreateInputConnection(EditorInfo())
                binding.randomPadPayPassword.setInputConnection(ic)
            }
        }

        binding.editPayPassword3.setOnFocusChangeListener { v, hasFocus ->
            if(hasFocus){

                v.post {
                    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(v.windowToken, 0)
                }

                val ic = binding.editPayPassword3.onCreateInputConnection(EditorInfo())
                binding.randomPadPayPassword.setInputConnection(ic)
            }
        }

        binding.editPayPassword4.setOnFocusChangeListener { v, hasFocus ->
            if(hasFocus){

                v.post {
                    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(v.windowToken, 0)
                }

                val ic = binding.editPayPassword4.onCreateInputConnection(EditorInfo())
                binding.randomPadPayPassword.setInputConnection(ic)
            }
        }

        binding.editPayPassword5.setOnFocusChangeListener { v, hasFocus ->
            if(hasFocus){

                v.post {
                    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(v.windowToken, 0)
                }

                val ic = binding.editPayPassword5.onCreateInputConnection(EditorInfo())
                binding.randomPadPayPassword.setInputConnection(ic)
            }
        }

        binding.editPayPassword6.setOnFocusChangeListener { v, hasFocus ->
            if(hasFocus){

                v.post {
                    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(v.windowToken, 0)
                }

                val ic = binding.editPayPassword6.onCreateInputConnection(EditorInfo())
                binding.randomPadPayPassword.setInputConnection(ic)
            }
        }

        binding.editPayPassword1.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if(s!!.isNotEmpty()){
                    binding.editPayPassword2.requestFocus()
                    check()
                }

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        binding.editPayPassword2.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if(s!!.isNotEmpty()){
                    binding.editPayPassword3.requestFocus()
                    check()
                }

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        binding.editPayPassword3.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if(s!!.isNotEmpty()){
                    binding.editPayPassword4.requestFocus()
                    check()
                }

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        binding.editPayPassword4.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if(s!!.isNotEmpty()){
                    binding.editPayPassword5.requestFocus()
                    check()
                }

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        binding.editPayPassword5.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if(s!!.isNotEmpty()){
                    binding.editPayPassword6.requestFocus()
                    check()
                }

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        binding.editPayPassword6.addTextChangedListener(object : TextWatcher {
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
        val pass1 = binding.editPayPassword1.text.toString().trim()
        val pass2 = binding.editPayPassword2.text.toString().trim()
        val pass3 = binding.editPayPassword3.text.toString().trim()
        val pass4 = binding.editPayPassword4.text.toString().trim()
        val pass5 = binding.editPayPassword5.text.toString().trim()
        val pass6 = binding.editPayPassword6.text.toString().trim()

        val pass = pass1+pass2+pass3+pass4+pass5+pass6
        if(pass.length == 6){
            if(!mIsConfirm){
                mIsConfirm = true
                binding.textPayPasswordTitle.text = getString(R.string.msg_set_re_pay_password)
                mPass = pass
                binding.randomPadPayPassword.initialize()
                initEditText()
            }else{
                mConfirmPass = pass
                if(mPass == mConfirmPass){
                    val params = HashMap<String, String>()
//                    params["payPassword"] = AES256.encrypt(mPass, "pplus1235")
                    params["payPassword"] = mPass
                    showProgress("")
                    ApiBuilder.create().updatePayPassword(params).setCallback(object : PplusCallback<NewResultResponse<Any>> {

                        override fun onResponse(call: Call<NewResultResponse<Any>>?, response: NewResultResponse<Any>?) {
                            hideProgress()
                            showAlert(R.string.msg_set_up_pay_password)
                            setResult(Activity.RESULT_OK)
                            finish()
                        }

                        override fun onFailure(call: Call<NewResultResponse<Any>>?, t: Throwable?, response: NewResultResponse<Any>?) {
                            hideProgress()
                        }

                    }).build().call()
                }else{
                    mIsConfirm = false
                    mPass = ""
                    mConfirmPass = ""
                    initEditText()
                    showAlert(R.string.msg_incorrect_pay_password)
                    binding.textPayPasswordTitle.text = getString(R.string.msg_set_pay_password)
                }
            }
        }
    }

    private fun initEditText(){
        binding.editPayPassword1.setText("")
        binding.editPayPassword2.setText("")
        binding.editPayPassword3.setText("")
        binding.editPayPassword4.setText("")
        binding.editPayPassword5.setText("")
        binding.editPayPassword6.setText("")
        binding.editPayPassword1.requestFocus()
    }

    override fun getToolbarOption(): ToolbarOption {

        val toolbarOption = ToolbarOption(this)
        toolbarOption.initializeDefaultToolbar(getString(R.string.word_set_pay_password), ToolbarOption.ToolbarMenu.LEFT)
        return toolbarOption
    }

    override fun getOnToolbarClickListener(): OnToolbarListener {

        return OnToolbarListener { v, toolbarMenu, tag ->
            when (toolbarMenu) {
                ToolbarOption.ToolbarMenu.LEFT -> if (tag == 1) {
                    onBackPressed()
                }
                else -> {}
            }
        }
    }
}
