//package com.pplus.prnumberuser.apps.post.ui
//
//import android.content.Intent
//import android.os.Bundle
//import com.pplus.prnumberuser.Const
//import com.pplus.prnumberuser.R
//import com.pplus.prnumberuser.apps.common.toolbar.ImplToolbar
//import com.pplus.prnumberuser.apps.common.toolbar.OnToolbarListener
//import com.pplus.prnumberuser.apps.common.toolbar.ToolbarOption
//import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
//import com.pplus.prnumberuser.core.code.common.EnumData
//import com.pplus.prnumberuser.core.network.model.dto.Page
//
//class ReviewActivity : BaseActivity(), ImplToolbar {
//
//    override fun getPID(): String {
//        return ""
//    }
//
//    override fun getLayoutRes(): Int {
//        return R.layout.activity_review
//    }
//
//    override fun initializeView(savedInstanceState: Bundle?) {
//        val page = intent.getParcelableExtra<Page>(Const.PAGE)
//
//        val ft = supportFragmentManager.beginTransaction()
//        ft.replace(R.id.review_container, ReviewListFragment.newInstance(page), ReviewListFragment::class.java.simpleName)
//        ft.commit()
//    }
//
//    override fun getToolbarOption(): ToolbarOption {
//
//        val toolbarOption = ToolbarOption(this)
//        toolbarOption.initializeDefaultToolbar(getString(R.string.word_review), ToolbarOption.ToolbarMenu.LEFT)
////        toolbarOption.setToolbarMenu(ToolbarOption.ToolbarMenu.RIGHT, getString(R.string.word_review_write))
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
//                    val intent = Intent(this@ReviewActivity, ReviewWriteActivity::class.java)
//                    intent.putExtra(Const.MODE, EnumData.MODE.WRITE)
//                    intent.putExtra(Const.PAGE, getIntent().getParcelableExtra<Page>(Const.PAGE))
//                    startActivityForResult(intent, Const.REQ_POST)
//                }
//            }
//        }
//    }
//}
