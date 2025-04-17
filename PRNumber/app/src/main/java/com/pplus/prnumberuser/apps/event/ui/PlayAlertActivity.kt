package com.pplus.prnumberuser.apps.event.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.pplus.prnumberuser.Const
import com.pplus.prnumberuser.R
import com.pplus.prnumberuser.apps.common.mgmt.LoginInfoManager
import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
import com.pplus.prnumberuser.core.code.common.EventType
import com.pplus.prnumberuser.core.network.model.dto.Event
import com.pplus.prnumberuser.core.util.PplusCommonUtil
import com.pplus.prnumberuser.databinding.ActivityPlayAlertBinding
import com.pplus.utils.part.format.FormatUtil

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
        val event = intent.getParcelableExtra<Event>(Const.DATA)

        if(event!!.primaryType == EventType.PrimaryType.lotto.name){
            binding.imagePlayAlertCharacter.setImageResource(R.drawable.img_lotto_event_popup_character_1)

            if(LoginInfoManager.getInstance().user.lottoDefaultTicketCount!! > 0){
                binding.textPlayAlert1.text = event.title
                binding.textPlayAlert2.text = getString(R.string.msg_join_free_lotto_ticket)
            }else if(LoginInfoManager.getInstance().user.lottoTicketCount!! > 0){
                binding.textPlayAlert1.text = getString(R.string.format_my_lotto_ticket, LoginInfoManager.getInstance().user.lottoTicketCount.toString())
                binding.textPlayAlert2.text = getString(R.string.msg_join_lotto_ticket)
            }
            else{
                PplusCommonUtil.reloadSession(object : PplusCommonUtil.Companion.ReloadListener {
                    override fun reload() {
                        binding.textPlayAlert1.text = getString(R.string.format_my_bol, FormatUtil.getMoneyType(LoginInfoManager.getInstance().user.totalBol.toString()))
                    }
                })

                binding.textPlayAlert2.text = getString(R.string.format_msg_use_bol, FormatUtil.getMoneyType(Math.abs(event.reward!!).toString()))
            }
        } else{
            if(event.primaryType == EventType.PrimaryType.lottoPlaybol.name){
                binding.imagePlayAlertCharacter.setImageResource(R.drawable.img_lotto_event_popup_character_1)
            }else{
                binding.imagePlayAlertCharacter.setImageResource(R.drawable.img_play_popup_character_1)
            }

            if(event.primaryType == EventType.PrimaryType.lottoPlaybol.name && LoginInfoManager.getInstance().user.lottoTicketCount!! > 0){
                binding.textPlayAlert1.text = getString(R.string.format_my_lotto_ticket, LoginInfoManager.getInstance().user.lottoTicketCount.toString())
                binding.textPlayAlert2.text = getString(R.string.msg_join_lotto_ticket)
            }else{
                PplusCommonUtil.reloadSession(object : PplusCommonUtil.Companion.ReloadListener {
                    override fun reload() {
                        binding.textPlayAlert1.text = getString(R.string.format_my_bol, FormatUtil.getMoneyType(LoginInfoManager.getInstance().user.totalBol.toString()))
                    }
                })

                binding.textPlayAlert2.text = getString(R.string.format_msg_use_bol, FormatUtil.getMoneyType(Math.abs(event.reward!!).toString()))
            }
        }



        binding.textPlayAlertCancel.setOnClickListener {
            setResult(Activity.RESULT_CANCELED)
            finish()
        }

        binding.textPlayAlertConfirm.setOnClickListener {
            val data = Intent()
            data.putExtra(Const.DATA, event)
            setResult(Activity.RESULT_OK, data)
            finish()
        }

    }
}
