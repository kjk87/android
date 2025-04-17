package com.pplus.luckybol.apps.common.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.pplus.luckybol.Const
import com.pplus.luckybol.R
import com.pplus.luckybol.apps.common.mgmt.CountryConfigManager
import com.pplus.luckybol.apps.common.mgmt.LoginInfoManager
import com.pplus.luckybol.apps.common.ui.base.BaseActivity
import com.pplus.luckybol.apps.my.ui.BolChargeStationActivity
import com.pplus.luckybol.core.network.model.dto.Event
import com.pplus.luckybol.core.util.PplusCommonUtil
import com.pplus.luckybol.databinding.ActivityBolChargeAlertBinding
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


        binding.textBolChargeAlertChargeStation1.setOnClickListener {
            val intent = Intent(this, BolChargeStationActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
        }
        val recommendPoint = FormatUtil.getMoneyTypeFloat(CountryConfigManager.getInstance().config.properties.recommendBol.toString())
        binding.textBolChargeAlertInvite.text = getString(R.string.format_invite_and_get_bol, recommendPoint)

        binding.textBolChargeAlertInvite.setOnClickListener {
            PplusCommonUtil.share(this)
        }

        binding.textBolChargeConfirm.setOnClickListener {
            finish()
        }

    }

    override fun onResume() {
        super.onResume()

        PplusCommonUtil.reloadSession(object : PplusCommonUtil.Companion.ReloadListener {
            override fun reload() {
                binding.textBolChargeRetentionBol.text = PplusCommonUtil.fromHtml(getString(R.string.html_bol_unit3, FormatUtil.getMoneyTypeFloat(LoginInfoManager.getInstance().user.totalBol.toString())))
            }
        })
    }

    override fun onPause() {
        super.onPause()
    }
}
