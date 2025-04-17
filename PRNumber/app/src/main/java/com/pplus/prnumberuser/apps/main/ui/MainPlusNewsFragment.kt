//package com.pplus.prnumberuser.apps.main.ui
//
//
//import android.content.Context
//import android.content.Intent
//import android.graphics.Rect
//import android.os.Bundle
//import android.view.View
//import androidx.annotation.DimenRes
//import androidx.recyclerview.widget.LinearLayoutManager
//import androidx.recyclerview.widget.RecyclerView
//import com.pplus.networks.common.PplusCallback
//import com.pplus.prnumberuser.Const
//import com.pplus.prnumberuser.R
//import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
//import com.pplus.prnumberuser.apps.common.ui.base.BaseFragment
//import com.pplus.prnumberuser.apps.news.ui.NewsDetailActivity
//import com.pplus.prnumberuser.apps.page.data.NewsAdapter
//import com.pplus.prnumberuser.core.network.ApiBuilder
//import com.pplus.prnumberuser.core.network.model.dto.News
//import com.pplus.prnumberuser.core.network.model.response.NewResultResponse
//import com.pplus.prnumberuser.core.network.model.response.SubResultResponse
//import com.pplus.utils.part.logs.LogUtil
//import kotlinx.android.synthetic.main.fragment_main_plus_news.*
//import retrofit2.Call
//import java.util.*
//
//class MainPlusNewsFragment : BaseFragment<BaseActivity>() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        arguments?.let {
////            mPage = it.getParcelable(Const.PAGE)!!
////            param2 = it.getString(ARG_PARAM2)
//        }
//    }
//
//    override fun getLayoutResourceId(): Int {
//        return R.layout.fragment_main_plus_news
//    }
//
//    override fun initializeView(container: View?) {
//
//    }
//
//    private var mTotalCount: Int = 0
//    private var mLockListView = false
//    private var mPaging = 0
//    private var mAdapter: NewsAdapter? = null
//    private var mLayoutManager: LinearLayoutManager? = null
//    private var mIsLast = false
//
//    override fun init() {
//        LogUtil.e(LOG_TAG, "init")
//
//
////        image_store_back.setOnClickListener {
////            activity?.finish()
////        }
//
//        mLayoutManager = LinearLayoutManager(activity)
//        recycler_main_plus_news.layoutManager = mLayoutManager!!
//        mAdapter = NewsAdapter(true)
//        recycler_main_plus_news.adapter = mAdapter
////        recycler_main_plus_news.addItemDecoration(CustomItemOffsetDecoration(activity!!, R.dimen.height_60))
//        recycler_main_plus_news.addOnScrollListener(object : RecyclerView.OnScrollListener() {
//
//            internal var pastVisibleItems: Int = 0
//            internal var visibleItemCount: Int = 0
//            internal var totalItemCount: Int = 0
//
//            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
//
//                super.onScrolled(recyclerView, dx, dy)
//
//                visibleItemCount = mLayoutManager!!.childCount
//                totalItemCount = mLayoutManager!!.itemCount
//                pastVisibleItems = mLayoutManager!!.findFirstVisibleItemPosition()
//                if (!mLockListView) {
//                    if (!mIsLast && visibleItemCount + pastVisibleItems >= totalItemCount * Const.NEXT_RECYCLERVIEW_MARGIN) {
//                        mPaging++
//                        listCall(mPaging)
//                    }
//                }
//            }
//        })
//
//        mAdapter!!.setOnItemClickListener(object : NewsAdapter.OnItemClickListener {
//            override fun onItemClick(position: Int) {
//                val intent = Intent(activity, NewsDetailActivity::class.java)
//                intent.putExtra(Const.DATA, mAdapter!!.getItem(position))
//                startActivity(intent)
//            }
//        })
//
//        mPaging = 0
//        listCall(mPaging)
//    }
//
//    private fun listCall(page: Int) {
//
//        mLockListView = true
//
//        val params = HashMap<String, String>()
//        params["page"] = page.toString()
//        showProgress("")
//        ApiBuilder.create().getPlusNewsList(params).setCallback(object : PplusCallback<NewResultResponse<SubResultResponse<News>>> {
//
//            override fun onResponse(call: Call<NewResultResponse<SubResultResponse<News>>>?,
//                                    response: NewResultResponse<SubResultResponse<News>>?) {
//                hideProgress()
//
//                if (response != null) {
//                    mIsLast = response.data.last!!
//                    if (response.data.first!!) {
//                        mTotalCount = response.data.totalElements!!
//                        if(mTotalCount == 0){
//                            layout_main_plus_news_not_exist?.visibility = View.VISIBLE
//                        }else{
//                            layout_main_plus_news_not_exist?.visibility = View.GONE
//                        }
//                        mAdapter!!.clear()
//                    }
//
//                    mLockListView = false
//                    mAdapter!!.addAll(response.data.content!!)
//                }
//
//            }
//
//            override fun onFailure(call: Call<NewResultResponse<SubResultResponse<News>>>?,
//                                   t: Throwable?,
//                                   response: NewResultResponse<SubResultResponse<News>>?) {
//                hideProgress()
//                mLockListView = false
//            }
//        }).build().call()
//    }
//
//    private inner class CustomItemOffsetDecoration(private val mItemOffset: Int) : RecyclerView.ItemDecoration() {
//
//        constructor(context: Context, @DimenRes lastOffsetId: Int) : this(context.resources.getDimensionPixelSize(lastOffsetId)) {}
//
//        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
//
//            super.getItemOffsets(outRect, view, parent, state)
//
//            val position = parent.getChildAdapterPosition(view)
//            if (position <= 1) {
//                outRect.set(0, mItemOffset, 0, mItemOffset)
//            } else {
//                outRect.set(0, 0, 0, mItemOffset)
//            }
//        }
//    }
//
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        when (requestCode) {
//            Const.REQ_DETAIL->{
//                mPaging = 0
//                listCall(mPaging)
//            }
//        }
//    }
//
//    override fun getPID(): String {
//        return ""
//    }
//
//    companion object {
//
//        @JvmStatic
//        fun newInstance() =
//                MainPlusNewsFragment().apply {
//                    arguments = Bundle().apply {
////                        putParcelable(Const.PAGE, page)
//                    }
//                }
//    }
//}
