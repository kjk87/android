//package com.pplus.prnumberuser.apps.page.ui
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
//import com.pplus.prnumberuser.core.code.common.EnumData
//import com.pplus.prnumberuser.core.network.ApiBuilder
//import com.pplus.prnumberuser.core.network.model.dto.News
//import com.pplus.prnumberuser.core.network.model.dto.Page
//import com.pplus.prnumberuser.core.network.model.response.NewResultResponse
//import com.pplus.prnumberuser.core.network.model.response.SubResultResponse
//import com.pplus.utils.part.format.FormatUtil
//import com.pplus.utils.part.logs.LogUtil
//import kotlinx.android.synthetic.main.fragment_store_news.*
//import retrofit2.Call
//import java.util.*
//
//class StoreNewsFragment : BaseFragment<BaseActivity>() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        arguments?.let {
//            mPage = it.getParcelable(Const.PAGE)!!
////            param2 = it.getString(ARG_PARAM2)
//        }
//    }
//
//    override fun getLayoutResourceId(): Int {
//        return R.layout.fragment_store_news
//    }
//
//    override fun initializeView(container: View?) {
//
//    }
//
//    private lateinit var mPage: Page
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
//        recycler_store_news.layoutManager = mLayoutManager!!
//        mAdapter = NewsAdapter(false)
//        recycler_store_news.adapter = mAdapter
////        recycler_store_news.addItemDecoration(CustomItemOffsetDecoration(activity!!, R.dimen.height_60))
//        recycler_store_news.addOnScrollListener(object : RecyclerView.OnScrollListener() {
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
//        val params = HashMap<String, String>()
//
//        params["pageSeqNo"] = mPage.no.toString()
//        params["sort"] = "${EnumData.BuyGoodsSort.seqNo.name},${EnumData.BuyGoodsSort.desc.name}"
//        params["page"] = page.toString()
//
//        showProgress("")
//        ApiBuilder.create().getNewsListByPageSeqNo(params).setCallback(object : PplusCallback<NewResultResponse<SubResultResponse<News>>> {
//
//            override fun onResponse(call: Call<NewResultResponse<SubResultResponse<News>>>?,
//                                    response: NewResultResponse<SubResultResponse<News>>?) {
//                hideProgress()
//
//                if (response != null) {
//                    mIsLast = response.data.last!!
//                    if (response.data.first!!) {
//                        mTotalCount = response.data.totalElements!!
//                        text_store_news_count?.text = getString(R.string.format_news, FormatUtil.getMoneyType(mTotalCount.toString()))
//
//                        if (mTotalCount == 0) {
//                            layout_store_news_not_exist?.visibility = View.VISIBLE
//                        } else {
//                            layout_store_news_not_exist?.visibility = View.GONE
//                        }
//                        mAdapter?.clear()
//                    }
//
//                    mLockListView = false
//                    mAdapter?.addAll(response.data.content!!)
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
//        fun newInstance(page: Page) =
//                StoreNewsFragment().apply {
//                    arguments = Bundle().apply {
//                        putParcelable(Const.PAGE, page)
//                    }
//                }
//    }
//}
