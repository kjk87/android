//package com.pplus.prnumberuser.apps.main.ui
//
//
//import android.content.Intent
//import android.net.Uri
//import android.os.Bundle
//import android.view.View
//import com.pplus.prnumberuser.R
//import com.pplus.prnumberuser.apps.common.mgmt.LoginInfoManager
//import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
//import com.pplus.prnumberuser.apps.common.ui.base.BaseFragment
//import kotlinx.android.synthetic.main.fragment_main_nfc_guide.*
//
//class MainNFCGuidFragment : BaseFragment<BaseActivity>() {
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
//        return R.layout.fragment_main_nfc_guide
//    }
//
//    override fun initializeView(container: View?) {
//
//
//    }
//
//    override fun init() {
//        text_main_nfc_guide_install.setOnClickListener {
//            val intent = Intent(Intent.ACTION_VIEW)
//            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
//            intent.data = Uri.parse("https://play.google.com/store/apps/details?id=kr.co.totlab.tot.apay&referrer=code%3D001%26param1%3D"+LoginInfoManager.getInstance().user.no)
//            startActivity(intent)
//        }
//    }
//
//    override fun getPID(): String {
//        return "Main_nfc_guide"
//    }
//
//    companion object {
//
//        @JvmStatic
//        fun newInstance() =
//                MainNFCGuidFragment().apply {
//                    arguments = Bundle().apply {
//                        //                        putString(Const.TAB, type)
////                        putString(ARG_PARAM2, param2)
//                    }
//                }
//    }
//}
