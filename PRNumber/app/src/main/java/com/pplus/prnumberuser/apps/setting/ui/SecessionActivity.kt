package com.pplus.prnumberuser.apps.setting.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.pplus.prnumberuser.R
import com.pplus.prnumberuser.apps.common.toolbar.ImplToolbar
import com.pplus.prnumberuser.apps.common.toolbar.OnToolbarListener
import com.pplus.prnumberuser.apps.common.toolbar.ToolbarOption
import com.pplus.prnumberuser.apps.common.toolbar.ToolbarOption.ToolbarMenu
import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
import com.pplus.prnumberuser.databinding.ActivitySecessionBinding
import com.pplus.utils.BusProvider

/**
 * 회원탈퇴
 */
class SecessionActivity : BaseActivity(), ImplToolbar {
    override fun getPID(): String? {
        return ""
    }

    private lateinit var binding: ActivitySecessionBinding

    override fun getLayoutView(): View {
        binding = ActivitySecessionBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun initializeView(savedInstanceState: Bundle?) {
        BusProvider.getInstance().register(this)
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.secession_container, SecessionPreCautionFragment.newInstance(), SecessionPreCautionFragment::class.java.simpleName)
        ft.commit()
    }

    /**
     * 회원 탈퇴 Second 호출 - 인증 수단 선택
     */
    fun secessionResult() {
        val intent = Intent(this, SecessionCompleteActivity::class.java)
        startActivity(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        BusProvider.getInstance().unregister(this)
    }

    override fun getToolbarOption(): ToolbarOption {
        val toolbarOption = ToolbarOption(this)
        toolbarOption.initializeDefaultToolbar(getString(R.string.word_member_leave), ToolbarMenu.LEFT)
        return toolbarOption
    }

    override fun getOnToolbarClickListener(): OnToolbarListener {
        return object : OnToolbarListener {
            override fun onClick(v: View?, toolbarMenu: ToolbarMenu?, tag: Any?) {
                when (toolbarMenu) {
                    ToolbarMenu.LEFT -> if (tag == 1) {
                        onBackPressed()
                    }
                }
            }
        }
    }
}