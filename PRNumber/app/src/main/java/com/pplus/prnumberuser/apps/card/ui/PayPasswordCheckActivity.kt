package com.pplus.prnumberuser.apps.card.ui

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import com.pplus.networks.common.PplusCallback
import com.pplus.prnumberuser.R
import com.pplus.prnumberuser.apps.common.toolbar.ImplToolbar
import com.pplus.prnumberuser.apps.common.toolbar.OnToolbarListener
import com.pplus.prnumberuser.apps.common.toolbar.ToolbarOption
import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
import com.pplus.prnumberuser.core.network.ApiBuilder
import com.pplus.prnumberuser.core.network.model.response.NewResultResponse
import com.pplus.prnumberuser.databinding.ActivityPayPasswordSetBinding
import retrofit2.Call

class PayPasswordCheckActivity : BaseActivity(), ImplToolbar {
    override fun getPID(): String {
        return ""
    }

    private lateinit var binding: ActivityPayPasswordSetBinding

    override fun getLayoutView(): View {
        binding = ActivityPayPasswordSetBinding.inflate(layoutInflater)
        return binding.root
    }

    private var mPass = ""

    override fun initializeView(savedInstanceState: Bundle?) {
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

        val ic = binding.editPayPassword1.onCreateInputConnection(EditorInfo())
        binding.randomPadPayPassword.setInputConnection(ic)

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

        binding.textPayPasswordTitle.text = getString(R.string.msg_input_pay_password)
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
            val params = HashMap<String, String>()
//                    params["payPassword"] = AES256.encrypt(mPass, "pplus1235")
            params["payPassword"] = pass
            showProgress("")
            ApiBuilder.create().checkPayPassword(params).setCallback(object : PplusCallback<NewResultResponse<Any>> {

                override fun onResponse(call: Call<NewResultResponse<Any>>?, response: NewResultResponse<Any>?) {
                    hideProgress()
                    setResult(Activity.RESULT_OK)
                    finish()
                }

                override fun onFailure(call: Call<NewResultResponse<Any>>?, t: Throwable?, response: NewResultResponse<Any>?) {
                    hideProgress()
                    binding.textPayPasswordInvalid.visibility = View.VISIBLE
                    initEditText()
                }

            }).build().call()
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
        toolbarOption.initializeDefaultToolbar(getString(R.string.word_input_pay_password), ToolbarOption.ToolbarMenu.LEFT)
        return toolbarOption
    }

    override fun getOnToolbarClickListener(): OnToolbarListener {

        return object : OnToolbarListener {
            override fun onClick(v: View?, toolbarMenu: ToolbarOption.ToolbarMenu?, tag: Any?) {
                when (toolbarMenu) {
                    ToolbarOption.ToolbarMenu.LEFT -> if (tag == 1) {
                        onBackPressed()
                    }
                }
            }
        }
    }
}
