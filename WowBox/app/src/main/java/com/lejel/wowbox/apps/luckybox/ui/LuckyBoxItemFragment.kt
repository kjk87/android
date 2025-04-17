package com.lejel.wowbox.apps.luckybox.ui


import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.DimenRes
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lejel.wowbox.Const
import com.lejel.wowbox.R
import com.lejel.wowbox.apps.common.ui.base.BaseActivity
import com.lejel.wowbox.apps.common.ui.base.BaseFragment
import com.lejel.wowbox.apps.luckybox.data.LuckyBoxAdapter
import com.lejel.wowbox.core.network.ApiBuilder
import com.lejel.wowbox.core.network.model.dto.*
import com.lejel.wowbox.core.network.model.response.NewResultResponse
import com.lejel.wowbox.core.util.PplusCommonUtil
import com.lejel.wowbox.databinding.FragmentLuckyBoxItemBinding
import com.pplus.networks.common.PplusCallback
import com.lejel.wowbox.core.network.model.response.ListResultResponse
import retrofit2.Call


/**
 * A simple [Fragment] subclass.
 * Use the [LuckyBoxItemFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LuckyBoxItemFragment : BaseFragment<BaseActivity>() {

    var mAdapter: LuckyBoxAdapter? = null
    var mLuckyBox: LuckyBox? = null
    private var mTotalCount: Int = 0
    private var mLockListView = false
    private var mPaging = 1
    private var mLayoutManager: GridLayoutManager? = null
//    private var mGroupSeqNos: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        mLuckyBox = PplusCommonUtil.getParcelable(requireArguments(), Const.DATA, LuckyBox::class.java)
    }

    private var _binding: FragmentLuckyBoxItemBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        _binding = FragmentLuckyBoxItemBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun init() {

        mAdapter = LuckyBoxAdapter()
        mAdapter!!.mLuckyBox = mLuckyBox
        mLayoutManager = GridLayoutManager(activity, 2)

        binding.recyclerLuckyBoxItem.layoutManager = mLayoutManager
        binding.recyclerLuckyBoxItem.addItemDecoration(CustomItemOffsetDecoration(requireActivity(), R.dimen.height_200))
        binding.recyclerLuckyBoxItem.adapter = mAdapter!!

        mAdapter!!.listener = object : LuckyBoxAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                if(!requireActivity().packageManager.hasSystemFeature(PackageManager.FEATURE_WEBVIEW)){
                    return
                }
                val intent = Intent(requireActivity(), LuckyBoxProductInfoActivity::class.java)
                val product = Product()
                product.seqNo = mAdapter!!.getItem(position).productSeqNo
                intent.putExtra(Const.DATA, product)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                startActivity(intent)
            }
        }

        binding.recyclerLuckyBoxItem.addOnScrollListener(object : RecyclerView.OnScrollListener() {

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

        binding.layoutLuckyBoxItemLoading.visibility = View.VISIBLE

//        for (i in 0 until mLuckyBox!!.entryList.size) {
//            val entry = mLuckyBox!!.entryList[i]
//            if (entry.luckyboxProductGroup != null) {
//                mGroupSeqNos += entry.luckyboxProductGroupSeqNo
//            }
//            if (i < mLuckyBox!!.entryList.size - 1) {
//                mGroupSeqNos += ","
//            }
//        }

        mPaging = 1
        listCall(mPaging)
    }

    private inner class CustomItemOffsetDecoration(private val mLastOffset: Int) : RecyclerView.ItemDecoration() {

        constructor(context: Context,
                    @DimenRes lastOffsetId: Int) : this(context.resources.getDimensionPixelSize(lastOffsetId)) {
        }

        override fun getItemOffsets(outRect: Rect,
                                    view: View,
                                    parent: RecyclerView,
                                    state: RecyclerView.State) {

            super.getItemOffsets(outRect, view, parent, state)

            val position = parent.getChildAdapterPosition(view)
            if (mAdapter!!.itemCount > 0 && position == mAdapter!!.itemCount - 1) {
                outRect.bottom = mLastOffset
            }

            if(mAdapter!!.itemCount > 1 && mAdapter!!.itemCount % 2 == 0  && position == mAdapter!!.itemCount - 2){
                outRect.bottom = mLastOffset
            }

        }
    }


    private fun listCall(page: Int) {

        mLockListView = true
        val params = HashMap<String, String>()
//        params["groupSeqNo"] = mGroupSeqNos

        params["paging[page]"] = page.toString()
        params["paging[limit]"] = "20"
        ApiBuilder.create().getLuckyBoxProductList(params).setCallback(object : PplusCallback<NewResultResponse<ListResultResponse<LuckyBoxProductGroupItem>>> {
            override fun onResponse(call: Call<NewResultResponse<ListResultResponse<LuckyBoxProductGroupItem>>>?,
                                    response: NewResultResponse<ListResultResponse<LuckyBoxProductGroupItem>>?) {
                if (!isAdded) {
                    return
                }

                binding.layoutLuckyBoxItemLoading.visibility = View.GONE
                if (response?.result != null) {
                    if (page == 1) {
                        mAdapter!!.clear()

                        mTotalCount = response.result!!.total!!
                    }

                    mLockListView = false

                    if (response.result!!.list != null) {
                        val dataList = response.result!!.list!!
                        mAdapter!!.addAll(dataList)
                    }

                }
            }

            override fun onFailure(call: Call<NewResultResponse<ListResultResponse<LuckyBoxProductGroupItem>>>?,
                                   t: Throwable?,
                                   response: NewResultResponse<ListResultResponse<LuckyBoxProductGroupItem>>?) {
                if (!isAdded) {
                    return
                }
                mLockListView = false
                binding.layoutLuckyBoxItemLoading.visibility = View.GONE
            }

        }).build().call()
    }


    override fun getPID(): String {
        return "Main_radnomevent"
    }

    companion object {
        @JvmStatic
        fun newInstance(luckyBox: LuckyBox) = LuckyBoxItemFragment().apply {
            arguments = Bundle().apply {
                putParcelable(Const.DATA, luckyBox) //                        putString(ARG_PARAM2, param2)
            }
        }
    }

} // Required empty public constructor
