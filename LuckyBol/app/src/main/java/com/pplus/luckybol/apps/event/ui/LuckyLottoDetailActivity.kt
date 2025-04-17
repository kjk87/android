package com.pplus.luckybol.apps.event.ui

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.LinearLayout
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.pplus.luckybol.Const
import com.pplus.luckybol.R
import com.pplus.luckybol.apps.common.ui.base.BaseActivity
import com.pplus.luckybol.apps.event.data.LottoWinHeaderAdapter
import com.pplus.luckybol.core.code.common.EventType
import com.pplus.luckybol.core.network.ApiBuilder
import com.pplus.luckybol.core.network.model.dto.Event
import com.pplus.luckybol.core.network.model.dto.EventGift
import com.pplus.luckybol.core.network.model.dto.EventWin
import com.pplus.luckybol.core.network.model.dto.EventWinJpa
import com.pplus.luckybol.core.network.model.response.NewResultResponse
import com.pplus.luckybol.core.network.model.response.SubResultResponse
import com.pplus.luckybol.databinding.ActivityLuckyLottoDetailBinding
import com.pplus.networks.common.PplusCallback
import com.pplus.utils.part.apps.resource.ResourceUtil
import com.pplus.utils.part.format.FormatUtil
import com.pplus.utils.part.utils.StringUtils
import com.pplus.utils.part.utils.time.DateFormatUtils
import jp.wasabeef.blurry.Blurry
import retrofit2.Call
import java.text.SimpleDateFormat
import java.util.*

class LuckyLottoDetailActivity : BaseActivity() {
    private lateinit var binding: ActivityLuckyLottoDetailBinding

