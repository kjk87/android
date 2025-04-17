package com.pplus.luckybol.apps.mobilegift.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import com.pplus.luckybol.Const
import com.pplus.luckybol.R
import com.pplus.luckybol.apps.common.mgmt.LoginInfoManager
import com.pplus.luckybol.apps.common.ui.base.BaseActivity
import com.pplus.luckybol.core.network.ApiBuilder
import com.pplus.luckybol.core.network.model.dto.GiftishowBuy
import com.pplus.luckybol.core.network.model.response.NewResultResponse
import com.pplus.luckybol.core.util.PplusCommonUtil
import com.pplus.luckybol.databinding.ActivityMobileGiftBuyBinding
import com.pplus.networks.common.PplusCallback
import com.pplus.utils.part.format.FormatUtil
import com.pplus.utils.part.utils.StringUtils
import retrofit2.Call
import java.util.*

class GiftishowBuyActivity : BaseActivity() {
    override fun getPID(): String {
        return ""
    }

    private lateinit var binding: ActivityMobileGiftBuyBinding

    override fun getLayoutView(): View {
        binding = ActivityMobileGiftBuyBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun initializeView(savedInstanceState: Bundle?) {

        val giftishowBuy = intent.getParcelableExtra<GiftishowBuy>(Const.DATA)
        val isMe = intent.getBooleanExtra(Const.IS_ME, false)

        binding.textMobileGiftBuyGoodsName.text = giftishowBuy!!.giftishow!!.goodsName
        binding.textMobileGiftBuyCompanyName.text = giftishowBuy.giftishow!!.brandName
        binding.textMobileGiftBuyCount.text = getString(R.string.format_count_unit, giftishowBuy.totalCount.toString())
        binding.textMobileGiftBuyPrice.text = PplusCommonUtil.fromHtml(getString(R.string.html_cash_unit, FormatUtil.getMoneyType(giftishowBuy.price.toString())))

        binding.imageMobileGiftBuyClose.setOnClickListener {
            onBackPressed()
        }

        if (isMe) {
            binding.textMobileGiftBuyTitle.setText(R.string.word_goods_buy)
            binding.layoutMobileGiftBuyReceiver.visibility = View.GONE
            binding.layoutMobileGiftBuyMsg.visibility = View.GONE
            binding.textMobileGiftBuyQuestion.setText(R.string.msg_question_point_exchange)
            binding.textMobileGiftBuy.setText(R.string.msg_exchange_by_cash)
        } else {
            binding.textMobileGiftBuyTitle.setText(R.string.msg_gift)
            binding.layoutMobileGiftBuyReceiver.visibility = View.VISIBLE
            binding.textMobileGiftBuyReceiverName.text = giftishowBuy.targetList!![0].name
            binding.layoutMobileGiftBuyMsg.visibility = View.VISIBLE
            binding.textMobileGiftBuyMsgCount.text = PplusCommonUtil.fromHtml(getString(R.string.html_format_count_per, 0, 30))
            binding.editMobileGiftBuyMsg.addTextChangedListener(object : TextWatcher{
                override fun afterTextChanged(s: Editable?) {
                    binding.textMobileGiftBuyMsgCount.text = PplusCommonUtil.fromHtml(getString(R.string.html_format_count_per, s!!.length, 30))
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                }
            })

            binding.textMobileGiftBuyQuestion.setText(R.string.msg_question_gift_by_point)
            binding.textMobileGiftBuy.setText(R.string.msg_gift_by_cash)
        }

        binding.textMobileGiftBuy.setOnClickListener {

            if (giftishowBuy.price!! > LoginInfoManager.getInstance().user.point!!) {
                showAlert(R.string.msg_not_enough_cash)
                return@setOnClickListener
            }

            val msg = binding.editMobileGiftBuyMsg.text.toString().trim()
            if(StringUtils.isNotEmpty(msg)){
                giftishowBuy.msg = msg
            }

            showProgress("")
            giftishowBuy.giftishow = null
            ApiBuilder.create().postGiftishowBuy(giftishowBuy).setCallback(object : PplusCallback<NewResultResponse<Any>>{

                override fun onResponse(call: Call<NewResultResponse<Any>>?, response: NewResultResponse<Any>?) {
                    hideProgress()
                    setEvent("giftishowBuy")
                    showAlert(R.string.msg_complete_mobile_gift)
                    finish()
                }

                override fun onFailure(call: Call<NewResultResponse<Any>>?, t: Throwable?, response: NewResultResponse<Any>?) {
                    hideProgress()
                    if (response!!.resultCode == 517) {
                        showAlert(R.string.msg_not_enough_cash)
                    }else if(response.resultCode == 662){
                        showAlert(R.string.msg_exceed_buy_limit)
                    }
                }
            }).build().call()
        }

        getPoint()
    }

    private fun getPoint(){
        PplusCommonUtil.reloadSession(object : PplusCommonUtil.Companion.ReloadListener {
            override fun reload() {
                binding.textMobileGiftBuyRetentionPoint.text = PplusCommonUtil.fromHtml(getString(R.string.html_cash_unit, FormatUtil.getMoneyTypeFloat(LoginInfoManager.getInstance().user.point.toString())))
            }
        })
    }
}
