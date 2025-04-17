package com.pplus.prnumberbiz.apps.goods.ui

import android.content.Intent
import android.os.Bundle
import com.pplus.prnumberbiz.Const
import com.pplus.prnumberbiz.R
import com.pplus.prnumberbiz.apps.common.toolbar.ImplToolbar
import com.pplus.prnumberbiz.apps.common.toolbar.OnToolbarListener
import com.pplus.prnumberbiz.apps.common.toolbar.ToolbarOption
import com.pplus.prnumberbiz.apps.common.ui.base.BaseActivity
import kotlinx.android.synthetic.main.activity_sale_goods_pre.*

class SaleGoodsPreActivity : BaseActivity(), ImplToolbar {
    override fun getPID(): String {
        return ""
    }

    override fun getLayoutRes(): Int {
        return R.layout.activity_sale_goods_pre
    }

    override fun initializeView(savedInstanceState: Bundle?) {
        text_sale_goods_sample.setOnClickListener {
            val intent = Intent(this, SaleGoodsSampleActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
        }

        text_apply_sale_goods.setOnClickListener {
            val intent = Intent(this, SellerApplyActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivityForResult(intent, Const.REQ_APPLY)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){
            Const.REQ_APPLY -> {

            }
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
