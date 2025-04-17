package com.lejel.wowbox.apps.luckybox.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.widget.addTextChangedListener
import com.lejel.wowbox.Const
import com.lejel.wowbox.R
import com.lejel.wowbox.apps.common.ui.base.BaseActivity
import com.lejel.wowbox.core.network.ApiBuilder
import com.lejel.wowbox.core.network.model.dto.LuckyBoxPurchaseItem
import com.lejel.wowbox.core.network.model.response.NewResultResponse
import com.lejel.wowbox.core.util.PplusCommonUtil
import com.lejel.wowbox.core.util.ToastUtil
import com.lejel.wowbox.databinding.ActivityAlertLuckyBoxImpressionBinding
import com.pplus.networks.common.PplusCallback
import com.pplus.utils.part.utils.StringUtils
import retrofit2.Call

class AlertLuckyBoxImpressionActivity : BaseActivity() {
    override fun getPID(): String {
        return ""
    }

    private lateinit var binding: ActivityAlertLuckyBoxImpressionBinding

    override fun getLayoutView(): View {
        binding = ActivityAlertLuckyBoxImpressionBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun initializeView(savedInstanceState: Bundle?) {
        val luckyBoxPurchaseItem = PplusCommonUtil.getParcelableExtra(intent, Const.DATA, LuckyBoxPurchaseItem::class.java)!!
        val position = intent.getIntExtra(Const.POSITION, -1)

        val data = Intent()
        data.putExtra(Const.DATA, luckyBoxPurchaseItem)
        data.putExtra(Const.POSITION, position)
        setResult(RESULT_OK, data)

        binding.editAlertLuckyBoxImpression.addTextChangedListener {
            val length = it.toString().trim().length
            binding.textAlertLuckyBoxImpressionCount.text = getString(R.string.format_count_per, length, 50)
        }

        if (StringUtils.isNotEmpty(luckyBoxPurchaseItem.impression)) {
            binding.textAlertLuckyBoxImpressionTitle.text = getString(R.string.word_modify_impression)
            binding.textAlertLuckyBoxImpressionDesc.visibility = View.GONE
            binding.textAlertLuckyBoxImpressionCancel.text = getString(R.string.word_cancel)
            binding.editAlertLuckyBoxImpression.setText(luckyBoxPurchaseItem.impression)

        } else {
            if(position == -1){
                binding.textAlertLuckyBoxImpressionTitle.text = getString(R.string.word_lucky_box_impression_title)
                binding.textAlertLuckyBoxImpressionDesc.visibility = View.VISIBLE
                binding.textAlertLuckyBoxImpressionCancel.text = getString(R.string.msg_write_next)
            }else{
                binding.textAlertLuckyBoxImpressionTitle.text = getString(R.string.word_write_impression)
                binding.textAlertLuckyBoxImpressionDesc.visibility = View.GONE
                binding.textAlertLuckyBoxImpressionCancel.text = getString(R.string.word_cancel)
            }

        }

        binding.textAlertLuckyBoxImpressionCancel.setOnClickListener {
            finish()
        }

        binding.textAlertLuckyBoxImpressionComplete.setOnClickListener {
            val impression = binding.editAlertLuckyBoxImpression.text.toString().trim()
            if (StringUtils.isEmpty(impression)) {
                showAlert(R.string.msg_input_win_impression)
                return@setOnClickListener
            }

            val params = HashMap<String, String>()
            params["seqNo"] = luckyBoxPurchaseItem.seqNo.toString()
            params["impression"] = impression
            showProgress("")
            ApiBuilder.create().updateLuckyBoxImpression(params).setCallback(object : PplusCallback<NewResultResponse<Any>> {
                override fun onResponse(call: Call<NewResultResponse<Any>>?,
                                        response: NewResultResponse<Any>?) {
                    hideProgress()
                    ToastUtil.show(this@AlertLuckyBoxImpressionActivity, getString(R.string.msg_wrote_impression))
                    finish()
                }

                override fun onFailure(call: Call<NewResultResponse<Any>>?,
                                       t: Throwable?,
                                       response: NewResultResponse<Any>?) {
                    hideProgress()
                }
            }).build().call()

        }

    }


}
