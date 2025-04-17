package com.lejel.wowbox.apps.luckydraw.ui

import android.os.Bundle
import android.view.View
import com.pplus.networks.common.PplusCallback
import com.pplus.utils.part.format.FormatUtil
import com.lejel.wowbox.Const
import com.lejel.wowbox.R
import com.lejel.wowbox.apps.common.ui.base.BaseActivity
import com.lejel.wowbox.core.network.ApiBuilder
import com.lejel.wowbox.core.network.model.dto.LuckyDraw
import com.lejel.wowbox.core.network.model.dto.LuckyDrawNumber
import com.lejel.wowbox.core.network.model.dto.LuckyDrawPurchase
import com.lejel.wowbox.core.network.model.response.NewResultResponse
import com.lejel.wowbox.core.util.PplusCommonUtil
import com.lejel.wowbox.databinding.ActivityAlertLuckyDrawJoinBinding
import retrofit2.Call

class AlertLuckyDrawJoinActivity : BaseActivity() {
    override fun getPID(): String {
        return ""
    }

    private lateinit var binding: ActivityAlertLuckyDrawJoinBinding

    override fun getLayoutView(): View {
        binding = ActivityAlertLuckyDrawJoinBinding.inflate(layoutInflater)
        return binding.root
    }

    lateinit var mLuckyDraw: LuckyDraw
    override fun initializeView(savedInstanceState: Bundle?) {
        mLuckyDraw = PplusCommonUtil.getParcelableExtra(intent, Const.DATA, LuckyDraw::class.java)!!
        val list = PplusCommonUtil.getParcelableArrayListExtra(intent, Const.NUMBER, LuckyDrawNumber::class.java)!!

        if(mLuckyDraw.engageType == "ball"){
            val totalPrice = list.size * mLuckyDraw.engageBall!!
            binding.textAlertLuckyDrawJoinEngageBall.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_lucky_draw_ball_m, 0, 0, 0)
            binding.textAlertLuckyDrawJoinEngageBall.text = getString(R.string.format_ball_unit, FormatUtil.getMoneyType(totalPrice.toString()))
            binding.textAlertLuckyDrawJoinDesc.setText(R.string.msg_alert_lucky_draw_join_desc)
        }else if(mLuckyDraw.engageType == "free"){
            binding.textAlertLuckyDrawJoinEngageBall.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
            binding.textAlertLuckyDrawJoinEngageBall.text = getString(R.string.word_free)
            binding.textAlertLuckyDrawJoinDesc.setText(R.string.msg_alert_lucky_draw_join_desc2)
        }

        binding.textAlertLuckyDrawJoinCancel.setOnClickListener {
            setResult(RESULT_CANCELED)
            finish()
        }

        binding.textAlertLuckyDrawJoin.setOnClickListener {
            val params = LuckyDrawPurchase()
            params.luckyDrawSeqNo = mLuckyDraw.seqNo
            params.selectNumberList = list
            showProgress("")
            ApiBuilder.create().postLuckyDrawPurchase(params).setCallback(object : PplusCallback<NewResultResponse<LuckyDrawPurchase>>{
                override fun onResponse(call: Call<NewResultResponse<LuckyDrawPurchase>>?, response: NewResultResponse<LuckyDrawPurchase>?) {
                    hideProgress()
                    setEvent("wowball_join")
                    showAlert(R.string.msg_complete_join)
                    setResult(RESULT_OK)
                    finish()
                }

                override fun onFailure(call: Call<NewResultResponse<LuckyDrawPurchase>>?, t: Throwable?, response: NewResultResponse<LuckyDrawPurchase>?) {
                    hideProgress()
                    if(response?.code == 663){
                        showAlert(R.string.msg_error_not_exist_lucky_draw)
                    }else if(response?.code == 517){
                        showAlert(R.string.msg_lack_ball)
                    }else if(response?.code == 662){
                        showAlert(R.string.msg_over_max_join_count)
                    }
                    setResult(RESULT_CANCELED)
                    finish()
                }
            }).build().call()
        }

    }
}
