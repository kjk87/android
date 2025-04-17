//package com.pplus.prnumberuser.apps.theme.ui
//
//import android.app.Activity.RESULT_OK
//import android.content.Context
//import android.content.Intent
//import android.graphics.Rect
//import android.os.Bundle
//import android.view.View
//import androidx.activity.result.contract.ActivityResultContracts
//import androidx.annotation.DimenRes
//import androidx.recyclerview.widget.LinearLayoutManager
//import androidx.recyclerview.widget.RecyclerView
//import com.pplus.networks.common.PplusCallback
//import com.pplus.prnumberuser.BusProviderData
//import com.pplus.prnumberuser.Const
//import com.pplus.prnumberuser.R
//import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
//import com.pplus.prnumberuser.apps.common.ui.base.BaseFragment
//import com.pplus.prnumberuser.apps.main.ui.LocationSelectActivity
//import com.pplus.prnumberuser.apps.page.data.PageAdapter
//import com.pplus.prnumberuser.core.code.common.SortCode
//import com.pplus.prnumberuser.core.location.LocationUtil
//import com.pplus.prnumberuser.core.network.ApiBuilder
//import com.pplus.prnumberuser.core.network.model.dto.LocationData
//import com.pplus.prnumberuser.core.network.model.dto.Page
//import com.pplus.prnumberuser.core.network.model.dto.ThemeCategory
//import com.pplus.prnumberuser.core.network.model.response.NewResultResponse
//import com.pplus.prnumberuser.core.util.PplusCommonUtil
//import com.pplus.utils.BusProvider
//import com.squareup.otto.Subscribe
//import kotlinx.android.synthetic.main.fragment_theme_page.*
//import retrofit2.Call
//import java.util.*
//
//class ThemePageFragment : BaseFragment<BaseActivity>() {
//
//    override fun getLayoutResourceId(): Int {
//        return R.layout.fragment_theme_page
//    }
//
//    override fun initializeView(container: View?) {
//
//    }
//
//    private var mTotalCount: Int = 0
//    private var mLockListView = false
//    private var mPaging = 1
//    private var mAdapter: PageAdapter? = null
//    private var mLayoutManager: LinearLayoutManager? = null
//    private var mLocationData: LocationData? = null
//    private var mThemeCategory: ThemeCategory? = null
//
//    override fun init() {
//        mLayoutManager = LinearLayoutManager(activity)
//        recycler_theme_page.layoutManager = mLayoutManager
//        mAdapter = PageAdapter()
//        recycler_theme_page.adapter = mAdapter
////        recycler_main_page.addItemDecoration(CustomItemOffsetDecoration(activity!!, R.dimen.height_60, R.dimen.height_60))
//
//        recycler_theme_page.addOnScrollListener(object : RecyclerView.OnScrollListener() {
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
//                    if (totalItemCount < mTotalCount && visibleItemCount + pastVisibleItems >= totalItemCount * Const.NEXT_RECYCLERVIEW_MARGIN) {
//                        mPaging++
//                        listCall(mPaging)
//                    }
//                }
//            }
//        })
//
//        mAdapter!!.setOnItemClickListener(object : PageAdapter.OnItemClickListener {
//
//            override fun onItemClick(position: Int, view: View) {
//                val location = IntArray(2)
//                view.getLocationOnScreen(location)
//                val x = location[0] + view.width / 2
//                val y = location[1] + view.height / 2
//                PplusCommonUtil.goPage(activity!!, mAdapter!!.getItem(position), x, y)
//            }
//        })
//
//        text_theme_page_address.setOnClickListener {
//            val intent = Intent(activity, LocationSelectActivity::class.java)
//            val location = IntArray(2)
//            it.getLocationOnScreen(location)
//            val x = location[0] + it.width/2
//            val y = location[1] + it.height/2
//            intent.putExtra(Const.X, x)
//            intent.putExtra(Const.Y, y)
//            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//            locationLauncher.launch(intent)
//        }
//
//        text_theme_page_view_map.setOnClickListener {
//            val intent = Intent(activity, ThemeMapActivity::class.java)
//            intent.putExtra(Const.THEME_CATEGORY, mThemeCategory)
//            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//            startActivity(intent)
//        }
//
//        setData()
//    }
//
//    private fun setData() {
//        mLocationData = LocationUtil.specifyLocationData
//        if (mLocationData != null) {
//
//            PplusCommonUtil.callAddress(LocationUtil.specifyLocationData, object : PplusCommonUtil.Companion.OnAddressCallListener {
//
//                override fun onResult(address: String) {
//
//                    if (!isAdded) {
//                        return
//                    }
//
//                    text_theme_page_address?.text = address
//                }
//            })
//
//            getCount()
//        }
//    }
//
//    private fun getCount() {
//
//        val params = HashMap<String, String>()
//        params["themeSeqNo"] = mThemeCategory!!.seqNo.toString()
//        showProgress("")
//        ApiBuilder.create().getPageCountByTheme(params).setCallback(object : PplusCallback<NewResultResponse<Int>> {
//
//            override fun onResponse(call: Call<NewResultResponse<Int>>, response: NewResultResponse<Int>) {
//
//                hideProgress()
//                if (!isAdded) {
//                    return
//                }
//
//                mTotalCount = response.data
//
//                mPaging = 1
//                mAdapter!!.clear()
//                listCall(mPaging)
//            }
//
//            override fun onFailure(call: Call<NewResultResponse<Int>>, t: Throwable, response: NewResultResponse<Int>) {
//                hideProgress()
//            }
//        }).build().call()
//    }
//
//    private fun listCall(page: Int) {
//
//        val params = HashMap<String, String>()
//        params["align"] = SortCode.distance.name
//        if (mLocationData != null) {
//            params["latitude"] = mLocationData!!.latitude.toString()
//            params["longitude"] = mLocationData!!.longitude.toString()
//        }
//        params["themeSeqNo"] = mThemeCategory!!.seqNo.toString()
//        params["pg"] = page.toString()
//
//        mLockListView = true
//        showProgress("")
//        ApiBuilder.create().getPageListByTheme(params).setCallback(object : PplusCallback<NewResultResponse<Page>> {
//
//            override fun onResponse(call: Call<NewResultResponse<Page>>, response: NewResultResponse<Page>) {
//                if (!isAdded) {
//                    return
//                }
//                mLockListView = false
//                hideProgress()
//                mAdapter!!.addAll(response.datas)
//            }
//
//            override fun onFailure(call: Call<NewResultResponse<Page>>, t: Throwable, response: NewResultResponse<Page>) {
//
//                mLockListView = false
//                hideProgress()
//            }
//        }).build().call()
//    }
//
//    private inner class CustomItemOffsetDecoration(private val mItemOffset: Int, private val mTopOffset: Int) : RecyclerView.ItemDecoration() {
//
//        constructor(context: Context, @DimenRes itemOffsetId: Int, @DimenRes topOffsetId: Int) : this(context.resources.getDimensionPixelSize(itemOffsetId), context.resources.getDimensionPixelSize(topOffsetId)) {}
//
//        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
//
//            super.getItemOffsets(outRect, view, parent, state)
//
//            val position = parent.getChildAdapterPosition(view)
//            if (position == 0) {
//                outRect.set(0, mTopOffset, 0, mItemOffset)
//            } else {
//                outRect.set(0, 0, 0, mItemOffset)
//            }
//
//        }
//    }
//
//    val locationLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
//        if (result.resultCode == RESULT_OK) {
//            setData()
//        }
//    }
//
//    @Subscribe
//    fun setPlus(data: BusProviderData){
//        if(data.type == BusProviderData.BUS_MAIN && data.subData is Page){
//            mAdapter!!.setBusPlus(data)
//        }
//    }
//
//    override fun getPID(): String {
//        return ""
//    }
//
//    override fun onDestroy() {
//        super.onDestroy()
//        BusProvider.getInstance().unregister(this)
//    }
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        BusProvider.getInstance().register(this)
//        arguments?.let {
//            mThemeCategory = it.getParcelable(Const.DATA)
//            //            param1 = it.getString(ARG_PARAM1)
////            param2 = it.getString(ARG_PARAM2)
//        }
//    }
//
//    companion object {
//
//        @JvmStatic
//        fun newInstance(themeCategory: ThemeCategory) =
//                ThemePageFragment().apply {
//                    arguments = Bundle().apply {
//                        putParcelable(Const.DATA, themeCategory)
////                        putString(ARG_PARAM2, param2)
//                    }
//                }
//    }
//}
