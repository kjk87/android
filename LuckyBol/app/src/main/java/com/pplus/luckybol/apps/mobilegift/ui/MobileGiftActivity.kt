package com.pplus.luckybol.apps.mobilegift.ui

import android.os.Bundle
import android.view.View
import com.pplus.luckybol.R
import com.pplus.luckybol.apps.common.ui.base.BaseActivity
import com.pplus.luckybol.apps.main.ui.GiftishowFragment
import com.pplus.luckybol.apps.main.ui.MainGiftishowFragment
import com.pplus.luckybol.databinding.ActivityMobileGiftBinding

class MobileGiftActivity : BaseActivity() {
    override fun getPID(): String {
        return ""
    }

    private lateinit var binding: ActivityMobileGiftBinding

    override fun getLayoutView(): View {
        binding = ActivityMobileGiftBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun initializeView(savedInstanceState: Bundle?) {
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.mobile_gift_container, MainGiftishowFragment.newInstance(), MainGiftishowFragment::class.java.simpleName)
        ft.commit()
    }

//    override fun getToolbarOption(): ToolbarOption {
//
//        val toolbarOption = ToolbarOption(this)
//        toolbarOption.initializeDefaultToolbar(getString(R.string.word_point_shop), ToolbarOption.ToolbarMenu.LEFT)
//        return toolbarOption
//    }
//
//    override fun getOnToolbarClickListener(): OnToolbarListener {
//
//        return OnToolbarListener { v, toolbarMenu, tag ->
//            when (toolbarMenu) {
//                ToolbarOption.ToolbarMenu.LEFT -> if (tag == 1) {
//                    onBackPressed()
//                }
//            }
//        }
//    }
}
