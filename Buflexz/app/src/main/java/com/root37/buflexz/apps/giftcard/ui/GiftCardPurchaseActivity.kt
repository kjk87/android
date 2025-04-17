package com.root37.buflexz.apps.giftcard.ui

import android.net.Uri
import android.os.Bundle
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.pplus.networks.common.PplusCallback
import com.pplus.utils.part.format.FormatUtil
import com.pplus.utils.part.utils.StringUtils
import com.root37.buflexz.Const
import com.root37.buflexz.R
import com.root37.buflexz.apps.common.builder.AlertBuilder
import com.root37.buflexz.apps.common.builder.OnAlertResultListener
import com.root37.buflexz.apps.common.builder.data.AlertData
import com.root37.buflexz.apps.common.mgmt.LoginInfoManager
import com.root37.buflexz.apps.common.mgmt.NationManager
import com.root37.buflexz.apps.common.toolbar.ImplToolbar
import com.root37.buflexz.apps.common.toolbar.OnToolbarListener
import com.root37.buflexz.apps.common.toolbar.ToolbarOption
import com.root37.buflexz.apps.common.ui.base.BaseActivity
import com.root37.buflexz.core.network.ApiBuilder
import com.root37.buflexz.core.network.model.dto.GiftCard
import com.root37.buflexz.core.network.model.dto.GiftCardPurchase
import com.root37.buflexz.core.network.model.response.NewResultResponse
import com.root37.buflexz.core.util.PplusCommonUtil
import com.root37.buflexz.databinding.ActivityGiftCardPurchaseBinding
import retrofit2.Call

class GiftCardPurchaseActivity : BaseActivity(), ImplToolbar {

    private lateinit var binding: ActivityGiftCardPurchaseBinding

