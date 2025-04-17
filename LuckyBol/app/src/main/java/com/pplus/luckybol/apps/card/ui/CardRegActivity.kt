package com.pplus.luckybol.apps.card.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.CompoundButton
import android.widget.EditText
import android.widget.TextView
import com.pplus.luckybol.Const
import com.pplus.luckybol.R
import com.pplus.luckybol.apps.common.mgmt.LoginInfoManager
import com.pplus.luckybol.apps.common.toolbar.ImplToolbar
import com.pplus.luckybol.apps.common.toolbar.OnToolbarListener
import com.pplus.luckybol.apps.common.toolbar.ToolbarOption
import com.pplus.luckybol.apps.common.ui.base.BaseActivity
import com.pplus.luckybol.core.network.ApiBuilder
import com.pplus.luckybol.core.network.model.dto.Card
import com.pplus.luckybol.core.network.model.dto.CardReq
import com.pplus.luckybol.core.network.model.response.NewResultResponse
import com.pplus.luckybol.databinding.ActivityCardRegBinding
import com.pplus.networks.common.PplusCallback
import com.pplus.utils.part.logs.LogUtil
import com.pplus.utils.part.utils.StringUtils
import retrofit2.Call

class CardRegActivity : BaseActivity(), ImplToolbar, CompoundButton.OnCheckedChangeListener {

    override fun getPID(): String {
        return ""
    }

    private lateinit var binding:ActivityCardRegBinding

    override fun getLayoutView(): View {
        binding = ActivityCardRegBinding.inflate(layoutInflater)
        return binding.root
    }

    private var focusedView:EditText? = null

