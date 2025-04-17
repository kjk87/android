//package com.pplus.prnumberuser.apps.search.ui
//
//
//import android.content.Intent
//import android.os.Bundle
//import androidx.fragment.app.Fragment
//import androidx.core.content.ContextCompat
//import androidx.recyclerview.widget.LinearLayoutManager
//import android.view.View
//import com.pplus.prnumberuser.R
//import com.pplus.prnumberuser.apps.common.builder.AlertBuilder
//import com.pplus.prnumberuser.apps.common.builder.OnAlertResultListener
//import com.pplus.prnumberuser.apps.common.mgmt.SearchHistoryManager
//import com.pplus.prnumberuser.apps.common.ui.base.BaseFragment
//import com.pplus.prnumberuser.apps.friend.ui.FriendActivity
//import com.pplus.prnumberuser.apps.page.ui.AroundPageActivity
//import com.pplus.prnumberuser.apps.search.data.SearchWordAdapter
//import kotlinx.android.synthetic.main.fragment_search_word.*
//
///**
// * A simple [Fragment] subclass.
// */
//class SearchWordFragment : BaseFragment<SearchActivity>() {
//
//    private var mRecentAdapter: SearchWordAdapter? = null
//
//    override fun getLayoutResourceId(): Int {
//        return R.layout.fragment_search_word
//    }
//
//    override fun getPID(): String {
//        return ""
//    }
//
//    override fun initializeView(container: View?) {
//
//    }
//
//    override fun init() {
////        val recLayoutManager = LinearLayoutManager(activity)
////        recLayoutManager.orientation = LinearLayoutManager.HORIZONTAL
////        recycler_search_word_recommend.layoutManager = recLayoutManager
//
////        val numberAdapter = RecommendNumberAdapter(activity!!)
////        recycler_search_word_recommend.adapter = numberAdapter
//
////        numberAdapter.setOnItemClickListener(object : RecommendNumberAdapter.OnItemClickListener {
////            override fun onItemClick(position: Int) {
////                when(position){
////                    0->{
////                        val intent = Intent(activity, PadActivity::class.java)
////                        intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
////                        activity?.startActivityForResult(intent, Const.REQ_SEARCH)
////                    }
////                    1->{
////                        val intent = Intent(activity, FriendActivity::class.java)
////                        intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
////                        activity?.startActivityForResult(intent, Const.REQ_SEARCH)
////                    }
////                    2->{
////                        val intent = Intent(activity, PersonPageActivity::class.java)
////                        intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
////                        startActivity(intent)
////                    }
////                }
////            }
////        })
//
////        text_search_pad.setOnClickListener {
////            val intent = Intent(activity, PadActivity::class.java)
////            intent.putExtra(Const.KEY, Const.PAD)
//////            intent.putExtra(Const.NUMBER, item.virtualNumber)
////            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
////            startActivity(intent)
////        }
//
//        image_search_word_around.setOnClickListener {
//            val intent = Intent(activity, AroundPageActivity::class.java)
//            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//            startActivity(intent)
//        }
//
//        image_search_word_friend.setOnClickListener {
//            val intent = Intent(activity, FriendActivity::class.java)
//            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//            startActivity(intent)
//        }
//
//        val wordLayoutManager = androidx.recyclerview.widget.LinearLayoutManager(activity)
//        wordLayoutManager.orientation = androidx.recyclerview.widget.LinearLayoutManager.HORIZONTAL
//
//        recycler_search_word_recent.layoutManager = wordLayoutManager
//        mRecentAdapter = SearchWordAdapter(true)
//        recycler_search_word_recent.adapter = mRecentAdapter
//
//        text_search_word_allDelete.setOnClickListener {
//            val builder = AlertBuilder.Builder()
//            builder.setTitle(getString(R.string.word_notice_alert))
//            builder.setStyle_alert(AlertBuilder.STYLE_ALERT.MESSAGE)
//            builder.setContents(getString(R.string.msg_question_all_delete))
//            builder.setLeftText(getString(R.string.word_cancel)).setRightText(getString(R.string.word_confirm))
//            builder.setOnAlertResultListener(object : OnAlertResultListener {
//
//                override fun onCancel() {
//
//                }
//
//                override fun onResult(event_alert: AlertBuilder.EVENT_ALERT) {
//
//                    when (event_alert) {
//                        AlertBuilder.EVENT_ALERT.RIGHT -> {
//                            SearchHistoryManager.getInstance().allDelete()
//                            mRecentAdapter!!.clear()
//                            setRecentWord()
//                        }
//                        AlertBuilder.EVENT_ALERT.LEFT -> {
//                        }
//                    }
//                }
//            }).builder().show(activity)
//        }
//
//        mRecentAdapter!!.setOnItemClickListener { position -> getParentActivity().search(mRecentAdapter!!.getItem(position)) }
//        mRecentAdapter!!.setDataChangedListener { setRecentWord() }
//    }
//
//    override fun onResume() {
//
//        super.onResume()
//        mRecentAdapter!!.setDataList(SearchHistoryManager.getInstance().searchHistoryList)
//        setRecentWord()
//    }
//
//    private fun setRecentWord() {
//
//        if (mRecentAdapter!!.itemCount > 0) {
//            text_search_word_allDelete.isEnabled = true
//            text_search_word_allDelete.setTextColor(ContextCompat.getColor(activity!!, R.color.color_2f2f2f))
//            recycler_search_word_recent.visibility = View.VISIBLE
//            text_search_word_not_exist.visibility = View.GONE
//        } else {
//            text_search_word_allDelete.setTextColor(ContextCompat.getColor(activity!!, R.color.color_acacac))
//            text_search_word_allDelete.isEnabled = false
//            recycler_search_word_recent.visibility = View.GONE
//            text_search_word_not_exist.visibility = View.VISIBLE
//        }
//    }
//
//    companion object {
//
//        fun newInstance(): SearchWordFragment {
//
//            val fragment = SearchWordFragment()
//            val args = Bundle()
//            fragment.arguments = args
//            return fragment
//        }
//    }
//
//}
