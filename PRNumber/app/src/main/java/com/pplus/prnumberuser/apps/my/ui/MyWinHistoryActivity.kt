package com.pplus.prnumberuser.apps.my.ui

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.pplus.networks.common.PplusCallback
import com.pplus.prnumberuser.Const
import com.pplus.prnumberuser.R
import com.pplus.prnumberuser.apps.bol.ui.BolConfigActivity
import com.pplus.prnumberuser.apps.common.mgmt.LoginInfoManager
import com.pplus.prnumberuser.apps.common.toolbar.ImplToolbar
import com.pplus.prnumberuser.apps.common.toolbar.OnToolbarListener
import com.pplus.prnumberuser.apps.common.toolbar.ToolbarOption
import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
import com.pplus.prnumberuser.apps.event.ui.EventImpressionActivity
import com.pplus.prnumberuser.apps.event.ui.LottoEventImpressionActivity
import com.pplus.prnumberuser.apps.my.data.MyWinHistoryAdapter
import com.pplus.prnumberuser.apps.my.data.TotalWinHistoryAdapter
import com.pplus.prnumberuser.apps.page.ui.PageEventAnnounceActivity
import com.pplus.prnumberuser.core.code.common.EventType
import com.pplus.prnumberuser.core.network.ApiBuilder
import com.pplus.prnumberuser.core.network.model.dto.Event
import com.pplus.prnumberuser.core.network.model.dto.EventWin
import com.pplus.prnumberuser.core.network.model.response.NewResultResponse
import com.pplus.prnumberuser.core.util.PplusCommonUtil
import com.pplus.prnumberuser.databinding.ActivityMyWinHistoryBinding
import com.pplus.utils.part.format.FormatUtil
import com.pplus.utils.part.utils.StringUtils
import retrofit2.Call
import java.util.*

class MyWinHistoryActivity : BaseActivity(), ImplToolbar {

    override fun getPID(): String {
        return "Main_bolpoint_Winning history"
    }

    private lateinit var binding: ActivityMyWinHistoryBinding

    override fun getLayoutView(): View {
        binding = ActivityMyWinHistoryBinding.inflate(layoutInflater)
        return binding.root
    }

    var mAdapter: MyWinHistoryAdapter? = null
    var mTotalAdapter: TotalWinHistoryAdapter? = null
    private var mPage: Int = 0
    private var mTotalCount = 0
    private var mLayoutManager: LinearLayoutManager? = null
    private var mLockListView = true

