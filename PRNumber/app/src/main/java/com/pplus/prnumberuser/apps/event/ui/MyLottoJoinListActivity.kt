package com.pplus.prnumberuser.apps.event.ui

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.pplus.networks.common.PplusCallback
import com.pplus.prnumberuser.Const
import com.pplus.prnumberuser.R
import com.pplus.prnumberuser.apps.common.toolbar.ImplToolbar
import com.pplus.prnumberuser.apps.common.toolbar.OnToolbarListener
import com.pplus.prnumberuser.apps.common.toolbar.ToolbarOption
import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
import com.pplus.prnumberuser.apps.event.data.LottoJoinListAdapter
import com.pplus.prnumberuser.core.network.ApiBuilder
import com.pplus.prnumberuser.core.network.model.dto.Event
import com.pplus.prnumberuser.core.network.model.response.NewResultResponse
import com.pplus.prnumberuser.core.util.PplusCommonUtil
import com.pplus.prnumberuser.databinding.ActivityMyLottoJoinListBinding
import com.pplus.prnumberuser.databinding.ItemSelectedLottoBinding
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

    private var mLottoTimes = "";
    private var mAdapter: LottoJoinListAdapter? = null
    override fun initializeView(savedInstanceState: Bundle?) {
        mLottoTimes = intent.getStringExtra(Const.LOTTO_TIMES)!!
        val winCode = intent.getStringExtra(Const.WIN_CODE)

        setTitle(getString(R.string.format_lotto_join_title, mLottoTimes))

        mAdapter = LottoJoinListAdapter()
        binding.recyclerMyLottoJoin.layoutManager = LinearLayoutManager(this)
        binding.recyclerMyLottoJoin.adapter = mAdapter

        if(StringUtils.isNotEmpty(winCode)){
            binding.layoutMyLottoJoinListLottoNumber.visibility = View.VISIBLE

            val winList = winCode!!.split(",")
            binding.layoutMyLottoJoinListWinCode.removeAllViews()
            for (i in 0 until winList.size) {
                val selectedLottoBinding = ItemSelectedLottoBinding.inflate(layoutInflater)
                selectedLottoBinding.textSelectedLottoNumber.text = winList[i]

                if (winList[i].toInt() in 1..10) {
                    selectedLottoBinding.textSelectedLottoNumber.setBackgroundResource(R.drawable.ic_lotto_mynumber_bg_1)
                } else if (winList[i].toInt() in 11..20) {
                    selectedLottoBinding.textSelectedLottoNumber.setBackgroundResource(R.drawable.ic_lotto_mynumber_bg_2)
                } else if (winList[i].toInt() in 21..30) {
                    selectedLottoBinding.textSelectedLottoNumber.setBackgroundResource(R.drawable.ic_lotto_mynumber_bg_3)
                } else if (winList[i].toInt() in 31..40) {
                    selectedLottoBinding.textSelectedLottoNumber.setBackgroundResource(R.drawable.ic_lotto_mynumber_bg_4)
                } else {
                    selectedLottoBinding.textSelectedLottoNumber.setBackgroundResource(R.drawable.ic_lotto_mynumber_bg_5)
                }

                binding.layoutMyLottoJoinListWinCode.addView(selectedLottoBinding.root)
                if (i < winList.size - 1) {
                    (binding.root.layoutParams as LinearLayout.LayoutParams).marginEnd = resources.getDimensionPixelSize(R.dimen.height_26)
                }
            }
        }else{
            binding.layoutMyLottoJoinListLottoNumber.visibility = View.GONE
        }

        getCount()
        getJoinList()
    }

    fun getCount() {
        val params = HashMap<String, String>()
        params["lottoTimes"] = mLottoTimes
        ApiBuilder.create().getLottoJoinCont(params).setCallback(object : PplusCallback<NewResultResponse<Int>> {
            override fun onResponse(call: Call<NewResultResponse<Int>>?, response: NewResultResponse<Int>?) {
                if (response != null) {
                    binding.textMyLottoJoinCount.text = PplusCommonUtil.fromHtml(getString(R.string.html_join_count, FormatUtil.getMoneyType(response.data.toString())))
                }

            }

            override fun onFailure(call: Call<NewResultResponse<Int>>?, t: Throwable?, response: NewResultResponse<Int>?) {

            }
        }).build().call()
    }

    fun getJoinList() {
        val params = HashMap<String, String>()
        params["lottoTimes"] = mLottoTimes
        ApiBuilder.create().getLottoJoinList(params).setCallback(object : PplusCallback<NewResultResponse<Event>> {
            override fun onResponse(call: Call<NewResultResponse<Event>>?, response: NewResultResponse<Event>?) {
                if (response != null) {
                    if (response.datas.size == 0) {
                        binding.layoutMyLottoJoinNotExist.visibility = View.VISIBLE
                    }
                    mAdapter!!.addAll(response.datas)
                }

            }

            override fun onFailure(call: Call<NewResultResponse<Event>>?, t: Throwable?, response: NewResultResponse<Event>?) {

            }
        }).build().call()
    }

    override fun getToolbarOption(): ToolbarOption {
        val toolbarOption = ToolbarOption(this)
        toolbarOption.initializeDefaultToolbar(getString(R.string.word_lotto), ToolbarOption.ToolbarMenu.LEFT)

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
