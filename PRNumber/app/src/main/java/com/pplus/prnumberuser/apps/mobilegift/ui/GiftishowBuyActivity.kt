package com.pplus.prnumberuser.apps.mobilegift.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import com.pplus.networks.common.PplusCallback
import com.pplus.prnumberuser.Const
import com.pplus.prnumberuser.R
import com.pplus.prnumberuser.apps.common.mgmt.LoginInfoManager
import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
import com.pplus.prnumberuser.core.network.ApiBuilder
import com.pplus.prnumberuser.core.network.model.dto.GiftishowBuy
import com.pplus.prnumberuser.core.network.model.response.NewResultResponse
import com.pplus.prnumberuser.core.util.PplusCommonUtil
import com.pplus.prnumberuser.databinding.ActivityMobileGiftBuyBinding
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
        binding.textMobileGiftBuyPrice.text = PplusCommonUtil.fromHtml(getString(R.string.html_cash_unit2, FormatUtil.getMoneyType(giftishowBuy.price.toString())))

        binding.imageMobileGiftBuyClose.setOnClickListener {
            onBackPressed()
        }

        if (isMe) {
            binding.textMobileGiftBuyTitle.setText(R.string.word_goods_buy)
            binding.layoutMobileGiftBuyReceiver.visibility = View.GONE
            binding.layoutMobileGiftBuyMsg.visibility = View.GONE
            binding.textMobileGiftBuyQuestion.setText(R.string.msg_question_point_exchange)
            binding.textMobileGiftBuy.setText(R.string.msg_point_exchange)
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
            binding.textMobileGiftBuy.setText(R.string.msg_gift_by_point)
        }

        binding.textMobileGiftBuy.setOnClickListener {

            if (giftishowBuy.price!! > LoginInfoManager.getInstance().user.totalBol) {
                showAlert(R.string.msg_not_enough_bol)
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
                    showAlert(R.string.msg_complete_mobile_gift)
                    finish()
                }

                override fun onFailure(call: Call<NewResultResponse<Any>>?, t: Throwable?, response: NewResultResponse<Any>?) {
                    hideProgress()
                    if (response!!.resultCode == 517) {
                        showAlert(R.string.msg_not_enough_bol)
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
                binding.textMobileGiftBuyRetentionPoint.text = PplusCommonUtil.fromHtml(getString(R.string.html_cash_unit2, FormatUtil.getMoneyType(LoginInfoManager.getInstance().user.totalBol.toString())))
            }
        })
    }
}
