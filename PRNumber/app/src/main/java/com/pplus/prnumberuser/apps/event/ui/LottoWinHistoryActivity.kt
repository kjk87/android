package com.pplus.prnumberuser.apps.event.ui

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pplus.networks.common.PplusCallback
import com.pplus.prnumberuser.Const
import com.pplus.prnumberuser.R
import com.pplus.prnumberuser.apps.common.toolbar.ImplToolbar
import com.pplus.prnumberuser.apps.common.toolbar.OnToolbarListener
import com.pplus.prnumberuser.apps.common.toolbar.ToolbarOption
import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
import com.pplus.prnumberuser.apps.my.data.TotalWinHistoryAdapter
import com.pplus.prnumberuser.core.code.common.EventType
import com.pplus.prnumberuser.core.network.ApiBuilder
import com.pplus.prnumberuser.core.network.model.dto.Event
import com.pplus.prnumberuser.core.network.model.response.NewResultResponse
import com.pplus.prnumberuser.databinding.ActivityLottoWinHistoryBinding
import retrofit2.Call
import java.util.*

class LottoWinHistoryActivity : BaseActivity(), ImplToolbar {

    override fun getPID(): String {
        return "Main_lotto_Winning history"
    }

    private lateinit var binding: ActivityLottoWinHistoryBinding

    override fun getLayoutView(): View {
        binding = ActivityLottoWinHistoryBinding.inflate(layoutInflater)
        return binding.root
    }

    var mAdapter: TotalWinHistoryAdapter? = null
    private var mPage: Int = 0
    private var mTotalCount = 0
    private var mLayoutManager: LinearLayoutManager? = null
    private var mLockListView = true

    override fun initializeView(savedInstanceState: Bundle?) {

        mAdapter = TotalWinHistoryAdapter(this)
        mLayoutManager = LinearLayoutManager(this)
        binding.recyclerLottoWinHistory.layoutManager = mLayoutManager
        binding.recyclerLottoWinHistory.adapter = mAdapter
        binding.recyclerLottoWinHistory.addOnScrollListener(object : RecyclerView.OnScrollListener() {

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


        mAdapter!!.setOnItemClickListener(object : TotalWinHistoryAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                val event = mAdapter!!.getItem(position)

                val intent = Intent(this@LottoWinHistoryActivity, LottoEventImpressionActivity::class.java)
                intent.putExtra(Const.LOTTO_TIMES, event.lottoTimes)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                startActivityForResult(intent, Const.REQ_RESULT)
            }
        })

        getCount()
    }


    private fun setSelect(view1: View, view2: View) {

        view1.isSelected = true
        view2.isSelected = false
    }

    private fun setBold(view1: TextView, view2: TextView) {

        view1.typeface = Typeface.DEFAULT_BOLD
        view2.typeface = Typeface.DEFAULT
    }

    private fun getCount() {
        val params = HashMap<String, String>()
        params["primaryType"] = EventType.PrimaryType.lotto.name
        params["active"] = "false"
        showProgress("")
        ApiBuilder.create().getLottoCount(params).setCallback(object : PplusCallback<NewResultResponse<Int>> {
//        ApiBuilder.create().getWinAnnounceCount(params).setCallback(object : PplusCallback<NewResultResponse<Int>> {

            override fun onResponse(call: Call<NewResultResponse<Int>>, response: NewResultResponse<Int>) {
                hideProgress()
                mTotalCount = response.data

                if (mTotalCount > 0) {
                    binding.textLottoWinHistoryNotExist.visibility = View.GONE
                } else {
                    binding.textLottoWinHistoryNotExist.visibility = View.VISIBLE
                }

                mPage = 1
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
        params["primaryType"] = EventType.PrimaryType.lotto.name
        params["active"] = "false"
        params["pg"] = page.toString()

        showProgress("")
        mLockListView = true
        ApiBuilder.create().getLottoList(params).setCallback(object : PplusCallback<NewResultResponse<Event>> {
//        ApiBuilder.create().getWinAnnounceList(params).setCallback(object : PplusCallback<NewResultResponse<Event>> {

            override fun onResponse(call: Call<NewResultResponse<Event>>, response: NewResultResponse<Event>) {

                mLockListView = false

                hideProgress()
                mAdapter?.addAll(response.datas)
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
                getCount()

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
