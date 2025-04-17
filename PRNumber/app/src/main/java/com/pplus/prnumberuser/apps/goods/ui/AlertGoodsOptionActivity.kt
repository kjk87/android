//package com.pplus.prnumberuser.apps.goods.ui
//
//import android.app.Activity
//import android.content.Intent
//import android.os.Bundle
//import androidx.recyclerview.widget.LinearLayoutManager
//import com.pplus.prnumberuser.Const
//import com.pplus.prnumberuser.R
//import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
//import com.pplus.prnumberuser.apps.goods.data.GoodsOptionDetailAdapter
//import com.pplus.prnumberuser.apps.goods.data.GoodsOptionItemAdapter
//import com.pplus.prnumberuser.core.network.model.dto.GoodsOptionDetail
//import com.pplus.prnumberuser.core.network.model.dto.GoodsOptionItem
//import kotlinx.android.synthetic.main.activity_alert_goods_option.*
//
//class AlertGoodsOptionActivity : BaseActivity() {
//    override fun getPID(): String {
//        return ""
//    }
//
//    override fun getLayoutRes(): Int {
//        return R.layout.activity_alert_goods_option
//    }
//
//    override fun initializeView(savedInstanceState: Bundle?) {
//        val name = intent.getStringExtra(Const.NAME)
//        val itemList = intent.getParcelableArrayListExtra<GoodsOptionItem>(Const.ITEM)
//
//        recycler_alert_goods_option.layoutManager = LinearLayoutManager(this)
//
//        text_alert_goods_option_name.setOnClickListener {
//            onBackPressed()
//        }
//
//
//        if(itemList == null){
//            val detailList = intent.getParcelableArrayListExtra<GoodsOptionDetail>(Const.DETAIL)
//            val adapter = GoodsOptionDetailAdapter()
//            recycler_alert_goods_option.adapter = adapter
//            adapter.setDataList(detailList!!)
//            adapter.setOnItemClickListener(object : GoodsOptionDetailAdapter.OnItemClickListener{
//                override fun onItemClick(position: Int) {
//                    val detail = detailList[position]
//                    val data = Intent()
//                    data.putExtra(Const.POSITION, intent.getIntExtra(Const.POSITION, 0))
//                    data.putExtra(Const.DATA, detail)
//                    setResult(Activity.RESULT_OK, data)
//                    finish()
//                }
//            })
//        }else{
//            val adapter = GoodsOptionItemAdapter()
//            recycler_alert_goods_option.adapter = adapter
//            adapter.setDataList(itemList)
//            adapter.setOnItemClickListener(object : GoodsOptionItemAdapter.OnItemClickListener{
//                override fun onItemClick(position: Int) {
//                    val item = itemList[position]
//                    val data = Intent()
//                    data.putExtra(Const.POSITION, intent.getIntExtra(Const.POSITION, 0))
//                    data.putExtra(Const.DATA, item)
//                    setResult(Activity.RESULT_OK, data)
//                    finish()
//                }
//            })
//        }
//
//        text_alert_goods_option_name.text = name
//
//    }
//
//}
