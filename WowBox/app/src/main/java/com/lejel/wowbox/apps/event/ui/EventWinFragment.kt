package com.lejel.wowbox.apps.event.ui


import android.content.Context
import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.DimenRes
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lejel.wowbox.Const
import com.lejel.wowbox.R
import com.lejel.wowbox.apps.common.ui.base.BaseActivity
import com.lejel.wowbox.apps.common.ui.base.BaseFragment
import com.lejel.wowbox.apps.event.data.EventWinHeaderAdapter
import com.lejel.wowbox.core.network.ApiBuilder
import com.lejel.wowbox.core.network.model.dto.EventWin
import com.lejel.wowbox.core.network.model.response.ListResultResponse
import com.lejel.wowbox.core.network.model.response.NewResultResponse
import com.lejel.wowbox.core.util.PplusCommonUtil
import com.lejel.wowbox.databinding.FragmentEventWinBinding
import com.pplus.networks.common.PplusCallback
import retrofit2.Call


/**
 * A simple [Fragment] subclass.
 * Use the [EventWinFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class EventWinFragment : BaseFragment<BaseActivity>() {

    private var mPage: Int = 0
    private var mTotalCount = 0
        private var mLayoutManager: LinearLayoutManager? = null
//    private var mGridLayoutManager: GridLayoutManager? = null
    private var mLockListView = true
    private var mAdapter: EventWinHeaderAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        if (arguments != null) {
//            type = arguments.getSerializable(Const.TYPE) as EnumData.AdsType
        }
    }

    private var _binding: FragmentEventWinBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        _binding = FragmentEventWinBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun init() {

        mLayoutManager = LinearLayoutManager(activity)


        binding.recyclerEventWin.layoutManager = mLayoutManager
        mAdapter = EventWinHeaderAdapter()
        mAdapter!!.launcher = eventReplyLauncher
        binding.recyclerEventWin.adapter = mAdapter

        binding.recyclerEventWin.addOnScrollListener(object : RecyclerView.OnScrollListener() {

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
                        mPage++
                        listCall(mPage)
                    }
                }
            }
        })

        mAdapter!!.listener = object : EventWinHeaderAdapter.OnItemClickListener {
            override fun onHeaderClick() {

            }

            override fun onItemClick(position: Int) {
            }
        }
        binding.layoutEventWinLoading.visibility = View.VISIBLE
        mPage = 1
        listCall(mPage)

    }



    private inner class CustomItemOffsetDecoration(private val mItemOffset: Int) : RecyclerView.ItemDecoration() {

        constructor(context: Context, @DimenRes lastOffsetId: Int) : this(context.resources.getDimensionPixelSize(lastOffsetId)) {}

        override fun getItemOffsets(outRect: Rect,
                                    view: View,
                                    parent: RecyclerView,
                                    state: RecyclerView.State) {

            super.getItemOffsets(outRect, view, parent, state)

            val position = parent.getChildAdapterPosition(view)

            if(position <= 1){
                outRect.set(0, mItemOffset, 0, mItemOffset)
            }else {
                outRect.set(0, 0, 0, mItemOffset)
            }

        }
    }

    private fun listCall(page: Int) {
        val params = HashMap<String, String>()
        params["paging[page]"] = page.toString()
        params["paging[limit]"] = "20"
//        showProgress("")

        mLockListView = true
        ApiBuilder.create().getWinListOnlyPresent(params).setCallback(object : PplusCallback<NewResultResponse<ListResultResponse<EventWin>>> {
            override fun onResponse(call: Call<NewResultResponse<ListResultResponse<EventWin>>>?, response: NewResultResponse<ListResultResponse<EventWin>>?) {
//                hideProgress()
                if (!isAdded) {
                    return
                }
                binding.layoutEventWinLoading.visibility = View.GONE
                if (response?.result != null) {
                    if (page == 1) {
                        mAdapter!!.clear()

                        mTotalCount = response.result!!.total!!
                        if (mTotalCount == 0) {
                            binding.layoutEventWinNotExist.visibility = View.VISIBLE
                        } else {
                            binding.layoutEventWinNotExist.visibility = View.GONE
                        }
                    }

                    mLockListView = false

                    if (response.result!!.list != null) {
                        val dataList = response.result!!.list!!
                        mAdapter!!.addAll(dataList)
                    }

                }
            }

            override fun onFailure(call: Call<NewResultResponse<ListResultResponse<EventWin>>>?, t: Throwable?, response: NewResultResponse<ListResultResponse<EventWin>>?) {
//                hideProgress()
                if (!isAdded) {
                    return
                }
                binding.layoutEventWinLoading.visibility = View.GONE
            }

        }).build().call()
    }

    val eventReplyLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if(result.data != null){
            val data = result.data!!
            val eventWin = PplusCommonUtil.getParcelableExtra(data, Const.EVENT_WIN, EventWin::class.java)
            val position = data.getIntExtra(Const.POSITION, -1)
            if(eventWin != null && position != -1){
                getEventWin(eventWin, position)
            }
        }
    }

    private fun getEventWin(eventWin: EventWin, position:Int){
        val params = HashMap<String, String>()
        params["seqNo"] = eventWin.seqNo.toString()
        showProgress("")
        ApiBuilder.create().getWinBySeqNo(params).setCallback(object : PplusCallback<NewResultResponse<EventWin>> {
            override fun onResponse(call: Call<NewResultResponse<EventWin>>?,
                                    response: NewResultResponse<EventWin>?) {
                hideProgress()
                if(response?.result != null){
                    mAdapter!!.mDataList!![position] = response.result!!
                    mAdapter!!.notifyDataSetChanged()
                }
            }

            override fun onFailure(call: Call<NewResultResponse<EventWin>>?,
                                   t: Throwable?,
                                   response: NewResultResponse<EventWin>?) {
                hideProgress()
            }
        }).build().call()
    }

    override fun getPID(): String {
        return "Main_eventwinlist"
    }

    companion object {

        fun newInstance(): EventWinFragment {

            val fragment = EventWinFragment()
            val args = Bundle()
//            args.putSerializable(Const.TYPE, type)
            fragment.arguments = args
            return fragment
        }
    }

}// Required empty public constructor
