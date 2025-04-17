//package com.pplus.prnumberbiz.apps.goods.ui
//
//import android.content.Intent
//import android.os.Bundle
//import com.pplus.prnumberbiz.Const
//import com.pplus.prnumberbiz.R
//import com.pplus.prnumberbiz.apps.common.mgmt.LoginInfoManager
//import com.pplus.prnumberbiz.apps.common.toolbar.ImplToolbar
//import com.pplus.prnumberbiz.apps.common.toolbar.OnToolbarListener
//import com.pplus.prnumberbiz.apps.common.toolbar.ToolbarOption
//import com.pplus.prnumberbiz.apps.common.ui.base.BaseActivity
//import com.pplus.prnumberbiz.core.code.common.EnumData
//import com.pplus.prnumberbiz.core.util.PplusCommonUtil
//import kotlinx.android.synthetic.main.activity_sale_goods.*
//
//class SaleGoodsActivity : BaseActivity(), ImplToolbar {
//    override fun getPID(): String {
//        return ""
//    }
//
//    override fun getLayoutRes(): Int {
//        return R.layout.activity_sale_goods
//    }
//
//    override fun initializeView(savedInstanceState: Bundle?) {
//        text_sale_goods_total_price.text = PplusCommonUtil.fromHtml(getString(R.string.html_money_unit, "1,000"))
//        text_sale_goods_total_count.text = PplusCommonUtil.fromHtml(getString(R.string.html_count_unit1, "1,000"))
//        text_sale_goods_reg_count.text = PplusCommonUtil.fromHtml(getString(R.string.html_count_unit2, "1,000"))
//
//        text_sale_goods_reg.setOnClickListener {
//            val intent = Intent(this, GoodsRegActivity::class.java)
//            intent.putExtra(Const.MODE, EnumData.MODE.WRITE)
//            intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
//            startActivityForResult(intent, Const.REQ_REG)
//        }
//
//        text_sale_goods_config.setOnClickListener {
//            val intent = Intent(this, GoodsListActivity::class.java)
//            intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
//            startActivityForResult(intent, Const.REQ_REG)
//        }
//
//        text_sale_goods_sale_history.setOnClickListener {
//            val intent = Intent(this, GoodsSaleTotalHistoryActivity::class.java)
//            intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
//            startActivity(intent)
//        }
//
//        text_sale_goods_withdraw.setOnClickListener {
//
//        }
//    }
//
//    override fun getToolbarOption(): ToolbarOption {
//        val toolbarOption = ToolbarOption(this)
//        toolbarOption.initializeDefaultToolbar(getString(R.string.format_sale_history, LoginInfoManager.getInstance().user.page!!.name), ToolbarOption.ToolbarMenu.LEFT)
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
