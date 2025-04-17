package com.pplus.prnumberbiz.apps.pages.ui


import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import androidx.annotation.DimenRes
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import com.pplus.prnumberbiz.Const
import com.pplus.prnumberbiz.R
import com.pplus.prnumberbiz.apps.common.mgmt.LoginInfoManager
import com.pplus.prnumberbiz.apps.common.ui.base.BaseActivity
import com.pplus.prnumberbiz.apps.common.ui.base.BaseFragment
import com.pplus.prnumberbiz.apps.common.ui.custom.BottomItemOffsetDecoration
import com.pplus.prnumberbiz.apps.post.data.MyPostAdapter
import com.pplus.prnumberbiz.apps.post.ui.PostDetailActivity
import com.pplus.prnumberbiz.core.network.ApiBuilder
import com.pplus.prnumberbiz.core.network.model.dto.Post
import com.pplus.prnumberbiz.core.network.model.response.NewResultResponse
import kotlinx.android.synthetic.main.fragment_post.*
import network.common.PplusCallback
import retrofit2.Call
import java.util.*


/**
 * A simple [Fragment] subclass.
 * Use the [PostFragment.newInstance] factory method to
 * create an instance of this mapFragment.
 */
class PostFragment : BaseFragment<BaseActivity>() {

    override fun getPID(): String {
        return ""
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        if (arguments != null) {
//            mPage = arguments!!.getParcelable(Const.PAGE)
        }
    }

    override fun getLayoutResourceId(): Int {
        return R.layout.fragment_post
    }

    override fun initializeView(container: View?) {

    }

    private var mAdapter: MyPostAdapter? = null

    private var mPaging: Int = 0
    private var mTotalCount = 0
    private var mLayoutManager: LinearLayoutManager? = null
    private var mLockListView = true

    override fun init() {
        mLayoutManager = LinearLayoutManager(activity)
        recycler_post.layoutManager = mLayoutManager
        mAdapter = MyPostAdapter(activity!!, this)
        recycler_post.adapter = mAdapter
        recycler_post.addItemDecoration(BottomItemOffsetDecoration(activity!!, R.dimen.height_60))
        mAdapter!!.setOnItemClickListener(object : MyPostAdapter.OnItemClickListener {

            override fun onItemClick(position: Int) {

                val intent = Intent(activity, PostDetailActivity::class.java)
                intent.putExtra(Const.DATA, mAdapter!!.getItem(position))
                activity!!.startActivityForResult(intent, Const.REQ_DETAIL)
            }

            override fun refresh() {
                getCount()
            }
        })

        recycler_post.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            internal var pastVisibleItems: Int = 0
            internal var visibleItemCount: Int = 0
            internal var totalItemCount: Int = 0

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {

                super.onScrolled(recyclerView, dx, dy)
                visibleItemCount = mLayoutManager!!.childCount
                totalItemCount = mLayoutManager!!.itemCount
                pastVisibleItems = mLayoutManager!!.findFirstVisibleItemPosition()
                if (!mLockListView) {
                    if (totalItemCount < mTotalCount && visibleItemCount + pastVisibleItems >= totalItemCount * Const.NEXT_RECYCLERVIEW_MARGIN) {
                        mPaging++
                        listCall(mPaging)
                    }
                }
            }
        })

        getCount()
    }


    private inner class CustomItemOffsetDecoration(private val mItemOffset: Int, private val mTopOffset: Int, private val mLastOffset: Int) : RecyclerView.ItemDecoration() {

        constructor(context: Context, @DimenRes itemOffsetId: Int, @DimenRes topOffsetId: Int, @DimenRes lastOffsetId: Int) : this(context.resources.getDimensionPixelSize(itemOffsetId), context.resources.getDimensionPixelSize(topOffsetId), context.resources.getDimensionPixelSize(lastOffsetId)) {}

        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {

            super.getItemOffsets(outRect, view, parent, state)

            val position = parent.getChildAdapterPosition(view)
            if (position == 0) {
                outRect.set(0, mTopOffset, 0, mItemOffset)
            } else {
                outRect.set(0, 0, 0, mItemOffset)
            }
            if (position == mTotalCount - 1) {
                outRect.bottom = mLastOffset
            }

        }
    }

    private fun getCount() {
        val params = HashMap<String, String>()
        params["boardNo"] = LoginInfoManager.getInstance().user.page!!.prBoard!!.no.toString()
        ApiBuilder.create().getBoardPostCount(params).setCallback(object : PplusCallback<NewResultResponse<Int>> {

            override fun onResponse(call: Call<NewResultResponse<Int>>, response: NewResultResponse<Int>) {
                if (!isAdded) {
                    return
                }
                mTotalCount = response.data
                mAdapter!!.clear()
                mPaging = 1
                listCall(mPaging)
            }

            override fun onFailure(call: Call<NewResultResponse<Int>>, t: Throwable, response: NewResultResponse<Int>) {

            }
        }).build().call()
    }

    private fun listCall(paging: Int) {

        mLockListView = true
        val params = HashMap<String, String>()
        params["boardNo"] = LoginInfoManager.getInstance().user.page!!.prBoard!!.no.toString()
        params["pg"] = "" + paging
        mLockListView = true
        showProgress("")
        ApiBuilder.create().getBoardPostList(params).setCallback(object : PplusCallback<NewResultResponse<Post>> {

            override fun onResponse(call: Call<NewResultResponse<Post>>, response: NewResultResponse<Post>) {
                if (!isAdded) {
                    return
                }
                mLockListView = false
                hideProgress()
                mAdapter!!.addAll(response.datas)
            }

            override fun onFailure(call: Call<NewResultResponse<Post>>, t: Throwable, response: NewResultResponse<Post>) {

                mLockListView = false
                hideProgress()
            }
        }).build().call()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                Const.REQ_MODIFY, Const.REQ_REG, Const.REQ_DETAIL -> getCount()
            }
        }
    }

    companion object {

        fun newInstance( ): PostFragment {

            val fragment = PostFragment()
//            val args = Bundle()
//            args.putParcelable(Const.PAGE, page)
//            mapFragment.arguments = args
            return fragment
        }
    }

}// Required empty public constructor
