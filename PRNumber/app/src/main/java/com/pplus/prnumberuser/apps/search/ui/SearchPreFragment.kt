//package com.pplus.prnumberuser.apps.search.ui
//
//
//import android.content.Intent
//import android.os.Bundle
//import android.view.View
//import com.pplus.prnumberuser.R
//import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
//import com.pplus.prnumberuser.apps.common.ui.base.BaseFragment
//import com.pplus.prnumberuser.apps.theme.ui.ThemeActivity
//import com.pplus.prnumberuser.core.util.PplusCommonUtil
//import kotlinx.android.synthetic.main.fragment_search_pre.*
//
//class SearchPreFragment : BaseFragment<BaseActivity>() {
//
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        arguments?.let {
//            //            mTab = it.getString(Const.TAB)
////            param2 = it.getString(ARG_PARAM2)
//        }
//    }
//
//    override fun getLayoutResourceId(): Int {
//        return R.layout.fragment_search_pre
//    }
//
//    override fun initializeView(container: View?) {
//
//    }
//
//    override fun init() {
//
//        layout_search_pre_around.setOnClickListener {
//            val intent = Intent(activity, LocationAroundPageActivity::class.java)
//            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//            startActivity(intent)
//        }
//
//        image_search_pre_banner1.setOnClickListener {
//            val intent = Intent(activity, ThemeActivity::class.java)
//            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//            startActivity(intent)
//        }
//
//        image_search_pre_banner2.setOnClickListener {
//            PplusCommonUtil.openChromeWebView(activity!!, getString(R.string.msg_seller2_url)+"?timestamp=" + System.currentTimeMillis())
//        }
//    }
//
//
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        when (requestCode) {
//
//        }
//    }
//
//    override fun getPID(): String {
//        return ""
//    }
//
//    companion object {
//
//
//        @JvmStatic
//        fun newInstance() =
//                SearchPreFragment().apply {
//                    arguments = Bundle().apply {
//                        //                        putString(Const.TAB, tab.name)
////                        putString(ARG_PARAM2, param2)
//                    }
//                }
//    }
//}
