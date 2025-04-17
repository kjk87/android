//package com.pplus.prnumberuser.apps.theme.ui
//
//import android.content.Intent
//import android.os.Bundle
//import android.view.View
//import androidx.recyclerview.widget.LinearLayoutManager
//import com.pplus.networks.common.PplusCallback
//import com.pplus.prnumberuser.Const
//import com.pplus.prnumberuser.R
//import com.pplus.prnumberuser.apps.common.toolbar.ImplToolbar
//import com.pplus.prnumberuser.apps.common.toolbar.OnToolbarListener
//import com.pplus.prnumberuser.apps.common.toolbar.ToolbarOption
//import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
//import com.pplus.prnumberuser.apps.theme.data.ThemeAdapter
//import com.pplus.prnumberuser.core.network.ApiBuilder
//import com.pplus.prnumberuser.core.network.model.dto.ThemeCategory
//import com.pplus.prnumberuser.core.network.model.response.NewResultResponse
//import kotlinx.android.synthetic.main.activity_theme.*
//import retrofit2.Call
//
//class ThemeActivity : BaseActivity(), ImplToolbar {
//    override fun getPID(): String {
//        return "Thema"
//    }
//
//    override fun getLayoutView(): Int {
//        return R.layout.activity_theme
//    }
//
//    private var mAdapter: ThemeAdapter? = null
//    private var mLayoutManager: LinearLayoutManager? = null
//    override fun initializeView(savedInstanceState: Bundle?) {
//
//        mLayoutManager = LinearLayoutManager(this)
//        recycler_theme.layoutManager = mLayoutManager!!
//        mAdapter = ThemeAdapter(this)
//        recycler_theme.adapter = mAdapter
//
//        mAdapter!!.setOnItemClickListener(object : ThemeAdapter.OnItemClickListener {
//
//            override fun onItemClick(position: Int, view: View) {
//                val intent = Intent(this@ThemeActivity, ThemeDetailActivity::class.java)
//                intent.putExtra(Const.DATA, mAdapter!!.getItem(position))
//                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//                startActivity(intent)
//            }
//        })
//
//        getCategoryAll()
//    }
//
//    private fun getCategoryAll() {
//        ApiBuilder.create().themeCategoryList.setCallback(object : PplusCallback<NewResultResponse<ThemeCategory>> {
//
//            override fun onResponse(call: Call<NewResultResponse<ThemeCategory>>, response: NewResultResponse<ThemeCategory>) {
//
//                val categoryList = response.datas
//                mAdapter!!.addAll(categoryList)
//
//            }
//
//            override fun onFailure(call: Call<NewResultResponse<ThemeCategory>>, t: Throwable, response: NewResultResponse<ThemeCategory>) {
//
//            }
//        }).build().call()
//    }
//
//    override fun getToolbarOption(): ToolbarOption {
//
//        val toolbarOption = ToolbarOption(this)
//        toolbarOption.initializeDefaultToolbar(getString(R.string.word_theme_place), ToolbarOption.ToolbarMenu.LEFT)
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