    override fun initializeView(savedInstanceState: Bundle?) {

        val tab = intent.getStringExtra(Const.TAB)

        mTotalAdapter = TotalWinHistoryAdapter(this)
        mAdapter = MyWinHistoryAdapter(this)
        mLayoutManager = LinearLayoutManager(this)
        binding.recyclerWinHistory.layoutManager = mLayoutManager
        binding.recyclerWinHistory.adapter = mAdapter
        binding.recyclerWinHistory.addOnScrollListener(object : RecyclerView.OnScrollListener() {

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
                        if (binding.textWinHistoryLotto.isSelected) {
                            listCall(mPage)
                        } else {
                            userListCall(mPage)
                        }
                    }
                }
            }
        })

        mAdapter!!.setOnItemClickListener(object : MyWinHistoryAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                val event = mAdapter!!.getItem(position).event

                if(event!!.primaryType == EventType.PrimaryType.lotto.name || event.primaryType == EventType.PrimaryType.lottoPlaybol.name){
                    val intent = Intent(this@MyWinHistoryActivity, LottoEventImpressionActivity::class.java)
                    intent.putExtra(Const.LOTTO_TIMES, event.lottoTimes)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                    startActivityForResult(intent, Const.REQ_RESULT)
                }else{
                    val intent = Intent(this@MyWinHistoryActivity, EventImpressionActivity::class.java)
                    intent.putExtra(Const.DATA, event)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                    startActivityForResult(intent, Const.REQ_RESULT)
                }


            }
        })

        mTotalAdapter!!.setOnItemClickListener(object : TotalWinHistoryAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                val event = mTotalAdapter!!.getItem(position)

                if(event.primaryType == EventType.PrimaryType.lotto.name || event.primaryType == EventType.PrimaryType.lottoPlaybol.name){
                    val intent = Intent(this@MyWinHistoryActivity, LottoEventImpressionActivity::class.java)
                    intent.putExtra(Const.LOTTO_TIMES, event.lottoTimes)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                    startActivityForResult(intent, Const.REQ_RESULT)
                }else if(event.primaryType == EventType.PrimaryType.page.name){
                    val intent = Intent(this@MyWinHistoryActivity, PageEventAnnounceActivity::class.java)
                    intent.putExtra(Const.DATA, event)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                    startActivityForResult(intent, Const.REQ_RESULT)
                }else{
                    val intent = Intent(this@MyWinHistoryActivity, EventImpressionActivity::class.java)
                    intent.putExtra(Const.DATA, event)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                    startActivityForResult(intent, Const.REQ_RESULT)
                }
            }
        })

        binding.layoutWinHistoryTotal.setOnClickListener {
            setSelect(binding.layoutWinHistoryTotal, binding.layoutWinHistoryMy)
            setBold(binding.textWinHistoryLotto, binding.textWinHistoryMy)
            binding.recyclerWinHistory.adapter = mTotalAdapter
            getCount()
        }

        binding.layoutWinHistoryMy.setOnClickListener {
            setSelect(binding.layoutWinHistoryMy, binding.layoutWinHistoryTotal)
            setBold(binding.textWinHistoryMy, binding.textWinHistoryLotto)
            binding.recyclerWinHistory.adapter = mAdapter
            getUserCount()
        }

        binding.textMyWinHistoryPoinConfig.setOnClickListener {
            val intent = Intent(this, BolConfigActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
        }

        val user = LoginInfoManager.getInstance().user
        if (user.profileImage != null && StringUtils.isNotEmpty(user.profileImage!!.url)) {
            Glide.with(this).load(user.profileImage!!.url).apply(RequestOptions().centerCrop().placeholder(R.drawable.ic_mypage_profile_default).error(R.drawable.ic_mypage_profile_default)).into(binding.imageMyWinHistoryProfile)
        }

        if(StringUtils.isNotEmpty(tab) && tab == Const.LOTTO){
            setSelect(binding.layoutWinHistoryTotal, binding.layoutWinHistoryMy)
            setBold(binding.textWinHistoryLotto, binding.textWinHistoryMy)
            binding.recyclerWinHistory.adapter = mTotalAdapter
            getCount()
        }else{
            setSelect(binding.layoutWinHistoryMy, binding.layoutWinHistoryTotal)
            setBold(binding.textWinHistoryMy, binding.textWinHistoryLotto)
            getUserCount()
        }

        setRetentionBol()
    }

    private fun setRetentionBol() {
        PplusCommonUtil.reloadSession(object : PplusCommonUtil.Companion.ReloadListener {

            override fun reload() {

                binding.textMyWinHistoryRetentionPoint.text = PplusCommonUtil.fromHtml(getString(R.string.html_cash_unit2, FormatUtil.getMoneyType(LoginInfoManager.getInstance().user.totalBol.toString())))
            }
        })
    }

    private fun setSelect(view1: View, view2: View) {

        view1.isSelected = true
        view2.isSelected = false
    }

    private fun setBold(view1: TextView, view2: TextView) {

        view1.typeface = Typeface.DEFAULT_BOLD
        view2.typeface = Typeface.DEFAULT
    }

    private fun getUserCount() {
        binding.textWinHistoryNotExist.visibility = View.GONE
        showProgress("")
        ApiBuilder.create().userWinCount.setCallback(object : PplusCallback<NewResultResponse<Int>> {

            override fun onResponse(call: Call<NewResultResponse<Int>>, response: NewResultResponse<Int>) {
                hideProgress()
                mTotalCount = response.data

                if (mTotalCount > 0) {
                    binding.textWinHistoryNotExist.visibility = View.GONE
                } else {
                    binding.textWinHistoryNotExist.visibility = View.VISIBLE
                }

                mPage = 1
                mTotalAdapter?.clear()
                mAdapter?.clear()
                userListCall(mPage)
            }

            override fun onFailure(call: Call<NewResultResponse<Int>>, t: Throwable, response: NewResultResponse<Int>) {
                hideProgress()
            }
        }).build().call()
    }

    private fun userListCall(page: Int) {
        val params = HashMap<String, String>()
        params["pg"] = page.toString()

        showProgress("")
        mLockListView = true
        ApiBuilder.create().getUserWinList(params).setCallback(object : PplusCallback<NewResultResponse<EventWin>> {

            override fun onResponse(call: Call<NewResultResponse<EventWin>>, response: NewResultResponse<EventWin>) {

                mLockListView = false

                hideProgress()
                mAdapter?.addAll(response.datas)
            }

            override fun onFailure(call: Call<NewResultResponse<EventWin>>, t: Throwable, response: NewResultResponse<EventWin>) {

                hideProgress()
            }
        }).build().call()
    }

    private fun getCount() {
        binding.textWinHistoryNotExist.visibility = View.GONE
        val params = HashMap<String, String>()
//        params["primaryType"] = EventType.PrimaryType.lotto.name
//        params["active"] = "false"
        showProgress("")
//        ApiBuilder.create().getLottoCount(params).setCallback(object : PplusCallback<NewResultResponse<Int>> {
        ApiBuilder.create().getWinAnnounceCount(params).setCallback(object : PplusCallback<NewResultResponse<Int>> {

            override fun onResponse(call: Call<NewResultResponse<Int>>, response: NewResultResponse<Int>) {
                hideProgress()
                mTotalCount = response.data

                if (mTotalCount > 0) {
                    binding.textWinHistoryNotExist.visibility = View.GONE
                } else {
                    binding.textWinHistoryNotExist.visibility = View.VISIBLE
                }

                mPage = 1
                mTotalAdapter?.clear()
                mAdapter?.clear()
                listCall(mPage)
            }

            override fun onFailure(call: Call<NewResultResponse<Int>>, t: Throwable, response: NewResultResponse<Int>) {
                hideProgress()
            }
        }).build().call()
    }

    private fun listCall(page: Int) {
        val params = HashMap<String, String>()
//        params["primaryType"] = EventType.PrimaryType.lotto.name
//        params["active"] = "false"
        params["pg"] = page.toString()

        showProgress("")
        mLockListView = true
//        ApiBuilder.create().getLottoList(params).setCallback(object : PplusCallback<NewResultResponse<Event>> {
        ApiBuilder.create().getWinAnnounceList(params).setCallback(object : PplusCallback<NewResultResponse<Event>> {

            override fun onResponse(call: Call<NewResultResponse<Event>>, response: NewResultResponse<Event>) {

                mLockListView = false

                hideProgress()
                mTotalAdapter?.addAll(response.datas)
            }

            override fun onFailure(call: Call<NewResultResponse<Event>>, t: Throwable, response: NewResultResponse<Event>) {

                hideProgress()
            }
        }).build().call()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            Const.REQ_RESULT -> {
                setRetentionBol()
                if (binding.textWinHistoryLotto.isSelected) {
                    getCount()
                } else {
                    getUserCount()
                }

            }
        }
    }

    override fun getToolbarOption(): ToolbarOption {

        val toolbarOption = ToolbarOption(this)
        toolbarOption.initializeDefaultToolbar(getString(R.string.word_event_win_history), ToolbarOption.ToolbarMenu.LEFT)
        return toolbarOption
    }

    override fun getOnToolbarClickListener(): OnToolbarListener {

        return object : OnToolbarListener {
            override fun onClick(v: View?, toolbarMenu: ToolbarOption.ToolbarMenu?, tag: Any?) {
                when (toolbarMenu) {
                    ToolbarOption.ToolbarMenu.LEFT -> if (tag == 1) {
                        onBackPressed()
                    }
                }
            }
        }
    }
}
