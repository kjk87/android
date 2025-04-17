//package com.pplus.prnumberuser.apps.main.ui
//
//
//import android.content.Intent
//import android.os.Bundle
//import android.view.View
//import com.pplus.prnumberuser.Const
//import com.pplus.prnumberuser.R
//import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
//import com.pplus.prnumberuser.apps.common.ui.base.BaseFragment
//import kotlinx.android.synthetic.main.fragment_event_news.*
//
//class EventNewsFragment : BaseFragment<BaseActivity>() {
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        arguments?.let {
//            key = it.getString(Const.KEY)!!
////            param2 = it.getString(ARG_PARAM2)
//        }
//    }
//
//    override fun getLayoutResourceId(): Int {
//        return R.layout.fragment_event_news
//    }
//
//    override fun initializeView(container: View?) {}
//
//    var key = Const.PLUS
//
//    override fun init() {
//        layout_even_news_tab1.setOnClickListener {
//            setPlusFragment()
//        }
//
//        layout_even_news_tab2.setOnClickListener {
//            setFavoriteFragment()
//        }
//
//        when (key) {
//            Const.PLUS -> {
//                setPlusFragment()
//            }
//            Const.FAV_TAG -> {
//                setFavoriteFragment()
//            }
//        }
//
//    }
//
//    private fun setPlusFragment() {
//        layout_even_news_tab1.isSelected = true
//        layout_even_news_tab2.isSelected = false
//
//        val ft = childFragmentManager.beginTransaction()
//        ft.replace(R.id.event_news_container, MainGoodsPlusFragment.newInstance(), MainGoodsPlusFragment::class.java.simpleName)
//        ft.commit()
//    }
//
//    private fun setFavoriteFragment() {
//        layout_even_news_tab1.isSelected = false
//        layout_even_news_tab2.isSelected = true
//        val ft = childFragmentManager.beginTransaction()
//        ft.replace(R.id.event_news_container, MainFavoriteFragment.newInstance(), MainFavoriteFragment::class.java.simpleName)
//        ft.commit()
//    }
//
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        when (requestCode) {
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
//        fun newInstance(key: String) =
//                EventNewsFragment().apply {
//                    arguments = Bundle().apply {
//                        putString(Const.KEY, key)
////                        putString(ARG_PARAM2, param2)
//                    }
//                }
//    }
//}