    override fun getLayoutView(): View {
        binding = ActivityGiftCardPurchaseBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun getPID(): String? {
        return ""
    }

    lateinit var mGiftCard: GiftCard

    override fun initializeView(savedInstanceState: Bundle?) {

        mGiftCard = PplusCommonUtil.getParcelableExtra(intent, Const.DATA, GiftCard::class.java)!!

        Glide.with(this).load(LoginInfoManager.getInstance().member!!.profile).apply(RequestOptions().centerCrop().placeholder(R.drawable.ic_profile_default).error(R.drawable.ic_profile_default)).into(binding.imageGiftCardPurchaseProfile)
        binding.textGiftCardPurchaseNickname.text = LoginInfoManager.getInstance().member!!.nickname
        Glide.with(this).load(Uri.parse("file:///android_asset/flags/${LoginInfoManager.getInstance().member!!.nation!!.uppercase()}.png")).into(binding.imageGiftCardPurchaseFlag)

        val nation = NationManager.getInstance().nationMap!![LoginInfoManager.getInstance().member!!.nation]
        if (nation!!.code == "KR") {
            binding.textGiftCardPurchaseNation.text = nation.name
        } else {
            binding.textGiftCardPurchaseNation.text = nation.nameEn
        }

        reloadSession()

        binding.textGiftCardPurchase1usd.text = getString(R.string.format_dollar_unit, "1")
        binding.textGiftCardPurchase1000point.text = getString(R.string.format_point_unit, "1,000")
        binding.textGiftCardPurchasePrice.text = FormatUtil.getMoneyTypeFloat(mGiftCard.price.toString())
        binding.textGiftCardPurchaseUsePoint.text = FormatUtil.getMoneyTypeFloat((mGiftCard.price!! * 1000).toString())

        if (StringUtils.isNotEmpty(LoginInfoManager.getInstance().member!!.email)) {
            binding.editGiftCardPurchaseEmail.setText(LoginInfoManager.getInstance().member!!.email)
        }

        binding.editGiftCardPurchaseEmail.setSingleLine()

        binding.textGiftCardPurchase.setOnClickListener {

            val email = binding.editGiftCardPurchaseEmail.text.toString().trim()

            if (StringUtils.isEmpty(email)) {
                showAlert(R.string.msg_input_email)
                return@setOnClickListener
            }

            if(!FormatUtil.isEmailAddress(email)){
                showAlert(R.string.msg_valid_email)
                return@setOnClickListener
            }

            if (!binding.checkGiftCardPurchaseTerms.isChecked) {
                showAlert(R.string.msg_agree_gift_card_purchase_caution)
                return@setOnClickListener
            }

            if (mGiftCard.price!! * 1000 > LoginInfoManager.getInstance().member!!.point!!) {
                showAlert(R.string.msg_lack_point)
                return@setOnClickListener
            }

            val builder = AlertBuilder.Builder()
            builder.setStyle_alert(AlertBuilder.STYLE_ALERT.MESSAGE)
            builder.setTitle(getString(R.string.msg_alert_gift_card_purchase))
            builder.addContents(AlertData.MessageData(getString(R.string.html_alert_gift_card_purchase_desc), AlertBuilder.MESSAGE_TYPE.HTML, 2))
            builder.setLeftText(getString(R.string.word_cancel)).setRightText(getString(R.string.word_confirm))
            builder.setOnAlertResultListener(object : OnAlertResultListener {

                override fun onCancel() {

                }

                override fun onResult(event_alert: AlertBuilder.EVENT_ALERT) {
                    when (event_alert) {
                        AlertBuilder.EVENT_ALERT.RIGHT -> {
                            val params = GiftCardPurchase()
                            params.giftCardSeqNo = mGiftCard.seqNo
                            params.userKey = LoginInfoManager.getInstance().member!!.userKey
                            params.giftCardName = mGiftCard.title
                            params.usePoint = mGiftCard.price!! * 1000
                            params.exchangeRate = 1000f
                            params.price = mGiftCard.price
                            params.amount = 1
                            params.unitPrice = mGiftCard.price
                            params.buyerEmail = email
                            showProgress("")
                            ApiBuilder.create().giftCardPurchase(params).setCallback(object : PplusCallback<NewResultResponse<Any>> {
                                override fun onResponse(call: Call<NewResultResponse<Any>>?, response: NewResultResponse<Any>?) {
                                    hideProgress()
                                    showAlert(R.string.msg_complete_purchase)
                                    setResult(RESULT_OK)
                                    finish()
                                }

                                override fun onFailure(call: Call<NewResultResponse<Any>>?, t: Throwable?, response: NewResultResponse<Any>?) {
                                    hideProgress()
                                    if (response?.code == 404) {
                                        showAlert(R.string.msg_not_found_gift_card)
                                    } else if (response?.code == 517) {
                                        showAlert(R.string.msg_lack_point)
                                    }
                                }
                            }).build().call()
                        }

                        else -> {}
                    }
                }
            })
            builder.builder().show(this)
        }

    }

    private fun reloadSession() {
        PplusCommonUtil.reloadSession(object : PplusCommonUtil.Companion.ReloadListener {

            override fun reload() {
                binding.textGiftCardPurchaseRetentionPoint.text = getString(R.string.format_point_unit, FormatUtil.getMoneyType(LoginInfoManager.getInstance().member!!.point!!.toInt().toString()))
            }
        })
    }

    override fun getToolbarOption(): ToolbarOption {

        val toolbarOption = ToolbarOption(this)
        toolbarOption.initializeDefaultToolbar(getString(R.string.word_gift_card_purchase), ToolbarOption.ToolbarMenu.LEFT)
        return toolbarOption
    }

    override fun getOnToolbarClickListener(): OnToolbarListener {

        return object : OnToolbarListener {
            override fun onClick(v: View?, toolbarMenu: ToolbarOption.ToolbarMenu?, tag: Any?) {
                when (toolbarMenu) {
                    ToolbarOption.ToolbarMenu.LEFT -> if (tag == 1) {
                        onBackPressedDispatcher.onBackPressed()
                    }

                    else -> {}
                }
            }
        }
    }
}