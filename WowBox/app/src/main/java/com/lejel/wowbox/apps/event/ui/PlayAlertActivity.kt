package com.lejel.wowbox.apps.event.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.pplus.utils.part.format.FormatUtil
import com.lejel.wowbox.Const
import com.lejel.wowbox.R
import com.lejel.wowbox.apps.common.mgmt.LoginInfoManager
import com.lejel.wowbox.apps.common.ui.base.BaseActivity
import com.lejel.wowbox.core.network.model.dto.Event
import com.lejel.wowbox.core.util.PplusCommonUtil
import com.lejel.wowbox.databinding.ActivityPlayAlertBinding
import kotlin.math.abs

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
        val event = PplusCommonUtil.getParcelableExtra(intent, Const.DATA, Event::class.java)!!

//        PplusCommonUtil.reloadSession(object : PplusCommonUtil.Companion.ReloadListener {
//            override fun reload() {
//                binding.textPlayAlertRetentionBol.text = PplusCommonUtil.fromHtml(getString(R.string.html_bol_unit3, FormatUtil.getMoneyTypeFloat(LoginInfoManager.getInstance().member!!.ball.toString())))
//            }
//        })
        binding.textPlayAlertPrice.text = getString(R.string.format_msg_use_bol, FormatUtil.getMoneyTypeFloat(abs(event.reward!!).toString()))



        binding.imagePlayAlertClose.setOnClickListener {
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
