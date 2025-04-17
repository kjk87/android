package com.pplus.prnumberuser.apps.subscription.ui

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.view.View
import com.pplus.networks.common.PplusCallback
import com.pplus.prnumberuser.Const
import com.pplus.prnumberuser.R
import com.pplus.prnumberuser.apps.common.builder.AlertBuilder
import com.pplus.prnumberuser.apps.common.builder.data.AlertData
import com.pplus.prnumberuser.apps.common.toolbar.ImplToolbar
import com.pplus.prnumberuser.apps.common.toolbar.OnToolbarListener
import com.pplus.prnumberuser.apps.common.toolbar.ToolbarOption
import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
import com.pplus.prnumberuser.core.network.ApiBuilder
import com.pplus.prnumberuser.core.network.model.dto.ProductPrice
import com.pplus.prnumberuser.core.network.model.dto.SubscriptionDownload
import com.pplus.prnumberuser.core.network.model.response.NewResultResponse
import com.pplus.prnumberuser.core.util.PplusCommonUtil
import com.pplus.prnumberuser.databinding.ActivityCheckBuySubscriptionAuthCodeBinding
import com.pplus.utils.part.format.FormatUtil
import com.pplus.utils.part.utils.StringUtils
import retrofit2.Call
import java.util.*
import kotlin.collections.HashMap
import kotlin.collections.set

class CheckBuySubscriptionAuthCodeActivity : BaseActivity(), ImplToolbar {
    override fun getPID(): String {
        return ""
    }

    private lateinit var binding: ActivityCheckBuySubscriptionAuthCodeBinding

