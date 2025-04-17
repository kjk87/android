package com.pplus.prnumberuser.apps.my.ui

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.pplus.networks.common.PplusCallback
import com.pplus.prnumberuser.Const
import com.pplus.prnumberuser.R
import com.pplus.prnumberuser.apps.common.builder.AlertBuilder
import com.pplus.prnumberuser.apps.common.builder.data.AlertData
import com.pplus.prnumberuser.apps.common.mgmt.LoginInfoManager
import com.pplus.prnumberuser.apps.common.toolbar.ImplToolbar
import com.pplus.prnumberuser.apps.common.toolbar.OnToolbarListener
import com.pplus.prnumberuser.apps.common.toolbar.ToolbarOption
import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
import com.pplus.prnumberuser.apps.page.data.HashTagValueAdapter
import com.pplus.prnumberuser.apps.page.ui.AlertFirstBenefitCompleteActivity
import com.pplus.prnumberuser.apps.page.ui.AlertFirstBenefitQrActivity
import com.pplus.prnumberuser.apps.page.ui.AlertPageCashBackCompleteActivity
import com.pplus.prnumberuser.core.network.ApiBuilder
import com.pplus.prnumberuser.core.network.model.dto.Page
import com.pplus.prnumberuser.core.network.model.dto.VisitorPointGiveHistory
import com.pplus.prnumberuser.core.network.model.response.NewResultResponse
import com.pplus.prnumberuser.core.util.PplusCommonUtil
import com.pplus.prnumberuser.databinding.ActivityCheckSavePointAuthCodeBinding
import com.pplus.utils.part.format.FormatUtil
import com.pplus.utils.part.utils.StringUtils
import retrofit2.Call
import java.util.*
import kotlin.collections.HashMap
import kotlin.collections.set

class CheckSavePointAuthCodeActivity : BaseActivity(), ImplToolbar {
    override fun getPID(): String {
        return ""
    }

    private lateinit var binding: ActivityCheckSavePointAuthCodeBinding

