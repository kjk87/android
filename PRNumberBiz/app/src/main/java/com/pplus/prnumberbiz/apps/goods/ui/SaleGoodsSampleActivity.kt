package com.pplus.prnumberbiz.apps.goods.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.pplus.prnumberbiz.Const
import com.pplus.prnumberbiz.R
import com.pplus.prnumberbiz.apps.common.toolbar.ImplToolbar
import com.pplus.prnumberbiz.apps.common.toolbar.OnToolbarListener
import com.pplus.prnumberbiz.apps.common.toolbar.ToolbarOption
import com.pplus.prnumberbiz.apps.common.ui.base.BaseActivity
import kotlinx.android.synthetic.main.activity_sale_goods_sample.*

class SaleGoodsSampleActivity : BaseActivity(), ImplToolbar {
    override fun getPID(): String {
        return ""
    }

    override fun getLayoutRes(): Int {
        return R.layout.activity_sale_goods_sample
    }

    override fun initializeView(savedInstanceState: Bundle?) {

        image_sale_goods_sample1.setOnClickListener {
            val intent = Intent(this, SaleGoodsSampleDetailActivity::class.java)
            intent.putExtra(Const.KEY, 0)
            intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
        }
        image_sale_goods_sample2.setOnClickListener {
            val intent = Intent(this, SaleGoodsSampleDetailActivity::class.java)
            intent.putExtra(Const.KEY, 1)
            intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
        }
        image_sale_goods_sample3.setOnClickListener {
            val intent = Intent(this, SaleGoodsSampleDetailActivity::class.java)
            intent.putExtra(Const.KEY, 2)
            intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
        }
        image_sale_goods_sample4.setOnClickListener {
            val intent = Intent(this, SaleGoodsSampleDetailActivity::class.java)
            intent.putExtra(Const.KEY, 3)
            intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
        }
    }

    override fun getToolbarOption(): ToolbarOption {
        val toolbarOption = ToolbarOption(this)
        toolbarOption.initializeDefaultToolbar("맛있는 베이커리님의 판매내역", ToolbarOption.ToolbarMenu.LEFT)

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
