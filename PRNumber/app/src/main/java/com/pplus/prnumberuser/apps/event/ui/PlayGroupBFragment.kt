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
import com.pplus.prnumberuser.apps.common.mgmt.LoginInfoManager
import com.pplus.prnumberuser.apps.common.ui.BolChargeAlertActivity
import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
import com.pplus.prnumberuser.apps.common.ui.base.BaseFragment
import com.pplus.prnumberuser.apps.event.data.EventLoadingView
import com.pplus.prnumberuser.apps.event.data.PlayGroupBAdapter
import com.pplus.prnumberuser.core.code.common.EventType
import com.pplus.prnumberuser.core.network.ApiBuilder
import com.pplus.prnumberuser.core.network.model.dto.Event
import com.pplus.prnumberuser.core.network.model.dto.EventResult
import com.pplus.prnumberuser.core.network.model.response.NewResultResponse
import com.pplus.prnumberuser.core.util.PplusCommonUtil
import com.pplus.prnumberuser.databinding.FragmentPlayGroupBBinding
import com.pplus.utils.part.utils.StringUtils
import retrofit2.Call
import java.util.*


/**
 * A simple [Fragment] subclass.
 * Use the [PlayGroupBFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PlayGroupBFragment : BaseFragment<BaseActivity>() {

    private var mPage: Int = 0
    private var mTotalCount = 0

    private var mLayoutManager: LinearLayoutManager? = null

    //    private var mGridLayoutManager: GridLayoutManager? = null
    private var mLockListView = true
    private var mAdapter: PlayGroupBAdapter? = null
    private var mSelectPos: Int = 0
    private var mSelectEvent: Event? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        if (arguments != null) {
            //            type = arguments.getSerializable(Const.TYPE) as EnumData.AdsType
        }
    }

    private var _binding: FragmentPlayGroupBBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        _binding = FragmentPlayGroupBBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun init() {

        binding.imagePlayGroupBBack.setOnClickListener {
            activity?.onBackPressed()
        }

        mLayoutManager = LinearLayoutManager(requireActivity())
        binding.recyclerPlayGroupB.layoutManager = mLayoutManager
        mAdapter = PlayGroupBAdapter()
        binding.recyclerPlayGroupB.adapter = mAdapter

        binding.recyclerPlayGroupB.addOnScrollListener(object : RecyclerView.OnScrollListener() {

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

        mAdapter!!.setOnItemClickListener(object : PlayGroupBAdapter.OnItemClickListener {

            private val MIN_CLICK_INTERVAL: Long = 1000 //in millis
            private var lastClickTime: Long = 0

            override fun onItemClick(position: Int) {

                val currentTime = SystemClock.elapsedRealtime()
                if (currentTime - lastClickTime > MIN_CLICK_INTERVAL) {
                    lastClickTime = currentTime

                    if (position < 0) {
                        return
                    }

                    val item = mAdapter!!.getItem(position)

                    mSelectPos = position
                    mSelectEvent = item


                    if (!PplusCommonUtil.loginCheck(requireActivity(), signInLauncher)) {
                        return
                    }

                    if (item.reward == null || item.reward!! >= 0) {
                        joinEvent(item)
                    } else {
                        if (LoginInfoManager.getInstance().user.totalBol >= Math.abs(item.reward!!)) {
                            val intent = Intent(activity, PlayAlertActivity::class.java)
                            intent.putExtra(Const.DATA, item)
                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                            joinAlertLauncher.launch(intent)
                        } else {
                            val intent = Intent(activity, BolChargeAlertActivity::class.java)
                            intent.putExtra(Const.EVENT, item)
                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                            cashChangeLauncher.launch(intent)
                        }
                    }
                }
            }
        })

        if (LoginInfoManager.getInstance().isMember) {
            setRetentionBol()
//            text_play_b_retention_bol?.visibility = View.VISIBLE
//            text_play_b_login?.visibility = View.GONE
        } else {
//            text_play_b_retention_bol?.visibility = View.GONE
//            text_play_b_login?.visibility = View.VISIBLE
        }
        getCount()

    }

    private fun setRetentionBol() {
        PplusCommonUtil.reloadSession(object : PplusCommonUtil.Companion.ReloadListener {

            override fun reload() {

//                text_play_b_retention_bol?.text = PplusCommonUtil.fromHtml(getString(R.string.html_cash_unit2, FormatUtil.getMoneyType(LoginInfoManager.getInstance().user.totalBol.toString())))
            }
        })
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
            override fun onResponse(call: Call<NewResultResponse<EventResult>>?,
                                    response: NewResultResponse<EventResult>?) {

                hideProgress()
                if (!isAdded) {
                    return
                }

                if (response?.data != null) {
                    if (event.winAnnounceType.equals(EventType.WinAnnounceType.immediately.name)) {
                        val mLoading = EventLoadingView()
                        mLoading.isCancelable = false
                        mLoading.setText(getString(R.string.msg_checking_event_result))
                        val delayTime = 2000L
                        mLoading.isCancelable = false
                        try {
                            mLoading.show(parentFragmentManager, "")
                        } catch (e: Exception) {

                        }

                        val handler = Handler()
                        handler.postDelayed(Runnable {

                            try {
                                mLoading.dismiss()
                            } catch (e: Exception) {

                            }

                            val intent = Intent(activity, EventResultActivity::class.java)
                            intent.putExtra(Const.EVENT_RESULT, response.data)
                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                            resultLauncher.launch(intent)
                        }, delayTime)

                    } else {
                        val intent = Intent(activity, EventResultActivity::class.java)
                        intent.putExtra(Const.EVENT_RESULT, response.data)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                        resultLauncher.launch(intent)
                    }
                }

            }

            override fun onFailure(call: Call<NewResultResponse<EventResult>>?,
                                   t: Throwable?,
                                   response: NewResultResponse<EventResult>?) {

                hideProgress()
                if (!isAdded) {
                    return
                }

                if (response?.data != null && StringUtils.isNotEmpty(response.data.joinDate)) {
                    PplusCommonUtil.showEventAlert(activity!!, response.resultCode, event, response.data.joinDate, response.data.joinTerm)
                } else {
                    PplusCommonUtil.showEventAlert(activity!!, response?.resultCode!!, event)
                }
                getEvent()

            }
        }).build().call()
    }

    private fun getCount() {
        val params = HashMap<String, String>()
        params["filter"] = EventType.PrimaryType.benefit.name
        params["platform"] = "aos"
        params["appType"] = Const.APP_TYPE
        //        showProgress("")
        ApiBuilder.create().getEventCount(params).setCallback(object : PplusCallback<NewResultResponse<Int>> {

            override fun onResponse(call: Call<NewResultResponse<Int>>,
                                    response: NewResultResponse<Int>) {
                //                hideProgress()
                if (!isAdded) {
                    return
                }
                mTotalCount = response.data

                //                if (mTotalCount == 0) {
                //                    layout_play_loading?.visibility = View.GONE
                //                    layout_play_not_exist.visibility = View.VISIBLE
                //                } else {
                //                    layout_play_not_exist.visibility = View.GONE
                //                }

                mPage = 1
                mAdapter?.clear()
                listCall(mPage)
            }

            override fun onFailure(call: Call<NewResultResponse<Int>>,
                                   t: Throwable,
                                   response: NewResultResponse<Int>) {
                //                hideProgress()
            }
        }).build().call()
    }

    private fun listCall(page: Int) {
        val params = HashMap<String, String>()
        params["filter"] = EventType.PrimaryType.goodluck.name
        params["pg"] = page.toString()
        params["platform"] = "aos"
        params["groupSeqNo"] = "2"
        params["appType"] = Const.APP_TYPE
        //        showProgress("")
        mLockListView = true
        ApiBuilder.create().getEventList(params).setCallback(object : PplusCallback<NewResultResponse<Event>> {

            override fun onResponse(call: Call<NewResultResponse<Event>>,
                                    response: NewResultResponse<Event>) {

                //                hideProgress()
                if (!isAdded) {
                    return
                }
                //                layout_play_loading?.visibility = View.GONE
                mLockListView = false

                mAdapter?.addAll(response.datas)
            }

            override fun onFailure(call: Call<NewResultResponse<Event>>,
                                   t: Throwable,
                                   response: NewResultResponse<Event>) {
                //                hideProgress()
            }
        }).build().call()
    }

    private fun checkSignIn(){
        if (LoginInfoManager.getInstance().isMember) {
            setRetentionBol()
        }else{
        }
    }

    val joinAlertLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            // There are no request codes
            val data = result.data
            if (data != null) {
                val event = data.getParcelableExtra<Event>(Const.DATA)
                if (LoginInfoManager.getInstance().user.totalBol >= Math.abs(event!!.reward!!)) {
                    joinEvent(event)
                } else {
                    val intent = Intent(activity, BolChargeAlertActivity::class.java)
                    intent.putExtra(Const.EVENT, event)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                    cashChangeLauncher.launch(intent)
                }
            }
        }else{
            getEvent()
        }
    }

    val signInLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) { // There are no request codes
            val data = result.data
            checkSignIn()
        }
    }

    val cashChangeLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        setRetentionBol()
    }

    var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            // There are no request codes
            val data = result.data

            if (data != null) {
                val event = data.getParcelableExtra<Event>(Const.DATA)
                showProgress("")
                PplusCommonUtil.reloadSession(object : PplusCommonUtil.Companion.ReloadListener {
                    override fun reload() {
                        hideProgress()
                        if (!isAdded) {
                            return
                        }

                        if (LoginInfoManager.getInstance().user.totalBol >= Math.abs(event!!.reward!!)) {
                            joinEvent(event)
                        }
                    }
                })
            } else {
                setRetentionBol()
                getEvent()
            }
        }else{
            setRetentionBol()
            getEvent()
        }
    }

    var defaultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        getEvent()
    }


    private fun getEvent() {
        if (mSelectEvent == null) {
            return
        }
        val params = HashMap<String, String>()
        params["no"] = mSelectEvent!!.no.toString()
        //        showProgress("")
        ApiBuilder.create().getEvent(params).setCallback(object : PplusCallback<NewResultResponse<Event>> {
            override fun onResponse(call: Call<NewResultResponse<Event>>?,
                                    response: NewResultResponse<Event>?) {
                //                hideProgress()
                if (!isAdded) {
                    return
                }
                if (response!!.data != null) {
                    mAdapter!!.replaceData(mSelectPos, response.data)
                }
            }

            override fun onFailure(call: Call<NewResultResponse<Event>>?,
                                   t: Throwable?,
                                   response: NewResultResponse<Event>?) {
                //                hideProgress()
            }
        }).build().call()
        mSelectEvent = null
    }

    override fun getPID(): String {
        return "Main_playeventlist"
    }

    companion object {

        fun newInstance(): PlayGroupBFragment {

            val fragment = PlayGroupBFragment()
            val args = Bundle()
            //            args.putSerializable(Const.TYPE, type)
            fragment.arguments = args
            return fragment
        }
    }

} // Required empty public constructor
