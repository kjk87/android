//package com.pplus.prnumberuser.apps.goods.ui
//
//import androidx.appcompat.app.AppCompatActivity
//import android.os.Bundle
//import com.pplus.utils.part.format.FormatUtil
//import com.pplus.prnumberuser.Const
//import com.pplus.prnumberuser.R
//import com.pplus.prnumberuser.apps.common.toolbar.ImplToolbar
//import com.pplus.prnumberuser.apps.common.toolbar.OnToolbarListener
//import com.pplus.prnumberuser.apps.common.toolbar.ToolbarOption
//import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
//import com.pplus.prnumberuser.core.network.model.dto.Goods
//import kotlinx.android.synthetic.main.activity_goods_refund_info.*
//
//class GoodsRefundInfoActivity : BaseActivity(), ImplToolbar {
//    override fun getPID(): String {
//        return ""
//    }
//
//    override fun getLayoutView(): Int {
//        return R.layout.activity_goods_refund_info
//    }
//
//    override fun initializeView(savedInstanceState: Bundle?) {
//
//        val goods = intent.getParcelableExtra<Goods>(Const.DATA)
//
//        val address = goods!!.page!!.roadAddress +" "+ goods.page!!.roadDetailAddress
//
//        text_goods_refund_info1.text = getString(R.string.format_goods_refund_info1, address, FormatUtil.getPhoneNumber(goods.page!!.phone))
//
//    }
//
//    override fun getToolbarOption(): ToolbarOption {
//
//        val toolbarOption = ToolbarOption(this)
//        toolbarOption.initializeDefaultToolbar(getString(R.string.word_refund_exchange_info), ToolbarOption.ToolbarMenu.RIGHT)
//        return toolbarOption
//    }
//
//    override fun getOnToolbarClickListener(): OnToolbarListener {
//
//        return OnToolbarListener { v, toolbarMenu, tag ->
//            when (toolbarMenu) {
//                ToolbarOption.ToolbarMenu.RIGHT -> if (tag == 1) {
//                    onBackPressed()
//                }
//            }
//        }
//    }
//}
