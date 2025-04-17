package com.pplus.prnumberuser.apps.event.ui


import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.DimenRes
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.pplus.networks.common.PplusCallback
import com.pplus.prnumberuser.Const
import com.pplus.prnumberuser.R
import com.pplus.prnumberuser.apps.common.mgmt.LoginInfoManager
import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
import com.pplus.prnumberuser.apps.common.ui.base.BaseFragment
import com.pplus.prnumberuser.apps.event.data.EventLoadingView
import com.pplus.prnumberuser.apps.event.data.TuneEventAdapter
import com.pplus.prnumberuser.apps.product.ui.ProductShipDetailActivity
import com.pplus.prnumberuser.core.code.common.EventType
import com.pplus.prnumberuser.core.network.ApiBuilder
import com.pplus.prnumberuser.core.network.model.dto.Event
import com.pplus.prnumberuser.core.network.model.dto.EventResult
import com.pplus.prnumberuser.core.network.model.dto.ProductPrice
import com.pplus.prnumberuser.core.network.model.response.NewResultResponse
import com.pplus.prnumberuser.core.util.PplusCommonUtil
import com.pplus.prnumberuser.databinding.FragmentTuneGroupBBinding
import com.pplus.utils.part.utils.StringUtils
import retrofit2.Call
import java.util.*