    override fun getLayoutView(): View {
        binding = ActivityCheckSavePointAuthCodeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun initializeView(savedInstanceState: Bundle?) {
        val type = intent.getStringExtra(Const.TYPE)
        val page = intent.getParcelableExtra<Page>(Const.PAGE)!!

        binding.editCheckSavePointAuthCode1.setSingleLine()
        binding.editCheckSavePointAuthCode1.inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_VARIATION_PASSWORD
        binding.editCheckSavePointAuthCode2.setSingleLine()
        binding.editCheckSavePointAuthCode2.inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_VARIATION_PASSWORD
        binding.editCheckSavePointAuthCode3.setSingleLine()
        binding.editCheckSavePointAuthCode3.inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_VARIATION_PASSWORD
        binding.editCheckSavePointAuthCode4.setSingleLine()
        binding.editCheckSavePointAuthCode4.inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_VARIATION_PASSWORD

        binding.editCheckSavePointAuthCode1.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s!!.isNotEmpty()) {
                    binding.editCheckSavePointAuthCode2.requestFocus()
                }

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        binding.editCheckSavePointAuthCode2.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s!!.isNotEmpty()) {
                    binding.editCheckSavePointAuthCode3.requestFocus()
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        binding.editCheckSavePointAuthCode3.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s!!.isNotEmpty()) {
                    binding.editCheckSavePointAuthCode4.requestFocus()
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        when (type) {
            "visit" -> {
                binding.layoutCheckSavePointAuthCodeVisitPoint.visibility = View.VISIBLE
                binding.layoutCheckSavePointAuthCodeSnsPoint.visibility = View.GONE
                binding.layoutCheckSavePointAuthCodeBenefit.visibility = View.GONE

                binding.textCheckSavePointAuthCodePoint.text = PplusCommonUtil.fromHtml(getString(R.string.html_save_cash, FormatUtil.getMoneyType(page.visitPoint.toString())))

                var minPrice = 6000
                if (page.visitMinPrice != null && page.visitMinPrice!! > 0) {
                    minPrice = page.visitMinPrice!!
                }

                binding.textCheckSavePointAuthCodePointMinPriceDesc.text = PplusCommonUtil.fromHtml(getString(R.string.html_visit_save_desc, page.name, FormatUtil.getMoneyType(minPrice.toString())))
            }
            "sns" -> {
                binding.layoutCheckSavePointAuthCodeVisitPoint.visibility = View.GONE
                binding.layoutCheckSavePointAuthCodeSnsPoint.visibility = View.VISIBLE
                binding.layoutCheckSavePointAuthCodeBenefit.visibility = View.GONE

                binding.textCheckSavePointAuthCodeSnsPoint.text = PplusCommonUtil.fromHtml(getString(R.string.html_save_cash, FormatUtil.getMoneyType(page.snsPoint.toString())))
                binding.textCheckSavePointAuthCodeSnsDesc.text = PplusCommonUtil.fromHtml(getString(R.string.html_sns_save_desc, page.name))

                val keywordList = ArrayList<String>()
                if (StringUtils.isNotEmpty(page.hashtag)) {
                    keywordList.addAll(page.hashtag!!.split(",").toMutableList())
                }

                val layoutManager = GridLayoutManager(this, 30)
                layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {

                    override fun getSpanSize(position: Int): Int {

                        val keyWord = keywordList[position]

                        return if (keyWord.length < 3) {
                            7
                        } else if (keyWord.length < 4) {
                            8
                        } else if (keyWord.length < 5) {
                            9
                        } else if (keyWord.length < 6) {
                            10
                        } else if (keyWord.length < 7) {
                            12
                        } else if (keyWord.length < 8) {
                            13
                        } else if (keyWord.length < 10) {
                            14
                        } else {
                            20
                        }
                    }
                }

                binding.recyclerCheckSavePointAuthCodeSnsHashtag.layoutManager = layoutManager
                val adapter = HashTagValueAdapter()
                adapter.setDataList(keywordList)
                binding.recyclerCheckSavePointAuthCodeSnsHashtag.adapter = adapter
            }
            "benefit" -> {
                binding.layoutCheckSavePointAuthCodeVisitPoint.visibility = View.GONE
                binding.layoutCheckSavePointAuthCodeSnsPoint.visibility = View.GONE
                binding.layoutCheckSavePointAuthCodeBenefit.visibility = View.VISIBLE

                binding.textCheckSavePointAuthCodeBenefitDesc1.text = PplusCommonUtil.fromHtml(getString(R.string.html_first_benefit_desc, FormatUtil.getMoneyType(page.name)))
                binding.textCheckSavePointAuthCodeBenefitDesc2.text = page.benefit
            }
        }

        binding.textCheckAuthCodeConfirm.setOnClickListener {

            val code1 = binding.editCheckSavePointAuthCode1.text.toString().trim()
            val code2 = binding.editCheckSavePointAuthCode2.text.toString().trim()
            val code3 = binding.editCheckSavePointAuthCode3.text.toString().trim()
            val code4 = binding.editCheckSavePointAuthCode4.text.toString().trim()

            if (StringUtils.isEmpty(code1) || StringUtils.isEmpty(code2) || StringUtils.isEmpty(code3) || StringUtils.isEmpty(code4)) {
                showAlert(R.string.msg_input_auth_code)
                return@setOnClickListener
            }

            val authCode = code1 + code2 + code3 + code4
            val params = HashMap<String, String>()
            params["no"] = page.no.toString()
            params["authCode"] = authCode
            showProgress("")
            ApiBuilder.create().checkAuthCodeForUser(params).setCallback(object : PplusCallback<NewResultResponse<Int>> {
                override fun onResponse(call: Call<NewResultResponse<Int>>?, response: NewResultResponse<Int>?) {
                    hideProgress()
                    val params = VisitorPointGiveHistory()
                    params.pageSeqNo = page.no
                    params.senderSeqNo = page.user!!.no
                    params.receiverSeqNo = LoginInfoManager.getInstance().user.no
                    params.type = type
                    when (type) {
                        "visit" -> {
                            params.price = page.visitPoint
                        }
                        "sns" -> {
                            params.price = page.snsPoint
                        }
                        "benefit" -> {
                            params.price = 0
                        }
                    }

                    params.isPayment = false
                    params.authCode = authCode

                    showProgress("")
                    ApiBuilder.create().postVisitorGivePoint(params).setCallback(object : PplusCallback<NewResultResponse<Any>> {
                        override fun onResponse(call: Call<NewResultResponse<Any>>?, response: NewResultResponse<Any>?) {
                            hideProgress()

                            when (type) {
                                "benefit" -> {
                                    val intent = Intent(this@CheckSavePointAuthCodeActivity, AlertFirstBenefitCompleteActivity::class.java)
                                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                                    startActivity(intent)
                                }
                                else -> {
                                    val intent = Intent(this@CheckSavePointAuthCodeActivity, AlertPageCashBackCompleteActivity::class.java)
                                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                                    intent.putExtra(Const.CASH, params.price)
                                    startActivity(intent)
                                }
                            }


                            setResult(RESULT_OK)
                            finish()
                        }

                        override fun onFailure(call: Call<NewResultResponse<Any>>?, t: Throwable?, response: NewResultResponse<Any>?) {
                            hideProgress()

                            if (response?.resultCode == 504) {
                                val builder = AlertBuilder.Builder()
                                builder.setTitle(getString(R.string.word_notice_alert))
                                builder.setStyle_alert(AlertBuilder.STYLE_ALERT.MESSAGE)
                                when (type) {
                                    "sns" -> {
                                        builder.addContents(AlertData.MessageData(getString(R.string.html_alert_already_sns_point_saved, page.name), AlertBuilder.MESSAGE_TYPE.HTML, 2))
                                    }
                                    "benefit" -> {
                                        builder.addContents(AlertData.MessageData(getString(R.string.html_alert_already_first_benefit_received, page.name), AlertBuilder.MESSAGE_TYPE.HTML, 2))
                                    }
                                }

                                builder.builder().show(this@CheckSavePointAuthCodeActivity)
                            } else {
                                if (type == "benefit") {
                                    showAlert(R.string.msg_failed_receive_first_benefit)
                                } else {
                                    showAlert(R.string.msg_failed_save_point)
                                }
                            }
                        }
                    }).build().call()
                }

                override fun onFailure(call: Call<NewResultResponse<Int>>?, t: Throwable?, response: NewResultResponse<Int>?) {
                    hideProgress()
                    binding.editCheckSavePointAuthCode1.setText("")
                    binding.editCheckSavePointAuthCode2.setText("")
                    binding.editCheckSavePointAuthCode3.setText("")
                    binding.editCheckSavePointAuthCode4.setText("")

                    val builder = AlertBuilder.Builder()
                    builder.setTitle(getString(R.string.word_alert_invalid_auth_code_title))
                    builder.addContents(AlertData.MessageData(getString(R.string.msg_alert_invalid_auth_code1), AlertBuilder.MESSAGE_TYPE.TEXT, 2))
                    builder.builder().show(this@CheckSavePointAuthCodeActivity)

                    //                    showAlert(getString(R.string.msg_alert_invalid_auth_code1))
                }
            }).build().call()
        }
    }

    override fun getToolbarOption(): ToolbarOption {

        val toolbarOption = ToolbarOption(this)
        toolbarOption.initializeDefaultToolbar("", ToolbarOption.ToolbarMenu.LEFT)
        val type = intent.getStringExtra(Const.TYPE)
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
                        val type = intent.getStringExtra(Const.TYPE)
                        if (type == "benefit") {
                            val intent = Intent(this@CheckSavePointAuthCodeActivity, AlertFirstBenefitQrActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                            startActivity(intent)
                            overridePendingTransition(R.anim.view_up, R.anim.fix)
                        } else {
                            val intent = Intent(this@CheckSavePointAuthCodeActivity, AlertSaveQrActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                            startActivity(intent)
                            overridePendingTransition(R.anim.view_up, R.anim.fix)
                        }

                    }
                }
            }
        }
    }
}
