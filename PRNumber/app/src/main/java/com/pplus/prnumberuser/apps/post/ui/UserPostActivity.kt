//package com.pplus.prnumberuser.apps.post.ui
//
//import android.app.Activity
//import android.content.Intent
//import android.os.Bundle
//import androidx.recyclerview.widget.LinearLayoutManager
//import androidx.recyclerview.widget.RecyclerView
//import android.view.View
//import androidx.activity.result.contract.ActivityResultContracts
//import com.pplus.utils.part.format.FormatUtil
//import com.pplus.utils.part.pref.PreferenceUtil
//import com.pplus.prnumberuser.Const
//import com.pplus.prnumberuser.R
//import com.pplus.prnumberuser.apps.common.component.RecyclerScaleScrollListener
//import com.pplus.prnumberuser.apps.common.mgmt.LoginInfoManager
//import com.pplus.prnumberuser.apps.common.toolbar.ImplToolbar
//import com.pplus.prnumberuser.apps.common.toolbar.OnToolbarListener
//import com.pplus.prnumberuser.apps.common.toolbar.ToolbarOption
//import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
//import com.pplus.prnumberuser.apps.common.ui.custom.BottomItemOffsetDecoration
//import com.pplus.prnumberuser.apps.post.data.UserPostAdapter
//import com.pplus.prnumberuser.core.code.common.EnumData
//import com.pplus.prnumberuser.core.network.ApiBuilder
//import com.pplus.prnumberuser.core.network.model.dto.Post
//import com.pplus.prnumberuser.core.network.model.response.NewResultResponse
//import com.pplus.prnumberuser.core.util.PplusCommonUtil
//import kotlinx.android.synthetic.main.activity_user_post.*
//import com.pplus.networks.common.PplusCallback
//import retrofit2.Call
//import java.util.*
//
//class UserPostActivity : BaseActivity(), ImplToolbar {
//    override fun getPID(): String {
//        return ""
//    }
//
//    override fun getLayoutRes(): Int {
//        return R.layout.activity_user_post
//    }
//
//    private var mLayoutManager: LinearLayoutManager? = null
//    private var mPaging = 1
//    private var mTotalCount = 0
//    private var mLockListView = true
//    private var mAdapter: UserPostAdapter? = null
//
//    override fun initializeView(savedInstanceState: Bundle?) {
//
//        if (LoginInfoManager.getInstance().isMember && PreferenceUtil.getDefaultPreference(this).get(Const.FIRST_USER_POST, true)) {
//            PreferenceUtil.getDefaultPreference(this).put(Const.FIRST_USER_POST, false)
//            val intent = Intent(this, RealImpressionAlertActivity::class.java)
//            intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
//            alertLauncher.launch(intent)
//        }
//
//        mLayoutManager = LinearLayoutManager(this)
//        recycler_user_post.layoutManager = mLayoutManager
//        mAdapter = UserPostAdapter(this, false)
//        recycler_user_post.adapter = mAdapter
//        recycler_user_post.addItemDecoration(BottomItemOffsetDecoration(this, R.dimen.height_60))
//        recycler_user_post.addOnScrollListener(RecyclerScaleScrollListener(layout_user_post_reg))
//
//        recycler_user_post.addOnScrollListener(object : RecyclerView.OnScrollListener() {
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
//        layout_user_post_reg.setOnClickListener {
//            if(!PplusCommonUtil.loginCheck(this, null)){
//                return@setOnClickListener
//            }
//            val intent = Intent(this, UserPostWriteActivity::class.java)
//            intent.putExtra(Const.MODE, EnumData.MODE.WRITE)
//            intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
//            detailLauncher.launch(intent)
//        }
//
//        getCount()
//    }
//
//    private fun getCount() {
//        val params = HashMap<String, String>()
//        params["filter"] = EnumData.PostType.member.name
//        ApiBuilder.create().getPostCount(params).setCallback(object : PplusCallback<NewResultResponse<Int>> {
//
//            override fun onResponse(call: Call<NewResultResponse<Int>>, response: NewResultResponse<Int>) {
//                mTotalCount = response.data
//
//                text_user_post_count.text = PplusCommonUtil.fromHtml(getString(R.string.html_real_impression_total_count, FormatUtil.getMoneyType(mTotalCount.toString())))
//
//                if (mTotalCount == 0) {
//                    layout_user_post_not_exist.visibility = View.VISIBLE
//                } else {
//                    layout_user_post_not_exist.visibility = View.GONE
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
//        params["filter"] = EnumData.PostType.member.name
//        params["pg"] = "" + paging
//        mLockListView = true
//        showProgress("")
//        ApiBuilder.create().getPostList(params).setCallback(object : PplusCallback<NewResultResponse<Post>> {
//
//            override fun onResponse(call: Call<NewResultResponse<Post>>, response: NewResultResponse<Post>) {
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
//    val alertLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
//        if (result.resultCode == Activity.RESULT_OK) {
//            // There are no request codes
//            val data = result.data
//            if (result.resultCode == Activity.RESULT_OK) {
//                if(!PplusCommonUtil.loginCheck(this, null)){
//                    return@registerForActivityResult
//                }
//                val intent = Intent(this, UserPostWriteActivity::class.java)
//                intent.putExtra(Const.MODE, EnumData.MODE.WRITE)
//                intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
//                detailLauncher.launch(intent)
//            }
//        }
//    }
//
//    val replyLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
//        if (result.resultCode == Activity.RESULT_OK) {
//            // There are no request codes
//            val data = result.data
//            if(data != null){
//                val position = data.getIntExtra(Const.POSITION, 0)
//                val post = data.getParcelableExtra<Post>(Const.DATA)
//
//                getPost(post, position)
//            }
//        }
//    }
//
//    private fun getPost(post:Post, position:Int) {
//
//        showProgress("")
//        ApiBuilder.create().getPost(post.no!!).setCallback(object : PplusCallback<NewResultResponse<Post>> {
//
//            override fun onResponse(call: Call<NewResultResponse<Post>>, response: NewResultResponse<Post>) {
//                hideProgress()
//                mAdapter!!.replaceData(position, response.data)
//            }
//
//            override fun onFailure(call: Call<NewResultResponse<Post>>, t: Throwable, response: NewResultResponse<Post>) {
//
//                hideProgress()
//            }
//        }).build().call()
//    }
//
//    override fun getToolbarOption(): ToolbarOption {
//
//        val toolbarOption = ToolbarOption(this)
//        toolbarOption.initializeDefaultToolbar(getString(R.string.word_real_impression), ToolbarOption.ToolbarMenu.LEFT)
//        toolbarOption.setToolbarMenu(ToolbarOption.ToolbarMenu.RIGHT, R.drawable.ic_top_winner_review_info)
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
//                ToolbarOption.ToolbarMenu.RIGHT -> if (tag == 1) {
//                    val intent = Intent(this, RealImpressionAlertActivity::class.java)
//                    intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
//                    alertLauncher.launch(intent)
//                }
//            }
//        }
//    }
//}