/**
 * A simple [Fragment] subclass.
 * Use the [TuneEventGroupBFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TuneEventGroupBFragment : BaseFragment<BaseActivity>() {

    // TODO: Rename and change types of parameters


    private var mPage: Int = 0
    private var mTotalCount = 0
    private var mLayoutManager: LinearLayoutManager? = null
    private var mLockListView = true
    private var mAdapter: TuneEventAdapter? = null
    private var mSelectPos: Int = -1
    private var mSelectEvent: Event? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
//        mGroupNo = arguments?.getInt(Const.GROUP)
    }

    private var _binding: FragmentTuneGroupBBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        _binding = FragmentTuneGroupBBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun init() {

        binding.imageTuneGroupBBack.setOnClickListener {
            activity?.onBackPressed()
        }

        mLayoutManager = LinearLayoutManager(requireActivity())
        binding.recyclerTuneGroupB.layoutManager = mLayoutManager
        mAdapter = TuneEventAdapter()
        binding.recyclerTuneGroupB.adapter = mAdapter
        binding.recyclerTuneGroupB.addItemDecoration(CustomItemOffsetDecoration(requireActivity(), R.dimen.height_40))
        binding.recyclerTuneGroupB.addOnScrollListener(object : RecyclerView.OnScrollListener() {

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

        binding.swipeRefreshEventByGroup.setOnRefreshListener(object : SwipeRefreshLayout.OnRefreshListener {
            override fun onRefresh() {
                getCount()
                binding.swipeRefreshEventByGroup.isRefreshing = false
            }
        })

//        if(mGroupNo == 1){
//            layout_event_by_group_banner.setBackgroundResource(R.drawable.img_event_bg_1)
//            image_event_by_group_banner.setImageResource(R.drawable.img_event_txt_1)
//        }else if(mGroupNo == 2){
//            layout_event_by_group_banner.setBackgroundResource(R.drawable.img_event_bg_2)
//            image_event_by_group_banner.setImageResource(R.drawable.img_event_txt_2)
//        }

        mAdapter!!.setOnItemClickListener(object : TuneEventAdapter.OnItemClickListener {

            override fun onItemClick(position: Int) {
                val item = mAdapter!!.getItem(position)
                if (StringUtils.isNotEmpty(item.moveTargetString)) {
                    val moveTargetNumber = item.moveTargetNumber
                    if(item.moveType == "inner" && moveTargetNumber != null){
                        when(moveTargetNumber){
                            5 -> { //배송상품
                                val intent = Intent(activity, ProductShipDetailActivity::class.java)
                                val productPrice = ProductPrice()
                                productPrice.seqNo = item.moveTargetString!!.toLong()
                                intent.putExtra(Const.DATA, productPrice)
                                startActivity(intent)
                            }
                        }
                    }else{
                        PplusCommonUtil.openChromeWebView(activity!!, item.moveTargetString!!)
                    }

                }

            }
        })


        binding.layoutTuneGroupBLoading.visibility = View.VISIBLE
        getCount()
    }

    private inner class CustomItemOffsetDecoration(private val mItemOffset: Int) : RecyclerView.ItemDecoration() {

        constructor(context: Context, @DimenRes lastOffsetId: Int) : this(context.resources.getDimensionPixelSize(lastOffsetId)) {
        }

        override fun getItemOffsets(outRect: Rect,
                                    view: View,
                                    parent: RecyclerView,
                                    state: RecyclerView.State) {

            super.getItemOffsets(outRect, view, parent, state)

            val position = parent.getChildAdapterPosition(view)

            if (position <= 1) {
                outRect.set(0, mItemOffset, 0, mItemOffset)
            } else {
                outRect.set(0, 0, 0, mItemOffset)
            }

        }
    }

    fun joinEvent(event: Event) {
        val params = HashMap<String, String>()
        params["no"] = event.no.toString()
        showProgress("")

        ApiBuilder.create().joinEvent(params).setCallback(object : PplusCallback<NewResultResponse<EventResult>> {
            override fun onResponse(call: Call<NewResultResponse<EventResult>>?, response: NewResultResponse<EventResult>?) {

                hideProgress()
                if (!isAdded) {
                    return
                }

                if (response!!.data != null) {
                    if (event.winAnnounceType.equals(EventType.WinAnnounceType.immediately.name)) {
                        val mLoading = EventLoadingView()
                        mLoading.isCancelable = false
                        mLoading.setText(getString(R.string.msg_checking_event_result))
                        var delayTime = 2000L
                        mLoading.isCancelable = false
                        try {
                            mLoading.show(parentFragmentManager, "")
                        } catch (e: Exception) {

                        }

                        val handler = Handler()
                        handler.postDelayed({

                            try {
                                mLoading.dismiss()
                            } catch (e: Exception) {

                            }

                            val intent = Intent(activity, EventResultActivity::class.java)
                            intent.putExtra(Const.EVENT_RESULT, response.data)
                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                            defaultLauncher.launch(intent)
                        }, delayTime)

                    } else {
                        val intent = Intent(activity, EventResultActivity::class.java)
                        intent.putExtra(Const.EVENT_RESULT, response.data)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                        defaultLauncher.launch(intent)
                    }
                }

            }

            override fun onFailure(call: Call<NewResultResponse<EventResult>>?, t: Throwable?, response: NewResultResponse<EventResult>?) {

                hideProgress()
                if (!isAdded) {
                    return
                }

                if (response != null) {
                    PplusCommonUtil.showEventAlert(activity!!, response.resultCode, event)
                }

            }
        }).build().call()
    }


    private fun getCount() {
        val params = HashMap<String, String>()
        params["filter"] = EventType.PrimaryType.tune.name
        params["platform"] = "aos"
        params["appType"] = Const.APP_TYPE
        params["groupSeqNo"] = "2"
//        showProgress("")
        ApiBuilder.create().getEventCount(params).setCallback(object : PplusCallback<NewResultResponse<Int>> {

            override fun onResponse(call: Call<NewResultResponse<Int>>, response: NewResultResponse<Int>) {
//                hideProgress()
                if (!isAdded) {
                    return
                }

                mTotalCount = response.data

                if (mTotalCount == 0) {

                    binding.layoutTuneGroupBLoading.visibility = View.GONE
                    binding.layoutTuneGroupBNotExist.visibility = View.VISIBLE
                }else{
                    binding.layoutTuneGroupBNotExist.visibility = View.GONE
                }

                mPage = 1
                mAdapter?.clear()
                listCall(mPage)
            }

            override fun onFailure(call: Call<NewResultResponse<Int>>, t: Throwable, response: NewResultResponse<Int>) {
//                hideProgress()
                if (!isAdded) {
                    return
                }
            }
        }).build().call()
    }

    private fun listCall(page: Int) {
        val params = HashMap<String, String>()
        params["filter"] = EventType.PrimaryType.tune.name
        params["pg"] = page.toString()
        params["platform"] = "aos"
        params["appType"] = Const.APP_TYPE
        params["groupSeqNo"] = "2"
        mLockListView = true
//        showProgress("")
        ApiBuilder.create().getEventList(params).setCallback(object : PplusCallback<NewResultResponse<Event>> {

            override fun onResponse(call: Call<NewResultResponse<Event>>, response: NewResultResponse<Event>) {
//                hideProgress()
                if (!isAdded) {
                    return
                }
                binding.layoutTuneGroupBLoading.visibility = View.GONE
                mLockListView = false
                mAdapter?.addAll(response.datas)
            }

            override fun onFailure(call: Call<NewResultResponse<Event>>, t: Throwable, response: NewResultResponse<Event>) {
//                hideProgress()
                if (!isAdded) {
                    return
                }

            }
        }).build().call()
    }

    val defaultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (LoginInfoManager.getInstance().isMember) {
            setRetentionBol()
        }
        getEvent()
    }

    private fun setRetentionBol() {
        PplusCommonUtil.reloadSession(object : PplusCommonUtil.Companion.ReloadListener {

            override fun reload() {
//                parentFragment?.text_main_target_event_retention_bol?.text = PplusCommonUtil.fromHtml(getString(R.string.html_cash_unit2, FormatUtil.getMoneyType(LoginInfoManager.getInstance().user.totalBol.toString())))
            }
        })
    }

    private fun getEvent() {
        if (mSelectEvent == null) {
            return
        }

        val params = HashMap<String, String>()
        params["no"] = mSelectEvent!!.no.toString()
        showProgress("")
        ApiBuilder.create().getEvent(params).setCallback(object : PplusCallback<NewResultResponse<Event>> {
            override fun onResponse(call: Call<NewResultResponse<Event>>?, response: NewResultResponse<Event>?) {
                hideProgress()
                if (!isAdded) {
                    return
                }

                if (response!!.data != null) {
                    mAdapter!!.replaceData(mSelectPos, response.data)
                }
            }

            override fun onFailure(call: Call<NewResultResponse<Event>>?, t: Throwable?, response: NewResultResponse<Event>?) {

                hideProgress()
                if (!isAdded) {
                    return
                }
            }
        }).build().call()
        mSelectEvent = null
    }

    override fun getPID(): String {
        return "Main_Event news"
    }

    companion object {

        @JvmStatic
        fun newInstance() = TuneEventGroupBFragment().apply {
            arguments = Bundle().apply {
                //                putInt(Const.TAB, tab)
                //                        putString(ARG_PARAM2, param2)
            }
        }
    }

}// Required empty public constructor