    override fun getLayoutView(): View {
        binding = ActivityLuckyLottoDetailBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun getPID(): String? {
        return ""
    }

    lateinit var mEvent: Event
    lateinit var mAdapter: LottoWinHeaderAdapter
    lateinit var mLayoutManager: LinearLayoutManager
    private var mPage: Int = 0
    private var mTotalCount = 0
    private var mLockListView = true
    private var mIsLast = false

    override fun initializeView(savedInstanceState: Bundle?) {
        mEvent = intent.getParcelableExtra(Const.DATA)!!

        binding.imageLuckyLottoDetailBack.setOnClickListener {
            onBackPressed()
        }

        binding.imageLuckyLottoDetailBack2.setOnClickListener {
            onBackPressed()
        }

        binding.appBarLuckyLottoDetail.addOnOffsetChangedListener { appBarLayout, verticalOffset ->

            if (verticalOffset <= -binding.collapsingLuckyLottoDetail.height + binding.toolbarLuckyLottoDetail.height) { //toolbar is collapsed here
                binding.imageLuckyLottoDetailBack.setImageResource(R.drawable.ic_navbar_back)
            } else {
                binding.imageLuckyLottoDetailBack.setImageResource(R.drawable.ic_navbar_back_light)
            }
        }
        mAdapter = LottoWinHeaderAdapter()
        mLayoutManager = LinearLayoutManager(this)
        binding.recyclerLuckyLotto.layoutManager = mLayoutManager
        binding.recyclerLuckyLotto.adapter = mAdapter

        binding.recyclerLuckyLotto.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            internal var pastVisibleItems: Int = 0
            internal var visibleItemCount: Int = 0
            internal var totalItemCount: Int = 0

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {

                super.onScrolled(recyclerView, dx, dy)
                visibleItemCount = mLayoutManager.childCount
                totalItemCount = mLayoutManager.itemCount
                pastVisibleItems = mLayoutManager.findFirstVisibleItemPosition()
                if (!mLockListView) {
                    if (!mIsLast && visibleItemCount + pastVisibleItems >= totalItemCount * Const.NEXT_RECYCLERVIEW_MARGIN) {
                        mPage++
                        listCall(mPage)
                    }
                }
            }
        })
        getEvent()
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        mEvent = intent?.getParcelableExtra(Const.DATA)!!
        getEvent()
    }

    private fun getEvent() {

        val params = HashMap<String, String>()
        params["no"] = mEvent.no.toString()
        showProgress("")
        ApiBuilder.create().getEvent(params).setCallback(object : PplusCallback<NewResultResponse<Event>> {
            override fun onResponse(call: Call<NewResultResponse<Event>>?,
                                    response: NewResultResponse<Event>?) {
                hideProgress()


                if (response?.data != null) {
                    mEvent = response.data!!

                    Glide.with(this@LuckyLottoDetailActivity).load(mEvent.detailImage).apply(RequestOptions().fitCenter().placeholder(R.drawable.luckybol_default_img).error(R.drawable.luckybol_default_img)).into(binding.imageLuckyLottoDetail)
                    if (mEvent.maxJoinCount != null && mEvent.maxJoinCount!! > 0) {
                        binding.layoutLuckyLottoDetailJoinRate.visibility = View.VISIBLE
                        val weightSum = mEvent.maxJoinCount!!

                        binding.layoutLuckyLottoDetailJoinGraphFull.weightSum = weightSum.toFloat()

                        val layoutParams = binding.viewLuckyLottoDetailJoinGraph.layoutParams

                        if (layoutParams is LinearLayout.LayoutParams) {
                            when (mEvent.winAnnounceType) {
                                EventType.WinAnnounceType.special.name -> {
                                    if (mEvent.joinCount!! > mEvent.maxJoinCount!!) {
                                        layoutParams.weight = mEvent.maxJoinCount!!.toFloat()
                                    } else {
                                        layoutParams.weight = mEvent.joinCount!!.toFloat()
                                    }

                                }
                                EventType.WinAnnounceType.limit.name -> {
                                    layoutParams.weight = mEvent.joinCount!!.toFloat()
                                }
                            }

                        }
                        binding.viewLuckyLottoDetailJoinGraph.requestLayout()

                        val percent = (mEvent.joinCount!!.toFloat() / mEvent.maxJoinCount!!.toFloat() * 100).toInt()

                        binding.textLuckyLottoDetailJoinRate.text = getString(R.string.format_percent3, percent.toString())

                    } else {
                        binding.layoutLuckyLottoDetailJoinRate.visibility = View.GONE
                    }

                    binding.textLuckyLottoDetailJoin.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
                    binding.textLuckyLottoDetailJoin.compoundDrawablePadding = 0
                    binding.layoutLuckyLottoDetailJoin.setOnClickListener { }
                    binding.layoutLuckyLottoDetailJoin.visibility = View.GONE
                    binding.layoutLuckyLottoDetailWinnerCount.visibility = View.GONE
                    binding.layoutLuckyLottoDetailAnnounceWait.visibility = View.GONE
                    mAdapter.mEvent = mEvent
                    getGiftAll()
                }
            }

            override fun onFailure(call: Call<NewResultResponse<Event>>?,
                                   t: Throwable?,
                                   response: NewResultResponse<Event>?) {

                hideProgress()
            }
        }).build().call()
    }

    private fun getGiftAll() {
        val params = HashMap<String, String>()
        params["no"] = mEvent.no.toString()
        showProgress("")
        ApiBuilder.create().getGiftAll(params).setCallback(object : PplusCallback<NewResultResponse<EventGift>> {
            override fun onResponse(call: Call<NewResultResponse<EventGift>>?,
                                    response: NewResultResponse<EventGift>?) {
                hideProgress()
                if (response?.datas != null && response.datas!!.isNotEmpty()) {
                    mAdapter.mEventGift = response.datas!![0]
                    mAdapter.notifyItemChanged(0)
                    getJoinCount()
                }
            }

            override fun onFailure(call: Call<NewResultResponse<EventGift>>?,
                                   t: Throwable?,
                                   response: NewResultResponse<EventGift>?) {
                hideProgress()
            }
        }).build().call()
    }

    private fun getJoinCount() {
        val params = HashMap<String, String>()
        params["eventSeqNo"] = mEvent.no.toString()
        showProgress("")
        ApiBuilder.create().getEventJoinCount(params).setCallback(object : PplusCallback<NewResultResponse<Int>> {
            override fun onResponse(call: Call<NewResultResponse<Int>>?,
                                    response: NewResultResponse<Int>?) {
                hideProgress()
                if (response?.data != null) {
                    val joinCount = response.data!!

                    if (joinCount > 0) {
                        binding.textLuckyLottoDetailJoinHistory.visibility = View.VISIBLE
                        binding.textLuckyLottoDetailJoinHistory.setOnClickListener {
                            val intent = Intent(this@LuckyLottoDetailActivity, MyLottoJoinListActivity::class.java)
                            intent.putExtra(Const.DATA, mEvent)
                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                            defaultLauncher.launch(intent)
                        }
                        binding.textLuckyLottoDetailJoinHistory2.visibility = View.VISIBLE
                        binding.textLuckyLottoDetailJoinHistory2.setOnClickListener {
                            val intent = Intent(this@LuckyLottoDetailActivity, MyLottoJoinListActivity::class.java)
                            intent.putExtra(Const.DATA, mEvent)
                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                            defaultLauncher.launch(intent)
                        }

                    } else {
                        binding.textLuckyLottoDetailJoinHistory2.visibility = View.GONE
                        binding.textLuckyLottoDetailJoinHistory.visibility = View.GONE
                    }

                    when (mEvent.status) {
                        "active" -> {
                            binding.layoutLuckyLottoDetailJoin.visibility = View.VISIBLE
                            if (joinCount < mEvent.joinLimitCount!!) {
                                if (mEvent.reward!! < 0) {
                                    binding.textLuckyLottoDetailJoin.text = getString(R.string.format_msg_lucky_lotto_join, FormatUtil.getMoneyTypeFloat(Math.abs(mEvent.reward!!).toString()))
                                    binding.textLuckyLottoDetailJoin.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_lucky_lotto_join, 0, 0, 0)
                                    binding.textLuckyLottoDetailJoin.compoundDrawablePadding = resources.getDimensionPixelSize(R.dimen.width_30)
                                } else {
                                    binding.textLuckyLottoDetailJoin.text = getString(R.string.msg_free_join)
                                }
                                binding.layoutLuckyLottoDetailJoin.setBackgroundColor(ResourceUtil.getColor(this@LuckyLottoDetailActivity, R.color.color_fc5c57))
                                binding.layoutLuckyLottoDetailJoin.setOnClickListener {
                                    val intent = Intent(this@LuckyLottoDetailActivity, LottoJoinActivity::class.java)
                                    intent.putExtra(Const.DATA, mEvent)
                                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                                    defaultLauncher.launch(intent)
                                }
                            } else {
                                binding.textLuckyLottoDetailJoin.text = getString(R.string.msg_limit_enable_join_count)
                                binding.layoutLuckyLottoDetailJoin.setBackgroundColor(ResourceUtil.getColor(this@LuckyLottoDetailActivity, R.color.color_c0c6cc))
                            }
                        }
                        "conclude" -> {
                            binding.layoutLuckyLottoDetailJoin.visibility = View.VISIBLE
                            if (StringUtils.isNotEmpty(mEvent.winAnnounceDate)) {
                                binding.layoutLuckyLottoDetailJoin.setBackgroundColor(ResourceUtil.getColor(this@LuckyLottoDetailActivity, R.color.color_fc5c57))
                                binding.textLuckyLottoDetailJoin.text = getString(R.string.format_lotto_win_announce_date, mEvent.winAnnounceDate)
                            } else {
                                binding.layoutLuckyLottoDetailJoin.setBackgroundColor(ResourceUtil.getColor(this@LuckyLottoDetailActivity, R.color.color_c0c6cc))
                                binding.textLuckyLottoDetailJoin.text = getString(R.string.msg_checking_win_announce_date)
                            }
                        }
                        "pending" -> {
                            binding.layoutLuckyLottoDetailJoin.visibility = View.VISIBLE
                            if (StringUtils.isNotEmpty(mEvent.winAnnounceDate)) {
                                binding.layoutLuckyLottoDetailJoin.setBackgroundColor(ResourceUtil.getColor(this@LuckyLottoDetailActivity, R.color.color_fc5c57))
                                binding.textLuckyLottoDetailJoin.text = getString(R.string.format_lotto_win_announce_date, mEvent.winAnnounceDate)
                            } else {
                                binding.layoutLuckyLottoDetailJoin.setBackgroundColor(ResourceUtil.getColor(this@LuckyLottoDetailActivity, R.color.color_c0c6cc))
                                binding.textLuckyLottoDetailJoin.text = getString(R.string.msg_checking_win_announce_date)
                            }

                            val handler = Handler(Looper.myLooper()!!)
                            showProgress("")
                            handler.postDelayed({
                                hideProgress()
                                binding.layoutLuckyLottoDetailAnnounceWait.visibility = View.VISIBLE
                                Blurry.with(this@LuckyLottoDetailActivity).color(Color.argb(153, 0, 0, 0)).capture(binding.layoutLuckyLottoDetail).into(binding.imageLuckyLottoBlur)
                                Glide.with(this@LuckyLottoDetailActivity).load(mAdapter.mEventGift!!.giftImageUrl).apply(RequestOptions().fitCenter().placeholder(R.drawable.luckybol_default_img).error(R.drawable.luckybol_default_img)).into(binding.imageLuckyLottoDetailGift)

                                if(StringUtils.isNotEmpty(mEvent.winAnnounceDate)){
                                    val output = SimpleDateFormat("yyyy.MM.dd")
                                    val announceDay = output.format(DateFormatUtils.PPLUS_DATE_FORMAT.parse(mEvent.winAnnounceDate))
                                    val today = output.format(Date())
                                    if(announceDay == today){
                                        val announceDate = SimpleDateFormat("HH:mm").format(DateFormatUtils.PPLUS_DATE_FORMAT.parse(mEvent.winAnnounceDate))
                                        binding.textLuckyLottoDetailAnnounceTime.text = getString(R.string.format_soon_after, announceDate)
                                    }else{
                                        val announceDate = SimpleDateFormat("yyyy.MM.dd HH:mm").format(DateFormatUtils.PPLUS_DATE_FORMAT.parse(mEvent.winAnnounceDate))
                                        binding.textLuckyLottoDetailAnnounceTime.text = announceDate
                                    }
                                }

                                if(StringUtils.isNotEmpty(mEvent.liveUrl)){
                                    binding.textLuckyLottoDetailLive.visibility = View.VISIBLE
                                    binding.textLuckyLottoDetailLive.setOnClickListener {
                                        if(StringUtils.isNotEmpty(mEvent.liveUrl)){
                                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(mEvent.liveUrl))
                                            startActivity(intent)
                                        }
                                    }
                                }else{
                                    binding.textLuckyLottoDetailLive.visibility = View.GONE
                                }

                            }, 500)
                        }
                        "announce", "complete", "finish" -> {
                            binding.layoutLuckyLottoDetailJoin.visibility = View.GONE
                            getLottoWinnerCount()
                        }
                    }
                }

            }

            override fun onFailure(call: Call<NewResultResponse<Int>>?,
                                   t: Throwable?,
                                   response: NewResultResponse<Int>?) {
                hideProgress()
            }
        }).build().call()
    }

    val defaultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        getEvent()
    }

    private fun getWinAll(winnerCount : Int) {
        val params = HashMap<String, String>()
        params["no"] = mEvent.no.toString()
        showProgress("")
        ApiBuilder.create().getWinAll(params).setCallback(object : PplusCallback<NewResultResponse<EventWin>> {

            override fun onResponse(call: Call<NewResultResponse<EventWin>>, response: NewResultResponse<EventWin>) {
                hideProgress()
                if (response.datas != null && response.datas!!.isNotEmpty()) {

                    var eventWin: EventWin? = null
                    for(data in response.datas!!){

                        if(StringUtils.isEmpty(data.impression)){
                            eventWin = data
                            break
                        }
                    }

                    if(eventWin == null){
                        return
                    }

                    eventWin.event = mEvent

                    val intent = Intent(this@LuckyLottoDetailActivity, AlertLottoWinActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                    intent.putExtra(Const.DATA, eventWin)
                    intent.putExtra(Const.WINNER_COUNT, winnerCount)
                    startActivity(intent)
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                }
            }

            override fun onFailure(call: Call<NewResultResponse<EventWin>>, t: Throwable, response: NewResultResponse<EventWin>) {
                hideProgress()
            }
        }).build().call()
    }

    private fun getLottoWinnerCount() {
        val params = HashMap<String, String>()
        params["eventSeqNo"] = mEvent.no.toString()
        showProgress("")
        ApiBuilder.create().getLottoWinnerCount(params).setCallback(object : PplusCallback<NewResultResponse<Int>> {
            override fun onResponse(call: Call<NewResultResponse<Int>>?,
                                    response: NewResultResponse<Int>?) {
                hideProgress()

                var winnerCount = 0
                if (response?.data != null) {
                    binding.layoutLuckyLottoDetailWinnerCount.visibility = View.VISIBLE
                    binding.textLuckyLottoDetailWinnerCount.text = response.data.toString()
                    winnerCount = response.data!!
                    mAdapter.mWinnerCount = winnerCount
                    mAdapter.notifyItemChanged(0)
                }
                if(winnerCount > 0){
                    getWinAll(winnerCount)
                    mPage = 0
                    listCall(mPage)
                }
            }

            override fun onFailure(call: Call<NewResultResponse<Int>>?,
                                   t: Throwable?,
                                   response: NewResultResponse<Int>?) {
                hideProgress()
            }
        }).build().call()
    }

    private fun listCall(page: Int) {

        mLockListView = true
        val params = HashMap<String, String>()
        params["eventSeqNo"] = mEvent.no.toString()
        params["page"] = page.toString()
        params["sort"] = "id,desc" //        showProgress("")
        ApiBuilder.create().getLottoWinnerList(params).setCallback(object : PplusCallback<NewResultResponse<SubResultResponse<EventWinJpa>>> {

            override fun onResponse(call: Call<NewResultResponse<SubResultResponse<EventWinJpa>>>?,
                                    response: NewResultResponse<SubResultResponse<EventWinJpa>>?) {

                if (response != null) {
                    mIsLast = response.data!!.last!!
                    if (response.data!!.first!!) {
                        mTotalCount = response.data!!.totalElements!!
                        mAdapter.clear()
                    }

                    mLockListView = false
                    mAdapter.addAll(response.data!!.content!!)
                }
            }

            override fun onFailure(call: Call<NewResultResponse<SubResultResponse<EventWinJpa>>>?,
                                   t: Throwable?,
                                   response: NewResultResponse<SubResultResponse<EventWinJpa>>?) {
                mLockListView = false
            }

        }).build().call()
    }
}