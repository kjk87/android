//package com.pplus.prnumberuser.apps.main.ui
//
//import android.app.Activity
//import android.content.Intent
//import android.os.Bundle
//import androidx.recyclerview.widget.LinearLayoutManager
//import android.view.View
//import com.pplus.prnumberuser.Const
//import com.pplus.prnumberuser.R
//import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
//import com.pplus.prnumberuser.apps.main.data.BannerListAdapter
//import kotlinx.android.synthetic.main.activity_banner_list.*
//
//class BannerListActivity : BaseActivity() {
//    override fun getPID(): String {
//        return "Main_banner_detail"
//    }
//
//    override fun getLayoutView(): Int {
//        return R.layout.activity_banner_list
//    }
//
//    override fun initializeView(savedInstanceState: Bundle?) {
//        image_banner_list_close.setOnClickListener {
//            finish()
//        }
//
//        recycler_banner_list.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this)
//        val adapter = BannerListAdapter(this)
//        recycler_banner_list.adapter = adapter
//
//        adapter.setOnItemClickListener(object : BannerListAdapter.OnItemClickListener{
//            override fun onItemClick(position: Int, view: View) {
//                val data = Intent()
//                data.putExtra(Const.POSITION, position)
//                setResult(Activity.RESULT_OK, data)
//                finish()
//            }
//        })
//    }
//
//}