    override fun initializeView(savedInstanceState: Bundle?) {

//        text_card_reg_number3.setTextIsSelectable(true)
        binding.textCardRegNumber3.setSingleLine()
        binding.textCardRegNumber3.inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_VARIATION_PASSWORD

        binding.textCardRegNumber4.setSingleLine()
        binding.textCardRegNumber4.inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_VARIATION_PASSWORD
        binding.randomPadCardReg.visibility = View.GONE
        binding.viewCardRegBottom.visibility = View.GONE

        binding.textCardRegPassword.setSingleLine()
        binding.textCardRegPassword.inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_VARIATION_PASSWORD

        binding.textCardRegPersonalCard.setOnClickListener {
            binding.textCardRegPersonalCard.isSelected = true
            binding.textCardRegCorporateCard.isSelected = false
            binding.layoutCardRegBirthday.visibility = View.VISIBLE
            binding.layoutCardRegBizNo.visibility = View.GONE
        }

        binding.textCardRegCorporateCard.setOnClickListener {
            binding.textCardRegPersonalCard.isSelected = false
            binding.textCardRegCorporateCard.isSelected = true
            binding.layoutCardRegBirthday.visibility = View.GONE
            binding.layoutCardRegBizNo.visibility = View.VISIBLE
        }

        binding.editCardRegNumber1.setOnFocusChangeListener { v, hasFocus ->
            if(hasFocus){
                focusedView = binding.editCardRegNumber1
                binding.randomPadCardReg.visibility = View.GONE
                binding.viewCardRegBottom.visibility = View.GONE
            }

        }
        binding.editCardRegNumber2.setOnFocusChangeListener { v, hasFocus ->
            if(hasFocus){
                focusedView = binding.editCardRegNumber2
                binding.randomPadCardReg.visibility = View.GONE
                binding.viewCardRegBottom.visibility = View.GONE
            }
        }

        binding.editCardRegValidityMonth.setOnFocusChangeListener { v, hasFocus ->
            if(hasFocus){
                focusedView = binding.editCardRegValidityMonth
                binding.randomPadCardReg.visibility = View.GONE
                binding.viewCardRegBottom.visibility = View.GONE
            }
        }

        binding.editCardRegValidityYear.setOnFocusChangeListener { v, hasFocus ->
            if(hasFocus){
                focusedView = binding.editCardRegValidityYear
                binding.randomPadCardReg.visibility = View.GONE
                binding.viewCardRegBottom.visibility = View.GONE
            }
        }

        binding.editCardRegBirthday.setOnFocusChangeListener { v, hasFocus ->
            if(hasFocus){
                focusedView = binding.editCardRegBirthday
                binding.randomPadCardReg.visibility = View.GONE
                binding.viewCardRegBottom.visibility = View.GONE
            }
        }

        binding.editCardRegBizNo.setOnFocusChangeListener { v, hasFocus ->
            if(hasFocus){
                focusedView = binding.editCardRegBizNo
                binding.randomPadCardReg.visibility = View.GONE
                binding.viewCardRegBottom.visibility = View.GONE
            }
        }

        binding.textCardRegNumber3.setOnClickListener {
            setRandomPad(binding.textCardRegNumber3)
        }

        binding.textCardRegNumber4.setOnClickListener {
            setRandomPad(binding.textCardRegNumber4)
        }

        binding.textCardRegPassword.setOnClickListener {
            setRandomPad(binding.textCardRegPassword)
        }

        binding.editCardRegNumber1.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if(s!!.length == 4){
                    binding.editCardRegNumber2.requestFocus()
                }

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        binding.editCardRegNumber2.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if(s!!.length == 4){
                    binding.editCardRegNumber2.clearFocus()
                    setRandomPad(binding.textCardRegNumber3)
                }

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        binding.textCardRegNumber3.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if(s!!.length == 4){
                    binding.textCardRegNumber3.clearFocus()
                    setRandomPad(binding.textCardRegNumber4)
                }

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        binding.textCardRegNumber4.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if(s!!.length == 4){
                    binding.editCardRegValidityMonth.requestFocus()
                }

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        binding.editCardRegValidityMonth.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if(s!!.length == 2){
                    binding.editCardRegValidityYear.requestFocus()
                }

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
        binding.editCardRegValidityYear.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if(s!!.length == 2){
                    binding.textCardRegPassword.clearFocus()
                    setRandomPad(binding.textCardRegPassword)
                }

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        binding.textCardRegPassword.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if(s!!.length == 2){
                    binding.randomPadCardReg.visibility = View.GONE
                    binding.viewCardRegBottom.visibility = View.GONE
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        binding.checkCardRegTotalAgree.setOnCheckedChangeListener(this)
        binding.checkCardRegTerms1.setOnCheckedChangeListener(object : CompoundButton.OnCheckedChangeListener{
            override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {



                var isAll = false
                if(isChecked && binding.checkCardRegTerms2.isChecked){
                    isAll = true
                }
                binding.checkCardRegTotalAgree.setOnCheckedChangeListener(null)
                binding.checkCardRegTotalAgree.isChecked = isAll
                binding.checkCardRegTotalAgree.setOnCheckedChangeListener(this@CardRegActivity)
            }
        })

        binding.checkCardRegTerms2.setOnCheckedChangeListener(object : CompoundButton.OnCheckedChangeListener{
            override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {

                var isAll = false
                if(isChecked && binding.checkCardRegTerms1.isChecked){
                    isAll = true
                }
                binding.checkCardRegTotalAgree.setOnCheckedChangeListener(null)
                binding.checkCardRegTotalAgree.isChecked = isAll
                binding.checkCardRegTotalAgree.setOnCheckedChangeListener(this@CardRegActivity)
            }
        })

        binding.textCardRegViewTerms1.setOnClickListener {
            val intent = Intent(this, CardRegTermsActivity::class.java)
            intent.putExtra(Const.KEY, Const.TERMS1)
            intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
        }

        binding.textCardRegViewTerms2.setOnClickListener {
            val intent = Intent(this, CardRegTermsActivity::class.java)
            intent.putExtra(Const.KEY, Const.TERMS2)
            intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
        }

        binding.textCardConfigReg.setOnClickListener {
            val cardNo1 = binding.editCardRegNumber1.text.toString()
            val cardNo2 = binding.editCardRegNumber2.text.toString()
            val cardNo3 = binding.textCardRegNumber3.text.toString()
            val cardNo4 = binding.textCardRegNumber4.text.toString()

            if (StringUtils.isEmpty(cardNo1) || StringUtils.isEmpty(cardNo2) || StringUtils.isEmpty(cardNo3) || StringUtils.isEmpty(cardNo4)) {
                showAlert(R.string.msg_input_card_no)
                return@setOnClickListener
            }

            if (cardNo1.length < 4 || cardNo2.length < 4 || cardNo3.length < 4 || cardNo4.length < 4) {
                showAlert(R.string.msg_input_valid_card_no)
                return@setOnClickListener
            }

            val cardReq = CardReq()
            cardReq.cardNo = cardNo1+cardNo2+cardNo3+cardNo4

            val mm = binding.editCardRegValidityMonth.text.toString().trim()
            val yy = binding.editCardRegValidityYear.text.toString().trim()

            if (mm.length < 2 || yy.length < 2) {
                showAlert(R.string.msg_input_card_expire_date)
                return@setOnClickListener
            }
            cardReq.expireDt = "20$yy"+mm
            val pwd = binding.textCardRegPassword.text.toString().trim()
            if (pwd.length < 2) {
                showAlert(R.string.msg_input_card_pwd)
                return@setOnClickListener
            }
            cardReq.cardPassword = pwd

//            if (text_card_reg_personal_card.isSelected) {
//                val birthday = edit_card_reg_birthday.text.toString()
//                if (StringUtils.isEmpty(birthday) || birthday.length < 6) {
//                    showAlert(R.string.msg_input_card_birthday2)
//                    return@setOnClickListener
//                }
//                cardReq.cardAuth = birthday
//
//            } else if (text_card_reg_corporate_card.isSelected) {
//                val bizNo = edit_card_reg_biz_no.text.toString()
//                if (StringUtils.isEmpty(bizNo) || bizNo.length < 10) {
//                    showAlert(R.string.msg_input_card_biz_no2)
//                    return@setOnClickListener
//                }
//                cardReq.cardAuth = bizNo
//            }

            LogUtil.e(LOG_TAG, LoginInfoManager.getInstance().user.birthday!!.substring(2))
            cardReq.cardAuth = LoginInfoManager.getInstance().user.birthday!!.substring(2)
            if (!binding.checkCardRegTerms1.isChecked || !binding.checkCardRegTerms2.isChecked) {

                showAlert(getString(R.string.msg_agree_terms))
                return@setOnClickListener
            }

//            val intent = Intent(this@CardRegActivity, VerificationMeActivity::class.java)
//            intent.putExtra(Const.KEY, Const.VERIFICATION_ME)
//            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//            startActivityForResult(intent, Const.REQ_VERIFICATION)

            reg(cardReq)

        }

        binding.textCardRegPersonalCard.isSelected = true
        binding.textCardRegCorporateCard.isSelected = false
        binding.layoutCardRegBirthday.visibility = View.GONE
        binding.layoutCardRegBizNo.visibility = View.GONE
    }

    private fun setRandomPad(view:TextView){
        if(focusedView != null){
            focusedView!!.clearFocus()
        }

        view.post {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }

        val ic = view.onCreateInputConnection(EditorInfo())
        binding.randomPadCardReg.setInputConnection(ic)

        binding.randomPadCardReg.initialize()
        binding.randomPadCardReg.visibility = View.VISIBLE
        binding.viewCardRegBottom.visibility = View.VISIBLE

    }

    override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {

        binding.checkCardRegTerms1.isChecked = isChecked
        binding.checkCardRegTerms2.isChecked = isChecked
    }

    private fun reg(cardReq: CardReq){
        showProgress("")
        ApiBuilder.create().postCardRegister(cardReq).setCallback(object : PplusCallback<NewResultResponse<Card>>{
            override fun onResponse(call: Call<NewResultResponse<Card>>?, response: NewResultResponse<Card>?) {
                hideProgress()
                showAlert(R.string.msg_complete_card_reg)
                setResult(Activity.RESULT_OK)
                finish()
            }

            override fun onFailure(call: Call<NewResultResponse<Card>>?, t: Throwable?, response: NewResultResponse<Card>?) {
                hideProgress()
                if(response?.data != null){
                    showAlert(response.data!!.errorMsg)
                }

            }
        }).build().call()
    }

    override fun onBackPressed() {
        if(binding.randomPadCardReg.visibility == View.VISIBLE){
            binding.randomPadCardReg.visibility = View.GONE
            binding.viewCardRegBottom.visibility = View.GONE
        }else{
            super.onBackPressed()
        }

    }

    override fun getToolbarOption(): ToolbarOption {

        val toolbarOption = ToolbarOption(this)
        toolbarOption.initializeDefaultToolbar(getString(R.string.word_card_reg), ToolbarOption.ToolbarMenu.RIGHT)
        return toolbarOption
    }

    override fun getOnToolbarClickListener(): OnToolbarListener {

        return OnToolbarListener { v, toolbarMenu, tag ->
            when (toolbarMenu) {
                ToolbarOption.ToolbarMenu.RIGHT -> if (tag == 1) {
                    onBackPressed()
                }
                else -> {}
            }
        }
    }
}
