package com.pplus.prnumberbiz.apps.sale.ui

import android.os.Bundle
import androidx.fragment.app.FragmentTransaction
import com.pplus.prnumberbiz.Const
import com.pplus.prnumberbiz.R
import com.pplus.prnumberbiz.apps.common.toolbar.ImplToolbar
import com.pplus.prnumberbiz.apps.common.toolbar.OnToolbarListener
import com.pplus.prnumberbiz.apps.common.toolbar.ToolbarOption
import com.pplus.prnumberbiz.apps.common.ui.base.BaseActivity
import com.pplus.prnumberbiz.core.code.common.EnumData
import kotlinx.android.synthetic.main.activity_sale_order_process.*

class SaleGoodsHistoryActivity : BaseActivity(), ImplToolbar {
    override fun getPID(): String {
        return ""
    }

    override fun getLayoutRes(): Int {
        return R.layout.activity_sale_history
    }

    override fun initializeView(savedInstanceState: Bundle?) {

        val type = intent.getStringExtra(Const.TYPE)
        setSaleDeliveryHistoryFragment(type)
    }

    private fun setSaleDeliveryHistoryFragment(type:String) {
        val ft = supportFragmentManager.beginTransaction()
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
        ft.replace(R.id.sale_history_container, SaleGoodsHistoryFragment.newInstance(type), SaleGoodsHistoryFragment::class.java.simpleName)
        ft.commitNow()
    }


    override fun getToolbarOption(): ToolbarOption {

        val toolbarOption = ToolbarOption(this)
        val type = intent.getStringExtra(Const.TYPE)
        when(type){
            EnumData.GoodsType.hotdeal.name->{
                toolbarOption.initializeDefaultToolbar(getString(R.string.word_hotdeal_goods_sale_history), ToolbarOption.ToolbarMenu.LEFT)
            }
            EnumData.GoodsType.plus.name->{
                toolbarOption.initializeDefaultToolbar(getString(R.string.word_plus_goods_sale_history), ToolbarOption.ToolbarMenu.LEFT)
            }
            else->{
                toolbarOption.initializeDefaultToolbar(getString(R.string.word_normal_goods_sale_history), ToolbarOption.ToolbarMenu.LEFT)
            }
        }
//        toolbarOption.initializeDefaultToolbar(getString(R.string.word_sale_history), ToolbarOption.ToolbarMenu.LEFT)
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
