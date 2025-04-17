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
import com.pplus.prnumberuser.core.network.model.dto.SubscriptionDownload
import com.pplus.prnumberuser.core.network.model.response.NewResultResponse
import com.pplus.prnumberuser.core.util.PplusCommonUtil
import com.pplus.prnumberuser.databinding.ActivityCheckUseSubscriptionAuthCodeBinding
import com.pplus.prnumberuser.databinding.ActivityProductBinding
import com.pplus.utils.part.format.FormatUtil
import com.pplus.utils.part.utils.StringUtils
import com.pplus.utils.part.utils.time.DateFormatUtils
import retrofit2.Call
import java.util.*
import kotlin.collections.HashMap
import kotlin.collections.set

class CheckUseSubscriptionAuthCodeActivity : BaseActivity(), ImplToolbar {
    override fun getPID(): String {
        return ""
    }

    private lateinit var binding: ActivityCheckUseSubscriptionAuthCodeBinding

    override fun getLayoutView(): View {
        binding = ActivityCheckUseSubscriptionAuthCodeBinding.inflate(layoutInflater)
        return binding.root
    }

    var mUseCount = 1

    override fun initializeView(savedInstanceState: Bundle?) {
        val subscriptionDownload = intent.getParcelableExtra<SubscriptionDownload>(Const.SUBSCRIPTION_DOWNLOAD)
        val usePrice = intent.getIntExtra(Const.PRICE, 0)

        binding.editCheckUseSubscriptionAuthCode1.setSingleLine()
        binding.editCheckUseSubscriptionAuthCode1.inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_VARIATION_PASSWORD
        binding.editCheckUseSubscriptionAuthCode2.setSingleLine()
        binding.editCheckUseSubscriptionAuthCode2.inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_VARIATION_PASSWORD
        binding.editCheckUseSubscriptionAuthCode3.setSingleLine()
        binding.editCheckUseSubscriptionAuthCode3.inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_VARIATION_PASSWORD
        binding.editCheckUseSubscriptionAuthCode4.setSingleLine()
        binding.editCheckUseSubscriptionAuthCode4.inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_VARIATION_PASSWORD

        binding.editCheckUseSubscriptionAuthCode1.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s!!.isNotEmpty()) {
                    binding.editCheckUseSubscriptionAuthCode2.requestFocus()
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        binding.editCheckUseSubscriptionAuthCode2.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s!!.isNotEmpty()) {
                    binding.editCheckUseSubscriptionAuthCode3.requestFocus()
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        binding.editCheckUseSubscriptionAuthCode3.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s!!.isNotEmpty()) {
                    binding.editCheckUseSubscriptionAuthCode4.requestFocus()
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        when (subscriptionDownload!!.type) {
            "prepayment" -> {
                binding.layoutCheckUseSubscription.visibility = View.GONE
                binding.layoutCheckUseSubscriptionMoneyProduct.visibility = View.VISIBLE

                binding.textCheckUseSubscriptionUsePrice.text = PplusCommonUtil.fromHtml(getString(R.string.html_use_money_product, FormatUtil.getMoneyType(usePrice.toString())))
                val remainPrice = subscriptionDownload.havePrice!! - subscriptionDownload.usePrice!!
                binding.textCheckUseSubscriptionRemainPrice.text = PplusCommonUtil.fromHtml(getString(R.string.html_remain_price_after_use, FormatUtil.getMoneyType((remainPrice - usePrice).toString())))
            }
            else -> {
                binding.layoutCheckUseSubscription.visibility = View.VISIBLE
                binding.layoutCheckUseSubscriptionMoneyProduct.visibility = View.GONE

                binding.textCheckUseSubscriptionAuthCodeName.text = subscriptionDownload.name

                mUseCount = 1
                binding.textCheckUseSubscriptionAuthCodeCount.text = DateFormatUtils.formatTime(mUseCount)
                binding.layoutCheckUseSubscriptionAuthCodeMinusCount.setOnClickListener {
                    if (mUseCount > 1) {
                        mUseCount--
                        binding.textCheckUseSubscriptionAuthCodeCount.text = DateFormatUtils.formatTime(mUseCount)
                    }
                }

                binding.layoutCheckUseSubscriptionAuthCodePlusCount.setOnClickListener {
                    val totalCount = subscriptionDownload.useCount!! + mUseCount
                    if (totalCount < subscriptionDownload.haveCount!!) {
                        mUseCount++
                        binding.textCheckUseSubscriptionAuthCodeCount.text = DateFormatUtils.formatTime(mUseCount)
                    }
                }

                binding.textCheckUseSubscriptionAuthCodeRemainCount.text = PplusCommonUtil.fromHtml(getString(R.string.html_subscription_remain_count2, (subscriptionDownload.haveCount!! - subscriptionDownload.useCount!!).toString()))
            }
        }


        binding.textCheckUseSubscriptionAuthCodeConfirm.setOnClickListener {

            val code1 = binding.editCheckUseSubscriptionAuthCode1.text.toString().trim()
            val code2 = binding.editCheckUseSubscriptionAuthCode2.text.toString().trim()
            val code3 = binding.editCheckUseSubscriptionAuthCode3.text.toString().trim()
            val code4 = binding.editCheckUseSubscriptionAuthCode4.text.toString().trim()

            if (StringUtils.isEmpty(code1) || StringUtils.isEmpty(code2) || StringUtils.isEmpty(code3) || StringUtils.isEmpty(code4)) {
                showAlert(R.string.msg_input_auth_code)
                return@setOnClickListener
            }

            val authCode = code1 + code2 + code3 + code4
            val params = HashMap<String, String>()
            params["no"] = subscriptionDownload.productPrice!!.page!!.seqNo.toString()
            params["authCode"] = authCode
            showProgress("")
            ApiBuilder.create().checkAuthCodeForUser(params).setCallback(object : PplusCallback<NewResultResponse<Int>> {
                override fun onResponse(call: Call<NewResultResponse<Int>>?, response: NewResultResponse<Int>?) {
                    hideProgress()
                    val params = HashMap<String, String>()
                    params["subscriptionDownloadSeqNo"] = subscriptionDownload.seqNo.toString()

                    params["authCode"] = authCode
                    when (subscriptionDownload.type) {
                        "prepayment" -> {
                            params["usePrice"] = usePrice.toString()
                        }
                        else -> {
                            params["useCount"] = mUseCount.toString()
                        }
                    }

                    showProgress("")
                    ApiBuilder.create().subscriptionUse(params).setCallback(object : PplusCallback<NewResultResponse<Any>> {
                        override fun onResponse(call: Call<NewResultResponse<Any>>?, response: NewResultResponse<Any>?) {
                            hideProgress()
                            when (subscriptionDownload.type) {
                                "prepayment" -> {
                                    showAlert(R.string.msg_money_product_use_complete)
                                }
                                else -> {
                                    showAlert(R.string.msg_subscription_use_complete)
                                }
                            }

                            setResult(RESULT_OK)
                            finish()
                        }

                        override fun onFailure(call: Call<NewResultResponse<Any>>?, t: Throwable?, response: NewResultResponse<Any>?) {
                            hideProgress()
                            when (subscriptionDownload.type) {
                                "prepayment" -> {
                                    if (response?.resultCode == 510) {
                                        showAlert(R.string.msg_can_not_use_money_product)
                                    } else if (response?.resultCode == 662) {
                                        showAlert(R.string.msg_exceed_use_price)
                                    }
                                }
                                else -> {
                                    if (response?.resultCode == 510) {
                                        showAlert(R.string.msg_can_not_use_subscription)
                                    } else if (response?.resultCode == 662) {
                                        showAlert(R.string.msg_exceed_use_count)
                                    }
                                }
                            }


                        }
                    }).build().call()

                }

                override fun onFailure(call: Call<NewResultResponse<Int>>?, t: Throwable?, response: NewResultResponse<Int>?) {
                    hideProgress()
                    binding.editCheckUseSubscriptionAuthCode1.setText("")
                    binding.editCheckUseSubscriptionAuthCode2.setText("")
                    binding.editCheckUseSubscriptionAuthCode3.setText("")
                    binding.editCheckUseSubscriptionAuthCode4.setText("")

                    val builder = AlertBuilder.Builder()
                    builder.setTitle(getString(R.string.word_alert_invalid_auth_code_title))
                    builder.addContents(AlertData.MessageData(getString(R.string.msg_alert_invalid_auth_code1), AlertBuilder.MESSAGE_TYPE.TEXT, 2))
                    builder.builder().show(this@CheckUseSubscriptionAuthCodeActivity)

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
                        val qrIntent = Intent(this@CheckUseSubscriptionAuthCodeActivity, AlertSubscriptionUseQrActivity::class.java)
                        qrIntent.putExtra(Const.SUBSCRIPTION_DOWNLOAD, intent.getParcelableExtra<SubscriptionDownload>(Const.SUBSCRIPTION_DOWNLOAD))
                        qrIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                        startActivity(qrIntent)
                        overridePendingTransition(R.anim.view_up, R.anim.fix)
                    }
                }
            }
        }
    }
}
