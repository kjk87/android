package com.pplus.prnumberbiz.apps.post.ui


import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.view.View
import androidx.annotation.DimenRes
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pple.pplus.utils.part.format.FormatUtil
import com.pplus.prnumberbiz.Const
import com.pplus.prnumberbiz.R
import com.pplus.prnumberbiz.apps.common.mgmt.LoginInfoManager
import com.pplus.prnumberbiz.apps.common.ui.base.BaseFragment
import com.pplus.prnumberbiz.apps.post.data.SelectPostAdapter
import com.pplus.prnumberbiz.core.network.ApiBuilder
import com.pplus.prnumberbiz.core.network.model.dto.Post
import com.pplus.prnumberbiz.core.network.model.response.NewResultResponse
import com.pplus.prnumberbiz.core.sns.kakao.KakaoLinkUtil
import com.pplus.prnumberbiz.core.util.PplusCommonUtil
import kotlinx.android.synthetic.main.fragment_select_post.*
import network.common.PplusCallback
import retrofit2.Call
import java.util.*


/**
 * A simple [Fragment] subclass.
 * Use the [SelectPostFragment.newInstance] factory method to
 * create an instance of this mapFragment.
 */
class SelectPostFragment : BaseFragment<SelectPostActivity>() {


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        if (arguments != null) {
            mSelectData = arguments!!.getParcelable(Const.DATA)
        }
    }

    override fun getLayoutResourceId(): Int {
        return R.layout.fragment_select_post
    }

    override fun initializeView(container: View?) {

    }

    private var mPage: Int = 0
    private var mTotalCount = 0
    private var mLayoutManager: LinearLayoutManager? = null
    private var mLockListView = true
    private var mAdapter: SelectPostAdapter? = null
    var mSelectData:Post? = null

    fun getSelectData(): Post? {
        return mAdapter!!.mSelectData
    }

    override fun init() {

        mLayoutManager = LinearLayoutManager(activity)
        recycler_select_post.layoutManager = mLayoutManager
        mAdapter = SelectPostAdapter(activity!!)
        recycler_select_post.adapter = mAdapter
        if(mSelectData != null){
            mAdapter!!.mSelectData = mSelectData
        }

        recycler_select_post.addItemDecoration(CustomItemOffsetDecoration(activity!!, R.dimen.height_60, R.dimen.height_60, R.dimen.height_300))

        recycler_select_post.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            internal var pastVisibleItems: Int = 0
            internal var visibleItemCount: Int = 0
            internal var totalItemCount: Int = 0

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {

                super.onScrolled(recyclerView, dx, dy)
                visibleItemCount = mLayoutManager!!.getChildCount()
                totalItemCount = mLayoutManager!!.getItemCount()
                pastVisibleItems = mLayoutManager!!.findFirstVisibleItemPosition()
                if (!mLockListView) {
                    if (totalItemCount < mTotalCount && visibleItemCount + pastVisibleItems >= totalItemCount * Const.NEXT_RECYCLERVIEW_MARGIN) {
                        mPage++
                        listCall(mPage)
                    }
                }
            }
        })

//        layout_select_post_not_exist.setOnClickListener {
//            val intent = Intent(activity, PostWriteActivity::class.java)
//            intent.putExtra(Const.MODE, EnumData.MODE.WRITE)
//            activity?.startActivityForResult(intent, Const.REQ_POST)
//        }

        mAdapter!!.setOnItemClickListener(object : SelectPostAdapter.OnItemClickListener{
            override fun onItemClick(position: Int) {
                mAdapter!!.mSelectData = mAdapter!!.getItem(position)
                mAdapter!!.notifyDataSetChanged()
            }
        })

        getCount()
    }

    private fun share(contents:String, imageUrl:String?, url: String) {

        KakaoLinkUtil.getInstance(activity).sendKakaoUrl(contents, imageUrl, url)

//        val smsUri = Uri.parse("sms:")
//        val sendIntent = Intent(Intent.ACTION_SENDTO, smsUri)
//        sendIntent.putExtra("sms_body", "$contents\n\n$url")
//        startActivity(sendIntent)
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
                text_select_post_total_count.text = PplusCommonUtil.fromHtml(getString(R.string.html_post_total_count, FormatUtil.getMoneyType(mTotalCount.toString())))
                if (mTotalCount == 0) {
                    layout_select_post_not_exist.visibility = View.VISIBLE
                } else {
                    layout_select_post_not_exist.visibility = View.GONE
                }

                mPage = 1
                mAdapter?.clear()
                listCall(mPage)
            }

            override fun onFailure(call: Call<NewResultResponse<Int>>, t: Throwable, response: NewResultResponse<Int>) {

            }
        }).build().call()
    }

    private fun listCall(page: Int) {

        val params = HashMap<String, String>()
        params["boardNo"] = LoginInfoManager.getInstance().user.page!!.prBoard!!.no.toString()
        params["pg"] = "" + page
        mLockListView = true
        showProgress("")
        ApiBuilder.create().getBoardPostList(params).setCallback(object : PplusCallback<NewResultResponse<Post>> {

            override fun onResponse(call: Call<NewResultResponse<Post>>, response: NewResultResponse<Post>) {

                hideProgress()
                if (!isAdded) {
                    return
                }
                mLockListView = false
                mAdapter!!.addAll(response.datas)
            }

            override fun onFailure(call: Call<NewResultResponse<Post>>, t: Throwable, response: NewResultResponse<Post>) {

                hideProgress()
                mLockListView = false

            }
        }).build().call()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){
            Const.REQ_POST ->{
                if(resultCode == RESULT_OK){
                    getCount()
                }
            }
        }
    }

    override fun getPID(): String {
        return ""
    }


    companion object {

        fun newInstance(selectData:Post?): SelectPostFragment {

            val fragment = SelectPostFragment()
            val args = Bundle()
            args.putParcelable(Const.DATA, selectData)
            fragment.arguments = args
            return fragment
        }
    }

}// Required empty public constructor
