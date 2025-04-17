package com.lejel.wowbox.apps.luckydraw.ui

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.pplus.networks.common.PplusCallback
import com.pplus.utils.part.format.FormatUtil
import com.lejel.wowbox.Const
import com.lejel.wowbox.R
import com.lejel.wowbox.apps.common.toolbar.ImplToolbar
import com.lejel.wowbox.apps.common.toolbar.OnToolbarListener
import com.lejel.wowbox.apps.common.toolbar.ToolbarOption
import com.lejel.wowbox.apps.common.ui.base.BaseActivity
import com.lejel.wowbox.apps.luckydraw.data.LuckyDrawGiftAdapter
import com.lejel.wowbox.core.network.ApiBuilder
import com.lejel.wowbox.core.network.model.dto.LuckyDraw
import com.lejel.wowbox.core.network.model.dto.LuckyDrawGift
import com.lejel.wowbox.core.network.model.response.ListResultResponse
import com.lejel.wowbox.core.network.model.response.NewResultResponse
import com.lejel.wowbox.core.util.PplusCommonUtil
import com.lejel.wowbox.databinding.ActivityLuckyDrawGiftBinding
import retrofit2.Call

class LuckyDrawGiftActivity : BaseActivity(), ImplToolbar {
    private lateinit var binding: ActivityLuckyDrawGiftBinding

    override fun getLayoutView(): View {
        binding = ActivityLuckyDrawGiftBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun getPID(): String? {
        return ""
    }

    private lateinit var mLuckyDraw: LuckyDraw
    lateinit var mAdapter: LuckyDrawGiftAdapter

    override fun initializeView(savedInstanceState: Bundle?) {
        mLuckyDraw = PplusCommonUtil.getParcelableExtra(intent, Const.DATA, LuckyDraw::class.java)!!
        mAdapter = LuckyDrawGiftAdapter()
        binding.recyclerLuckyDrawGift.adapter = mAdapter
        binding.recyclerLuckyDrawGift.layoutManager = LinearLayoutManager(this)

        binding.textLuckyDrawGiftDrawTitle.text = mLuckyDraw.title
        binding.textLuckyDrawGiftTotalPrice.text = FormatUtil.getMoneyTypeFloat(mLuckyDraw.totalPrice.toString())

        getList()
    }


    private fun getList() {
        val params = HashMap<String, String>()
        params["luckyDrawSeqNo"] = mLuckyDraw.seqNo.toString()
        showProgress("")
        ApiBuilder.create().getLuckyDrawGiftList(params).setCallback(object : PplusCallback<NewResultResponse<ListResultResponse<LuckyDrawGift>>> {
            override fun onResponse(call: Call<NewResultResponse<ListResultResponse<LuckyDrawGift>>>?, response: NewResultResponse<ListResultResponse<LuckyDrawGift>>?) {
                hideProgress()
                if (response?.result != null && response.result!!.list != null) {
                    val dataList = response.result!!.list!!
                    mAdapter.setDataList(dataList as MutableList<LuckyDrawGift>)
                }
            }

            override fun onFailure(call: Call<NewResultResponse<ListResultResponse<LuckyDrawGift>>>?, t: Throwable?, response: NewResultResponse<ListResultResponse<LuckyDrawGift>>?) {
                hideProgress()
            }
        }).build().call()
    }

    override fun getToolbarOption(): ToolbarOption {

        val toolbarOption = ToolbarOption(this)
        toolbarOption.initializeDefaultToolbar(getString(R.string.msg_look_around_gift), ToolbarOption.ToolbarMenu.LEFT)
        return toolbarOption
    }

    override fun getOnToolbarClickListener(): OnToolbarListener {

        return object : OnToolbarListener {
            override fun onClick(v: View?, toolbarMenu: ToolbarOption.ToolbarMenu?, tag: Any?) {
                when (toolbarMenu) {
                    ToolbarOption.ToolbarMenu.LEFT -> if (tag == 1) {
                        onBackPressedDispatcher.onBackPressed()
                    }

                    else -> {}
                }
            }
        }
    }
}