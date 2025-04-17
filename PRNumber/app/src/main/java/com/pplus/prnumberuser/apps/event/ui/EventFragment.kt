package com.pplus.prnumberuser.apps.event.ui


import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.os.Handler
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.DimenRes
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pplus.networks.common.PplusCallback
import com.pplus.prnumberuser.Const
import com.pplus.prnumberuser.R
import com.pplus.prnumberuser.apps.bol.ui.BolConfigActivity
import com.pplus.prnumberuser.apps.common.mgmt.LoginInfoManager
import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
import com.pplus.prnumberuser.apps.common.ui.base.BaseFragment
import com.pplus.prnumberuser.apps.event.data.EventAdapter
import com.pplus.prnumberuser.apps.event.data.EventLoadingView
import com.pplus.prnumberuser.apps.main.ui.PadActivity
import com.pplus.prnumberuser.apps.signin.ui.SnsLoginActivity
import com.pplus.prnumberuser.core.code.common.EventType
import com.pplus.prnumberuser.core.network.ApiBuilder
import com.pplus.prnumberuser.core.network.model.dto.Event
import com.pplus.prnumberuser.core.network.model.dto.EventResult
import com.pplus.prnumberuser.core.network.model.response.NewResultResponse
import com.pplus.prnumberuser.core.util.PplusCommonUtil
import com.pplus.prnumberuser.databinding.FragmentEventBinding
import com.pplus.utils.part.format.FormatUtil
import com.pplus.utils.part.utils.StringUtils
import com.pplus.utils.part.utils.time.DateFormatUtils
import retrofit2.Call
import java.util.*