    override fun getLayoutView(): View {
        binding = ActivityCheckBuySubscriptionAuthCodeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun initializeView(savedInstanceState: Bundle?) {
        val productPrice = intent.getParcelableExtra<ProductPrice>(Const.PRODUCT_PRICE)

        binding.editCheckBuySubscriptionAuthCode1.setSingleLine()
        binding.editCheckBuySubscriptionAuthCode1.inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_VARIATION_PASSWORD
        binding.editCheckBuySubscriptionAuthCode2.setSingleLine()
        binding.editCheckBuySubscriptionAuthCode2.inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_VARIATION_PASSWORD
        binding.editCheckBuySubscriptionAuthCode3.setSingleLine()
        binding.editCheckBuySubscriptionAuthCode3.inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_VARIATION_PASSWORD
        binding.editCheckBuySubscriptionAuthCode4.setSingleLine()
        binding.editCheckBuySubscriptionAuthCode4.inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_VARIATION_PASSWORD

        binding.editCheckBuySubscriptionAuthCode1.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                if(s!!.isNotEmpty()){
                    binding.editCheckBuySubscriptionAuthCode2.requestFocus()
                }

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        binding.editCheckBuySubscriptionAuthCode2.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                if(s!!.isNotEmpty()){
                    binding.editCheckBuySubscriptionAuthCode3.requestFocus()
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        binding.editCheckBuySubscriptionAuthCode3.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                if(s!!.isNotEmpty()){
                    binding.editCheckBuySubscriptionAuthCode4.requestFocus()
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        binding.textCheckBuySubscriptionAuthCodeName.text = productPrice!!.product!!.name

        if(productPrice.isSubscription != null && productPrice.isSubscription!!){
            binding.textCheckBuySubscriptionAuthCodeTitle.text = getString(R.string.word_subscription_download)
            binding.textCheckBuySubscriptionAuthCodePrice.text = PplusCommonUtil.fromHtml(getString(R.string.html_subscription_pay_price, FormatUtil.getMoneyType(productPrice.price.toString())))
        }else{
            binding.textCheckBuySubscriptionAuthCodeTitle.text = getString(R.string.word_money_product_download)
            binding.textCheckBuySubscriptionAuthCodePrice.text = PplusCommonUtil.fromHtml(getString(R.string.html_money_product_pay_price, FormatUtil.getMoneyType(productPrice.price.toString())))
        }


        binding.textCheckBuySubscriptionAuthCodeConfirm.setOnClickListener {

            val code1 = binding.editCheckBuySubscriptionAuthCode1.text.toString().trim()
            val code2 = binding.editCheckBuySubscriptionAuthCode2.text.toString().trim()
            val code3 = binding.editCheckBuySubscriptionAuthCode3.text.toString().trim()
            val code4 = binding.editCheckBuySubscriptionAuthCode4.text.toString().trim()

            if(StringUtils.isEmpty(code1) || StringUtils.isEmpty(code2) || StringUtils.isEmpty(code3) || StringUtils.isEmpty(code4)){
                showAlert(R.string.msg_input_auth_code)
                return@setOnClickListener
            }

            val authCode = code1+code2+code3+code4
            val params = HashMap<String, String>()
            params["no"] = productPrice.page!!.seqNo.toString()
            params["authCode"] = authCode
            showProgress("")
            ApiBuilder.create().checkAuthCodeForUser(params).setCallback(object : PplusCallback<NewResultResponse<Int>> {
                override fun onResponse(call: Call<NewResultResponse<Int>>?, response: NewResultResponse<Int>?) {
                    hideProgress()

                    val params = HashMap<String, String>()
                    params["productPriceSeqNo"] = productPrice.seqNo.toString()
                    params["authCode"] = authCode
                    showProgress("")
                    ApiBuilder.create().subscriptionDownload(params).setCallback(object : PplusCallback<NewResultResponse<SubscriptionDownload>> {
                        override fun onResponse(call: Call<NewResultResponse<SubscriptionDownload>>?, response: NewResultResponse<SubscriptionDownload>?) {
                            hideProgress()
                            showAlert(R.string.msg_subscription_download_complete)
                            setResult(RESULT_OK)
                            finish()
                        }

                        override fun onFailure(call: Call<NewResultResponse<SubscriptionDownload>>?, t: Throwable?, response: NewResultResponse<SubscriptionDownload>?) {
                            hideProgress()
                            if(response?.resultCode == 504){
                                showAlert(R.string.msg_already_exist_subscription)
                            }
                        }
                    }).build().call()

                }

                override fun onFailure(call: Call<NewResultResponse<Int>>?, t: Throwable?, response: NewResultResponse<Int>?) {
                    hideProgress()
                    binding.editCheckBuySubscriptionAuthCode1.setText("")
                    binding.editCheckBuySubscriptionAuthCode2.setText("")
                    binding.editCheckBuySubscriptionAuthCode3.setText("")
                    binding.editCheckBuySubscriptionAuthCode4.setText("")

                    val builder = AlertBuilder.Builder()
                    builder.setTitle(getString(R.string.word_alert_invalid_auth_code_title))
                    builder.addContents(AlertData.MessageData(getString(R.string.msg_alert_invalid_auth_code1), AlertBuilder.MESSAGE_TYPE.TEXT, 2))
                    builder.builder().show(this@CheckBuySubscriptionAuthCodeActivity)

//                    showAlert(getString(R.string.msg_alert_invalid_auth_code1))
                }
            }).build().call()
        }
    }

    override fun getToolbarOption(): ToolbarOption {

        val toolbarOption = ToolbarOption(this)
        toolbarOption.initializeDefaultToolbar("", ToolbarOption.ToolbarMenu.LEFT)
        toolbarOption.setToolbarMenu(ToolbarOption.ToolbarMenu.RIGHT, R.drawable.ic_top_qr)
        return toolbarOption
    }

    override fun getOnToolbarClickListener(): OnToolbarListener {

        return object : OnToolbarListener {
            override fun onClick(v: View?, toolbarMenu: ToolbarOption.ToolbarMenu?, tag: Any?) {
                when (toolbarMenu) {
                    ToolbarOption.ToolbarMenu.LEFT -> if (tag == 1) {
                        onBackPressed()
                    }
                    ToolbarOption.ToolbarMenu.RIGHT -> if (tag == 1) {
                        val qrIntent = Intent(this@CheckBuySubscriptionAuthCodeActivity, AlertBuySubscriptionQrActivity::class.java)
                        qrIntent.putExtra(Const.PRODUCT_PRICE, intent.getParcelableExtra<ProductPrice>(Const.PRODUCT_PRICE))
                        qrIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                        startActivity(qrIntent)
                        overridePendingTransition(R.anim.view_up, R.anim.fix)
                    }
                }
            }
        }
    }
}
