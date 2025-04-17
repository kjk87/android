package com.pplus.luckybol.apps.event.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.pplus.utils.part.format.FormatUtil
import com.pplus.luckybol.Const
import com.pplus.luckybol.R
import com.pplus.luckybol.apps.common.mgmt.LoginInfoManager
import com.pplus.luckybol.apps.common.ui.base.BaseActivity
import com.pplus.luckybol.core.code.common.EventType
import com.pplus.luckybol.core.network.model.dto.Event
import com.pplus.luckybol.core.util.PplusCommonUtil
import com.pplus.luckybol.databinding.ActivityPlayAlertBinding

class PlayAlertActivity : BaseActivity() {
    override fun getPID(): String {
        return ""
    }

    private lateinit var binding: ActivityPlayAlertBinding

    override fun getLayoutView(): View {
        binding = ActivityPlayAlertBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun initializeView(savedInstanceState: Bundle?) {
        val event = intent.getParcelableExtra<Event>(Const.DATA)!!

        PplusCommonUtil.reloadSession(object : PplusCommonUtil.Companion.ReloadListener {
            override fun reload() {
                binding.textPlayAlertRetentionBol.text = PplusCommonUtil.fromHtml(getString(R.string.html_bol_unit3, FormatUtil.getMoneyTypeFloat(LoginInfoManager.getInstance().user.totalBol.toString())))
            }
        })
        if(event.primaryType == EventType.PrimaryType.lotto.name){
            binding.imagePlayAlertCharacter.setImageResource(R.drawable.img_lotto_event_popup_character_1)

            if(LoginInfoManager.getInstance().user.lottoDefaultTicketCount!! > 0){
                binding.textPlayAlert1.text = event.title
                binding.textPlayAlert2.text = getString(R.string.msg_question_join_lotto)
            }else if(LoginInfoManager.getInstance().user.lottoTicketCount!! > 0){
                binding.textPlayAlert1.text = getString(R.string.format_my_lotto_ticket, LoginInfoManager.getInstance().user.lottoTicketCount.toString())
                binding.textPlayAlert2.text = getString(R.string.msg_question_join_lotto)
            }
            else{
                binding.textPlayAlert1.text = getString(R.string.format_msg_use_bol, FormatUtil.getMoneyTypeFloat(Math.abs(event.reward!!).toString()))
                binding.textPlayAlert2.text = getString(R.string.msg_question_join_lotto)
            }
        } else{
            if(event.primaryType == EventType.PrimaryType.lottoPlaybol.name){
                binding.imagePlayAlertCharacter.setImageResource(R.drawable.img_lotto_event_popup_character_1)
            }else{
                binding.imagePlayAlertCharacter.setImageResource(R.drawable.img_play_popup_character_1)
            }

            if(event.primaryType == EventType.PrimaryType.lottoPlaybol.name && LoginInfoManager.getInstance().user.lottoTicketCount!! > 0){
                binding.textPlayAlert1.text = getString(R.string.format_my_lotto_ticket, LoginInfoManager.getInstance().user.lottoTicketCount.toString())
                binding.textPlayAlert2.text = getString(R.string.msg_question_join_lotto)
            }else if(event.primaryType == EventType.PrimaryType.goodluck.name){
                binding.textPlayAlert1.text = getString(R.string.format_msg_use_bol, FormatUtil.getMoneyTypeFloat(Math.abs(event.reward!!).toString()))
                binding.textPlayAlert2.text = getString(R.string.msg_question_join_play)
            }else{
                binding.textPlayAlert1.text = getString(R.string.format_msg_use_bol, FormatUtil.getMoneyTypeFloat(Math.abs(event.reward!!).toString()))
                binding.textPlayAlert2.text = getString(R.string.msg_question_join)
            }
        }



        binding.textPlayAlertCancel.setOnClickListener {
            setResult(Activity.RESULT_CANCELED)
            finish()
        }

        binding.textPlayAlertConfirm.setOnClickListener {
            val data = Intent()
            data.putExtra(Const.DATA, event)
            data.putExtra(Const.POSITION, intent.getIntExtra(Const.POSITION, 0))
            data.putExtra(Const.PROPERTIES, intent.getStringExtra(Const.PROPERTIES))
            setResult(Activity.RESULT_OK, data)
            finish()
        }

    }
}
