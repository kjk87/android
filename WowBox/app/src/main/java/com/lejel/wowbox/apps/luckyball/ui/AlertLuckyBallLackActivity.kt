package com.lejel.wowbox.apps.luckyball.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.pplus.utils.part.format.FormatUtil
import com.lejel.wowbox.Const
import com.lejel.wowbox.R
import com.lejel.wowbox.apps.common.ui.base.BaseActivity
import com.lejel.wowbox.apps.invite.ui.InviteActivity
import com.lejel.wowbox.apps.main.ui.MainActivity
import com.lejel.wowbox.core.util.PplusCommonUtil
import com.lejel.wowbox.databinding.ActivityAlertLuckyBallLackBinding

class AlertLuckyBallLackActivity : BaseActivity() {
    override fun getPID(): String {
        return ""
    }

    private lateinit var binding: ActivityAlertLuckyBallLackBinding

    override fun getLayoutView(): View {
        binding = ActivityAlertLuckyBallLackBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun initializeView(savedInstanceState: Bundle?) {
        val lackBall = intent.getIntExtra(Const.DATA, 0)

        binding.textAlertLuckyBallLackBall.text = FormatUtil.getMoneyType(lackBall.toString())

        binding.layoutAlertLuckyBallWowBall.setOnClickListener {

            PplusCommonUtil.wowMallJoin()

            val url = "https://wowboxmall.com/shop/step_wowbox.php"
            PplusCommonUtil.openChromeWebView(this, url)
            finish()
        }

        binding.layoutAlertLuckyBallInvite.setOnClickListener {
            if (!PplusCommonUtil.loginCheck(this, null)) {
                return@setOnClickListener
            }

            val intent = Intent(this, InviteActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
            finish()
        }

        binding.layoutAlertLuckyBallEvent.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra(Const.KEY, Const.EVENT)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
            finish()
        }

        binding.imageAlertLuckyBallLackClose.setOnClickListener {
            finish()
        }

    }
}
