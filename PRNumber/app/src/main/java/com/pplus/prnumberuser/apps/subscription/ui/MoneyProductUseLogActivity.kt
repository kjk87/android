package com.pplus.prnumberuser.apps.subscription.ui

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.pplus.networks.common.PplusCallback
import com.pplus.prnumberuser.Const
import com.pplus.prnumberuser.R
import com.pplus.prnumberuser.apps.common.toolbar.ImplToolbar
import com.pplus.prnumberuser.apps.common.toolbar.OnToolbarListener
import com.pplus.prnumberuser.apps.common.toolbar.ToolbarOption
import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
import com.pplus.prnumberuser.apps.subscription.data.MoneyProductLogAdapter
import com.pplus.prnumberuser.core.network.ApiBuilder
import com.pplus.prnumberuser.core.network.model.dto.SubscriptionDownload
import com.pplus.prnumberuser.core.network.model.dto.SubscriptionLog
import com.pplus.prnumberuser.core.network.model.response.NewResultResponse
import com.pplus.prnumberuser.databinding.ActivityMoneyProductUseLogBinding
import retrofit2.Call
import java.util.*

class MoneyProductUseLogActivity : BaseActivity(), ImplToolbar {
    private lateinit var binding: ActivityMoneyProductUseLogBinding

    override fun getLayoutView(): View {
        binding = ActivityMoneyProductUseLogBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun getPID(): String? {
        return ""
    }

    var mSubscriptionDownload: SubscriptionDownload? = null
    var mAdapter : MoneyProductLogAdapter? = null
    var mSort = "recent"

    override fun initializeView(savedInstanceState: Bundle?) {
        mSubscriptionDownload = intent.getParcelableExtra(Const.SUBSCRIPTION_DOWNLOAD)
        mAdapter = MoneyProductLogAdapter()
        binding.recyclerMoneyProductUseLog.layoutManager = LinearLayoutManager(this)
        binding.recyclerMoneyProductUseLog.adapter = mAdapter


        binding.textMoneyProductUseLogSortPast.setOnClickListener {
            binding.textMoneyProductUseLogSortRecent.isSelected = false
            binding.textMoneyProductUseLogSortPast.isSelected = true
            mSort = "past"
            getSubscriptionLogList()
        }

        binding.textMoneyProductUseLogSortRecent.setOnClickListener {
            binding.textMoneyProductUseLogSortRecent.isSelected = true
            binding.textMoneyProductUseLogSortPast.isSelected = false
            mSort = "recent"
            getSubscriptionLogList()
        }

        binding.textMoneyProductUseLogSortRecent.isSelected = true
        binding.textMoneyProductUseLogSortPast.isSelected = false
        mSort = "recent"

        getSubscriptionLogList()
    }

    private fun getSubscriptionLogList(){
        val params = HashMap<String, String>()
        params["subscriptionDownSeqNo"] = mSubscriptionDownload!!.seqNo.toString()
        params["sort"] = mSort
        showProgress("")
        ApiBuilder.create().getSubscriptionLogListBySubscriptionDownloadSeqNo(params).setCallback(object : PplusCallback<NewResultResponse<SubscriptionLog>> {
            override fun onResponse(call: Call<NewResultResponse<SubscriptionLog>>?, response: NewResultResponse<SubscriptionLog>?) {
                hideProgress()
                if(response?.datas != null){
                    if(response.datas!!.isEmpty()){
                        binding.textMoneyProductUseLogNotExist.visibility = View.VISIBLE
                    }else{
                        binding.textMoneyProductUseLogNotExist.visibility = View.GONE
                    }
                    mAdapter!!.setDataList(response.datas!!)
                }
            }

            override fun onFailure(call: Call<NewResultResponse<SubscriptionLog>>?, t: Throwable?, response: NewResultResponse<SubscriptionLog>?) {
                hideProgress()
            }
        }).build().call()
    }

    override fun getToolbarOption(): ToolbarOption {

        val toolbarOption = ToolbarOption(this)
        toolbarOption.initializeDefaultToolbar(getString(R.string.word_money_product_use_history), ToolbarOption.ToolbarMenu.LEFT)
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