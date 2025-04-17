package com.root37.buflexz.apps.my.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import com.root37.buflexz.R
import com.root37.buflexz.apps.common.mgmt.LoginInfoManager
import com.root37.buflexz.apps.common.toolbar.ImplToolbar
import com.root37.buflexz.apps.common.toolbar.OnToolbarListener
import com.root37.buflexz.apps.common.toolbar.ToolbarOption
import com.root37.buflexz.apps.common.ui.base.BaseActivity
import com.root37.buflexz.apps.terms.ui.AlarmSettingActivity
import com.root37.buflexz.apps.terms.ui.TermsListActivity
import com.root37.buflexz.core.util.PplusCommonUtil
import com.root37.buflexz.databinding.ActivitySettingBinding

class SettingActivity : BaseActivity(), ImplToolbar {

    private lateinit var binding: ActivitySettingBinding

    override fun getLayoutView(): View {
        binding = ActivitySettingBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun getPID(): String? {
        return ""
    }

    override fun initializeView(savedInstanceState: Bundle?) {

        binding.layoutSettingAccountTitle.setOnClickListener {
            if (!PplusCommonUtil.loginCheck(this, defaultLauncher)) {
                return@setOnClickListener
            }

            if(binding.layoutSettingAccount.visibility == View.VISIBLE){
                binding.layoutSettingAccount.visibility = View.GONE
                binding.imageSettingAccountArrow.setImageResource(R.drawable.ic_faq_arrow_down)
            }else{
                binding.layoutSettingAccount.visibility = View.VISIBLE
                binding.imageSettingAccountArrow.setImageResource(R.drawable.ic_faq_arrow_up)
            }
        }

        binding.textSettingAccountWithdrawal.setOnClickListener {
            if (!PplusCommonUtil.loginCheck(this, defaultLauncher)) {
                return@setOnClickListener
            }

            val intent = Intent(this, WithdrawalActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            defaultLauncher.launch(intent)
        }

        binding.layoutSettingTerms.setOnClickListener {
            val intent = Intent(this, TermsListActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            defaultLauncher.launch(intent)
        }

        binding.textSettingVersion.text = PplusCommonUtil.getAppVersion(this)
        binding.layoutSettingVersion.setOnClickListener {
            val intent = Intent(this, VersionActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            defaultLauncher.launch(intent)
        }

        binding.layoutSettingOpensourceLicence.setOnClickListener {
            val intent = Intent(this, OpensourceLicenceActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            defaultLauncher.launch(intent)
        }

        binding.layoutSettingAlarmSetting.setOnClickListener {
            val intent = Intent(this, AlarmSettingActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            defaultLauncher.launch(intent)
        }

        binding.textSettingLogOut.setOnClickListener {
            PplusCommonUtil.logOutAndRestart()
        }

        loginCheck()
    }

    private fun loginCheck(){
        if(LoginInfoManager.getInstance().isMember()){
            binding.textSettingLogOut.visibility = View.VISIBLE
            binding.textSettingAccountPlatformEmail.text = LoginInfoManager.getInstance().member!!.platformEmail
            binding.textSettingAccountUserKey.text = LoginInfoManager.getInstance().member!!.userKey
        }else{
            binding.textSettingLogOut.visibility = View.GONE
        }
    }

    val defaultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        loginCheck()
    }

    override fun getToolbarOption(): ToolbarOption {

        val toolbarOption = ToolbarOption(this)
        toolbarOption.initializeDefaultToolbar(getString(R.string.word_setting), ToolbarOption.ToolbarMenu.LEFT)
        return toolbarOption
    }

    override fun getOnToolbarClickListener(): OnToolbarListener {

        return object : OnToolbarListener {
            override fun onClick(v: View?, toolbarMenu: ToolbarOption.ToolbarMenu?, tag: Any?) {
                when (toolbarMenu) {
                    ToolbarOption.ToolbarMenu.LEFT -> if (tag == 1) {
                        onBackPressedDispatcher.onBackPressed()
                    }

                    else -> {}
                }
            }
        }
    }
}