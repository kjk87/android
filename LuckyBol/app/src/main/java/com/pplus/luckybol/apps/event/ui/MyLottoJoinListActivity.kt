package com.pplus.luckybol.apps.event.ui

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.pplus.luckybol.Const
import com.pplus.luckybol.R
import com.pplus.luckybol.apps.common.toolbar.ImplToolbar
import com.pplus.luckybol.apps.common.toolbar.OnToolbarListener
import com.pplus.luckybol.apps.common.toolbar.ToolbarOption
import com.pplus.luckybol.apps.common.ui.base.BaseActivity
import com.pplus.luckybol.apps.event.data.LottoJoinListAdapter
import com.pplus.luckybol.core.network.ApiBuilder
import com.pplus.luckybol.core.network.model.dto.Event
import com.pplus.luckybol.core.network.model.dto.EventJoinJpa
import com.pplus.luckybol.core.network.model.response.NewResultResponse
import com.pplus.luckybol.core.util.PplusCommonUtil
import com.pplus.luckybol.databinding.ActivityMyLottoJoinListBinding
import com.pplus.luckybol.databinding.ItemSelectedLottoBinding
import com.pplus.networks.common.PplusCallback
import com.pplus.utils.part.format.FormatUtil
import com.pplus.utils.part.utils.StringUtils
import retrofit2.Call
import java.util.*

class MyLottoJoinListActivity : BaseActivity(), ImplToolbar {
    override fun getPID(): String {
        return ""
    }

    private lateinit var binding: ActivityMyLottoJoinListBinding

    override fun getLayoutView(): View {
        binding = ActivityMyLottoJoinListBinding.inflate(layoutInflater)
        return binding.root
    }

    lateinit var mEvent: Event
    private var mAdapter: LottoJoinListAdapter? = null
    override fun initializeView(savedInstanceState: Bundle?) {
        mEvent = intent.getParcelableExtra(Const.DATA)!!

        setTitle(getString(R.string.format_lotto_join_title, mEvent.title))

        mAdapter = LottoJoinListAdapter()
        binding.recyclerMyLottoJoin.layoutManager = LinearLayoutManager(this)
        binding.recyclerMyLottoJoin.adapter = mAdapter

        getJoinList()
    }


    fun getJoinList() {
        val params = HashMap<String, String>()
        params["eventSeqNo"] = mEvent.no.toString()
        ApiBuilder.create().getLottoJoinList(params).setCallback(object : PplusCallback<NewResultResponse<EventJoinJpa>> {
            override fun onResponse(call: Call<NewResultResponse<EventJoinJpa>>?, response: NewResultResponse<EventJoinJpa>?) {
                if (response?.datas != null) {
                    if (response.datas!!.isEmpty()) {
                        binding.layoutMyLottoJoinNotExist.visibility = View.VISIBLE
                    }
                    binding.textMyLottoJoinCount.text = PplusCommonUtil.fromHtml(getString(R.string.html_join_count, FormatUtil.getMoneyType(response.datas!!.size.toString())))
                    mAdapter!!.addAll(response.datas!!)
                }

            }

            override fun onFailure(call: Call<NewResultResponse<EventJoinJpa>>?, t: Throwable?, response: NewResultResponse<EventJoinJpa>?) {

            }
        }).build().call()
    }

    override fun getToolbarOption(): ToolbarOption {
        val toolbarOption = ToolbarOption(this)
        toolbarOption.initializeDefaultToolbar("", ToolbarOption.ToolbarMenu.LEFT)

        return toolbarOption
    }

    override fun getOnToolbarClickListener(): OnToolbarListener {

        return OnToolbarListener { v, toolbarMenu, tag ->
            when (toolbarMenu) {
                ToolbarOption.ToolbarMenu.LEFT -> if (tag == 1) {
                    onBackPressed()
                }
                else -> {}
            }
        }
    }
}
