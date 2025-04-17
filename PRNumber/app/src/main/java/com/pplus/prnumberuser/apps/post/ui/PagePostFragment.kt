//package com.pplus.prnumberuser.apps.post.ui
//
//
//import android.app.Activity
//import android.content.Intent
//import android.os.Bundle
//import androidx.recyclerview.widget.LinearLayoutManager
//import androidx.recyclerview.widget.RecyclerView
//import android.view.View
//import androidx.activity.result.contract.ActivityResultContracts
//import com.pplus.utils.part.format.FormatUtil
//import com.pplus.prnumberuser.Const
//import com.pplus.prnumberuser.R
//import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
//import com.pplus.prnumberuser.apps.common.ui.base.BaseFragment
//import com.pplus.prnumberuser.apps.common.ui.custom.BottomItemOffsetDecoration
//import com.pplus.prnumberuser.apps.post.data.PostAdapter
//import com.pplus.prnumberuser.core.network.ApiBuilder
//import com.pplus.prnumberuser.core.network.model.dto.Page
//import com.pplus.prnumberuser.core.network.model.dto.Post
//import com.pplus.prnumberuser.core.network.model.response.NewResultResponse
//import com.pplus.prnumberuser.core.util.PplusCommonUtil
//import kotlinx.android.synthetic.main.activity_event_goods.*
//import com.pplus.networks.common.PplusCallback
//import retrofit2.Call
//import java.util.*
//
//class PagePostFragment : BaseFragment<BaseActivity>() {
//
//    private var mLayoutManager: androidx.recyclerview.widget.LinearLayoutManager? = null
//    private var mPaging = 1
//    private var mTotalCount = 0
//    private var mLockListView = true
//    private var mAdapter: PostAdapter? = null
//    private var mPage: Page? = null
//
//    override fun getPID(): String? {
//
//        return null
//    }
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//
//        super.onCreate(savedInstanceState)
//        arguments?.let {
//            mPage = it.getParcelable(Const.PAGE)
//        }
//    }
//
//    override fun getLayoutResourceId(): Int {
//
//        return R.layout.activity_event_goods
//    }
//
//    override fun initializeView(container: View?) {
//
//    }
//
//    override fun init() {
//        mLayoutManager = androidx.recyclerview.widget.LinearLayoutManager(activity)
//        recycler_event_goods.layoutManager = mLayoutManager
//        mAdapter = PostAdapter(requireActivity(), false)
//        recycler_event_goods.adapter = mAdapter
//        recycler_event_goods.addItemDecoration(BottomItemOffsetDecoration(requireActivity(), R.dimen.height_60))
//        mAdapter!!.setOnItemClickListener(object : PostAdapter.OnItemClickListener {
//
//            override fun onItemClick(position: Int) {
//
//                val intent = Intent(activity, PostDetailActivity::class.java)
//                intent.putExtra(Const.DATA, mAdapter!!.getItem(position))
//                detailLauncher.launch(intent)
//            }
//        })
//
//        recycler_event_goods.addOnScrollListener(object : androidx.recyclerview.widget.RecyclerView.OnScrollListener() {
//
//            internal var pastVisibleItems: Int = 0
//            internal var visibleItemCount: Int = 0
//            internal var totalItemCount: Int = 0
//
//            override fun onScrolled(recyclerView: androidx.recyclerview.widget.RecyclerView, dx: Int, dy: Int) {
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
//
//        getCount()
//    }
//
//    private fun getCount() {
//        val params = HashMap<String, String>()
//        params["boardNo"] = mPage!!.prBoard!!.no.toString()
//        ApiBuilder.create().getBoardPostCount(params).setCallback(object : PplusCallback<NewResultResponse<Int>> {
//
//            override fun onResponse(call: Call<NewResultResponse<Int>>, response: NewResultResponse<Int>) {
//                if (!isAdded) {
//                    return
//                }
//                mTotalCount = response.data
//                text_event_goods_count?.text = PplusCommonUtil.fromHtml(getString(R.string.html_total_count2, FormatUtil.getMoneyType(mTotalCount.toString())))
//                if (mTotalCount == 0) {
//                    layout_event_goods_not_exist?.visibility = View.VISIBLE
//                } else {
//                    layout_event_goods_not_exist?.visibility = View.GONE
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
//        mLockListView = true
//        val params = HashMap<String, String>()
//        params["boardNo"] = mPage!!.prBoard!!.no.toString()
//        params["pg"] = "" + paging
//        mLockListView = true
//        showProgress("")
//        ApiBuilder.create().getBoardPostList(params).setCallback(object : PplusCallback<NewResultResponse<Post>> {
//
//            override fun onResponse(call: Call<NewResultResponse<Post>>, response: NewResultResponse<Post>) {
//                if (!isAdded) {
//                    return
//                }
//                mLockListView = false
//                hideProgress()
//                mAdapter!!.addAll(response.datas)
//            }
//
//            override fun onFailure(call: Call<NewResultResponse<Post>>, t: Throwable, response: NewResultResponse<Post>) {
//
//                mLockListView = false
//                hideProgress()
//            }
//        }).build().call()
//    }
//
//    val detailLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
//        getCount()
//    }
//
//    companion object {
//
//
//        @JvmStatic
//        fun newInstance(page: Page) =
//                PagePostFragment().apply {
//                    arguments = Bundle().apply {
//                        putParcelable(Const.PAGE, page)
////                        putString(ARG_PARAM2, param2)
//                    }
//                }
//    }
//
//}// Required empty public constructor
