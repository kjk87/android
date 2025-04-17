//package com.pplus.prnumberuser.apps.category.ui
//
//import android.content.Intent
//import android.os.Bundle
//import androidx.recyclerview.widget.GridLayoutManager
//import com.pplus.prnumberuser.Const
//import com.pplus.prnumberuser.R
//import com.pplus.prnumberuser.apps.category.data.CategoryAdapter
//import com.pplus.prnumberuser.apps.common.mgmt.CategoryInfoManager
//import com.pplus.prnumberuser.apps.common.toolbar.ImplToolbar
//import com.pplus.prnumberuser.apps.common.toolbar.OnToolbarListener
//import com.pplus.prnumberuser.apps.common.toolbar.ToolbarOption
//import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
//import com.pplus.prnumberuser.apps.common.ui.custom.BottomItemOffsetDecoration
//import kotlinx.android.synthetic.main.activity_category.*
//
//class CategoryActivity : BaseActivity(), ImplToolbar {
//    override fun getPID(): String {
//        return ""
//    }
//
//    override fun getLayoutRes(): Int {
//        return R.layout.activity_category
//    }
//
//    override fun initializeView(savedInstanceState: Bundle?) {
//        val layoutManager = GridLayoutManager(this, 3)
//        recycler_category.layoutManager = layoutManager
//        recycler_category.addItemDecoration(BottomItemOffsetDecoration(this, R.dimen.height_60))
//
//        val adapter = CategoryAdapter()
//        recycler_category.adapter = adapter
//        adapter.addAll(CategoryInfoManager.getInstance().categoryList)
//
//        if (CategoryInfoManager.getInstance().categoryListPerson != null) {
//            adapter.addAll(CategoryInfoManager.getInstance().categoryListPerson)
//        }
//        adapter.setOnItemClickListener(object : CategoryAdapter.OnItemClickListener {
//            override fun onItemClick(position: Int) {
//                val category = adapter.getItem(position)
//
//                val intent = Intent(this@CategoryActivity, CategoryPageActivity::class.java)
//                intent.putExtra(Const.DATA, category)
//                intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
//                startActivity(intent)
//            }
//        })
//    }
//
//    override fun getToolbarOption(): ToolbarOption {
//
//        val toolbarOption = ToolbarOption(this)
//        toolbarOption.initializeDefaultToolbar(getString(R.string.title_category), ToolbarOption.ToolbarMenu.LEFT)
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
//}
