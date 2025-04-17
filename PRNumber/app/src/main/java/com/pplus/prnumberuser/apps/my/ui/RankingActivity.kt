//package com.pplus.prnumberuser.apps.my.ui
//
//import android.content.Intent
//import android.os.Bundle
//import com.pplus.prnumberuser.Const
//import com.pplus.prnumberuser.R
//import com.pplus.prnumberuser.apps.common.toolbar.ImplToolbar
//import com.pplus.prnumberuser.apps.common.toolbar.OnToolbarListener
//import com.pplus.prnumberuser.apps.common.toolbar.ToolbarOption
//
//import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
//import com.pplus.prnumberuser.apps.recommend.ui.RecommendHistoryActivity
//import com.pplus.prnumberuser.core.util.PplusCommonUtil
//
//class RankingActivity : BaseActivity(), ImplToolbar {
//    override fun getPID(): String {
//        return ""
//    }
//
//    override fun getLayoutView(): Int {
//        return R.layout.activity_ranking
//    }
//
//    override fun initializeView(savedInstanceState: Bundle?) {
//        val ft = supportFragmentManager.beginTransaction()
//        ft.replace(R.id.ranking_container, RankingFragment.newInstance(intent.getStringExtra(Const.TYPE)!!), RankingFragment::class.java.simpleName)
//        ft.commit()
//    }
//
//    override fun getToolbarOption(): ToolbarOption {
//
//        val toolbarOption = ToolbarOption(this)
//        toolbarOption.initializeDefaultToolbar(getString(R.string.word_ranking), ToolbarOption.ToolbarMenu.LEFT)
//        toolbarOption.setToolbarMenu(ToolbarOption.ToolbarMenu.RIGHT, getString(R.string.word_recommend_history))
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
//                ToolbarOption.ToolbarMenu.RIGHT -> if (tag == 1) {
//                    if(!PplusCommonUtil.loginCheck(this@RankingActivity, null)){
//                        return@OnToolbarListener
//                    }
//                    val intent = Intent(this, RecommendHistoryActivity::class.java)
//                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//                    startActivity(intent)
//                }
//            }
//        }
//    }
//}
