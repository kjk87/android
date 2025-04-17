//package com.pplus.prnumberuser.apps.news.ui
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
//import com.pplus.prnumberuser.apps.common.builder.AlertBuilder
//import com.pplus.prnumberuser.apps.common.builder.OnAlertResultListener
//import com.pplus.prnumberuser.apps.common.mgmt.LoginInfoManager
//import com.pplus.prnumberuser.apps.common.toolbar.ImplToolbar
//import com.pplus.prnumberuser.apps.common.toolbar.OnToolbarListener
//import com.pplus.prnumberuser.apps.common.toolbar.ToolbarOption
//import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
//import com.pplus.prnumberuser.apps.news.data.NewsHeaderReviewAdapter
//import com.pplus.prnumberuser.core.network.ApiBuilder
//import com.pplus.prnumberuser.core.network.model.dto.News
//import com.pplus.prnumberuser.core.network.model.dto.NewsReview
//import com.pplus.prnumberuser.core.network.model.response.NewResultResponse
//import com.pplus.prnumberuser.core.network.model.response.SubResultResponse
//import com.pplus.utils.part.utils.StringUtils
//import kotlinx.android.synthetic.main.activity_post_detail.*
//import kotlinx.android.synthetic.main.layout_input_reply.*
//import retrofit2.Call
//import java.util.*
//
//class NewsDetailActivity : BaseActivity(), ImplToolbar {
//    override fun getPID(): String {
//        return ""
//    }
//
//    override fun getLayoutView(): Int {
//        return R.layout.activity_post_detail
//    }
//
//    lateinit var mNews: News
//    var mAdapter: NewsHeaderReviewAdapter? = null
//    private var mIsLast = false
//    private var mSort = "seqNo,asc"
//    private var mTotalCount: Int = 0
//    private var mLockListView = false
//    private var mPaging = 0
//    private var mLayoutManager: LinearLayoutManager? = null
//
//    override fun initializeView(savedInstanceState: Bundle?) {
//        mNews = intent.getParcelableExtra(Const.DATA)!!
//
//        mAdapter = NewsHeaderReviewAdapter()
//        mLayoutManager = LinearLayoutManager(this)
//        recycler_post_detail_reply.layoutManager = mLayoutManager!!
//        recycler_post_detail_reply.adapter = mAdapter
//        recycler_post_detail_reply.addItemDecoration(CustomItemOffsetDecoration(this, R.dimen.height_100))
//
//        recycler_post_detail_reply.addOnScrollListener(object : RecyclerView.OnScrollListener() {
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
//        text_input_reply.setOnClickListener {
//            val review = edit_reply.text.toString().trim()
//
//            if (StringUtils.isEmpty(review)) {
//                return@setOnClickListener
//            }
//
//            showProgress("")
//            val params = NewsReview()
//            params.review = review
//            params.pageSeqNo = mNews.pageSeqNo
//            params.newsSeqNo = mNews.seqNo
//
//            ApiBuilder.create().insertNewsReview(params).setCallback(object : PplusCallback<NewResultResponse<Any>> {
//
//                override fun onResponse(call: Call<NewResultResponse<Any>>, response: NewResultResponse<Any>) {
//
//                    hideProgress()
//                    edit_reply.setText("")
//                    mPaging = 0
//                    listCall(mPaging)
//                }
//
//                override fun onFailure(call: Call<NewResultResponse<Any>>, t: Throwable, response: NewResultResponse<Any>) {
//
//                    hideProgress()
//                }
//            }).build().call()
//
//        }
//
//        mAdapter!!.setOnItemClickListener(object : NewsHeaderReviewAdapter.OnItemClickListener {
//            override fun onItemClick(position: Int) {
//
//                if(!LoginInfoManager.getInstance().isMember){
//                    return
//                }
//                val item = mAdapter!!.getItem(position)
//                val isMe = LoginInfoManager.getInstance().user.no == item.memberSeqNo
//
//                if (isMe) {
//                    val builder = AlertBuilder.Builder()
//                    builder.setLeftText(getString(R.string.word_cancel))
//                    builder.setContents(getString(R.string.word_modified), getString(R.string.word_delete))
//                    builder.setStyle_alert(AlertBuilder.STYLE_ALERT.LIST_BOTTOM).setOnAlertResultListener(object : OnAlertResultListener {
//
//                        override fun onCancel() {
//
//                        }
//
//                        override fun onResult(event_alert: AlertBuilder.EVENT_ALERT) {
//
//                            when (event_alert.getValue()) {
//                                1 -> {
//                                    val intent = Intent(this@NewsDetailActivity, NewsReviewEditActivity::class.java)
//                                    intent.putExtra(Const.DATA, item)
//                                    startActivityForResult(intent, Const.REQ_REPLY)
//                                }
//                                2 -> {
//                                    AlertBuilder.Builder().setContents(getString(R.string.msg_question_delete_reply)).setLeftText(getString(R.string.word_cancel)).setRightText(getString(R.string.word_confirm)).setOnAlertResultListener(object : OnAlertResultListener {
//
//                                        override fun onCancel() {
//
//                                        }
//
//                                        override fun onResult(event_alert: AlertBuilder.EVENT_ALERT) {
//
//                                            when (event_alert) {
//                                                AlertBuilder.EVENT_ALERT.RIGHT -> {
//                                                    deleteNewsReview(item.seqNo)
//                                                }
//                                            }
//                                        }
//                                    }).builder().show(this@NewsDetailActivity)
//                                }
//                            }
//                        }
//                    }).builder().show(this@NewsDetailActivity)
//                }
//            }
//        })
//
//        getNews()
//    }
//
//    private inner class CustomItemOffsetDecoration(private val mLastOffset: Int) : RecyclerView.ItemDecoration() {
//
//        constructor(context: Context, @DimenRes lastOffsetId: Int) : this(context.resources.getDimensionPixelSize(lastOffsetId)) {}
//
//        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
//
//            super.getItemOffsets(outRect, view, parent, state)
//
//            val position = parent.getChildAdapterPosition(view)
//            if (mAdapter!!.itemCount > 0 && position == mAdapter!!.itemCount - 1) {
//                outRect.bottom = mLastOffset
//            }
//
//        }
//    }
//
//    private fun getNews() {
//        val params = HashMap<String, String>()
//        params["seqNo"] = mNews.seqNo.toString()
//        showProgress("")
//        ApiBuilder.create().getNews(params).setCallback(object : PplusCallback<NewResultResponse<News>> {
//            override fun onResponse(call: Call<NewResultResponse<News>>?, response: NewResultResponse<News>?) {
//                hideProgress()
//                if(response?.data != null){
//                    mNews = response.data
//                    mAdapter!!.mNews = mNews
//                }
//
//                mPaging = 0
//                listCall(mPaging)
//            }
//
//            override fun onFailure(call: Call<NewResultResponse<News>>?, t: Throwable?, response: NewResultResponse<News>?) {
//                hideProgress()
//            }
//
//        }).build().call()
//    }
//
//    private fun listCall(page: Int) {
//
//        mLockListView = true
//        val params = HashMap<String, String>()
//
//        params["newsSeqNo"] = mNews.seqNo.toString()
//        params["sort"] = mSort
//        params["page"] = page.toString()
//        showProgress("")
//        ApiBuilder.create().getNewsReviewList(params).setCallback(object : PplusCallback<NewResultResponse<SubResultResponse<NewsReview>>> {
//            override fun onResponse(call: Call<NewResultResponse<SubResultResponse<NewsReview>>>?,
//                                    response: NewResultResponse<SubResultResponse<NewsReview>>?) {
//                hideProgress()
//                if (response?.data != null) {
//                    mIsLast = response.data.last!!
//                    if (response.data.first!!) {
//                        mTotalCount = response.data.totalElements!!
//                        mAdapter!!.mTotalCount = mTotalCount
//                        mAdapter!!.clear()
//                    }
//
//                    mLockListView = false
//
//                    val dataList = response.data.content!!
//                    mAdapter!!.addAll(dataList)
//                }
//            }
//
//            override fun onFailure(call: Call<NewResultResponse<SubResultResponse<NewsReview>>>?,
//                                   t: Throwable?,
//                                   response: NewResultResponse<SubResultResponse<NewsReview>>?) {
//                hideProgress()
//            }
//        }).build().call()
//
//    }
//
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        when (requestCode) {
//            Const.REQ_REPLY -> {
//                mPaging = 0
//                listCall(mPaging)
//            }
//        }
//    }
//
//    fun deleteNewsReview(seqNo: Long?) {
//        val params = HashMap<String, String>()
//
//        params["seqNo"] = seqNo.toString()
//        showProgress("")
//        ApiBuilder.create().deleteNewsReview(params).setCallback(object : PplusCallback<NewResultResponse<Any>> {
//
//            override fun onResponse(call: Call<NewResultResponse<Any>>, response: NewResultResponse<Any>) {
//
//                hideProgress()
//                mPaging = 0
//                listCall(mPaging)
//            }
//
//            override fun onFailure(call: Call<NewResultResponse<Any>>, t: Throwable, response: NewResultResponse<Any>) {
//
//                hideProgress()
//            }
//        }).build().call()
//    }
//
//    override fun getToolbarOption(): ToolbarOption {
//
//        val toolbarOption = ToolbarOption(this)
//        toolbarOption.initializeDefaultToolbar(getString(R.string.word_news), ToolbarOption.ToolbarMenu.LEFT)
//
//        return toolbarOption
//    }
//
//    override fun getOnToolbarClickListener(): OnToolbarListener {
//
//        return OnToolbarListener { v, toolbarMenu, tag ->
//            when (toolbarMenu) {
//                ToolbarOption.ToolbarMenu.LEFT -> if (tag == 1) {
//                    onBackPressed()
//                }
//            }
//        }
//    }
//}
