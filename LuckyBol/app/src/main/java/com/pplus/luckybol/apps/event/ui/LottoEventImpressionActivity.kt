//package com.pplus.luckybol.apps.event.ui
//
//import android.content.Context
//import android.content.Intent
//import android.graphics.Rect
//import android.os.Bundle
//import android.os.Handler
//import android.view.View
//import androidx.activity.result.contract.ActivityResultContracts
//import androidx.annotation.DimenRes
//import androidx.recyclerview.widget.LinearLayoutManager
//import androidx.recyclerview.widget.RecyclerView
//import com.bumptech.glide.Glide
//import com.bumptech.glide.request.RequestOptions
//import com.pplus.luckybol.Const
//import com.pplus.luckybol.R
//import com.pplus.luckybol.apps.common.builder.AlertBuilder
//import com.pplus.luckybol.apps.common.builder.OnAlertResultListener
//import com.pplus.luckybol.apps.common.mgmt.LoginInfoManager
//import com.pplus.luckybol.apps.common.toolbar.ImplToolbar
//import com.pplus.luckybol.apps.common.toolbar.OnToolbarListener
//import com.pplus.luckybol.apps.common.toolbar.ToolbarOption
//import com.pplus.luckybol.apps.common.ui.base.BaseActivity
//import com.pplus.luckybol.apps.event.data.EventLoadingView
//import com.pplus.luckybol.apps.event.data.LottoEventImpressionHeaderAdapter
//import com.pplus.luckybol.core.code.common.EventType
//import com.pplus.luckybol.core.network.ApiBuilder
//import com.pplus.luckybol.core.network.model.dto.Event
//import com.pplus.luckybol.core.network.model.dto.EventResult
//import com.pplus.luckybol.core.network.model.dto.EventWin
//import com.pplus.luckybol.core.network.model.dto.LottoGift
//import com.pplus.luckybol.core.network.model.response.NewResultResponse
//import com.pplus.luckybol.databinding.ActivityEventImpressionBinding
//import com.pplus.networks.common.PplusCallback
//import com.pplus.utils.part.utils.StringUtils
//import retrofit2.Call
//import java.util.*
//
//class LottoEventImpressionActivity : BaseActivity(), ImplToolbar {
//
//    override fun getPID(): String {
//        return "Main_mypage lotto_Winning history _prizewinner"
//    }
//
//    private lateinit var binding: ActivityEventImpressionBinding
//
//    override fun getLayoutView(): View {
//        binding = ActivityEventImpressionBinding.inflate(layoutInflater)
//        return binding.root
//    }
//
//    var mAdapter: LottoEventImpressionHeaderAdapter? = null
//    private var mPage: Int = 0
//    private var mTotalCount = 0
//    private var mLayoutManager: LinearLayoutManager? = null
//    private var mLockListView = true
//    private var mLottoTimes = "";
//
//    override fun initializeView(savedInstanceState: Bundle?) {
//
//        mLottoTimes = intent.getStringExtra(Const.LOTTO_TIMES)!!
//
//
//        mAdapter = LottoEventImpressionHeaderAdapter(this)
//        mLayoutManager = LinearLayoutManager(this)
//        binding.recyclerEventImpression.layoutManager = mLayoutManager
//        binding.recyclerEventImpression.adapter = mAdapter
//        binding.recyclerEventImpression.addItemDecoration(CustomItemOffsetDecoration(this, R.dimen.height_300))
//        binding.recyclerEventImpression.addOnScrollListener(object : RecyclerView.OnScrollListener() {
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
//                        mPage++
//                        listCall(mPage)
//                    }
//                }
//            }
//        })
//
//        mAdapter!!.setOnItemClickListener(object : LottoEventImpressionHeaderAdapter.OnItemClickListener{
//            override fun onItemClick(position: Int) {
//                val eventWin = mAdapter!!.getItem(position)
//
//                if(LoginInfoManager.getInstance().isMember && eventWin.user!!.no == LoginInfoManager.getInstance().user.no){
//                    val builder = AlertBuilder.Builder()
//                    builder.setLeftText(getString(R.string.word_cancel))
//
//                    builder.setContents(getString(R.string.word_modified))
//
//                    builder.setStyle_alert(AlertBuilder.STYLE_ALERT.LIST_BOTTOM).setOnAlertResultListener(object : OnAlertResultListener {
//
//                        override fun onCancel() {
//
//                        }
//
//                        override fun onResult(event_alert: AlertBuilder.EVENT_ALERT) {
//
//                            when (event_alert.getValue()) {
//                                1 -> {
//                                    val intent = Intent(this@LottoEventImpressionActivity, EventWinImpressionActivity::class.java)
//                                    intent.putExtra(Const.EVENT_WIN, eventWin)
//                                    resultLauncher.launch(intent)
//                                }
//                            }
//                        }
//                    }).builder().show(this@LottoEventImpressionActivity)
//                }
//
//            }
//        })
//
//        mAdapter!!.mLottoTimes = mLottoTimes
//        getEvent()
//        if(LoginInfoManager.getInstance().isMember){
//            getWinAll()
//        }
//
//    }
//
//    fun getJoinCount() {
//        val params = HashMap<String, String>()
//        params["eventSeqNo"] = mAdapter!!.mEvent!!.no.toString()
//        ApiBuilder.create().getEventJoinCount(params).setCallback(object : PplusCallback<NewResultResponse<Int>> {
//            override fun onResponse(call: Call<NewResultResponse<Int>>?, response: NewResultResponse<Int>?) {
//                if (response != null) {
//                    if(response.data!! > 0){
//                        binding.layoutEventImpressionJoin.visibility = View.VISIBLE
//
//                        if (LoginInfoManager.getInstance().user!!.profileImage != null) {
//                            Glide.with(this@LottoEventImpressionActivity).load(LoginInfoManager.getInstance().user!!.profileImage!!.url).apply(RequestOptions().centerCrop().placeholder(R.drawable.ic_event_profile_default).error(R.drawable.ic_event_profile_default)).into(binding.imageEventImpressionJoinProfile)
//                        } else {
//                            binding.imageEventImpressionJoinProfile.setImageResource(R.drawable.ic_event_profile_default)
//                        }
//
//                        binding.textEventImpressionLottoJoinCount.text = getString(R.string.format_lotto_join_count, mLottoTimes, response.data.toString())
//                        binding.textEventImpressionJoinMore.setOnClickListener {
//                            val intent = Intent(this@LottoEventImpressionActivity, MyLottoJoinListActivity::class.java)
//                            intent.putExtra(Const.DATA, mAdapter!!.mEvent)
//                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//                            startActivity(intent)
//                        }
//                    }else{
//                        binding.layoutEventImpressionJoin.visibility = View.GONE
//                    }
//                }
//
//            }
//
//            override fun onFailure(call: Call<NewResultResponse<Int>>?, t: Throwable?, response: NewResultResponse<Int>?) {
//            }
//        }).build().call()
//    }
//
//    private fun getEvent(){
//        val params = HashMap<String, String>()
//        params["lottoTimes"] = mLottoTimes
//        params["primaryType"] = EventType.PrimaryType.lotto.name
//        ApiBuilder.create().getEvent(params).setCallback(object : PplusCallback<NewResultResponse<Event>> {
//            override fun onResponse(call: Call<NewResultResponse<Event>>?, response: NewResultResponse<Event>?) {
//                if(response != null){
//                    mAdapter!!.mEvent = response.data
//                    if(LoginInfoManager.getInstance().isMember){
//                        getJoinCount()
//                    }
//
//                    getGiftList()
//                }
//
//            }
//
//            override fun onFailure(call: Call<NewResultResponse<Event>>?, t: Throwable?, response: NewResultResponse<Event>?) {
//            }
//        }).build().call()
//    }
//
//    private fun getGiftList(){
//        val params = HashMap<String, String>()
//        params["lottoTimes"] = mLottoTimes
//        params["sz"] = "100"
//        params["pg"] = "1"
//        ApiBuilder.create().getLottoPlayGiftList(params).setCallback(object : PplusCallback<NewResultResponse<LottoGift>> {
//            override fun onResponse(call: Call<NewResultResponse<LottoGift>>?, response: NewResultResponse<LottoGift>?) {
//                if(response != null){
//                    mAdapter!!.mGiftList = (response.datas as MutableList<LottoGift>?)!!
//                    getCount()
//                }
//            }
//
//            override fun onFailure(call: Call<NewResultResponse<LottoGift>>?, t: Throwable?, response: NewResultResponse<LottoGift>?) {
//
//            }
//        }).build().call()
//    }
//
//    private fun getWinAll() {
//        val params = HashMap<String, String>()
//        params["lottoTimes"] = mLottoTimes
//        showProgress("")
//        ApiBuilder.create().getLottoResult(params).setCallback(object : PplusCallback<NewResultResponse<EventWin>> {
//
//            override fun onResponse(call: Call<NewResultResponse<EventWin>>, response: NewResultResponse<EventWin>) {
//                hideProgress()
//                if (response.datas != null && response.datas!!.isNotEmpty()) {
//
//                    var eventWin: EventWin? = null
////                    val eventWinList = response.datas.filter{ it -> StringUtils.isEmpty(it.impression)}
////                    if(eventWinList.isNotEmpty()){
////                        eventWin = eventWinList[0]
////                    }
//
//                    for(data in response.datas!!){
//                        if(StringUtils.isEmpty(data.impression)){
//                            eventWin = data
//                            break
//                        }
//                    }
//
//                    if(eventWin == null){
//                        return
//                    }
//
//                    val loading = EventLoadingView()
//                    loading.setText(getString(R.string.msg_checking_event_result))
//                    loading.isCancelable = false
//                    try{
//                        loading.show(supportFragmentManager, "")
//                    }catch (e : Exception){
//
//                    }
//
//                    val handler = Handler()
//                    handler.postDelayed({
//                        try{
//                            loading.dismiss()
//                        }catch (e : Exception){
//
//                        }
//                        val eventResult = EventResult()
//                        eventResult.win = eventWin
//                        val intent = Intent(this@LottoEventImpressionActivity, EventResultActivity::class.java)
//                        intent.putExtra(Const.EVENT_RESULT, eventResult)
//                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//                        resultLauncher.launch(intent)
//                    }, 2000)
//                }
//            }
//
//            override fun onFailure(call: Call<NewResultResponse<EventWin>>, t: Throwable, response: NewResultResponse<EventWin>) {
//                hideProgress()
//            }
//        }).build().call()
//    }
//
//    private inner class CustomItemOffsetDecoration(private val mLastOffset: Int) : RecyclerView.ItemDecoration() {
//
//        constructor(context: Context, @DimenRes lastOffsetId: Int) : this(context.resources.getDimensionPixelSize(lastOffsetId)) {}
//
//        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
//
//            super.getItemOffsets(outRect, view, parent, state)
//
//            val position = parent.getChildAdapterPosition(view)
//            if (mAdapter!!.itemCount > 0 && position == mAdapter!!.itemCount - 1) {
//                outRect.bottom = mLastOffset
//            }
//
//        }
//    }
//
//
//    private fun getCount() {
//        val params = HashMap<String, String>()
//        params["lottoTimes"] = mLottoTimes
////        params["primaryType"] = EventType.PrimaryType.lottoPlaybol.name
//        showProgress("")
//        ApiBuilder.create().getLottoWinnerCount(params).setCallback(object : PplusCallback<NewResultResponse<Int>> {
//
//            override fun onResponse(call: Call<NewResultResponse<Int>>, response: NewResultResponse<Int>) {
//                hideProgress()
//                mTotalCount = response.data!!
//
//                mPage = 1
//                mAdapter?.clear()
//                listCall(mPage)
//            }
//
//            override fun onFailure(call: Call<NewResultResponse<Int>>, t: Throwable, response: NewResultResponse<Int>) {
//                hideProgress()
//            }
//        }).build().call()
//    }
//
//    private fun listCall(page: Int) {
//        val params = HashMap<String, String>()
//        params["lottoTimes"] = mLottoTimes
////        params["primaryType"] = EventType.PrimaryType.lottoPlaybol.name
//        params["pg"] = page.toString()
//
//        showProgress("")
//        mLockListView = true
//        ApiBuilder.create().getLottoWinnerList(params).setCallback(object : PplusCallback<NewResultResponse<EventWin>> {
//
//            override fun onResponse(call: Call<NewResultResponse<EventWin>>, response: NewResultResponse<EventWin>) {
//
//                mLockListView = false
//
//                hideProgress()
//                mAdapter?.addAll(response.datas!!)
//            }
//
//            override fun onFailure(call: Call<NewResultResponse<EventWin>>, t: Throwable, response: NewResultResponse<EventWin>) {
//
//                hideProgress()
//            }
//        }).build().call()
//    }
//
//    val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
//        getWinAll()
//        getCount()
//    }
//
//    override fun getToolbarOption(): ToolbarOption {
//
//        val toolbarOption = ToolbarOption(this)
//        toolbarOption.initializeDefaultToolbar(getString(R.string.word_win_status2), ToolbarOption.ToolbarMenu.LEFT)
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
//                else -> {}
//            }
//        }
//    }
//}