/**
 * A simple [Fragment] subclass.
 * Use the [EventFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class EventFragment : BaseFragment<BaseActivity>() {

    // TODO: Rename and change types of parameters


    private var mPage: Int = 0
    private var mTotalCount = 0
    private var mLayoutManager: LinearLayoutManager? = null
    private var mLockListView = true
    private var mAdapter: EventAdapter? = null
    private var mSelectPos: Int = -1
    private var mSelectEvent: Event? = null
    private var mGroupNo = 1

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        if (arguments != null) {
            mGroupNo = requireArguments().getInt(Const.GROUP)
        }
    }

    private var _binding: FragmentEventBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        _binding = FragmentEventBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun init() {

        binding.imageEventBack.setOnClickListener {
            activity?.onBackPressed()
        }

        mLayoutManager = LinearLayoutManager(requireActivity())
        binding.recyclerEvent.layoutManager = mLayoutManager
        mAdapter = EventAdapter(requireActivity(), false)
        binding.recyclerEvent.adapter = mAdapter
        binding.recyclerEvent.addItemDecoration(CustomItemOffsetDecoration(requireActivity(), R.dimen.height_30, R.dimen.height_30))
        binding.recyclerEvent.addOnScrollListener(object : RecyclerView.OnScrollListener() {

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

        mAdapter!!.setOnItemClickListener(object : EventAdapter.OnItemClickListener {

            private val MIN_CLICK_INTERVAL: Long = 1000 //in millis
            private var lastClickTime: Long = 0

            override fun onItemClick(position: Int, isOpen : Boolean) {

                val currentTime = SystemClock.elapsedRealtime();
                if (currentTime - lastClickTime > MIN_CLICK_INTERVAL) {
                    lastClickTime = currentTime

                    if (position < 0) {
                        return
                    }

                    val item = mAdapter!!.getItem(position)

                    mSelectPos = position
                    mSelectEvent = item

                    if (item.winAnnounceType.equals(EventType.WinAnnounceType.special.name)) {
                        val currentMillis = System.currentTimeMillis()
                        val winAnnounceDate = DateFormatUtils.PPLUS_DATE_FORMAT.parse(item.winAnnounceDate).time
                        if (currentMillis > winAnnounceDate) {

                            val intent = Intent(activity, EventImpressionActivity::class.java)
                            intent.putExtra(Const.DATA, item)
                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                            defaultLauncher.launch(intent)
                            return
                        }
                    }

                    if(!PplusCommonUtil.loginCheck(requireActivity(), signInLauncher)){
                        return
                    }

                    if (item.primaryType.equals(EventType.PrimaryType.move.name)) {
                        val intent = Intent(activity, EventMoveDetailActivity::class.java)
                        intent.putExtra(Const.DATA, item)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                        defaultLauncher.launch(intent)
                    } else {
                        if (StringUtils.isNotEmpty(item.eventLink)) {
                            val intent = Intent(activity, EventDetailActivity::class.java)
                            intent.putExtra(Const.DATA, item)
                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                            defaultLauncher.launch(intent)
                        } else {
                            if (item.primaryType.equals(EventType.PrimaryType.insert.name)) {
                                val intent = Intent(activity, PadActivity::class.java)
                                intent.putExtra(Const.KEY, Const.PAD)
                                intent.putExtra(Const.NUMBER, item.virtualNumber)
                                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                                startActivity(intent)

                            } else {

                                joinEvent(item)
                            }
                        }
                    }
                }

            }
        })
        binding.swipeRefreshEvent.setOnRefreshListener(object : androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener {
            override fun onRefresh() {
                getCount()
                binding.swipeRefreshEvent.isRefreshing = false
            }
        })

        binding.textEventRetentionBol.setOnClickListener {
            val intent = Intent(activity, BolConfigActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
        }

        binding.textEventLogin.setOnClickListener {
            val intent = Intent(activity, SnsLoginActivity::class.java)
            signInLauncher.launch(intent)
        }

        if (LoginInfoManager.getInstance().isMember) {
            setRetentionBol()
            binding.textEventRetentionBol.visibility = View.VISIBLE
            binding.textEventLogin.visibility = View.GONE
        }else{
            binding.textEventRetentionBol.visibility = View.GONE
            binding.textEventLogin.visibility = View.VISIBLE
        }

//        setBuzzvil()
        getCount()
    }

    private inner class CustomItemOffsetDecoration(private val mTopOffset: Int, private val mItemOffset: Int) : RecyclerView.ItemDecoration() {

        constructor(context: Context, @DimenRes topOffsetId: Int, @DimenRes lastOffsetId: Int) : this(context.resources.getDimensionPixelSize(topOffsetId), context.resources.getDimensionPixelSize(lastOffsetId)) {}

        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {

            super.getItemOffsets(outRect, view, parent, state)

            val position = parent.getChildAdapterPosition(view)
            if (position == 0) {
                outRect.set(0, mTopOffset, 0, mItemOffset)
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

                if (response?.data != null) {
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

//    private fun getGroupAll(){
//        ApiBuilder.create().eventGroupAll.setCallback(object  : PplusCallback<NewResultResponse<EventGroup>>{
//            override fun onResponse(call: Call<NewResultResponse<EventGroup>>?, response: NewResultResponse<EventGroup>?) {
//                if(response!!.datas.isNotEmpty()){
//                    header_main_event.visibility = View.VISIBLE
//                }else{
//                    header_main_event.visibility = View.GONE
//                }
//            }
//
//            override fun onFailure(call: Call<NewResultResponse<EventGroup>>?, t: Throwable?, response: NewResultResponse<EventGroup>?) {
//                header_main_event.visibility = View.GONE
//            }
//        }).build().call()
//    }

    private fun getCount() {
        val params = HashMap<String, String>()
        params["no"] = mGroupNo.toString()
        params["appType"] = Const.APP_TYPE
        showProgress("")
        ApiBuilder.create().getEventCountByGroup(params).setCallback(object : PplusCallback<NewResultResponse<Int>> {

            override fun onResponse(call: Call<NewResultResponse<Int>>, response: NewResultResponse<Int>) {
                hideProgress()
                if (!isAdded) {
                    return
                }

                mTotalCount = response.data

                if (mTotalCount == 0) {
                    binding.layoutEventNotExist.visibility = View.VISIBLE
                } else {
                    binding.layoutEventNotExist.visibility = View.GONE
                }

                mPage = 1
                mAdapter?.clear()
                listCall(mPage)
            }

            override fun onFailure(call: Call<NewResultResponse<Int>>, t: Throwable, response: NewResultResponse<Int>) {

                hideProgress()
                if (!isAdded) {
                    return
                }
            }
        }).build().call()
    }

    private fun listCall(page: Int) {
        val params = HashMap<String, String>()
        params["no"] = mGroupNo.toString()
        params["pg"] = page.toString()
        params["appType"] = Const.APP_TYPE
        showProgress("")
        mLockListView = true
        ApiBuilder.create().getEventListByGroup(params).setCallback(object : PplusCallback<NewResultResponse<Event>> {

            override fun onResponse(call: Call<NewResultResponse<Event>>, response: NewResultResponse<Event>) {

                hideProgress()
                if (!isAdded) {
                    return
                }

                mLockListView = false
                mAdapter?.addAll(response.datas)
            }

            override fun onFailure(call: Call<NewResultResponse<Event>>, t: Throwable, response: NewResultResponse<Event>) {

                hideProgress()
                if (!isAdded) {
                    return
                }

            }
        }).build().call()
    }

    private fun checkSignIn(){

        if (LoginInfoManager.getInstance().isMember) {
            setRetentionBol()
            binding.textEventRetentionBol.visibility = View.VISIBLE
            binding.textEventLogin.visibility = View.GONE
        }else{
            binding.textEventRetentionBol.visibility = View.GONE
            binding.textEventLogin.visibility = View.VISIBLE
        }
    }

    val signInLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            // There are no request codes
            val data = result.data
            checkSignIn()
        }
    }

    var defaultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (LoginInfoManager.getInstance().isMember) {
            setRetentionBol()
        }
        getEvent()
    }

    private fun setRetentionBol() {
        PplusCommonUtil.reloadSession(object : PplusCommonUtil.Companion.ReloadListener {

            override fun reload() {

                binding.textEventRetentionBol.text = PplusCommonUtil.fromHtml(getString(R.string.html_cash_unit2, FormatUtil.getMoneyType(LoginInfoManager.getInstance().user.totalBol.toString())))
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
        return ""
    }

    companion object {

        fun newInstance(group:Int): EventFragment {

            val fragment = EventFragment()
            val args = Bundle()
            args.putSerializable(Const.GROUP, group)
            fragment.arguments = args
            return fragment
        }
    }

}// Required empty public constructor
