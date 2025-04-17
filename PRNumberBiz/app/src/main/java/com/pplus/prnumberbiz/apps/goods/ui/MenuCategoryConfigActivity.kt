package com.pplus.prnumberbiz.apps.goods.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.pple.pplus.utils.part.format.FormatUtil
import com.pplus.prnumberbiz.Const
import com.pplus.prnumberbiz.R
import com.pplus.prnumberbiz.apps.common.mgmt.LoginInfoManager
import com.pplus.prnumberbiz.apps.common.toolbar.ImplToolbar
import com.pplus.prnumberbiz.apps.common.toolbar.OnToolbarListener
import com.pplus.prnumberbiz.apps.common.toolbar.ToolbarOption
import com.pplus.prnumberbiz.apps.common.ui.base.BaseActivity
import com.pplus.prnumberbiz.apps.menu.data.CategoryConfigAdapter
import com.pplus.prnumberbiz.apps.menu.ui.AlertCategoryRegActivity
import com.pplus.prnumberbiz.core.network.ApiBuilder
import com.pplus.prnumberbiz.core.network.model.dto.PageGoodsCategory
import com.pplus.prnumberbiz.core.network.model.response.NewResultResponse
import kotlinx.android.synthetic.main.activity_menu_category_config.*
import network.common.PplusCallback
import retrofit2.Call
import java.util.*

class MenuCategoryConfigActivity : BaseActivity(), ImplToolbar {
    override fun getPID(): String {
        return ""
    }

    override fun getLayoutRes(): Int {
        return R.layout.activity_menu_category_config
    }

    var mAdapter: CategoryConfigAdapter? = null

    override fun initializeView(savedInstanceState: Bundle?) {

        text_menu_category_config_reg.setOnClickListener {
            val intent = Intent(this, AlertCategoryRegActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivityForResult(intent, Const.REQ_REG)
        }

        recycler_menu_category_config.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this)
        mAdapter = CategoryConfigAdapter(this)
        recycler_menu_category_config.adapter = mAdapter

        mAdapter!!.setOnItemClickListener(object : CategoryConfigAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {

            }

            override fun onItemChanged() {
                getCategory()
            }
        })

        getCategory()
    }

    private fun getCategory() {
        mAdapter!!.clear()
        val params = HashMap<String, String>()
        params["pageSeqNo"] = LoginInfoManager.getInstance().user.page!!.no.toString()
        params["depth"] = "1"
        showProgress("")
        ApiBuilder.create().getPageGoodsCategory(params).setCallback(object : PplusCallback<NewResultResponse<PageGoodsCategory>> {
            override fun onResponse(call: Call<NewResultResponse<PageGoodsCategory>>?, response: NewResultResponse<PageGoodsCategory>?) {
                hideProgress()
                if (response != null) {

                    text_menu_category_config_total_count.text = getString(R.string.format_total_count2, FormatUtil.getMoneyType(response.datas.size.toString()))
                    mAdapter!!.addAll(response.datas)
                }
            }

            override fun onFailure(call: Call<NewResultResponse<PageGoodsCategory>>?, t: Throwable?, response: NewResultResponse<PageGoodsCategory>?) {
                hideProgress()
            }
        }).build().call()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            Const.REQ_REG, Const.REQ_MODIFY -> {
                if (resultCode == Activity.RESULT_OK) {
                    getCategory()
                }
            }
        }
    }

    override fun getToolbarOption(): ToolbarOption {
        val toolbarOption = ToolbarOption(this)
        toolbarOption.initializeDefaultToolbar(getString(R.string.word_category_config), ToolbarOption.ToolbarMenu.LEFT)

        return toolbarOption
    }

    override fun getOnToolbarClickListener(): OnToolbarListener {

        return OnToolbarListener { v, toolbarMenu, tag ->
            when (toolbarMenu) {
                ToolbarOption.ToolbarMenu.LEFT -> if (tag == 1) {
                    onBackPressed()
                }
                ToolbarOption.ToolbarMenu.RIGHT -> if (tag == 1) {

                }
            }
        }
    }
}
