package com.lejel.wowbox.apps.verify.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.lejel.wowbox.Const
import com.lejel.wowbox.apps.common.toolbar.ImplToolbar
import com.lejel.wowbox.apps.common.toolbar.OnToolbarListener
import com.lejel.wowbox.apps.common.toolbar.ToolbarOption
import com.lejel.wowbox.apps.common.ui.base.BaseActivity
import com.lejel.wowbox.core.network.model.dto.Member
import com.lejel.wowbox.databinding.ActivitySelectVerifyTypeBinding
import com.lejel.wowbox.databinding.ActivityVerifyWhatsAppBinding

class SelectVerifyTypeActivity : BaseActivity(), ImplToolbar {

    private lateinit var binding: ActivitySelectVerifyTypeBinding

    override fun getLayoutView(): View {
        binding = ActivitySelectVerifyTypeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun getPID(): String? {
        return ""
    }

    var mMobileNumber = ""

    override fun initializeView(savedInstanceState: Bundle?) {
        mMobileNumber = intent.getStringExtra(Const.MOOBILE_NUMBER)!!

        binding.textSelectVerifyTypeMobileNumber1.text = mMobileNumber
        binding.textSelectVerifyTypeMobileNumber2.text = mMobileNumber

        binding.layoutSelectVerifyTypeWhatsapp.setOnClickListener {
            val data = Intent()
            data.putExtra(Const.MOOBILE_NUMBER, mMobileNumber)
            data.putExtra(Const.VERIFY_TYPE, "whatsapp")
            setResult(RESULT_OK, data)
            finish()
        }

        binding.layoutSelectVerifyTypeSms.setOnClickListener {
            val data = Intent()
            data.putExtra(Const.MOOBILE_NUMBER, mMobileNumber)
            data.putExtra(Const.VERIFY_TYPE, "sms")
            setResult(RESULT_OK, data)
            finish()
        }
    }

    override fun getToolbarOption(): ToolbarOption {

        val toolbarOption = ToolbarOption(this)
        toolbarOption.initializeDefaultToolbar("", ToolbarOption.ToolbarMenu.RIGHT)
        return toolbarOption
    }

    override fun getOnToolbarClickListener(): OnToolbarListener {

        return object : OnToolbarListener {
            override fun onClick(v: View?, toolbarMenu: ToolbarOption.ToolbarMenu?, tag: Any?) {
                when (toolbarMenu) {
                    ToolbarOption.ToolbarMenu.RIGHT -> if (tag == 1) {
                        onBackPressedDispatcher.onBackPressed()
                    }

                    else -> {}
                }
            }
        }
    }
}