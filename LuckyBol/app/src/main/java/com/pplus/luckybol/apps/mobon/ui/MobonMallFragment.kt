//package com.pplus.luckybol.apps.mobon.ui
//
//
//import android.content.Context
//import android.graphics.Rect
//import android.os.Bundle
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import androidx.annotation.DimenRes
//import androidx.fragment.app.Fragment
//import androidx.recyclerview.widget.RecyclerView
//import androidx.recyclerview.widget.StaggeredGridLayoutManager
//import com.pplus.luckybol.Const
//import com.pplus.luckybol.R
//import com.pplus.luckybol.apps.common.ui.base.BaseActivity
//import com.pplus.luckybol.apps.common.ui.base.BaseFragment
//import com.pplus.luckybol.apps.mobon.data.MobonMallAdapter
//import com.pplus.luckybol.core.network.ApiController
//import com.pplus.luckybol.core.network.model.dto.MobonMall
//import com.pplus.luckybol.core.network.model.dto.MobonResult
//import com.pplus.luckybol.core.util.PplusCommonUtil
//import com.pplus.luckybol.databinding.ActivityMobonMallBinding
//import com.pplus.utils.part.logs.LogUtil
//import retrofit2.Call
//import retrofit2.Callback
//import retrofit2.Response
//
//
///**
// * A simple [Fragment] subclass.
// * Use the [MobonMallFragment.newInstance] factory method to
// * create an instance of this fragment.
// */
//class MobonMallFragment : BaseFragment<BaseActivity>() {
//
//    // TODO: Rename and change types of parameters
//
//
//    var mTab = 0
//    var s = ""
//    var category = ""
//    var adapter: MobonMallAdapter? = null
//    private var mLockListView = true
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//
//        super.onCreate(savedInstanceState)
//        if (arguments != null) {
//            mTab = requireArguments().getInt(Const.TAB)
//        }
//    }
//
//    private var _binding: ActivityMobonMallBinding? = null
//    private val binding get() = _binding!!
//
//    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
//
//        _binding = ActivityMobonMallBinding.inflate(inflater, container, false)
//        val view = binding.root
//        return view
//    }
//
//    override fun onDestroyView() {
//        super.onDestroyView()
//        _binding = null
//    }
//
//    override fun init() {
//        when(mTab){
//            0->{
//                s = "20629"
//                category = "288"
//            }
//            1->{
//                s = "20628"
//                category = "289"
//            }
//        }
//
//        val layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
//        binding.recyclerMobonMall.layoutManager = layoutManager
//        binding.recyclerMobonMall.addItemDecoration(CustomItemOffsetDecoration(requireActivity(), R.dimen.width_3, R.dimen.height_30))
//        adapter = MobonMallAdapter(s)
//        binding.recyclerMobonMall.adapter = adapter
//
//        adapter!!.setOnItemClickListener(object : MobonMallAdapter.OnItemClickListener {
//            override fun onItemClick(position: Int) {
//                if(position != 0){
//                    PplusCommonUtil.openChromeWebView(activity!!, adapter!!.getItem(position).drc_link!!)
//                }
//            }
//        })
//
//        binding.recyclerMobonMall.addOnScrollListener(object : RecyclerView.OnScrollListener() {
//
//            var pastVisibleItems: Int = 0
//            var visibleItemCount: Int = 0
//            var totalItemCount: Int = 0
//
//            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
//
//                super.onScrolled(recyclerView, dx, dy)
//                visibleItemCount = layoutManager.childCount
//                totalItemCount = layoutManager.itemCount
//
//                var firstVisibleItems: IntArray? = null
//                firstVisibleItems = layoutManager.findFirstVisibleItemPositions(firstVisibleItems)
//                if (firstVisibleItems != null && firstVisibleItems.isNotEmpty()) {
//                    pastVisibleItems = firstVisibleItems[0]
//                }
////                pastVisibleItems = layoutManager.findFirstVisibleItemPosition()
//                if (!mLockListView) {
//                    if (totalItemCount < 200 && visibleItemCount + pastVisibleItems >= totalItemCount * Const.NEXT_RECYCLERVIEW_MARGIN) {
//                        listCall()
//                    }
//                }
//            }
//        })
//
//        adapter!!.add(MobonMall())
//        listCall()
//    }
//
//    private fun listCall() {
//        mLockListView = true
//        showProgress("")
//        ApiController.getMobonApi().requestShopping(s, "ST", 50, category).enqueue(object : Callback<MobonResult> {
//            override fun onFailure(call: Call<MobonResult>, t: Throwable) {
//                hideProgress()
//                LogUtil.e(LOG_TAG, "onFailure : {}", t.toString())
//            }
//
//            override fun onResponse(call: Call<MobonResult>, response: Response<MobonResult>) {
//                hideProgress()
//                mLockListView = false
//                val data = response.body()
//                if (response.body()!!.result_code == 200) {
//                    if (data!!.data != null) {
//                        adapter!!.addAll(data.data!!)
//                    }
//                }
//            }
//        })
//    }
//
//    private inner class CustomItemOffsetDecoration(private val mOffset: Int, private val mTopOffset: Int) : RecyclerView.ItemDecoration() {
//
//        constructor(context: Context, @DimenRes offsetId: Int, @DimenRes topOffsetId: Int) : this(context.resources.getDimensionPixelSize(offsetId), context.resources.getDimensionPixelSize(topOffsetId)) {}
//
//        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
//
//            super.getItemOffsets(outRect, view, parent, state)
//
//            val position = parent.getChildAdapterPosition(view)
//            if (position < 2) {
//                outRect.set(mOffset, mTopOffset, mOffset, mOffset)
//            }else{
//                outRect.set(mOffset, mOffset, mOffset, mOffset)
//            }
//
//
//        }
//    }
//
//    override fun getPID(): String {
//        return ""
//    }
//
//    companion object {
//
//        fun newInstance(tab:Int): MobonMallFragment {
//
//            val fragment = MobonMallFragment()
//            val args = Bundle()
//            args.putSerializable(Const.TAB, tab)
//            fragment.arguments = args
//            return fragment
//        }
//    }
//
//}// Required empty public constructor
