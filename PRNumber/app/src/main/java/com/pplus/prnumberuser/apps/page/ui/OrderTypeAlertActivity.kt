//package com.pplus.prnumberuser.apps.page.ui
//
//import android.app.Activity
//import android.content.Intent
//import android.os.Bundle
//import android.view.View
//import com.pplus.prnumberuser.Const
//import com.pplus.prnumberuser.R
//import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
//import com.pplus.prnumberuser.core.code.common.EnumData
//import com.pplus.prnumberuser.core.network.model.dto.Page
//import com.pplus.prnumberuser.core.network.model.dto.Page2
//import kotlinx.android.synthetic.main.activity_order_type_alert.*
//
//class OrderTypeAlertActivity : BaseActivity() {
//    override fun getPID(): String {
//        return ""
//    }
//
//    override fun getLayoutView(): Int {
//        return R.layout.activity_order_type_alert
//    }
//
//    override fun initializeView(savedInstanceState: Bundle?) {
//
//        val page = intent.getParcelableExtra<Page>(Const.PAGE)
//        val page2 = intent.getParcelableExtra<Page2>(Const.PAGE2)
//
//
//        if(page != null){
//            if(page.isShopOrderable!!){
//                layout_order_type_store.visibility = View.VISIBLE
//            }else{
//                layout_order_type_store.visibility = View.GONE
//            }
//
//            if(page.isPackingOrderable!!){
//                layout_order_type_packing.visibility = View.VISIBLE
//            }else{
//                layout_order_type_packing.visibility = View.GONE
//            }
//
//            if(page.isDeliveryOrderable!!){
//                layout_order_type_delivery.visibility = View.VISIBLE
//            }else{
//                layout_order_type_delivery.visibility = View.GONE
//            }
//        }else{
//            if(page2!!.isShopOrderable!!){
//                layout_order_type_store.visibility = View.VISIBLE
//            }else{
//                layout_order_type_store.visibility = View.GONE
//            }
//
//            if(page2.isPackingOrderable!!){
//                layout_order_type_packing.visibility = View.VISIBLE
//            }else{
//                layout_order_type_packing.visibility = View.GONE
//            }
//
//            if(page2.isDeliveryOrderable!!){
//                layout_order_type_delivery.visibility = View.VISIBLE
//            }else{
//                layout_order_type_delivery.visibility = View.GONE
//            }
//        }
//
//
//
//        layout_order_type_store.setOnClickListener {
//
//            val type = EnumData.OrderType.store.type
//            val data = Intent()
//            data.putExtra(Const.TYPE, type);
//            setResult(Activity.RESULT_OK, data)
//            finish()
//        }
//
//        layout_order_type_packing.setOnClickListener {
//            val type = EnumData.OrderType.packing.type
//            val data = Intent()
//            data.putExtra(Const.TYPE, type);
//            setResult(Activity.RESULT_OK, data)
//            finish()
//        }
//
//        layout_order_type_delivery.setOnClickListener {
//            val type = EnumData.OrderType.delivery.type
//            val data = Intent()
//            data.putExtra(Const.TYPE, type);
//            setResult(Activity.RESULT_OK, data)
//            finish()
//        }
//
//        image_order_type_cancel.setOnClickListener {
//            setResult(Activity.RESULT_CANCELED)
//            finish()
//        }
//
//    }
//}
