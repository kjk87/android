//package com.pplus.prnumberuser.apps.main.ui
//
//
//import android.content.Intent
//import android.os.Bundle
//import android.view.View
//import androidx.recyclerview.widget.LinearLayoutManager
//import androidx.recyclerview.widget.RecyclerView
//import com.pplus.prnumberuser.Const
//import com.pplus.prnumberuser.R
//import com.pplus.prnumberuser.apps.common.mgmt.LoginInfoManager
//import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
//import com.pplus.prnumberuser.apps.common.ui.base.BaseFragment
//import com.pplus.prnumberuser.apps.common.ui.custom.BottomItemOffsetDecoration
//import com.pplus.prnumberuser.apps.post.data.PostAdapter
//import com.pplus.prnumberuser.apps.post.ui.PostDetailActivity
//import com.pplus.prnumberuser.apps.search.ui.SearchActivity
//import com.pplus.prnumberuser.core.network.ApiBuilder
//import com.pplus.prnumberuser.core.network.model.dto.Post
//import com.pplus.prnumberuser.core.network.model.response.NewResultResponse
//import kotlinx.android.synthetic.main.fragment_main_feed.*
//import com.pplus.networks.common.PplusCallback
//import retrofit2.Call
//import java.util.*
//
//class MainFeedFragment : BaseFragment<BaseActivity>() {
//
//    private var mLayoutManager: LinearLayoutManager? = null
//    private var mPaging = 1
//    private var mTotalCount = 0
//    private var mLockListView = true
//    private var mAdapter: PostAdapter? = null
//
//    override fun getPID(): String? {
//
//        return "Main_number_plus product"
//    }
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//
//        super.onCreate(savedInstanceState)
//        if (arguments != null) {
//        }
//    }
//
//    override fun getLayoutResourceId(): Int {
//
//        return R.layout.fragment_main_feed
//    }
//
//    override fun initializeView(container: View?) {
//
//    }
//
//    override fun init() {
//        mLayoutManager = LinearLayoutManager(activity)
//        recycler_feed.layoutManager = mLayoutManager
//        mAdapter = PostAdapter(requireActivity(), true)
//        recycler_feed.adapter = mAdapter
//        recycler_feed.addItemDecoration(BottomItemOffsetDecoration(requireActivity(), R.dimen.height_60))
//        mAdapter!!.setOnItemClickListener(object : PostAdapter.OnItemClickListener {
//
//            override fun onItemClick(position: Int) {
//
//                val intent = Intent(activity, PostDetailActivity::class.java)
//                intent.putExtra(Const.DATA, mAdapter!!.getItem(position))
//                startActivity(intent)
//            }
//        })
//
//        recycler_feed.addOnScrollListener(object : RecyclerView.OnScrollListener() {
//
//            internal var pastVisibleItems: Int = 0
//            internal var visibleItemCount: Int = 0
//            internal var totalItemCount: Int = 0
//
//            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
//
//                super.onScrolled(recyclerView, dx, dy)
//                visibleItemCount = mLayoutManager!!.childCount
//                totalItemCount = mLayoutManager!!.itemCount
//                pastVisibleItems = mLayoutManager!!.findFirstVisibleItemPosition()
//                if (!mLockListView) {
//                    if (totalItemCount < mTotalCount && visibleItemCount + pastVisibleItems >= totalItemCount * Const.NEXT_RECYCLERVIEW_MARGIN) {
//                        mPaging++
//                        listCall(mPaging)
//                    }
//                }
//            }
//        })
//
//        text_main_feed_plus_page.setOnClickListener {
////            val intent = Intent(activity, MainActivity::class.java)
////            val intent = Intent(activity, SearchActivity::class.java)
////            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
////            startActivity(intent)
//
//            (getParentActivity() as AppMainActivity).setMainPageFragment()
//        }
//
//        getCount()
//    }
//
//    private fun getCount() {
//
//        ApiBuilder.create().feedCount.setCallback(object : PplusCallback<NewResultResponse<Int>> {
//
//            override fun onResponse(call: Call<NewResultResponse<Int>>, response: NewResultResponse<Int>) {
//                if (!isAdded) {
//                    return
//                }
//                mTotalCount = response.data
//                if (mTotalCount == 0) {
//                    layout_main_feed_not_exist.visibility = View.VISIBLE
//                } else {
//                    layout_main_feed_not_exist.visibility = View.GONE
//                }
//
//                mAdapter!!.clear()
//                mPaging = 1
//                listCall(mPaging)
//            }
//
//            override fun onFailure(call: Call<NewResultResponse<Int>>, t: Throwable, response: NewResultResponse<Int>) {
//
//            }
//        }).build().call()
//    }
//
//    private fun listCall(paging: Int) {
//
//        val params = HashMap<String, String>()
//        params["pg"] = "" + paging
//        params["sz"] = "" + Const.LOAD_DATA_LIMIT_CNT
//        mLockListView = true
////        showProgress("")
//        ApiBuilder.create().getFeedList(params).setCallback(object : PplusCallback<NewResultResponse<Post>> {
//
//            override fun onResponse(call: Call<NewResultResponse<Post>>, response: NewResultResponse<Post>) {
//                if (!isAdded) {
//                    return
//                }
//                mLockListView = false
////                hideProgress()
//                mAdapter!!.addAll(response.datas)
//            }
//
//            override fun onFailure(call: Call<NewResultResponse<Post>>, t: Throwable, response: NewResultResponse<Post>) {
//
//                mLockListView = false
////                hideProgress()
//            }
//        }).build().call()
//    }
//
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        when (requestCode) {
//            Const.REQ_PAGE -> {
//                getCount()
//            }
//        }
//    }
//
//    companion object {
//
//        fun newInstance(): MainFeedFragment {
//
//            val fragment = MainFeedFragment()
//            val args = Bundle()
//            fragment.arguments = args
//            return fragment
//        }
//    }
//
//}// Required empty public constructor
