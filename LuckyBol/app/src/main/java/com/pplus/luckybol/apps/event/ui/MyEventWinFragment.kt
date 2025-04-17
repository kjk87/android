package com.pplus.luckybol.apps.event.ui


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
import com.pplus.luckybol.Const
import com.pplus.luckybol.R
import com.pplus.luckybol.apps.common.ui.base.BaseActivity
import com.pplus.luckybol.apps.common.ui.base.BaseFragment
import com.pplus.luckybol.apps.event.data.MyEventWinAdapter
import com.pplus.luckybol.core.network.ApiBuilder
import com.pplus.luckybol.core.network.model.dto.EventWin
import com.pplus.luckybol.core.network.model.response.NewResultResponse
import com.pplus.luckybol.databinding.FragmentEventWinBinding
import com.pplus.networks.common.PplusCallback
import com.pplus.utils.part.format.FormatUtil
import retrofit2.Call
import java.util.*


/**
 * A simple [Fragment] subclass.
 * Use the [MyEventWinFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MyEventWinFragment : BaseFragment<BaseActivity>() {

    private var mPage: Int = 0
    private var mTotalCount = 0
        private var mLayoutManager: LinearLayoutManager? = null
//    private var mGridLayoutManager: GridLayoutManager? = null
    private var mLockListView = true
    private var mAdapter: MyEventWinAdapter? = null

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
        mAdapter = MyEventWinAdapter()
        mAdapter!!.mMyEventWinFragment = this
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

        mAdapter!!.setOnItemClickListener(object : MyEventWinAdapter.OnItemClickListener {


            override fun onItemClick(position: Int) {
            }
        })
        binding.layoutEventWinLoading.visibility = View.VISIBLE
        getCount()

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



    private fun getCount() {
        showProgress("")
        ApiBuilder.create().myWinCountOnlyPresent.setCallback(object : PplusCallback<NewResultResponse<Int>> {

            override fun onResponse(call: Call<NewResultResponse<Int>>,
                                    response: NewResultResponse<Int>) {
                                hideProgress()
                if (!isAdded) {
                    return
                }
                mTotalCount = response.data!!
                if (mTotalCount == 0) {
                    binding.layoutEventWinLoading.visibility = View.GONE
                    binding.layoutEventWinNotExist.visibility = View.VISIBLE
                } else {
                    binding.layoutEventWinNotExist.visibility = View.GONE
                }

                (getParentActivity() as MyEventReviewActivity).getBinding().textMyEventReviewWinTab.text = getString(R.string.format_enable_review, FormatUtil.getMoneyType(mTotalCount.toString()))

                mPage = 1
                mAdapter?.clear()
                listCall(mPage)
            }

            override fun onFailure(call: Call<NewResultResponse<Int>>,
                                   t: Throwable,
                                   response: NewResultResponse<Int>) {
                //                hideProgress()
                binding.layoutEventWinLoading.visibility = View.GONE
            }
        }).build().call()
    }

    private fun listCall(page: Int) {
        val params = HashMap<String, String>()
        params["pg"] = page.toString()
        showProgress("")
        mLockListView = true
        ApiBuilder.create().getMyWinListOnlyPresent(params).setCallback(object : PplusCallback<NewResultResponse<EventWin>> {
            override fun onResponse(call: Call<NewResultResponse<EventWin>>?,
                                    response: NewResultResponse<EventWin>?) {
                hideProgress()
                if (!isAdded) {
                    return
                }
                mLockListView = false
                binding.layoutEventWinLoading.visibility = View.GONE
                mAdapter?.addAll(response!!.datas!!)
            }

            override fun onFailure(call: Call<NewResultResponse<EventWin>>?,
                                   t: Throwable?,
                                   response: NewResultResponse<EventWin>?) {
                hideProgress()
            }
        }).build().call()
    }


    val eventReviewRegLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        getCount()
    }

    private fun getEventWin(eventWin: EventWin, position:Int){
        val params = HashMap<String, String>()
        params["seqNo"] = eventWin.winNo.toString()
        params["eventSeqNo"] = eventWin.event!!.no.toString()
        showProgress("")
        ApiBuilder.create().getWinBySeqNo(params).setCallback(object : PplusCallback<NewResultResponse<EventWin>> {
            override fun onResponse(call: Call<NewResultResponse<EventWin>>?,
                                    response: NewResultResponse<EventWin>?) {
                hideProgress()
                if(response?.data != null){
                    mAdapter!!.mDataList!![position] = response.data!!
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

        fun newInstance(): MyEventWinFragment {

            val fragment = MyEventWinFragment()
            val args = Bundle()
//            args.putSerializable(Const.TYPE, type)
            fragment.arguments = args
            return fragment
        }
    }

}// Required empty public constructor
