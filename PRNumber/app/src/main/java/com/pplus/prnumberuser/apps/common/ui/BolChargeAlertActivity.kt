package com.pplus.prnumberuser.apps.common.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.pplus.prnumberuser.Const
import com.pplus.prnumberuser.R
import com.pplus.prnumberuser.apps.common.mgmt.CountryConfigManager
import com.pplus.prnumberuser.apps.common.mgmt.LoginInfoManager
import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
import com.pplus.prnumberuser.apps.recommend.ui.InviteActivity
import com.pplus.prnumberuser.core.code.common.EventType
import com.pplus.prnumberuser.core.network.model.dto.Event
import com.pplus.prnumberuser.core.util.PplusCommonUtil
import com.pplus.prnumberuser.databinding.ActivityBolChargeAlertBinding
import com.pplus.utils.part.format.FormatUtil

class BolChargeAlertActivity : BaseActivity() {
    override fun getPID(): String {
        return ""
    }

    private lateinit var binding: ActivityBolChargeAlertBinding

    override fun getLayoutView(): View {
        binding = ActivityBolChargeAlertBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun initializeView(savedInstanceState: Bundle?) {

        val event = intent.getParcelableExtra<Event>(Const.EVENT)

        if(event != null && event.primaryType == EventType.PrimaryType.lotto.name){
            binding.imageBolChargeCharacter.setImageResource(R.drawable.img_lotto_event_popup_character_2)
            binding.textBolChargeAlertInvite.text = getString(R.string.msg_invite_get_lotto_ticket)
            binding.layoutBolChargeAlertOption1.visibility = View.VISIBLE
            binding.layoutBolChargeAlertOption2.visibility = View.GONE
        }else if(event != null && event.primaryType == EventType.PrimaryType.lottoPlaybol.name){
            binding.imageBolChargeCharacter.setImageResource(R.drawable.img_lotto_event_popup_character_2)
            binding.layoutBolChargeAlertOption1.visibility = View.VISIBLE
            binding.layoutBolChargeAlertOption2.visibility = View.GONE
        }else{
            binding.imageBolChargeCharacter.setImageResource(R.drawable.img_play_popup_character_2)
            binding.textBolChargeAlertInvite.text = getString(R.string.format_invite_and_get_bol, CountryConfigManager.getInstance().config.properties!!.recommendBol.toString())
            binding.layoutBolChargeAlertOption1.visibility = View.VISIBLE
            binding.layoutBolChargeAlertOption2.visibility = View.GONE
        }

        binding.layoutBolChargeAlertOption2.setOnClickListener {
        }

        binding.textBolChargeAlertChargeStation1.setOnClickListener {
        }

        binding.textBolChargeAlertInvite.setOnClickListener {
            val intent = Intent(this, InviteActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
        }

        binding.textBolChargeConfirm.setOnClickListener {
            finish()
        }

    }

    override fun onResume() {
        super.onResume()

        PplusCommonUtil.reloadSession(object : PplusCommonUtil.Companion.ReloadListener {
            override fun reload() {
                binding.textBolChargeRetentionBol.text = getString(R.string.format_my_bol, FormatUtil.getMoneyType(LoginInfoManager.getInstance().user.totalBol.toString()))
            }
        })
    }

    override fun onPause() {
        super.onPause()
    }
}
