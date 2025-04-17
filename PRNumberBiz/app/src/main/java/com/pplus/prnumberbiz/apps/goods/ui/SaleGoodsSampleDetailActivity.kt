package com.pplus.prnumberbiz.apps.goods.ui

import android.content.Intent
import android.os.Bundle
import com.pplus.prnumberbiz.Const
import com.pplus.prnumberbiz.R
import com.pplus.prnumberbiz.apps.common.toolbar.ImplToolbar
import com.pplus.prnumberbiz.apps.common.toolbar.OnToolbarListener
import com.pplus.prnumberbiz.apps.common.toolbar.ToolbarOption
import com.pplus.prnumberbiz.apps.common.ui.base.BaseActivity
import kotlinx.android.synthetic.main.activity_sale_goods_sample_detail.*

class SaleGoodsSampleDetailActivity : BaseActivity(), ImplToolbar {
    override fun getPID(): String {
        return ""
    }

    override fun getLayoutRes(): Int {
        return R.layout.activity_sale_goods_sample_detail
    }

    override fun initializeView(savedInstanceState: Bundle?) {

        onNewIntent(intent)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        if(intent != null){
            val index = intent.getIntExtra(Const.KEY, 0)

            val res = intArrayOf(R.drawable.commerce_sample_1, R.drawable.commerce_sample_2, R.drawable.commerce_sample_3, R.drawable.commerce_sample_4)
            val title = arrayListOf("상품등록", "선결제 상품관리", "판매내역 관리", "출금관리")

            image_sale_goods_sample_detail.setImageResource(res[index])
            setTitle(title[index])
        }

    }

    override fun getToolbarOption(): ToolbarOption {
        val toolbarOption = ToolbarOption(this)
        toolbarOption.initializeDefaultToolbar(getString(R.string.word_pre_pay_deal), ToolbarOption.ToolbarMenu.LEFT)

        return toolbarOption
    }

    override fun getOnToolbarClickListener(): OnToolbarListener {

        return OnToolbarListener { v, toolbarMenu, tag ->
            when (toolbarMenu) {
                ToolbarOption.ToolbarMenu.LEFT -> if (tag == 1) {
                    onBackPressed()
                }
            }
        }
    }
}
