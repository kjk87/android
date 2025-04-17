package com.pplus.luckybol.apps.buff.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import com.pplus.luckybol.apps.common.ui.base.BaseActivity
import com.pplus.luckybol.databinding.ActivityAlertBuffMakeCompleteBinding

class AlertBuffMakeCompleteActivity : BaseActivity() {
    override fun getPID(): String {
        return ""
    }

    private lateinit var binding: ActivityAlertBuffMakeCompleteBinding

    override fun getLayoutView(): View {
        binding = ActivityAlertBuffMakeCompleteBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun initializeView(savedInstanceState: Bundle?) {

        binding.textAlertBuffMakeCompleteCancel.setOnClickListener {
            finish()
        }

        binding.textAlertBuffMakeCompleteInvite.setOnClickListener {
            val intent = Intent(this, BuffInviteActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            launcher.launch(intent)
        }
    }

    val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        finish()
    }

}
