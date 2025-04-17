//package com.pplus.prnumberuser.apps.main.ui
//
//
//import android.content.Intent
//import android.os.Bundle
//import android.view.View
//import android.view.inputmethod.EditorInfo
//import com.pplus.prnumberuser.Const
//import com.pplus.prnumberuser.R
//import com.pplus.prnumberuser.apps.common.mgmt.SearchHistoryManager
//import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
//import com.pplus.prnumberuser.apps.common.ui.base.BaseFragment
//import com.pplus.prnumberuser.apps.search.ui.SearchResultFragment
//import com.pplus.prnumberuser.core.location.LocationUtil
//import com.pplus.prnumberuser.core.util.PplusCommonUtil
//import kotlinx.android.synthetic.main.activity_search.*
//
//class MainSearchFragment : BaseFragment<BaseActivity>() {
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
//        return R.layout.activity_search
//    }
//
//    override fun initializeView(container: View?) {
//
//    }
//
//    override fun init() {
//        image_search.setOnClickListener {
//            search()
//        }
//
//        edit_search_word.setSingleLine()
//        edit_search_word.imeOptions = EditorInfo.IME_ACTION_SEARCH
//        edit_search_word.setOnEditorActionListener { v, actionId, event ->
//            when (actionId) {
//                EditorInfo.IME_ACTION_SEARCH -> search()
//            }
//            true
//        }
//
//        image_search_number.setOnClickListener {
//            val intent = Intent(activity, PadActivity::class.java)
//            intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
//            startActivity(intent)
//        }
//        PplusCommonUtil.alertLocation(getParentActivity(), false, object : PplusCommonUtil.Companion.SuccessLocationListener {
//            override fun onSuccess() {
//                if (!isAdded) {
//                    return
//                }
//                search()
//            }
//        })
//    }
//
//    fun search(word: String) {
//
//        edit_search_word.setText(word)
//        search()
//    }
//
//    private fun search() {
//
//        val word = edit_search_word.text.toString().trim()
//
////        if (word.isEmpty()) {
////            showAlert(R.string.msg_input_searchWord)
////            return
////        }
////
////        if (NumberUtils.isDigits(word) && word.length < 4) {
////            showAlert(R.string.msg_input_over_4_number)
////            return
////        }
//
//        PplusCommonUtil.hideKeyboard(activity)
//        SearchHistoryManager.getInstance().add(word)
//
//        val fragment = childFragmentManager.findFragmentById(R.id.search_container)
//
//        if (fragment is SearchResultFragment) {
//            fragment.reSearch(word)
//        } else {
//            openFragment(SearchResultFragment.newInstance(word))
//        }
//    }
//
//    fun openFragment(fragment: BaseFragment<*>) {
//
//        val ft = childFragmentManager.beginTransaction()
//        ft.replace(R.id.search_container, fragment, fragment.javaClass.simpleName)
//        ft.addToBackStack(fragment.javaClass.simpleName)
//        ft.commit()
//    }
//
//    override fun getPID(): String {
//        return "Main_search"
//    }
//
//    companion object {
//
//
//        @JvmStatic
//        fun newInstance() =
//                MainSearchFragment().apply {
//                    arguments = Bundle().apply {
//                        //                        putString(Const.TAB, tab.name)
////                        putString(ARG_PARAM2, param2)
//                    }
//                }
//    }
//}
