package com.pplus.prnumberuser.apps.main.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.pplus.prnumberuser.Const
import com.pplus.prnumberuser.R
import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
import com.pplus.prnumberuser.databinding.ActivityPadBinding

class PadActivity : BaseActivity(){
    override fun getPID(): String {
        return ""
    }

    private lateinit var binding: ActivityPadBinding

    override fun getLayoutView(): View {
        binding = ActivityPadBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun initializeView(savedInstanceState: Bundle?) {

        onNewIntent(intent)
    }

    private fun setPadFragment(key: String?, number: String?) {
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.pad_container, MainPadFragment.newInstance(key, number), MainPadFragment::class.java.simpleName)
        ft.commit()
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)

        if(intent != null){
            val key = intent.getStringExtra(Const.KEY)
            val number = intent.getStringExtra(Const.NUMBER)
            setPadFragment(key, number)
        }
    }

//    override fun getToolbarOption(): ToolbarOption {
//
//        val toolbarOption = ToolbarOption(this)
//        toolbarOption.initializeDefaultToolbar("", ToolbarOption.ToolbarMenu.LEFT)
////        toolbarOption.setToolbarMenu(ToolbarOption.ToolbarMenu.RIGHT, R.drawable.ic_top_search)
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
////                ToolbarOption.ToolbarMenu.RIGHT -> if (tag == 1) {
////                    val intent = Intent(this@PadActivity, SearchActivity::class.java)
////                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
////                    startActivity(intent)
////                }
//            }
//        }
//    }
}
