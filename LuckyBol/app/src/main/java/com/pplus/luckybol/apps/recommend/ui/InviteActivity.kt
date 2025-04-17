package com.pplus.luckybol.apps.recommend.ui

import android.os.Bundle
import android.view.View
import com.pplus.luckybol.Const
import com.pplus.luckybol.R
import com.pplus.luckybol.apps.common.toolbar.ImplToolbar
import com.pplus.luckybol.apps.common.toolbar.OnToolbarListener
import com.pplus.luckybol.apps.common.toolbar.ToolbarOption
import com.pplus.luckybol.apps.common.ui.base.BaseActivity
import com.pplus.luckybol.apps.main.ui.MainInviteFragment
import com.pplus.luckybol.apps.main.ui.ReferralInviteFragment
import com.pplus.luckybol.databinding.ActivityInviteBinding

class InviteActivity : BaseActivity(), ImplToolbar {

    override fun getPID(): String {
        return ""
    }

    private lateinit var binding: ActivityInviteBinding

    override fun getLayoutView(): View {
        binding = ActivityInviteBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun initializeView(savedInstanceState: Bundle?) {

        val key = intent.getStringExtra(Const.KEY)

        when(key){
            "referral"->{
                setTitle(getString(R.string.word_referral_save))
                val ft = supportFragmentManager.beginTransaction()
                ft.replace(R.id.invite_container, ReferralInviteFragment.newInstance(), MainInviteFragment::class.java.simpleName)
                ft.commit()
            }
            else->{
                setTitle(getString(R.string.word_invite))
                val ft = supportFragmentManager.beginTransaction()
                ft.replace(R.id.invite_container, MainInviteFragment.newInstance(), MainInviteFragment::class.java.simpleName)
                ft.commit()
            }
        }


    }

    override fun getToolbarOption(): ToolbarOption {

        val toolbarOption = ToolbarOption(this)
        toolbarOption.initializeDefaultToolbar(getString(R.string.word_invite), ToolbarOption.ToolbarMenu.LEFT)
        return toolbarOption
    }

    override fun getOnToolbarClickListener(): OnToolbarListener {

        return OnToolbarListener { v, toolbarMenu, tag ->
            when (toolbarMenu) {
                ToolbarOption.ToolbarMenu.LEFT -> if (tag == 1) {
                    onBackPressed()
                }
                else -> {}
            }
        }
    }
}
